package simplepets.brainsynder.nms;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import lib.brainsynder.ServerVersion;
import lib.brainsynder.SupportedVersion;
import lib.brainsynder.item.ItemBuilder;
import lib.brainsynder.nbt.JsonToNBT;
import lib.brainsynder.nbt.StorageTagCompound;
import lib.brainsynder.nbt.other.NBTException;
import lib.brainsynder.optional.BiOptional;
import lib.brainsynder.storage.RandomCollection;
import lib.brainsynder.utils.Colorize;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.TagParser;
import net.minecraft.resources.RegistryOps;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_21_R6.CraftServer;
import org.bukkit.craftbukkit.v1_21_R6.inventory.CraftItemStack;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.ItemStack;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.ISpawnUtil;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.event.entity.PetEntitySpawnEvent;
import simplepets.brainsynder.api.event.user.PetRenameEvent;
import simplepets.brainsynder.api.pet.CommandReason;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.plugin.SimplePets;
import simplepets.brainsynder.api.plugin.config.ConfigOption;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.debug.DebugBuilder;
import simplepets.brainsynder.debug.DebugLevel;
import simplepets.brainsynder.files.MessageFile;
import simplepets.brainsynder.files.options.MessageOption;
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
            // if (type == PetType.ARMOR_STAND) continue;
            // if (type == PetType.SHULKER) continue;

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

    public ItemStack convertToItemStack (StorageTagCompound compound) {
        Codec<net.minecraft.world.item.ItemStack> codec = net.minecraft.world.item.ItemStack.CODEC;
        DataResult<net.minecraft.world.item.ItemStack> result = null;
        try {
            CompoundTag nbtTag = TagParser.parseCompoundFully(compound.toString());
            RegistryAccess.Frozen registryAccess = ((CraftServer)Bukkit.getServer()).getServer().registryAccess();
            RegistryOps<Tag> registryOps = registryAccess.createSerializationContext(NbtOps.INSTANCE);
            result = codec.parse(registryOps, nbtTag);
        } catch (CommandSyntaxException e) {
            throw new RuntimeException(e);
        }


        return CraftItemStack.asBukkitCopy(result.getOrThrow());
    }

    public StorageTagCompound convertToCompound (ItemStack stack) {
        net.minecraft.world.item.ItemStack nmsStack = CraftItemStack.asNMSCopy(stack);
        RegistryAccess.Frozen registryAccess = ((CraftServer)Bukkit.getServer()).getServer().registryAccess();
        RegistryOps<Tag> registryOps = registryAccess.createSerializationContext(NbtOps.INSTANCE);
        Tag tag = net.minecraft.world.item.ItemStack.CODEC.encodeStart(registryOps, nmsStack).getOrThrow();

        try {
            return JsonToNBT.getTagFromJson(tag.toString());
        } catch (NBTException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void renameViaAnvil(PetUser user, PetType type) {
        AnvilGUI.Builder builder = new AnvilGUI.Builder().plugin(PetCore.getInstance());
        builder.itemLeft(new ItemBuilder(Material.NAME_TAG).withName(MessageFile.getTranslation(MessageOption.RENAME_ANVIL_TAG)).build());
        builder.onClick((slot, stateSnapshot) -> {
            if (slot != AnvilGUI.Slot.OUTPUT) return Collections.emptyList();

            String name = stateSnapshot.text();

            if (name.equalsIgnoreCase("reset")) name = null;
            PetRenameEvent renameEvent = new PetRenameEvent(user, type, name);
            Bukkit.getPluginManager().callEvent(renameEvent);

            if (!renameEvent.isCancelled()) user.setPetName(renameEvent.getName(), type);
            return Arrays.asList(AnvilGUI.ResponseAction.close());
        });
        builder.title(MessageFile.getTranslation(MessageOption.RENAME_ANVIL_TITLE));
        builder.open(user.getPlayer());
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

        if (ConfigOption.INSTANCE.MISC_TOGGLES_WORLD_CONFINES_PET_LIMITS.getValue()) {
            int maxHeight = location.getWorld().getMaxHeight();
            int minHeight = location.getWorld().getMinHeight();
            int y = location.getBlockY();

            if ( (y > maxHeight) || (minHeight > y) )
                return BiOptional.of(null, Colorize.translateBungeeHex(ConfigOption.INSTANCE.MISC_TOGGLES_EXCEEDS_WORLD_CONFINES.getValue()));
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
            customEntity.setInvulnerable(true);
            customEntity.snapTo(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
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
