package simplepets.brainsynder.listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.inventory.Item;
import simplepets.brainsynder.api.plugin.SimplePets;
import simplepets.brainsynder.managers.InventoryManager;
import simplepets.brainsynder.menu.inventory.AddonMenu;
import simplepets.brainsynder.menu.inventory.holders.AddonHolder;
import simplepets.brainsynder.utils.Keys;

import java.util.Optional;

public class AddonGUIListener implements Listener {
    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getInventory().getHolder() == null) return;
        if (!(e.getInventory().getHolder() instanceof AddonHolder)) return;
        AddonMenu menu = InventoryManager.ADDONS;
        if ((e.getWhoClicked() instanceof Player)) {
            e.setCancelled(true);
            e.setResult(Event.Result.DENY);
            final Player player = (Player) e.getWhoClicked();
            if (e.getCurrentItem() == null) return;

            SimplePets.getUserManager().getPetUser(player).ifPresent(user -> {
                Optional<Item> optionalItem = SimplePets.getItemHandler().getItem(e.getCurrentItem());
                if (optionalItem.isPresent()) {
                    if (e.getClick().isShiftClick()) {
                        optionalItem.get().onShiftClick(user, menu);
                        return;
                    }
                    optionalItem.get().onClick(user, menu);
                    return;
                }

                ItemStack stack = e.getCurrentItem();
                ItemMeta meta = stack.getItemMeta();
                PersistentDataContainer container = meta.getPersistentDataContainer();
                String name = container.get(Keys.ADDON_NAME, PersistentDataType.STRING);
                if (container.has(Keys.ADDON_URL, PersistentDataType.STRING)) {
                    meta.setDisplayName(ChatColor.GRAY+"Downloading...");
                    stack.setItemMeta(meta);
                    e.getInventory().setItem(e.getRawSlot(), stack);

                    PetCore.getInstance().getAddonManager().downloadAddon(name, container.get(Keys.ADDON_URL, PersistentDataType.STRING), () -> {
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                menu.open(user, menu.getCurrentPage(user), menu.isInstallerGUI(user));
                            }
                        }.runTaskLater(PetCore.getInstance(), 10);
                    });
                    return;
                }
                PetCore.getInstance().getAddonManager().fetchAddon(name).ifPresent(addon -> {
                    if (e.isShiftClick() && container.has(Keys.ADDON_UPDATE, PersistentDataType.STRING)) {
                        meta.setDisplayName(ChatColor.GRAY+"Updating...");
                        stack.setItemMeta(meta);
                        e.getInventory().setItem(e.getRawSlot(), stack);

                        PetCore.getInstance().getAddonManager().update(addon, container.get(Keys.ADDON_UPDATE, PersistentDataType.STRING), () -> {
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    menu.open(user, menu.getCurrentPage(user), menu.isInstallerGUI(user));
                                }
                            }.runTaskLater(PetCore.getInstance(), 2);
                        });
                        return;
                    }

                    boolean enabled = !addon.isEnabled();
                    PetCore.getInstance().getAddonManager().toggleAddon(addon, enabled);

                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            menu.open(user, menu.getCurrentPage(user), menu.isInstallerGUI(user));
                        }
                    }.runTaskLater(PetCore.getInstance(), 2);
                });

            });
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent e) {
        if (e.getInventory().getHolder() == null) return;
        if (!(e.getInventory().getHolder() instanceof AddonHolder)) return;
        AddonMenu menu = InventoryManager.ADDONS;
        if (e.getPlayer().getOpenInventory() == null)
            SimplePets.getUserManager().getPetUser((Player) e.getPlayer()).ifPresent(menu::reset);
    }
}
