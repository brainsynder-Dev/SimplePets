package simplepets.brainsynder.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.plugin.SimplePets;
import simplepets.brainsynder.managers.InventoryManager;

public class InteractListener implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onManipulate(PlayerArmorStandManipulateEvent e) {
        SimplePets.getSpawnUtil().getHandle(e.getRightClicked()).ifPresent(handle -> {
            if (handle instanceof IEntityPet) {
                e.setCancelled(true);
                IEntityPet entity = (IEntityPet) handle;
                if (entity.getOwnerUUID().equals(e.getPlayer().getUniqueId())) {
                    if (InventoryManager.PET_DATA.getType(e.getPlayer()) != entity.getPetType()) InventoryManager.PET_DATA.setType(e.getPlayer(), entity.getPetType());
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
            if (handle instanceof IEntityPet) {
                e.setCancelled(true);
                if (e.getPlayer().hasPermission("pet.open.data.changer")) {
                    IEntityPet entity = (IEntityPet) handle;
                    if (entity.getOwnerUUID().equals(e.getPlayer().getUniqueId())) {
                        if (InventoryManager.PET_DATA.getType(e.getPlayer()) != entity.getPetType()) InventoryManager.PET_DATA.setType(e.getPlayer(), entity.getPetType());
                        InventoryManager.PET_DATA.open(entity.getPetUser());
                    }
                }
            }
        });
    }

}
