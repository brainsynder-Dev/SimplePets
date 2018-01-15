package simplepets.brainsynder.events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import simple.brainsynder.storage.IStorage;
import simple.brainsynder.storage.StorageList;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.PetTypeStorage;
import simplepets.brainsynder.api.event.inventory.PetInventoryOpenEvent;
import simplepets.brainsynder.api.event.inventory.PetInventorySelectTypeEvent;
import simplepets.brainsynder.menu.holders.PetHolder;
import simplepets.brainsynder.menu.items.Items;
import simplepets.brainsynder.pet.PetType;
import simplepets.brainsynder.player.PetOwner;
import simplepets.brainsynder.utils.PetMap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PetSelectionMenu implements Listener {
    private static final Pattern COMPILE = Pattern.compile("%pet%", Pattern.LITERAL);
    private static PetMap<String, IStorage<PetTypeStorage>> petMap = new PetMap<>();
    private static Map<String, Integer> pageSave = new HashMap<>();

    public static void openMenu(Player p, int page) {
        IStorage<Integer> slots = PetCore.get().getAvailableSlots().copy();
        pageSave.put(p.getName(), page);
        Inventory inv = Bukkit.createInventory(new PetHolder(), 54, PetCore.get().getMessages().getString("Menu-Name", true));
        int placeHolder = inv.getSize();
        while (placeHolder > 0) {
            if (!slots.contains((placeHolder - 1))) {
                inv.setItem(placeHolder - 1, Items.PLACEHOLDER.getItem());
            }
            placeHolder--;
        }
        if (PetCore.get().getConfiguration().getBoolean("Allow-Pets-Being-Hats"))
            inv.setItem(Items.HAT.getSlot(), Items.HAT.getItem());
        if (PetCore.get().getConfiguration().getBoolean("Allow-Pets-Being-Mounts"))
            inv.setItem(Items.RIDE.getSlot(), Items.RIDE.getItem());
        inv.setItem(Items.REMOVE.getSlot(), Items.REMOVE.getItem());
        if (PetCore.get().getConfiguration().getBoolean("PlayerPetNaming"))
            inv.setItem(Items.NAME.getSlot(), Items.NAME.getItem());

        if (PetCore.get().petTypes.totalPages() > (page)) {
            inv.setItem(Items.NEXT.getSlot(), Items.NEXT.getItem());
        }

        if (page > 1) {
            inv.setItem(Items.PREVIOUS.getSlot(), Items.PREVIOUS.getItem());
        }
        IStorage<PetTypeStorage> petTypes = new StorageList<>();
        for (PetType type : PetCore.get().petTypes.getPage(page)) {
            if (type.hasPermission(p)) {
                petTypes.add(new PetTypeStorage(type));
            }
        }
        PetInventoryOpenEvent event = new PetInventoryOpenEvent(petTypes, p);
        Bukkit.getServer().getPluginManager().callEvent(event);
        if (event.isCancelled()) return;
        IStorage<ItemStack> types = event.getItems().copy();
        while (types.hasNext()) {
            inv.addItem(types.next());
        }
        petMap.put(p.getName(), event.getShownPetTypes());
        p.openInventory(inv);
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getInventory().getHolder() == null) return;
        if (!(e.getInventory().getHolder() instanceof PetHolder)) return;

        if ((e.getWhoClicked() instanceof Player)) {
            e.setCancelled(true);
            e.setResult(Event.Result.DENY);
            final Player p = (Player) e.getWhoClicked();
            PetOwner petOwner = PetOwner.getPetOwner(p);
            if (e.getCurrentItem() == null)
                return;
            if (e.getClick().isShiftClick()) {
                e.setCancelled(true);
                return;
            }
            int currentPage = 1;
            if (pageSave.containsKey(p.getName())) currentPage = pageSave.get(p.getName());
            if (e.getSlot() == Items.REMOVE.getSlot()) {
                if (petOwner.hasPet()) {
                    petOwner.removePet();
                    p.closeInventory();
                }
            } else if (e.getSlot() == Items.NAME.getSlot()) {
                if (PetCore.get().getConfiguration().getBoolean("PlayerPetNaming")) {
                    p.closeInventory();
                    petOwner.renamePet();
                }
            } else if (e.getSlot() == Items.RIDE.getSlot()) {
                if (!petOwner.hasPet()) {
                    e.setCancelled(true);
                    return;
                }
                if (!PetCore.get().getConfiguration().getBoolean("Allow-Pets-Being-Mounts")) return;
                if (!petOwner.getPet().getPetType().canMount(p)) {
                    e.setCancelled(true);
                    return;
                }
                e.setCancelled(true);
                p.closeInventory();
                petOwner.getPet().setVehicle(!petOwner.getPet().isVehicle());
            } else if (e.getSlot() == Items.HAT.getSlot()) {
                if (!petOwner.hasPet()) return;
                if (!PetCore.get().getConfiguration().getBoolean("Allow-Pets-Being-Hats")) return;
                if (!petOwner.getPet().getPetType().canHat(p)) return;
                e.setCancelled(true);
                p.closeInventory();
                petOwner.getPet().setHat(!petOwner.getPet().isHat());
            } else if (e.getSlot() == Items.PREVIOUS.getSlot()) {
                if (currentPage > 1) openMenu(p, (currentPage - 1));
            } else if (e.getSlot() == Items.NEXT.getSlot()) {
                if (PetCore.get().petTypes.totalPages() > currentPage) openMenu(p, (currentPage + 1));
            } else {
                if (e.getCurrentItem() == null) return;
                final List<PetType> types = PetCore.get().petTypes.getPage(currentPage);
                if (types == null) return;
                if (!petMap.containsKey(p.getName())) return;
                IStorage<PetTypeStorage> storage = petMap.getKey(p.getName()).copy();
                while (storage.hasNext()) {
                    final PetTypeStorage type = storage.next();
                    if (type.getItem().isSimilar(e.getCurrentItem())) {
                        if (!type.getType().hasPermission(p)) return;
                        PetInventorySelectTypeEvent event = new PetInventorySelectTypeEvent(type.getType(), p);
                        Bukkit.getServer().getPluginManager().callEvent(event);
                        if (event.isCancelled()) return;
                        p.closeInventory();
                        p.sendMessage(COMPILE.matcher(PetCore.get().getMessages().getString("Select-Pet", true))
                                .replaceAll(Matcher.quoteReplacement(type.getType().getNoColorName())));
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                type.getType().setPet(p);
                            }
                        }.runTask(PetCore.get());
                        break;
                    }
                }
            }
        }
    }
}
