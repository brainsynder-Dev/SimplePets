package simplepets.brainsynder.menu;

import lib.brainsynder.nbt.JsonToNBT;
import lib.brainsynder.nbt.StorageTagCompound;
import lib.brainsynder.nbt.StorageTagString;
import lib.brainsynder.nbt.other.NBTException;
import lib.brainsynder.utils.Base64Wrapper;
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
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.player.PetOwner;
import simplepets.brainsynder.storage.InventoryStorage;
import simplepets.brainsynder.storage.files.PlayerStorage;

public class ItemStorageMenu implements Listener {
    public static boolean loadFromPlayer(Player player) {
        PetOwner owner = PetOwner.getPetOwner(player);
        Inventory inventory = Bukkit.createInventory(new ItemHandler(), PetCore.get().getConfiguration().getInt("PetItemStorage.Inventory-Size", 27), player.getName() + "'s Item Storage");
        if (owner.getStoredInventory() != null) {
            InventoryStorage storage = InventoryStorage.fromCompound(new ItemHandler(), owner.getStoredInventory());
            inventory = storage.getInventory();
        }
        player.openInventory(inventory);
        return true;
    }

    public static void loadFromName(Player player, String name) {
        PetCore.get().getPlayerStorageByName(name, new PetCore.Call<PlayerStorage>() {
            @Override
            public void call(PlayerStorage file) {
                PetOwner owner = PetOwner.getPetOwner(name);
                StorageTagCompound compound = null;
                if (owner != null) {
                    compound = owner.getStoredInventory();
                }

                if (compound == null) {
                    if (file == null) {
                        onFail();
                        return;
                    }

                    if (file.hasKey("ItemStorage")) {
                        if (file.getTag("ItemStorage") instanceof StorageTagString) {
                            try {
                                compound = JsonToNBT.getTagFromJson(Base64Wrapper.decodeString(file.getString("ItemStorage")));
                            } catch (NBTException e) {
                                compound = null;
                            }
                        }else{
                            compound = file.getCompoundTag("ItemStorage");
                        }
                    }
                }
                if (compound == null) {
                    onFail();
                    return;
                }

                InventoryStorage storage = InventoryStorage.fromCompound(new ItemHandler(), compound);
                // Opening inventories async does not work
                Bukkit.getScheduler().runTask(PetCore.get(), () -> {
                    player.openInventory(storage.getInventory());
                });
            }

            @Override
            public void onFail() {
                player.sendMessage(PetCore.get().getCommands().getString("Inv.No-Pet-Items-Other")
                        .replace("%player%", name));
            }
        });
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
                InventoryStorage storage = new InventoryStorage(e.getInventory(), title);
                PetOwner.getPetOwner(player).setStoredInventory(storage.toCompound());
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
