package simplepets.brainsynder.menu.inventory.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.ambient.IEntityArmorStandPet;
import simplepets.brainsynder.menu.holders.ArmorHolder;
import simplepets.brainsynder.menu.inventory.list.ArmorMenu;
import simplepets.brainsynder.menu.items.Item;
import simplepets.brainsynder.menu.items.list.Air;
import simplepets.brainsynder.player.PetOwner;

public class ArmorListener implements Listener {
    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getView().getTopInventory().getHolder() == null) return;
        if (!(e.getView().getTopInventory().getHolder() instanceof ArmorHolder)) return;
        ArmorMenu menu = PetCore.get().getInvLoaders().ARMOR;
        ItemStack clickedItem;
        switch (e.getRawSlot()) {
            case 13:
            case 21:
            case 22:
            case 23:
            case 31:
            case 40:
                clickedItem = e.getCursor();
                break;
            default:
                clickedItem = e.getCurrentItem();
                break;
        }
        if (e.getRawSlot() > 54) {
            clickedItem = e.getCursor();
        }
        if (clickedItem == null) return;
        if (e.getWhoClicked() instanceof Player) {
            final Player p = (Player) e.getWhoClicked();
            PetOwner owner = PetOwner.getPetOwner(p);
            Item item = PetCore.get().getItemLoaders().getLoader(clickedItem);
            if (item != null && !(item instanceof Air)) {
                e.setCancelled(true);
                e.setResult(Event.Result.DENY);
                item.onClick(owner, menu);
                return;
            }
            menu.onClick(e.getRawSlot(), clickedItem, p);
        }
    }

    @EventHandler
    public void onClose (InventoryCloseEvent e) {
        if (e.getInventory().getHolder() == null) return;
        if (!(e.getInventory().getHolder() instanceof ArmorHolder)) return;
        ArmorMenu menu = PetCore.get().getInvLoaders().ARMOR;
        //PetCore.get().getItemLoaders().getLoader("update").onClick(PetOwner.getPetOwner((Player) e.getPlayer()), menu);
        menu.reset(PetOwner.getPetOwner((Player) e.getPlayer()));
    }
}
