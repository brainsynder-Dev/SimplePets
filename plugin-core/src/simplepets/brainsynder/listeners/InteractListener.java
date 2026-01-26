package simplepets.brainsynder.listeners;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.util.BlockIterator;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.plugin.SimplePets;
import simplepets.brainsynder.api.plugin.config.ConfigOption;
import simplepets.brainsynder.managers.InventoryManager;

public class InteractListener implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onManipulate(PlayerArmorStandManipulateEvent e) {
        SimplePets.getSpawnUtil().getHandle(e.getRightClicked()).ifPresent(handle -> {
            if (handle instanceof IEntityPet entity) {
                e.setCancelled(true);
                if (entity.getOwnerUUID().equals(e.getPlayer().getUniqueId())) {
                    if (ConfigOption.INSTANCE.MISC_TOGGLES_DISABLE_CLICKING.getValue()) return;
                    if (ConfigOption.INSTANCE.MISC_TOGGLES_LINE_OF_SIGHT_REQUIRED.getValue()
                            && !hasLineOfSight(e.getPlayer(), e.getRightClicked())) return;
                    if (InventoryManager.PET_DATA.getType(e.getPlayer()) != entity.getPetType())
                        InventoryManager.PET_DATA.setType(e.getPlayer(), entity.getPetType());
                    InventoryManager.PET_DATA.open(entity.getPetUser());
                }
            }
        });
    }

//    Is this even used anymore?
//    @EventHandler
//    public void onInteract(EntityMountEvent e) {
//        SimplePets.getSpawnUtil().getHandle(e.getMount()).ifPresent(handle -> {
//            if (handle instanceof IEntityPet) {
//                IEntityPet entity = (IEntityPet) handle;
//                e.setCancelled(!entity.getPetUser().isPetVehicle(entity.getPetType()));
//            }
//        });
//    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInteract(PlayerInteractEntityEvent e) {
        SimplePets.getSpawnUtil().getHandle(e.getRightClicked()).ifPresent(handle -> {
            if (handle instanceof IEntityPet entity) {
                e.setCancelled(true);
                if (entity.getOwnerUUID().equals(e.getPlayer().getUniqueId())) {
                    if (ConfigOption.INSTANCE.MISC_TOGGLES_DISABLE_CLICKING.getValue()) return;
                    if (ConfigOption.INSTANCE.MISC_TOGGLES_LINE_OF_SIGHT_REQUIRED.getValue()
                            && !hasLineOfSight(e.getPlayer(), e.getRightClicked())) return;
                    if (InventoryManager.PET_DATA.getType(e.getPlayer()) != entity.getPetType())
                        InventoryManager.PET_DATA.setType(e.getPlayer(), entity.getPetType());
                    InventoryManager.PET_DATA.open(entity.getPetUser());
                }
            }
        });
    }

    /**
     * Checks if a player has clear line of sight to the target entity.
     * This prevents exploits where players access pets through blocks
     * that appear broken client-side but still exist server-side.
     *
     * @param player The player to check line of sight from
     * @param target The entity the player is trying to interact with
     * @return true if the player can see the target entity, false if blocked
     */
    private static boolean hasLineOfSight(Player player, Entity target) {
        Location eye = player.getEyeLocation();
        Location targetLoc = target.getLocation().add(0, target.getHeight() / 2, 0);
        Material eyeMaterial = eye.getBlock().getType();
        boolean passThroughWater = (eyeMaterial == Material.WATER);

        double distance = eye.distance(targetLoc);
        int maxDistance = (int) Math.ceil(distance) + 1;

        try {
            BlockIterator iterator = new BlockIterator(player.getLocation(), player.getEyeHeight(), maxDistance);
            while (iterator.hasNext()) {
                Block block = iterator.next();
                Location blockCenter = block.getLocation().add(0.5, 0.5, 0.5);

                if (blockCenter.distance(targetLoc) < 1.5) {
                    return true;
                }

                Material type = block.getType();
                if (!Tag.REPLACEABLE.isTagged(type) && (!passThroughWater || type != Material.WATER)) {
                    return false;
                }
            }
        } catch (IllegalStateException ignored) {
            return false;
        }

        return true;
    }
}
