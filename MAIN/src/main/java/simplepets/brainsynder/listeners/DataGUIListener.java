package simplepets.brainsynder.listeners;

import lib.brainsynder.item.ItemBuilder;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import simplepets.brainsynder.api.inventory.Item;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.plugin.SimplePets;
import simplepets.brainsynder.managers.InventoryManager;
import simplepets.brainsynder.menu.inventory.DataMenu;
import simplepets.brainsynder.menu.inventory.holders.PetDataHolder;
import simplepets.brainsynder.utils.Utilities;

import java.util.Optional;

public class DataGUIListener implements Listener {
    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getInventory().getHolder() == null) return;
        if (!(e.getInventory().getHolder() instanceof PetDataHolder)) return;

        DataMenu menu = InventoryManager.PET_DATA;
        if ((e.getWhoClicked() instanceof Player)) {
            e.setCancelled(true);
            e.setResult(Event.Result.DENY);

            Player player = (Player) e.getWhoClicked();

            SimplePets.getUserManager().getPetUser(player).ifPresent(user -> {
                if (e.getCurrentItem() == null) return;

                PetType type = menu.getType(player);

                user.getPetEntity(type).ifPresent(entityPet -> {

                    Optional<Item> optionalItem = SimplePets.getItemHandler().getItem(e.getCurrentItem());
                    if (optionalItem.isPresent()) {
                        if (e.getClick().isShiftClick()) {
                            optionalItem.get().onShiftClick(user, menu, entityPet);
                            return;
                        }
                        optionalItem.get().onClick(user, menu, entityPet);
                        return;
                    }

                    type.getPetData().forEach(petData -> {
                        petData.getItem(entityPet).ifPresent(o -> {
                            ItemBuilder builder = (ItemBuilder) o;

                            if (Utilities.isSimilar(builder.build(), e.getCurrentItem())) {
                                if (e.getClick().toString().toLowerCase().contains("right")) {
                                    petData.onRightClick(entityPet);
                                }else{
                                    petData.onLeftClick(entityPet);
                                }
                                petData.getItem(entityPet).ifPresent(o1 -> e.getInventory().setItem(e.getSlot(), ((ItemBuilder)o1).build()));
                            }
                        });
                    });
                });
            });
        }
    }

    @EventHandler
    public void onClose (InventoryCloseEvent e) {
        if (e.getInventory().getHolder() == null) return;
        if (!(e.getInventory().getHolder() instanceof PetDataHolder)) return;
        DataMenu menu = InventoryManager.PET_DATA;
        SimplePets.getUserManager().getPetUser((Player) e.getPlayer()).ifPresent(menu::reset);
    }
}
