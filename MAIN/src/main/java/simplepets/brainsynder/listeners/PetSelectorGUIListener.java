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
import simplepets.brainsynder.managers.InventoryManager;
import simplepets.brainsynder.menu.inventory.PetSelectorMenu;
import simplepets.brainsynder.menu.inventory.holders.SelectorHolder;
import simplepets.brainsynder.utils.Keys;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class PetSelectorGUIListener implements Listener {
    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getInventory().getHolder() == null) return;
        if (!(e.getInventory().getHolder() instanceof SelectorHolder)) return;
        PetSelectorMenu menu = InventoryManager.SELECTOR;
        if ((e.getWhoClicked() instanceof Player)) {
            e.setCancelled(true);
            e.setResult(Event.Result.DENY);
            final Player player = (Player) e.getWhoClicked();
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
                PetType.getPetType(rawType).ifPresent(type -> menu.getTask(player.getName()).run(user, type));
            });
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent e) {
        if (e.getInventory().getHolder() == null) return;
        if (!(e.getInventory().getHolder() instanceof SelectorHolder)) return;
        PetSelectorMenu menu = InventoryManager.SELECTOR;

        Player player = (Player) e.getPlayer();
        PetCore.getInstance().getScheduler().getImpl().runAtEntityLater(player, () -> {
            if (!(player.getOpenInventory().getTopInventory().getHolder() instanceof SelectorHolder)) {
                SimplePets.getUserManager().getPetUser(player).ifPresent(menu::reset);
            }
        }, 150L, TimeUnit.MILLISECONDS);
    }
}
