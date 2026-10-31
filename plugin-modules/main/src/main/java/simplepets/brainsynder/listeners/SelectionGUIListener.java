package simplepets.brainsynder.listeners;

import org.bsdevelopment.nbt.StorageTagCompound;
import org.bsdevelopment.pluginutils.PluginUtilities;
import org.bsdevelopment.pluginutils.inventory.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.event.inventory.PetSelectTypeEvent;
import simplepets.brainsynder.api.event.inventory.PetTypeStorage;
import simplepets.brainsynder.api.inventory.Item;
import simplepets.brainsynder.api.plugin.SimplePets;
import simplepets.brainsynder.api.plugin.config.MessageOption;
import simplepets.brainsynder.managers.InventoryManager;
import simplepets.brainsynder.menu.inventory.SelectionMenu;
import simplepets.brainsynder.menu.inventory.holders.SelectionHolder;
import simplepets.brainsynder.utils.Utilities;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SelectionGUIListener implements Listener {
    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getInventory().getHolder() == null) return;
        if (!(e.getInventory().getHolder() instanceof SelectionHolder)) return;
        SelectionMenu menu = InventoryManager.SELECTION;
        if ((e.getWhoClicked() instanceof Player player)) {
            e.setCancelled(true);
            e.setResult(Event.Result.DENY);
            if (e.getCurrentItem() == null) return;

            SimplePets.getUserManager().getPetUser(player).ifPresent(user -> {
                Optional<Item> optionalItem = Utilities.getClickedItem(menu, e);
                if (optionalItem.isPresent()) {
                    if (e.getClick().isShiftClick()) {
                        optionalItem.get().onShiftClick(user, menu);
                        return;
                    }
                    optionalItem.get().onClick(user, menu);
                    return;
                }

                if (!menu.getPetMap().containsKey(player.getName())) {
                    e.setCancelled(true);
                    player.closeInventory();
                    return;
                }

                List<PetTypeStorage> storage = new ArrayList<>(menu.getPetMap().get(player.getName()));
                for (PetTypeStorage type : storage) {
                    if (!ItemBuilder.of(type.getItem()).build().isSimilar(e.getCurrentItem())) continue;
                    PetSelectTypeEvent event = new PetSelectTypeEvent(type.getType(), user);
                    Bukkit.getServer().getPluginManager().callEvent(event);

                    if (event.isCancelled()) return;

                    if (!user.canSpawnMorePets()) {
                        e.setCancelled(true);
                        player.closeInventory();
                        player.sendMessage(PetCore.getInstance().getMessageFile().getTranslation(MessageOption.CANT_SPAWN_MORE_PETS));
                        return;
                    }

                    PluginUtilities.getScheduler().runTask(() ->
                        Utilities.handlePetSpawning(user, type.getType(), new StorageTagCompound(), false));
                    break;

                }
            });
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent e) {
        if (e.getInventory().getHolder() == null) return;
        if (!(e.getInventory().getHolder() instanceof SelectionHolder)) return;
        SelectionMenu menu = InventoryManager.SELECTION;
        PluginUtilities.getScheduler().runTaskLater(() -> {
            if (!(e.getPlayer().getOpenInventory().getTopInventory().getHolder() instanceof SelectionHolder)) {
                SimplePets.getUserManager().getPetUser((Player) e.getPlayer()).ifPresent(menu::reset);
            }
        }, 3);
    }
}
