package simplepets.brainsynder.utils;

import lib.brainsynder.ServerVersion;
import lib.brainsynder.files.YamlFile;
import lib.brainsynder.nbt.StorageTagCompound;
import lib.brainsynder.nms.Tellraw;
import lib.brainsynder.optional.BiOptional;
import lib.brainsynder.reflection.FieldAccessor;
import lib.brainsynder.reflection.Reflection;
import org.apache.commons.io.FileUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.permissions.PermissionAttachmentInfo;
import simplepets.brainsynder.api.ISpawnUtil;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.misc.IEntityControllerPet;
import simplepets.brainsynder.api.pet.CommandReason;
import simplepets.brainsynder.api.pet.PetData;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.plugin.SimplePets;
import simplepets.brainsynder.api.plugin.config.ConfigOption;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.debug.DebugBuilder;
import simplepets.brainsynder.debug.DebugLevel;
import simplepets.brainsynder.files.MessageFile;
import simplepets.brainsynder.files.options.MessageOption;
import simplepets.brainsynder.managers.ParticleManager;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Utilities {
    public static List<Material> getBlacklistedMaterials() {
        List<Material> materials = new ArrayList<>();
        for (Material material : Material.values()) {
            String name = material.name();

            if (name.contains("GLASS_PANE")) materials.add(material);
            if (name.contains("FENCE")) materials.add(material);
            if (name.contains("DOOR") && (!name.contains("TRAP"))) materials.add(material);
            if (name.contains("FENCE_GATE")) materials.add(material);
            if (name.contains("THIN")) materials.add(material);
        }

        return materials;
    }

    public static boolean handlePetSpawning(PetUser user, PetType type, StorageTagCompound compound, boolean checkDataPermissions) {
        Player player = user.getPlayer();
        if (type.isInDevelopment()
                && (!ConfigOption.INSTANCE.PET_TOGGLES_DEV_MOBS.getValue())) {
            player.sendMessage(MessageFile.getTranslation(MessageOption.PET_IN_DEVELOPMENT).replace("{type}", type.getName()));
            return false;
        }

        if (!type.isSupported()) {
            player.sendMessage(MessageFile.getTranslation(MessageOption.PET_NOT_SUPPORTED).replace("{type}", type.getName()));
            return false;
        }

        if (!SimplePets.getSpawnUtil().isRegistered(type)) {
            player.sendMessage(MessageFile.getTranslation(MessageOption.PET_NOT_REGISTERED).replace("{type}", type.getName()));
            return false;
        }

        if (!Utilities.hasPermission(player, type.getPermission())
                && ((!user.getOwnedPets().contains(type)) && ConfigOption.INSTANCE.UTILIZE_PURCHASED_PETS.getValue())) {
            return false;
        }

        ISpawnUtil spawner = SimplePets.getSpawnUtil();
        if (spawner == null) {
            return false;
        }

        if (compound.hasNoTags()) {
            // Should load the defaults based on the Pet data
            for (PetData petData : type.getPetData()) {
                if (checkDataPermissions && (!hasPermission(player, type.getPermission("data." + petData.getNamespace().namespace()))))
                    continue;
                petData.getDefault(type).ifPresent(o -> {
                    compound.set(petData.getNamespace().namespace(), o);
                });
            }
        }

        BiOptional<IEntityPet, String> entityPet = spawner.spawnEntityPet(type, user, compound);

        if (entityPet.isFirstPresent()) {
            player.sendMessage(MessageFile.getTranslation(MessageOption.SUMMONED_PET).replace("{type}", type.getName()));
            return true;
        } else {
            SimplePets.getParticleHandler().sendParticle(ParticleManager.Reason.FAILED, player, player.getLocation());
            if (entityPet.isSecondPresent()) {
                Tellraw.fromLegacy(MessageFile.getTranslation(MessageOption.FAILED_SUMMON).replace("{type}", type.getName()))
                        .tooltip(entityPet.second().get()).send(player);
                return false;
            }

            player.sendMessage(MessageFile.getTranslation(MessageOption.FAILED_SUMMON).replace("{type}", type.getName()));
            return false;
        }
    }

    public static void runPetCommands(CommandReason reason, PetUser owner, PetType type) {
        SimplePets.getPetUtilities().runPetCommands(reason, owner, type, null);
    }

    public static void runPetCommands(CommandReason reason, PetUser owner, PetType type, Location location) {
        SimplePets.getPetUtilities().runPetCommands(reason, owner, type, location);
    }

    /**
     * This method replaces all the placeholders with the correct replacements
     * <p>
     * List of Placeholders
     * <ul>
     *     <li>{petX} - The pets X coordinate</li>
     *     <li>{petY} - The pets Y coordinate</li>
     *     <li>{petZ} - The pets Z coordinate</li>
     *     <li>{ownerX} - The pet owners X coordinate</li>
     *     <li>{ownerY} - The pet owners Y coordinate</li>
     *     <li>{ownerZ} - The pet owners Z coordinate</li>
     *     <li>{ownerName} - The pet owners name</li>
     *     <li>{petName} - The pets current name</li>
     *     <li>{petType} - The type of pet</li>
     * </ul>
     *
     * @param owner   The owner of the pet
     * @param entity  The pet the commands are run for
     * @param command The command that is being run
     * @return | Returns the command with all the replaced placeholders
     */
    public static String handleCommandPlaceholders(PetUser owner, IEntityPet entity, Location petLoc, String command) {
        return SimplePets.getPetUtilities().handlePlaceholders(owner, entity, petLoc, command);
    }

    public static void setPassenger(Player player, Entity entity, Entity passenger) {
        try {
            SimplePets.getDebugLogger().debug(DebugLevel.HIDDEN, "Set passenger: " + entity.addPassenger(passenger) + " - " + entity.getClass().getName() + ", " + passenger.getClass().getName());
        } catch (Exception e) {
            SimplePets.getDebugLogger().debug(DebugLevel.ERROR, "Could not run method IEntityPet#setPassenger");
            e.printStackTrace();
        }
    }

    public static boolean hasPermission(CommandSender sender, String permission) {
        return hasPermission(sender, permission, false);
    }

    public static boolean hasPermission(CommandSender sender, String permission, boolean strict) {
        if (sender instanceof ConsoleCommandSender) return true;
        if ((permission == null) || (permission.isEmpty())) return true;
        //if (sender.isOp()) return true;

        int value = getPermission(sender, permission, strict);
        if (ConfigOption.INSTANCE.PERMISSIONS_IGNORE_LIST.getValue().contains(permission))
            return true;
        return value == 1;
    }

    public static int getPermission(CommandSender sender, String permission, boolean strict) {
        if (ConfigOption.INSTANCE.PERMISSIONS_ENABLED.getValue()) {
            if (strict) {
                for (PermissionAttachmentInfo info : sender.getEffectivePermissions()) {
                    if (info.getPermission().equalsIgnoreCase(permission)) {
                        return info.getValue() ? 1 : 0;
                    }
                }
                return -1;
            } else {
                return sender.hasPermission(permission) ? 1 : 0;
            }
        } else {
            return 1;
        }
    }

    public static int parseTypeSaveLimit(PetType type) {
        for (String line : ConfigOption.INSTANCE.PET_SAVES_TYPE_LIMIT.getValue()) {
            if (!line.contains("-")) continue;
            String[] args = line.split("-");
            if (args.length != 2) continue;

            PetType target = PetType.getPetType(args[0]).orElse(null);
            if (target == null) continue;

            if (type != target) continue;
            try {
                return Integer.parseInt(args[1].trim());
            } catch (NumberFormatException e) {
                SimplePets.getDebugLogger().debug(DebugLevel.ERROR, "Unable to parse pet-type-limit for '" + args[0] + "', " + args[1] + " is not a valid number.");
                return -1;
            }
        }

        return -1;
    }

    public static int getPermissionAmount(Player player, int defaultValue, String partialPermission) {
        int amount = defaultValue;
        if (!partialPermission.endsWith(".")) return defaultValue;

        for (PermissionAttachmentInfo permission : player.getEffectivePermissions()) {
            if (!permission.getValue()) continue;
            if (!permission.getPermission().startsWith(partialPermission)) continue;

            String strAmount = permission.getPermission().substring((partialPermission.lastIndexOf(".") + 1));
            int permAmount = Integer.parseInt(strAmount);
            if (permAmount >= amount) amount = permAmount;
        }
        return amount;
    }

    public static void removePassenger(Entity entity, Entity passenger) {
        try {
            entity.eject();
            if (entity instanceof Player) {
                resetRideCooldown(passenger);
            }
        } catch (Exception e) {
            SimplePets.getDebugLogger().debug(DebugLevel.ERROR, "Could not run method IEntityPet#removePassenger");
            e.printStackTrace();
        }
    }

    public static void resetRideCooldown(Entity entity) {
        FieldAccessor<Integer> field;

        // Class: net.minecraft.world.entity.Entity
        // protected int    (Below public com.google.common.collect.ImmutableList<Entity>)

        String targetField;
        if (ServerVersion.isEqualNew(ServerVersion.v1_20_4)) {
            targetField = "J";
        } else if (ServerVersion.isEqualNew(ServerVersion.v1_20_3)) {
            targetField = "r";
        } else if (ServerVersion.isEqualNew(ServerVersion.v1_20_2)) {
            targetField = "J";
        } else if (ServerVersion.isEqualNew(ServerVersion.v1_20)) {
            targetField = "I";
        } else if (ServerVersion.isEqualNew(ServerVersion.v1_19_4)) {
            targetField = "G";
        } else if (ServerVersion.isEqualNew(ServerVersion.v1_18_2)) {
            targetField = "r";
        }else{
            targetField = "s";
        }

        field = FieldAccessor.getField(Reflection.getNmsClass("Entity", "world.entity"), targetField, Integer.TYPE);

        field.set(Reflection.getHandle(entity), 0);
    }

    public static void hidePet(PetUser user, IEntityPet entityPet) {
        UUID entityID = entityPet.getEntity().getUniqueId();
        if (entityPet instanceof IEntityControllerPet)
            entityID = ((IEntityControllerPet) entityPet).getVisibleEntity().getEntity().getUniqueId();
        managePetVisibility(user.getPlayer(), "PacketPlayOutEntityDestroy", Integer.TYPE, entityID);
    }

    public static void showPet(PetUser user, IEntityPet entityPet) {
        Entity entity = entityPet.getEntity();
        if (entityPet instanceof IEntityControllerPet)
            entity = ((IEntityControllerPet) entityPet).getVisibleEntity().getEntity();
        managePetVisibility(user.getPlayer(), "PacketPlayOutSpawnEntityLiving", Reflection.getNmsClass("EntityLiving"), entity);
    }

    private static void managePetVisibility(Player p, String nmsClass, Class<?> o1, Object o2) {
        Class<?> entity = Reflection.getNmsClass(nmsClass);
        Constructor<?> constructor = Reflection.fillConstructor(entity, o1);
        Object packet = Reflection.initiateClass(constructor, o2);
        Reflection.sendPacket(p, packet);
    }

    public static boolean isSimilar(ItemStack main, ItemStack check) {
        List<Boolean> values = new ArrayList<>();
        if ((main == null) || (check == null)) return false;
        //if (main.isSimilar(check)) return true;

        if (main.getType() == check.getType()) {
            if (main.hasItemMeta() && check.hasItemMeta()) {
                ItemMeta mainMeta = main.getItemMeta();
                ItemMeta checkMeta = check.getItemMeta();
                if (mainMeta.hasDisplayName() && checkMeta.hasDisplayName()) {
                    values.add(mainMeta.getDisplayName().equals(checkMeta.getDisplayName()));
                }

                if (mainMeta.hasLore() && checkMeta.hasLore()) {
                    values.add(mainMeta.getLore().equals(checkMeta.getLore()));
                }

                if (mainMeta.hasEnchants() && checkMeta.hasEnchants()) {
                    values.add(mainMeta.getEnchants().equals(checkMeta.getEnchants()));
                }

                if (!values.isEmpty()) return !values.contains(false);
            }
        }

        return main.isSimilar(check);
    }

    public static void makeBackup(YamlFile yamlFile, File backup) {
        File file = yamlFile.getFile();
        try {
            if (!backup.exists()) backup.createNewFile();
            FileUtils.copyFile(file, backup);
            SimplePets.getDebugLogger().debug(DebugBuilder.build(yamlFile.getClass()).setLevel(DebugLevel.NORMAL).setMessages(
                    "A new major config change was detected",
                    "Saving the old config to 'plugins/SimplePets/" + backup.getParentFile().getName() + "/" + backup.getName() + "'"
            ));
        } catch (IOException e) {
            SimplePets.getDebugLogger().debug(DebugBuilder.build(yamlFile.getClass()).setLevel(DebugLevel.ERROR).setMessages(
                    "Failed to create file backup for: " + file.getName(),
                    "Error: " + e.getMessage()
            ));
            e.printStackTrace();
        }

    }

    public static String itemToString(ItemStack itemStack) {
        YamlConfiguration config = new YamlConfiguration();
        config.set("i", itemStack);
        return config.saveToString();
    }

    public static ItemStack stringToItem(String stringBlob) {
        YamlConfiguration config = new YamlConfiguration();
        try {
            config.loadFromString(stringBlob);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return config.getItemStack("i", null);
    }
}
