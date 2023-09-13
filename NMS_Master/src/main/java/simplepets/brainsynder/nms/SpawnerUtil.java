package simplepets.brainsynder.nms;

import lib.brainsynder.ServerVersion;
import lib.brainsynder.SupportedVersion;
import lib.brainsynder.nbt.StorageTagCompound;
import lib.brainsynder.optional.BiOptional;
import lib.brainsynder.storage.RandomCollection;
import lib.brainsynder.utils.Colorize;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.CreatureSpawnEvent;
import simplepets.brainsynder.api.ISpawnUtil;
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

import java.util.*;

public class SpawnerUtil implements ISpawnUtil {
    private final Map<PetType, Class<?>> petMap;
    private final Map<PetType, Integer> spawnCount;

    public SpawnerUtil (ClassLoader classLoader) {
        petMap = new HashMap<>();
        spawnCount = new HashMap<>();

        for (PetType type : PetType.values()) {
            if (type.getEntityClass() == null) continue;

            String name = type.getEntityClass().getSimpleName().replaceFirst("I", "");
            try {
                Class<?> clazz = Class.forName("simplepets.brainsynder.versions."+ ServerVersion.getVersion().name() +".entity.list."+name, false, classLoader);
                if (clazz.isAnnotationPresent(SupportedVersion.class)) {
                    SupportedVersion version = clazz.getAnnotation(SupportedVersion.class);
                    if (!ServerVersion.isEqualNew(version.version())) {
                        SimplePets.getDebugLogger().debug(DebugBuilder.build(getClass()).setLevel(DebugLevel.WARNING).setMessages(
                                "The '"+type.getName()+"' pet is not supported for your server version [will NOT affect your server]"
                        ));
                        continue;
                    }
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
    public BiOptional<IEntityPet, String> spawnEntityPet(PetType type, PetUser user) {
        if (user.getUserLocation().isPresent()) return spawnEntityPet(type, user, getRandomLocation(type, user.getUserLocation().get()));
        return BiOptional.empty();
    }

    @Override
    public BiOptional<IEntityPet, String> spawnEntityPet(PetType type, PetUser user, StorageTagCompound compound) {
        if (user.getUserLocation().isPresent()) return spawnEntityPet(type, user, compound, getRandomLocation(type, user.getUserLocation().get()));
        return BiOptional.empty();
    }

    @Override
    public BiOptional<IEntityPet, String> spawnEntityPet(PetType type, PetUser user, Location location) {
        return spawnEntityPet(type, user, new StorageTagCompound(), location);
    }

    @Override
    public BiOptional<IEntityPet, String> spawnEntityPet(PetType type, PetUser user, StorageTagCompound compound, Location location) {
        if (ConfigOption.INSTANCE.WORLDS_ENABLED.getValue()) {
            if (!ConfigOption.INSTANCE.WORLDS_ALLOWED_WORLDS.getValue().contains(location.getWorld().getName()))
                return BiOptional.of(null, Colorize.translateBungeeHex(ConfigOption.INSTANCE.WORLDS_FAIL_MESSAGE.getValue()));
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
                    return BiOptional.of(null, ChatColor.RED + e.getMessage());
                }
            }

            customEntity.setInvisible(false);
            customEntity.moveTo(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
            customEntity.setPersistenceRequired();

            // Call the spawn event
            PetEntitySpawnEvent event = new PetEntitySpawnEvent(user, customEntity);
            Bukkit.getServer().getPluginManager().callEvent(event);
            if (event.isCancelled()) {
                SimplePets.getPetUtilities().runPetCommands(CommandReason.FAILED, user, type);
                String reason = "";
                if (event.getReason() != null) reason = event.getReason();
                if (!reason.isEmpty()) return BiOptional.of(null, reason);
                return BiOptional.empty();
            }

            if (!location.getChunk().isLoaded()) location.getChunk().load();

            if (VersionTranslator.addEntity(VersionTranslator.getWorldHandle(location.getWorld()), customEntity, CreatureSpawnEvent.SpawnReason.CUSTOM)) {
                user.setPet(customEntity);
                if (compound.hasKey("name")) {
                    String name = compound.getString("name");
                    if (name != null) name = name.replace("~", " ");
                    customEntity.setPetName(name);
                }
                SimplePets.getPetUtilities().runPetCommands(CommandReason.SPAWN, user, type);
                int count = spawnCount.getOrDefault(type, 0);
                spawnCount.put(type, (count+1));
                return BiOptional.of(customEntity);
            }
        }catch (Exception e) {
            e.printStackTrace();
            SimplePets.getPetUtilities().runPetCommands(CommandReason.FAILED, user, type, location);
            return BiOptional.of(null, e.getMessage());
        }

        return BiOptional.empty();
    }

    @Override
    public boolean isRegistered(PetType type) {
        return petMap.containsKey(type);
    }

    @Override
    public Optional<Object> getHandle(Entity entity) {
        if (entity == null) return Optional.empty();
        try {
            return Optional.of(VersionTranslator.getEntityHandle(entity));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public Map<PetType, Integer> getSpawnCount() {
        return spawnCount;
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
