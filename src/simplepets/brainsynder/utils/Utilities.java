package simplepets.brainsynder.utils;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import simple.brainsynder.utils.Reflection;
import simple.brainsynder.utils.ServerVersion;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.errors.SimplePetsException;
import simplepets.brainsynder.player.PetOwner;
import simplepets.brainsynder.reflection.FieldAccessor;
import simplepets.brainsynder.reflection.ReflectionUtil;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

public class Utilities {

    public void setPassenger(Entity entity, Entity passenger) {
        try {
            entity.setPassenger(passenger);
            if (passenger instanceof Player) {
                sendMountPacket((Player) passenger, entity);
            }
            if (entity instanceof Player) {
                sendMountPacket((Player) entity, passenger);
            }
        } catch (Exception e) {
            PetCore.get().debug(2, "Could not run method IEntityPet#setPassenger");
            e.printStackTrace();
        }
    }

    public void removePassenger(Entity entity, Entity passenger) {
        try {
            entity.eject();
            if (entity instanceof Player) {
                resetRideCooldown (passenger);
                sendMountPacket((Player) entity, passenger);
            }
        } catch (Exception e) {
            PetCore.get().debug(2, "Could not run method IEntityPet#removePassenger");
            e.printStackTrace();
        }
    }

    public void resetRideCooldown (Entity entity) {
        FieldAccessor<Integer> field = FieldAccessor.getField(Reflection.getNmsClass("Entity"), "j", Integer.TYPE);
        field.set(Reflection.getHandle(entity), 0);
    }

    public void sendMountPacket(Player player, Entity entity) {
        if (ServerVersion.getVersion() == ServerVersion.v1_8_R3) return;
        Class<?> outMount = ReflectionUtil.getNmsClass("PacketPlayOutMount");
        Constructor<?> constructor = ReflectionUtil.fillConstructor(outMount, ReflectionUtil.getNmsClass("Entity"));
        Object packet = ReflectionUtil.initiateClass(constructor, ReflectionUtil.getEntityHandle(entity));
        ReflectionUtil.sendPacket(packet, player);
    }

    public void handlePathfinders(Player player, Entity entity, double speed) {
        try {
            Class<?> clazz = Class.forName("simplepets.brainsynder.nms.pathfinders." + ReflectionUtil.getVersion() + ".HandlePathfinders");
            if (clazz == null) throw new SimplePetsException("HandlePathfinders not found");
            Constructor<?> con = clazz.getDeclaredConstructor(Player.class, Entity.class, double.class);
            con.newInstance(player, entity, speed);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clearPathfinders(Entity entity) {
        try {
            Class<?> clazz = Class.forName("simplepets.brainsynder.nms.pathfinders." + ReflectionUtil.getVersion() + ".ClearPathfinders");
            if (clazz == null) throw new SimplePetsException("ClearPathfinders not found");
            Constructor<?> con = clazz.getDeclaredConstructor(Entity.class);
            con.newInstance(entity);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void hidePet(Player player) {
        managePetVisibility(player, "PacketPlayOutEntityDestroy", Integer.TYPE, PetOwner.getPetOwner(player).getPet().getEntity().getEntity().getEntityId());
    }

    public void showPet(Player player) {
        managePetVisibility(player, "PacketPlayOutSpawnEntityLiving", ReflectionUtil.getNmsClass("EntityLiving"), ReflectionUtil.getEntityHandle(PetOwner.getPetOwner(player).getPet().getEntity().getEntity()));
    }

    private void managePetVisibility(Player p, String nmsClass, Class<?> o1, Object o2) {
        Class<?> entity = ReflectionUtil.getNmsClass(nmsClass);
        Constructor<?> constructor = ReflectionUtil.fillConstructor(entity, o1);
        Object packet = ReflectionUtil.initiateClass(constructor, o2);
        ReflectionUtil.sendPacket(packet, p);
    }

    public boolean isSimilar(ItemStack main, ItemStack check) {
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

    public String itemToString(ItemStack itemStack) {
        YamlConfiguration config = new YamlConfiguration();
        config.set("i", itemStack);
        return config.saveToString();
    }

    public ItemStack stringToItem(String stringBlob) {
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
