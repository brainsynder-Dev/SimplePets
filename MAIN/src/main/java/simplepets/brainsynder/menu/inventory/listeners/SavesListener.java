package simplepets.brainsynder.menu.inventory.listeners;

import lib.brainsynder.nbt.StorageTagCompound;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.scheduler.BukkitRunnable;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.menu.holders.SavesHolder;
import simplepets.brainsynder.menu.inventory.list.SavesMenu;
import simplepets.brainsynder.menu.items.Item;
import simplepets.brainsynder.player.PetOwner;

import java.util.List;

public class SavesListener implements Listener {
    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getInventory().getHolder() == null) return;
        if (!(e.getInventory().getHolder() instanceof SavesHolder)) return;
        SavesMenu menu = PetCore.get().getInvLoaders().SAVES;
        if ((e.getWhoClicked() instanceof Player)) {
            e.setCancelled(true);
            e.setResult(Event.Result.DENY);
            Player p = (Player) e.getWhoClicked();
            PetOwner owner = PetOwner.getPetOwner(p);
            if (e.getCurrentItem() == null) return;
            if (e.getClick().isShiftClick()) {
                e.setCancelled(true);
                return;
            }
            Item item = PetCore.get().getItemLoaders().getLoader(e.getCurrentItem());
            if (item != null) {
                item.onClick(owner, menu);
                return;
            }

            if (menu.getItemStorage(owner).isEmpty()) return;

            menu.getItemStorage(owner).forEach((compound, itemStack) -> {
                if (PetCore.get().getUtilities().isSimilar(itemStack, e.getCurrentItem())) {
                    if (e.getClick().isRightClick()) {
                        List<StorageTagCompound> saves = owner.getSavedPets();
                        saves.remove(compound);
                        owner.setSavedPets(saves);
                        p.closeInventory();
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                menu.open(owner);
                            }
                        }.runTaskLater(PetCore.get(), 2);
                        return;
                    }
                    if (compound.getString("PetType").equals("armor_stand")) {
                        compound.setBoolean("restricted", true);
                    }
                    owner.respawnPet(compound);
                }
            });
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent e) {
        if (e.getInventory().getHolder() == null) return;
        if (!(e.getInventory().getHolder() instanceof SavesHolder)) return;
        SavesMenu menu = PetCore.get().getInvLoaders().SAVES;
        PetOwner.getPetOwner((Player) e.getPlayer()).getFile().save(false, false);
        menu.reset(PetOwner.getPetOwner((Player) e.getPlayer()));
    }
}
