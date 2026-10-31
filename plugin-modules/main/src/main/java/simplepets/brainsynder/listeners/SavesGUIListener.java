package simplepets.brainsynder.listeners;

import org.bsdevelopment.pluginutils.PluginUtilities;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.inventory.Item;
import simplepets.brainsynder.api.plugin.SimplePets;
import simplepets.brainsynder.api.plugin.config.MessageOption;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.managers.InventoryManager;
import simplepets.brainsynder.menu.inventory.SavesMenu;
import simplepets.brainsynder.menu.inventory.holders.SavesHolder;
import simplepets.brainsynder.utils.Utilities;

import java.util.Optional;

public class SavesGUIListener implements Listener {
    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getInventory().getHolder() == null) return;
        if (!(e.getInventory().getHolder() instanceof SavesHolder)) return;
        SavesMenu menu = InventoryManager.PET_SAVES;
        if ((e.getWhoClicked() instanceof Player player)) {
            e.setCancelled(true);
            e.setResult(Event.Result.DENY);
            Optional<PetUser> optionalUser = SimplePets.getUserManager().getPetUser(player);
            if (!optionalUser.isPresent()) return;
            PetUser user = optionalUser.get();
            if (e.getCurrentItem() == null) return;
            if (e.getClick().isShiftClick()) {
                e.setCancelled(true);
                return;
            }

            Optional<Item> optionalItem = Utilities.getClickedItem(menu, e);
            if (optionalItem.isPresent()) {
                optionalItem.get().onClick(user, menu);
                return;
            }

            if (menu.getItemStorage(user).isEmpty()) return;

            menu.getItemStorage(user).forEach((compound, entry) -> {
                if (Utilities.isSimilar(entry.getValue(), e.getCurrentItem())) {
                    if (e.getClick().isRightClick()) {
                        user.removePetSave(compound);
                        PluginUtilities.getScheduler().runTaskLater(() -> menu.open(user), 2);
                        return;
                    }

                    if (!user.canSpawnMorePets()) {
                        e.setCancelled(true);
                        player.closeInventory();
                        player.sendMessage(PetCore.getInstance().getMessageFile().getTranslation(MessageOption.CANT_SPAWN_MORE_PETS));
                        return;
                    }

                    if (compound.getString("PetType").equals("armor_stand"))
                        compound.setBoolean("restricted", true);

                    Utilities.handlePetSpawning(user, entry.getKey(), compound, true);
                }
            });
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent e) {
        if (e.getInventory().getHolder() == null) return;
        if (!(e.getInventory().getHolder() instanceof SavesHolder)) return;
        SavesMenu menu = InventoryManager.PET_SAVES;
        PluginUtilities.getScheduler().runTaskLater(() -> {
            if (!(e.getPlayer().getOpenInventory().getTopInventory().getHolder() instanceof SavesHolder)) {
                SimplePets.getUserManager().getPetUser((Player) e.getPlayer()).ifPresent(menu::reset);
            }
        }, 3);
    }
}
