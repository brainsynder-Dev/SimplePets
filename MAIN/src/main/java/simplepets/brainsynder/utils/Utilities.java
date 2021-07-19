package simplepets.brainsynder.utils;

import lib.brainsynder.files.YamlFile;
import lib.brainsynder.nbt.StorageTagCompound;
import lib.brainsynder.nms.Tellraw;
import lib.brainsynder.optional.BiOptional;
import lib.brainsynder.reflection.FieldAccessor;
import lib.brainsynder.reflection.Reflection;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.craftbukkit.libs.org.apache.commons.io.FileUtils;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.permissions.PermissionAttachmentInfo;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.ISpawnUtil;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.misc.IEntityControllerPet;
import simplepets.brainsynder.api.pet.PetData;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.plugin.SimplePets;
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

    public static boolean handlePetSpawning (PetUser user, PetType type, StorageTagCompound compound, boolean checkDataPermissions) {
        Player player = user.getPlayer();
        if (!type.isSupported()) {
            player.sendMessage(MessageFile.getTranslation(MessageOption.PET_NOT_SUPPORTED).replace("{type}", type.getName()));
            return false;
        }

        if (!SimplePets.getSpawnUtil().isRegistered(type)) {
            player.sendMessage(MessageFile.getTranslation(MessageOption.PET_NOT_REGISTERED).replace("{type}", type.getName()));
            return false;
        }

        if (!Utilities.hasPermission(player, type.getPermission())) return false;

        ISpawnUtil spawner = SimplePets.getSpawnUtil();
        if (spawner == null) return false;

        if (compound.hasNoTags()) {
            // Should load the defaults based on the Pet data
            for (PetData petData : type.getPetData()) {
                if (checkDataPermissions && (!hasPermission(player, type.getPermission("data."+petData.getNamespace().namespace())))) continue;
                petData.getDefault(type).ifPresent(o -> {
                    compound.set(petData.getNamespace().namespace(), o);
                });
            }
        }

        BiOptional<IEntityPet, String> entityPet = spawner.spawnEntityPet(type, user, compound);
        if (entityPet.isFirstPresent()) {
            player.sendMessage(MessageFile.getTranslation(MessageOption.SUMMONED_PET).replace("{type}", type.getName()));
            return true;
        }else{
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

    public static void setPassenger(Player player, Entity entity, Entity passenger) {
        try {
            entity.setPassenger(passenger);
        } catch (Exception e) {
            SimplePets.getDebugLogger().debug(DebugLevel.ERROR, "Could not run method IEntityPet#setPassenger");
            e.printStackTrace();
        }
    }

    public static boolean hasPermission (CommandSender sender, String permission) {
        return hasPermission(sender, permission, false);
    }

    public static boolean hasPermission (CommandSender sender, String permission, boolean strict) {
        if (sender instanceof ConsoleCommandSender) return true;
        if ((permission == null) || (permission.isEmpty())) return true;
        //if (sender.isOp()) return true;

        int value = getPermission(sender, permission, strict);
        if (PetCore.getInstance().getConfiguration().getStringList("Permissions.Ignored-List").contains(permission)) return true;
        return value == 1;
    }

    public static int getPermission(CommandSender sender, String permission, boolean strict) {
        if (PetCore.getInstance().getConfiguration().getBoolean("Permissions.Enabled")) {
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
        field = FieldAccessor.getField(Reflection.getNmsClass("Entity", "world.entity"), "s", Integer.TYPE);

        field.set(Reflection.getHandle(entity), 0);
    }

    public static void hidePet(PetUser user, IEntityPet entityPet) {
        UUID entityID = entityPet.getEntity().getUniqueId();
        if (entityPet instanceof IEntityControllerPet) entityID = ((IEntityControllerPet)entityPet).getVisibleEntity().getEntity().getUniqueId();
        managePetVisibility(user.getPlayer(), "PacketPlayOutEntityDestroy", Integer.TYPE, entityID);
    }

    public static void showPet(PetUser user, IEntityPet entityPet) {
        Entity entity = entityPet.getEntity();
        if (entityPet instanceof IEntityControllerPet) entity = ((IEntityControllerPet)entityPet).getVisibleEntity().getEntity();
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

    public static void makeBackup (YamlFile yamlFile, File backup) {
        File file = yamlFile.getFile();
        try {
            if (!backup.exists()) backup.createNewFile();
            FileUtils.copyFile(file, backup);
            SimplePets.getDebugLogger().debug(DebugBuilder.build(yamlFile.getClass()).setLevel(DebugLevel.NORMAL).setMessages(
                    "A new major config change was detected",
                    "Saving the old config to 'plugins/SimplePets/"+backup.getParentFile().getName()+"/"+backup.getName()+"'"
            ));
        } catch (IOException e) {
            SimplePets.getDebugLogger().debug(DebugBuilder.build(yamlFile.getClass()).setLevel(DebugLevel.ERROR).setMessages(
                    "Failed to create file backup for: "+file.getName(),
                    "Error: "+e.getMessage()
            ));
            e.printStackTrace();
        }

    }
}
