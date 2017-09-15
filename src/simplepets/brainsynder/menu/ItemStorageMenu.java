package simplepets.brainsynder.menu;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.files.PlayerPetInv;
import simplepets.brainsynder.utils.InventoryStorage;

import java.util.Map;

public class ItemStorageMenu implements Listener {
    public static boolean loadFromPlayer(Player player) {
        PlayerPetInv inv = PetCore.get().getPlayerPetInv(player);
        if (inv == null)
            return false;
        Inventory inventory = Bukkit.createInventory(new ItemHandler(), 27, player.getName() + "'s Item Storage");
        if (inv.isSet("ItemStorage")) {
            ConfigurationSection section = inv.getSection("ItemStorage");
            Map map = section.getValues(false);
            InventoryStorage storage = new InventoryStorage(new ItemHandler(), map);
            inventory = storage.getInventory();
        }
        player.openInventory(inventory);
        return true;
    }

    public static boolean loadFromName(Player player, String name) {
        PlayerPetInv inv = PetCore.get().getPetInvByName(name);
        if (inv == null)
            return false;
        Inventory inventory = Bukkit.createInventory(new ItemHandler(), 27, name + "'s Item Storage");
        if (inv.isSet("ItemStorage")) {
            ConfigurationSection section = inv.getSection("ItemStorage");
            Map map = section.getValues(false);
            InventoryStorage storage = new InventoryStorage(new ItemHandler(), map);
            inventory = storage.getInventory();
        }
        player.openInventory(inventory);
        return true;
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onClose(InventoryCloseEvent e) {
        Player player = (Player) e.getPlayer();
        if (e.getView().getTopInventory().getHolder() == null)
            return;
        if (e.getView().getTopInventory().getHolder() instanceof ItemHandler) {
            if (e.getView().getTopInventory().getTitle().contains("'s Item Storage")) {
                String name = e.getView().getTopInventory().getTitle().replace("'s Item Storage", "");
                if (!name.equalsIgnoreCase(player.getName())) return;
                PlayerPetInv inv = PetCore.get().getPlayerPetInv(player);
                InventoryStorage storage = new InventoryStorage(e.getInventory());
                if (inv.isSet("Username")) {
                    if (!inv.getString("Username").equalsIgnoreCase(player.getName()))
                        inv.set("Username", player.getName());
                } else {
                    inv.set("Username", player.getName());
                }
                inv.set("ItemStorage", storage.serialize());
            }
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        if (e.getView().getTopInventory().getHolder() == null)
            return;
        if (e.getView().getTopInventory().getHolder() instanceof ItemHandler) {
            if (e.getView().getTopInventory().getTitle().contains("'s Item Storage")) {
                String name = e.getView().getTopInventory().getTitle().replace("'s Item Storage", "");
                PlayerPetInv inv = PetCore.get().getPlayerPetInv(player);
                if (!name.equalsIgnoreCase(player.getName())) {
                    e.setCancelled(true);
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
