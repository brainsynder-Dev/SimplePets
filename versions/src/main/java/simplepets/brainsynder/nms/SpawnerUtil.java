package simplepets.brainsynder.nms;

import org.bsdevelopment.nbt.StorageTagCompound;
import org.bsdevelopment.pluginutils.storage.RandomCollection;
import org.bsdevelopment.pluginutils.text.Colorize;
import org.bsdevelopment.pluginutils.version.VersionCompatibility;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.CreatureSpawnEvent;
import simplepets.brainsynder.api.ISpawnUtil;
import simplepets.brainsynder.api.SpawnResult;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.event.entity.PetEntitySpawnEvent;
import simplepets.brainsynder.api.pet.CommandReason;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.plugin.SimplePets;
import simplepets.brainsynder.api.plugin.config.ConfigOption;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.debug.DebugBuilder;
import simplepets.brainsynder.debug.DebugLevel;
import simplepets.brainsynder.nms.entity.EntityPet;
import simplepets.brainsynder.nms.entity.special.EntityControllerPet;
import simplepets.brainsynder.nms.helper.VersionHelper;
import simplepets.brainsynder.utils.SpawnBlockDiagnostics;

import java.util.*;

public class SpawnerUtil implements ISpawnUtil {
    private final Map<PetType, Class<?>> petMap;
    private final Map<PetType, Integer> spawnCount;

    public SpawnerUtil (ClassLoader classLoader, String targetVersionName) {
        petMap = new HashMap<>();
        spawnCount = new HashMap<>();

        for (PetType type : PetType.values()) {
            if (type.getEntityClass() == null) continue;
            // if (type == PetType.ARMOR_STAND) continue;
            // if (type == PetType.SHULKER) continue;

            String name = type.getEntityClass().getSimpleName().replaceFirst("I", "");
            try {
                Class<?> clazz = Class.forName("simplepets.brainsynder.versions."+ targetVersionName +".entity.list."+name, false, classLoader);
                if (!VersionCompatibility.isCompatible(clazz)) {
                    SimplePets.getDebugLogger().debug(DebugBuilder.build(getClass()).setLevel(DebugLevel.WARNING).setMessages(
                            "The '"+type.getName()+"' pet is not supported for your server version [will NOT affect your server]"
                    ));
                    continue;
                }
                petMap.put(type, clazz);
            }catch (Exception ignored) {
                SimplePets.getDebugLogger().debug(DebugBuilder.build(getClass()).setLevel(DebugLevel.WARNING).setMessages(
                        "The '"+type.getName()+" ("+name+")' pet is not available for your server version [will NOT affect your server]"
                ));
            }
        }
    }

    @Override
    public SpawnResult<IEntityPet> spawnEntityPet(PetType type, PetUser user) {
        if (user.getUserLocation().isPresent()) return spawnEntityPet(type, user, getRandomLocation(type, user.getUserLocation().get()));
        return SpawnResult.fail("missing user location, unable to spawn pet due to no location provided");
    }

    @Override
    public SpawnResult<IEntityPet> spawnEntityPet(PetType type, PetUser user, StorageTagCompound compound) {
        if (user.getUserLocation().isPresent()) return spawnEntityPet(type, user, compound, getRandomLocation(type, user.getUserLocation().get()));
        return SpawnResult.fail("missing user location, unable to spawn pet due to no location provided");
    }

    @Override
    public SpawnResult<IEntityPet> spawnEntityPet(PetType type, PetUser user, Location location) {
        return spawnEntityPet(type, user, new StorageTagCompound(), location);
    }

    @Override
    public SpawnResult<IEntityPet> spawnEntityPet(PetType type, PetUser user, StorageTagCompound compound, Location location) {
        if (ConfigOption.WORLDS_ENABLED.get()) {
            if (!ConfigOption.WORLDS_ALLOWED_WORLDS.get().contains(location.getWorld().getName()))
                return SpawnResult.fail(Colorize.translateBungeeHex(ConfigOption.WORLDS_FAIL_MESSAGE.get()));
        }

        if (ConfigOption.MISC_TOGGLES_WORLD_CONFINES_PET_LIMITS.get()) {
            int maxHeight = location.getWorld().getMaxHeight();
            int minHeight = location.getWorld().getMinHeight();
            int y = location.getBlockY();

            if ( (y > maxHeight) || (minHeight > y) )
                return SpawnResult.fail(Colorize.translateBungeeHex(ConfigOption.MISC_TOGGLES_EXCEEDS_WORLD_CONFINES.get()));
        }

        try {
            EntityPet customEntity;

            if ((type == PetType.ARMOR_STAND) || (type == PetType.SHULKER)) {
                customEntity = new EntityControllerPet(type, user, location);
            }else{
                customEntity = (EntityPet) petMap.get(type).getDeclaredConstructor(PetType.class, PetUser.class).newInstance(type, user);
            }

            if ((compound != null) && (!compound.hasNoTags())) {
                try {
                    customEntity.applyCompound(compound);
                } catch (Exception e) {
                    return SpawnResult.fail(ChatColor.RED+"An error occurred while applying NBT data to the pet: " + e.getMessage());
                }
            }

            customEntity.setInvisible(false);
            customEntity.setInvulnerable(true);
            customEntity.snapTo(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
            customEntity.setPersistenceRequired();

            // Call the spawn event
            PetEntitySpawnEvent event = new PetEntitySpawnEvent(user, customEntity);
            Bukkit.getServer().getPluginManager().callEvent(event);
            if (event.isCancelled()) {
                SimplePets.getPetUtilities().runPetCommands(CommandReason.FAILED, user, type);

                String reason = event.getReason();
                if ((reason != null) && (!reason.isEmpty())) return SpawnResult.fail(reason);

                return reportBlockedSpawn(type, user, location, "PetEntitySpawnEvent", new PetEntitySpawnEvent(user, customEntity));
            }

            if (!location.getChunk().isLoaded()) location.getChunk().load();

            if (VersionHelper.addEntity(((CraftWorld) location.getWorld()).getHandle(), customEntity, CreatureSpawnEvent.SpawnReason.CUSTOM)) {
                user.setPet(customEntity);
                if (compound.hasKey("name")) {
                    String name = compound.getString("name");
                    if (name != null) name = name.replace("~", " ");
                    customEntity.setPetName(name);
                }
                SimplePets.getPetUtilities().runPetCommands(CommandReason.SPAWN, user, type);
                int count = spawnCount.getOrDefault(type, 0);
                spawnCount.put(type, (count+1));
                return SpawnResult.success(customEntity);
            }

            SimplePets.getPetUtilities().runPetCommands(CommandReason.FAILED, user, type, location);
            return reportBlockedSpawn(type, user, location, "CreatureSpawnEvent", buildSpawnProbeEvent(customEntity));
        }catch (Exception e) {
            e.printStackTrace();
            SimplePets.getPetUtilities().runPetCommands(CommandReason.FAILED, user, type, location);
            return SpawnResult.fail("An error occurred while trying to spawn the pet: " + e.getMessage());
        }
    }

    @Override
    public boolean isRegistered(PetType type) {
        return petMap.containsKey(type);
    }

    @Override
    public Optional<Object> getHandle(Entity entity) {
        if (entity == null) return Optional.empty();
        try {
            return Optional.of(VersionHelper.getEntityHandle(entity));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public Map<PetType, Integer> getSpawnCount() {
        return spawnCount;
    }

    private Event buildSpawnProbeEvent(EntityPet customEntity) {
        Entity bukkitEntity = customEntity.getBukkitEntity();
        if (!(bukkitEntity instanceof LivingEntity livingEntity)) return null;
        return new CreatureSpawnEvent(livingEntity, CreatureSpawnEvent.SpawnReason.CUSTOM);
    }

    private SpawnResult<IEntityPet> reportBlockedSpawn(PetType type, PetUser user, Location location, String eventName, Event probeEvent) {
        List<String> details = new ArrayList<>();
        String blockingPlugin = findBlockingPlugin(probeEvent);

        if (blockingPlugin != null) {
            details.add("The spawn was cancelled by '" + blockingPlugin + "' (via " + eventName + ")");
        } else {
            details.add("The spawn was cancelled by another plugin (via " + eventName + ")");

            List<String> listeningPlugins = (probeEvent == null) ? List.of() : SpawnBlockDiagnostics.getListeningPlugins(probeEvent);
            if (listeningPlugins.isEmpty()) {
                details.add("No other plugin is listening to that event, so the server itself rejected the pet");
            } else {
                details.add("Plugins listening to that event: " + String.join(", ", listeningPlugins));
            }
        }

        if (ConfigOption.PET_TOGGLES_SPAWN_BYPASS.get()) details.add("'pet-toggles.mob-spawn-bypass' is enabled but did not override the cancellation");

        List<String> report = new ArrayList<>();
        report.add("Failed to spawn the '" + type.getName() + "' pet for " + getUserName(user));
        report.add("Location: " + location.getWorld().getName() + " @ " + location.getBlockX() + ", " + location.getBlockY() + ", " + location.getBlockZ());
        report.addAll(details);

        SimplePets.getDebugLogger().debug(DebugBuilder.build(getClass()).setLevel(DebugLevel.ERROR).setMessages(report.toArray(String[]::new)));
        return SpawnResult.fail(ChatColor.RED + String.join("\n" + ChatColor.RED, details));
    }

    private String findBlockingPlugin(Event probeEvent) {
        if (probeEvent == null) return null;
        if (!ConfigOption.MISC_TOGGLES_SPAWN_FAILURE_DIAGNOSTICS.get()) return null;
        return SpawnBlockDiagnostics.findCancellingPlugin(probeEvent).orElse(null);
    }

    private String getUserName(PetUser user) {
        Player player = user.getPlayer();
        return (player == null) ? String.valueOf(user.getOwnerUUID()) : player.getName();
    }

    private Location getRandomLocation (PetType type, Location center) {
        List<Location> locationList = circle(center, modifyInt(type, 4), 3, false, false, true);
        return RandomCollection.fromCollection(locationList).next();
    }

    private int modifyInt(PetType type, int number) {
        return (type.isLargePet() ? (number + number) : number);
    }

    private List<Location> circle(Location loc, double radius, double height, boolean hollow, boolean sphere, boolean checks) {
        ArrayList circleblocks = new ArrayList();
        double cx = loc.getX();
        double cy = loc.getY();
        double cz = loc.getZ();

        for (double x = cx - radius; x <= cx + radius; ++x) {
            for (double z = cz - radius; z <= cz + radius; ++z) {
                for (double y = sphere ? cy - radius : cy; y < (sphere ? cy + radius : cy + height); ++y) {
                    double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z) + (sphere ? (cy - y) * (cy - y) : 0.0D);
                    if (dist < radius * radius && (!hollow || dist >= (radius - 1.0D) * (radius - 1.0D))) {
                        Location l = new Location(loc.getWorld(), x, y, z);
                        if (checks) {
                            if ((!l.getBlock().isEmpty()) && (!l.getBlock().isPassable())) continue;
                            if ((l.getBlock().getRelative(BlockFace.DOWN).isEmpty())
                                    || (l.getBlock().getRelative(BlockFace.DOWN).isPassable()))
                                continue;
                        }
                        circleblocks.add(l);
                    }
                }
            }
        }

        if (circleblocks.isEmpty()) return circle(loc, radius, height, hollow, sphere, false);

        return circleblocks;
    }
}
