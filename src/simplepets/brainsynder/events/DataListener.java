package simplepets.brainsynder.events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import simple.brainsynder.storage.IStorage;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.event.pet.PetDataChangeEvent;
import simplepets.brainsynder.menu.ItemStorageMenu;
import simplepets.brainsynder.menu.holders.PetDataHolder;
import simplepets.brainsynder.menu.items.Items;
import simplepets.brainsynder.menu.menuItems.base.MenuItem;
import simplepets.brainsynder.menu.menuItems.base.MenuItemAbstract;
import simplepets.brainsynder.player.PetOwner;

public class DataListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getInventory().getHolder() == null)
            return;
        if (e.getInventory().getHolder() instanceof PetDataHolder) {
            if (e.getWhoClicked() instanceof Player) {
                Player p = (Player) e.getWhoClicked();
                PetOwner petOwner = PetOwner.getPetOwner(p);
                if (e.getCurrentItem() == null)
                    return;
                e.setCancelled(true);
                if (e.getSlot() == Items.REMOVE.getSlot()) {
                    if (!e.getCurrentItem().isSimilar(Items.REMOVE.getItem())) return;
                    if (petOwner.hasPet()) {
                        petOwner.removePet();
                        p.closeInventory();
                    }
                } else if (e.getSlot() == Items.NAME.getSlot()) {
                    if (!e.getCurrentItem().isSimilar(Items.NAME.getItem())) return;
                    if (PetCore.get().getConfiguration().getBoolean("PlayerPetNaming")) {
                        p.closeInventory();
                        petOwner.renamePet();
                    }
                } else if (e.getSlot() == Items.STORAGE.getSlot()) {
                    if (!e.getCurrentItem().isSimilar(Items.STORAGE.getItem())) return;
                    if (PetCore.get().getConfiguration().getBoolean("PetItemStorage.Enable"))
                        ItemStorageMenu.loadFromPlayer(p);
                } else if (e.getSlot() == Items.RIDE.getSlot()) {
                    if (!e.getCurrentItem().isSimilar(Items.RIDE.getItem())) return;
                    if (!petOwner.hasPet()) {
                        e.setCancelled(true);
                        return;
                    }
                    if (!PetCore.get().getConfiguration().getBoolean("Allow-Pets-Being-Mounts"))
                        return;
                    if (!petOwner.getPet().getPetType().canMount(p)) {
                        e.setCancelled(true);
                        return;
                    }
                    if (!PetCore.hasPerm(p, "Pet.PetToMount")) {
                        e.setCancelled(true);
                        return;
                    }
                    e.setCancelled(true);
                    p.closeInventory();
                    petOwner.getPet().toggleRiding();
                } else if (e.getSlot() == Items.HAT.getSlot()) {
                    if (!e.getCurrentItem().isSimilar(Items.HAT.getItem())) return;
                    if (!petOwner.hasPet()) {
                        return;
                    }
                    if (!PetCore.get().getConfiguration().getBoolean("Allow-Pets-Being-Hats"))
                        return;
                    if (!petOwner.getPet().getPetType().canHat(p)) {
                        return;
                    }
                    e.setCancelled(true);
                    p.closeInventory();
                    petOwner.getPet().toggleHat();
                } else {
                    IStorage<MenuItem> menuItems = petOwner.getPet().getItems().copy();
                    if (!menuItems.isEmpty()) {
                        while (menuItems.hasNext()) {
                            MenuItemAbstract item = (MenuItemAbstract) menuItems.next();
                            ItemStack stack = item.getItem().create();
                            PetDataChangeEvent.ClickType type = PetDataChangeEvent.ClickType.LEFT_CLICK;
                            if (e.getClick().toString().toLowerCase().contains("right"))
                                type = PetDataChangeEvent.ClickType.RIGHT_CLICK;
                            if (stack.isSimilar(e.getCurrentItem())) {
                                PetDataChangeEvent event = new PetDataChangeEvent(item, type);
                                Bukkit.getServer().getPluginManager().callEvent(event);
                                if (!event.isCancelled()) {
                                    if (type == PetDataChangeEvent.ClickType.LEFT_CLICK) {
                                        item.onLeftClick();
                                    } else {
                                        item.onRightClick();
                                    }
                                    e.getInventory().setItem(e.getSlot(), item.getItem().create());
                                }
                                e.setCancelled(true);
                                return;
                            }
                        }
                    }
                }
            }
        }
    }
}
