package simplepets.brainsynder.menu.inventory.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.ItemStack;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.ambient.IEntityArmorStandPet;
import simplepets.brainsynder.menu.holders.ArmorHolder;
import simplepets.brainsynder.menu.inventory.list.ArmorMenu;
import simplepets.brainsynder.menu.items.Item;
import simplepets.brainsynder.menu.items.list.Air;
import simplepets.brainsynder.player.PetOwner;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class ArmorListener implements Listener {

    private static final HashSet<Integer> slots = new HashSet<>(Arrays.asList(13, 21, 22, 23, 31, 40));

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getView().getTopInventory().getHolder() == null) return;
        if (!(e.getView().getTopInventory().getHolder() instanceof ArmorHolder)) return;
        ArmorMenu menu = PetCore.get().getInvLoaders().ARMOR;
        ItemStack clickedItem;
        int slot = e.getRawSlot();
        switch (e.getRawSlot()) {
            case 13:
            case 21:
            case 22:
            case 23:
            case 31:
            case 40:
                clickedItem = e.getCursor();
                break;
            default:
                clickedItem = e.getCurrentItem();
                break;
        }
        if (e.isShiftClick()) {
            clickedItem = e.getCurrentItem();
            if (e.getRawSlot() > 54) {
                slot = e.getView().getTopInventory().firstEmpty();
            } else if (e.getView().getBottomInventory().firstEmpty() != -1) {
                clickedItem = new ItemStack(Material.AIR);
            }
        }
        if (clickedItem == null) return;
        if (e.getWhoClicked() instanceof Player) {
            final Player p = (Player) e.getWhoClicked();
            PetOwner owner = PetOwner.getPetOwner(p);
            if (!slots.contains(e.getRawSlot()) && e.getRawSlot() < 54) {
                Item item = PetCore.get().getItemLoaders().getLoader(clickedItem);
                if (item != null && !(item instanceof Air)) {
                    e.setCancelled(true);
                    e.setResult(Event.Result.DENY);
                    item.onClick(owner, menu);
                    return;
                }
            }

            if (owner.getPet() == null) {
                e.setCancelled(true);
                Bukkit.getScheduler().runTask(PetCore.get(), p::closeInventory);
                return;
            }
            IEntityPet pet = owner.getPet().getVisableEntity();
            if (!(pet instanceof IEntityArmorStandPet)) return;
            if (((IEntityArmorStandPet) pet).isRestricted()) {
                p.sendMessage(PetCore.get().getMessages().getString("ArmorMenu.Restricted", true));
                e.setCancelled(true);
                e.setResult(Event.Result.DENY);
                return;
            }
            menu.onClick(slot, clickedItem, p);
        }
    }

    @EventHandler
    public void onDragEvent(InventoryDragEvent e) {
        if (e.getView().getTopInventory().getHolder() == null) return;
        if (!(e.getView().getTopInventory().getHolder() instanceof ArmorHolder)) return;
        ArmorMenu menu = PetCore.get().getInvLoaders().ARMOR;
        if (e.getWhoClicked() instanceof Player) {
            final Player p = (Player) e.getWhoClicked();
            PetOwner owner = PetOwner.getPetOwner(p);
            if (owner.getPet() == null) {
                e.setCancelled(true);
                Bukkit.getScheduler().runTask(PetCore.get(), p::closeInventory);
                return;
            }
            IEntityPet pet = owner.getPet().getVisableEntity();
            if (!(pet instanceof IEntityArmorStandPet)) return;
            if (((IEntityArmorStandPet) pet).isRestricted()) {
                p.sendMessage(PetCore.get().getMessages().getString("ArmorMenu.Restricted", true));
                e.setCancelled(true);
                e.setResult(Event.Result.DENY);
                return;
            }
            Map<Integer, ItemStack> items = e.getNewItems();
            for (Integer slot : items.keySet()) {
                ItemStack clickedItem = items.get(slot);
                if (clickedItem == null) continue;
                if (!slots.contains(slot)) {
                    Item item = PetCore.get().getItemLoaders().getLoader(clickedItem);
                    if (item != null && !(item instanceof Air)) {
                        e.setCancelled(true);
                        e.setResult(Event.Result.DENY);
                        item.onClick(owner, menu);
                        continue;
                    }
                }
                menu.onClick(slot, clickedItem, p);
            }
        }
    }

    @EventHandler
    public void onClose (InventoryCloseEvent e) {
        if (e.getInventory().getHolder() == null) return;
        if (!(e.getInventory().getHolder() instanceof ArmorHolder)) return;
        ArmorMenu menu = PetCore.get().getInvLoaders().ARMOR;
        //PetCore.get().getItemLoaders().getLoader("update").onClick(PetOwner.getPetOwner((Player) e.getPlayer()), menu);
        menu.reset(PetOwner.getPetOwner((Player) e.getPlayer()));
    }
}
