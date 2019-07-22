package simplepets.brainsynder.menu;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.json.simple.JSONObject;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.player.PetOwner;
import simplepets.brainsynder.storage.InventoryStorage;
import simplepets.brainsynder.storage.files.PlayerStorage;

public class ItemStorageMenu implements Listener {
    public static boolean loadFromPlayer(Player player) {
        PetOwner owner = PetOwner.getPetOwner(player);
        Inventory inventory = Bukkit.createInventory(new ItemHandler(), PetCore.get().getConfiguration().getInt("PetItemStorage.Inventory-Size"), player.getName() + "'s Item Storage");
        if (owner.getStoredInventory() != null) {
            InventoryStorage storage = InventoryStorage.fromJSON(new ItemHandler(), owner.getStoredInventory());
            inventory = storage.getInventory();
        }
        player.openInventory(inventory);
        return true;
    }

    public static boolean loadFromName(Player player, String name) {
        PetOwner owner = PetOwner.getPetOwner(name);
        JSONObject json = null;
        if (owner != null) {
            json = owner.getStoredInventory();
        }

        if (json == null) {
            PlayerStorage file = PetCore.get().getPlayerStorageByName(name);
            if (file == null) return false;
            if (file.hasKey("ItemStorage")) {
                json = file.getJSONObject("ItemStorage");
            }
        }
        if (json == null) return false;

        InventoryStorage storage = InventoryStorage.fromJSON(new ItemHandler(), json);
        player.openInventory(storage.getInventory());
        return true;
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onClose(InventoryCloseEvent e) {
        Player player = (Player) e.getPlayer();
        if (e.getView().getTopInventory().getHolder() == null)
            return;
        if (e.getView().getTopInventory().getHolder() instanceof ItemHandler) {
            String title = e.getView().getTitle();
            if (title.contains("'s Item Storage")) {
                String name = title.replace("'s Item Storage", "");
                if (!name.equalsIgnoreCase(player.getName())) return;
                InventoryStorage storage = new InventoryStorage(e.getInventory());
                PetOwner.getPetOwner(player).setStoredInventory(storage.toJSON(e));
            }
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        if (e.getView().getTopInventory().getHolder() == null)
            return;
        if (e.getView().getTopInventory().getHolder() instanceof ItemHandler) {
            String title = e.getView().getTitle();
            if (title.contains("'s Item Storage")) {
                String name = title.replace("'s Item Storage", "");
                if (!name.equalsIgnoreCase(player.getName())) {
                    e.setCancelled(true);
                    e.setResult(Event.Result.DENY);
                }
            }
        }
    }

    private static class ItemHandler implements InventoryHolder {
        @Override
        public Inventory getInventory() {
            return null;
        }
    }
}
