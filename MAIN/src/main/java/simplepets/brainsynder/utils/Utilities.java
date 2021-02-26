package simplepets.brainsynder.utils;

import lib.brainsynder.ServerVersion;
import lib.brainsynder.reflection.FieldAccessor;
import lib.brainsynder.reflection.Reflection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.misc.IEntityControllerPet;
import simplepets.brainsynder.api.plugin.SimplePets;
import simplepets.brainsynder.api.user.PetUser;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Utilities {
    public static void setPassenger(Entity entity, Entity passenger) {
        try {
            entity.setPassenger(passenger);
            if (passenger instanceof Player) {
                sendMountPacket((Player) passenger, entity);
            }
            if (entity instanceof Player) {
                sendMountPacket((Player) entity, passenger);
            }
        } catch (Exception e) {
            Debug.debug(DebugLevel.ERROR, "Could not run method IEntityPet#setPassenger");
            e.printStackTrace();
        }
    }

    public static void removePassenger(Entity entity, Entity passenger) {
        try {
            entity.eject();
            if (entity instanceof Player) {
                resetRideCooldown(passenger);
                sendMountPacket((Player) entity, passenger);
            }
        } catch (Exception e) {
            Debug.debug(DebugLevel.ERROR, "Could not run method IEntityPet#removePassenger");
            e.printStackTrace();
        }
    }

    public static void resetRideCooldown(Entity entity) {
        FieldAccessor<Integer> field;
        if (ServerVersion.getVersion() == ServerVersion.v1_13_R1 || ServerVersion.getVersion() == ServerVersion.v1_13_R2) {
            field = FieldAccessor.getField(Reflection.getNmsClass("Entity"), "k", Integer.TYPE);
        } else {
            field = FieldAccessor.getField(Reflection.getNmsClass("Entity"), "j", Integer.TYPE);
        }
        field.set(Reflection.getHandle(entity), 0);
    }

    public static void sendMountPacket(Player player, Entity entity) {
        if (ServerVersion.getVersion() == ServerVersion.v1_8_R3) return;
        SimplePets.getSpawnUtil().getHandle(entity).ifPresent(handle -> {
            Class<?> outMount = Reflection.getNmsClass("PacketPlayOutMount");
            Constructor<?> constructor = Reflection.fillConstructor(outMount, Reflection.getNmsClass("Entity"));
            Object packet = Reflection.initiateClass(constructor, handle);
            Reflection.sendPacket(player, packet);
        });
    }

    public static void hidePet(PetUser user, IEntityPet entityPet) {
        UUID entityID = entityPet.getEntity().getUniqueId();
        if (entityPet instanceof IEntityControllerPet) entityID = ((IEntityControllerPet)entityPet).getVisibleEntity().getEntity().getUniqueId();
        managePetVisibility((Player) user.getPlayer(), "PacketPlayOutEntityDestroy", Integer.TYPE, entityID);
    }

    public static void showPet(PetUser user, IEntityPet entityPet) {
        Entity entity = entityPet.getEntity();
        if (entityPet instanceof IEntityControllerPet) entity = ((IEntityControllerPet)entityPet).getVisibleEntity().getEntity();
        managePetVisibility((Player) user.getPlayer(), "PacketPlayOutSpawnEntityLiving", Reflection.getNmsClass("EntityLiving"), entity);
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
}
