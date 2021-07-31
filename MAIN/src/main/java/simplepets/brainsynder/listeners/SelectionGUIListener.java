package simplepets.brainsynder.listeners;

import lib.brainsynder.item.ItemBuilder;
import lib.brainsynder.nbt.StorageTagCompound;
import lib.brainsynder.storage.IStorage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.scheduler.BukkitRunnable;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.event.inventory.PetSelectTypeEvent;
import simplepets.brainsynder.api.event.inventory.PetTypeStorage;
import simplepets.brainsynder.api.inventory.Item;
import simplepets.brainsynder.api.plugin.SimplePets;
import simplepets.brainsynder.files.MessageFile;
import simplepets.brainsynder.files.options.MessageOption;
import simplepets.brainsynder.managers.InventoryManager;
import simplepets.brainsynder.menu.inventory.SelectionMenu;
import simplepets.brainsynder.menu.inventory.holders.SelectionHolder;
import simplepets.brainsynder.utils.Utilities;

import java.util.Optional;

public class SelectionGUIListener implements Listener {
    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getInventory().getHolder() == null) return;
        if (!(e.getInventory().getHolder() instanceof SelectionHolder)) return;
        SelectionMenu menu = InventoryManager.SELECTION;
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

                if (!menu.getPetMap().containsKey(player.getName())) {
                    e.setCancelled(true);
                    player.closeInventory();
                    return;
                }

                if (!user.canSpawnMorePets()) {
                    e.setCancelled(true);
                    player.closeInventory();
                    player.sendMessage(MessageFile.getTranslation(MessageOption.CANT_SPAWN_MORE_PETS));
                    return;
                }

                IStorage<PetTypeStorage> storage = menu.getPetMap().get(player.getName()).copy();
                while (storage.hasNext()) {
                    final PetTypeStorage type = storage.next();
                    if (!ItemBuilder.fromItem(type.getItem()).isSimilar(e.getCurrentItem())) continue;
                    PetSelectTypeEvent event = new PetSelectTypeEvent(type.getType(), user);
                    Bukkit.getServer().getPluginManager().callEvent(event);

                    if (event.isCancelled()) return;
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            Utilities.handlePetSpawning(user, type.getType(), new StorageTagCompound(), false); }
                    }.runTask(PetCore.getInstance());
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
        SimplePets.getUserManager().getPetUser((Player) e.getPlayer()).ifPresent(menu::reset);
    }
}
