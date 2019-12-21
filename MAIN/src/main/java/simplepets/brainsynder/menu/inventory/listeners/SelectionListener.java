package simplepets.brainsynder.menu.inventory.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.scheduler.BukkitRunnable;
import simple.brainsynder.storage.IStorage;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.event.inventory.PetInventorySelectTypeEvent;
import simplepets.brainsynder.menu.holders.SelectionHolder;
import simplepets.brainsynder.menu.inventory.list.SelectionMenu;
import simplepets.brainsynder.menu.items.Item;
import simplepets.brainsynder.player.PetOwner;
import simplepets.brainsynder.storage.PetTypeStorage;

public class SelectionListener implements Listener {
    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getInventory().getHolder() == null) return;
        if (!(e.getInventory().getHolder() instanceof SelectionHolder)) return;
        SelectionMenu menu = PetCore.get().getInvLoaders().SELECTION;
        if ((e.getWhoClicked() instanceof Player)) {
            e.setCancelled(true);
            e.setResult(Event.Result.DENY);
            final Player p = (Player) e.getWhoClicked();
            PetOwner owner = PetOwner.getPetOwner(p);
            if (e.getCurrentItem() == null) return;
            if (e.getClick().isShiftClick()) {
                return;
            }
            Item item = PetCore.get().getItemLoaders().getLoader(e.getCurrentItem());
            if (item != null) {
                item.onClick(owner, menu);
                return;
            }

            if (!menu.getPetMap().containsKey(p.getName())) {
                return;
            }
            IStorage<PetTypeStorage> storage = menu.getPetMap().getKey(p.getName()).copy();
            while (storage.hasNext()) {
                final PetTypeStorage type = storage.next();
                if (type.getItem().getItemMeta().getDisplayName().equals(e.getCurrentItem().getItemMeta().getDisplayName())) {
                    if (!type.getType().hasPermission(p)) return;
                    PetInventorySelectTypeEvent event = new PetInventorySelectTypeEvent(type.getType(), p);
                    Bukkit.getServer().getPluginManager().callEvent(event);
                    if (event.isCancelled()) return;
                    p.closeInventory();
                    p.sendMessage(PetCore.get().getMessages().getString("Select-Pet", true).replace("%pet%", type.getType().getDisplayName()));
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            type.getType().setPet(p);
                        }
                    }.runTask(PetCore.get());
                    break;
                }
            }
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent e) {
        if (e.getInventory().getHolder() == null) return;
        if (!(e.getInventory().getHolder() instanceof SelectionHolder)) return;
        SelectionMenu menu = PetCore.get().getInvLoaders().SELECTION;
        if (e.getPlayer().getOpenInventory() == null)
            menu.reset(PetOwner.getPetOwner((Player) e.getPlayer()));
    }
}
