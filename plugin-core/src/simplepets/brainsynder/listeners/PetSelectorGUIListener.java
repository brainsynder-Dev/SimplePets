package simplepets.brainsynder.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.inventory.Item;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.plugin.SimplePets;
import simplepets.brainsynder.api.plugin.config.ConfigOption;
import simplepets.brainsynder.managers.InventoryManager;
import simplepets.brainsynder.menu.inventory.PetSelectorMenu;
import simplepets.brainsynder.menu.inventory.holders.SelectorHolder;
import simplepets.brainsynder.utils.Keys;
import simplepets.brainsynder.utils.Utilities;

import java.util.Optional;

public class PetSelectorGUIListener implements Listener {
    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getInventory().getHolder() == null) return;
        if (!(e.getInventory().getHolder() instanceof SelectorHolder)) return;
        PetSelectorMenu menu = InventoryManager.SELECTOR;
        if ((e.getWhoClicked() instanceof Player player)) {
            e.setCancelled(true);
            e.setResult(Event.Result.DENY);
            ItemStack stack = e.getCurrentItem();
            if (stack == null) return;

            SimplePets.getUserManager().getPetUser(player).ifPresent(user -> {
                Optional<Item> optionalItem = SimplePets.getItemHandler().getItem(stack);
                if (optionalItem.isPresent()) {
                    if (e.getClick().isShiftClick()) {
                        optionalItem.get().onShiftClick(user, menu);
                        return;
                    }
                    optionalItem.get().onClick(user, menu);
                    return;
                }

                ItemMeta meta = stack.getItemMeta();
                if (!meta.getPersistentDataContainer().has(Keys.PET_TYPE_ITEM, PersistentDataType.STRING)) return;
                String rawType = meta.getPersistentDataContainer().get(Keys.PET_TYPE_ITEM, PersistentDataType.STRING);
                PetType.getPetType(rawType).ifPresent(type -> {
                    if (Utilities.hasPermission(player, type.getPermission())
                            || (user.getOwnedPets().contains(type) && ConfigOption.INSTANCE.UTILIZE_PURCHASED_PETS.getValue()))
                        menu.getTask(player.getName()).run(user, type);
                });
            });
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent e) {
        if (e.getInventory().getHolder() == null) return;
        if (!(e.getInventory().getHolder() instanceof SelectorHolder)) return;
        PetSelectorMenu menu = InventoryManager.SELECTOR;
        Bukkit.getScheduler().runTaskLater(PetCore.getInstance(), () -> {
            if (!(e.getPlayer().getOpenInventory().getTopInventory().getHolder() instanceof SelectorHolder)) {
                SimplePets.getUserManager().getPetUser((Player) e.getPlayer()).ifPresent(menu::reset);
            }
        }, 3);
    }
}
