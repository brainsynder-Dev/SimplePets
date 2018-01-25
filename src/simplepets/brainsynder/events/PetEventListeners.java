package simplepets.brainsynder.events;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.json.simple.JSONArray;
import simple.brainsynder.api.ItemMaker;
import simple.brainsynder.storage.IStorage;
import simple.brainsynder.storage.StorageList;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.PetTypeStorage;
import simplepets.brainsynder.files.EconomyFile;
import simplepets.brainsynder.inventory.PetInventoryOpenEvent;
import simplepets.brainsynder.inventory.PetInventorySelectTypeEvent;
import simplepets.brainsynder.links.IVaultLink;
import simplepets.brainsynder.pet.PetNameChangeEvent;
import simplepets.brainsynder.pet.PetType;
import simplepets.brainsynder.player.PetOwner;
import simplepets.brainsynder.utils.LinkRetriever;

import java.util.Arrays;
import java.util.List;

public class PetEventListeners implements Listener {
    private EconomyFile economyFile;

    public PetEventListeners() {
        economyFile = new EconomyFile();
        economyFile.loadDefaults();
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onSelect(PetInventorySelectTypeEvent event) {
        if (!PetCore.get().getConfiguration().getBoolean("UseVaultEconomy")) return;
        if (event.getPlayer().hasPermission("Pets.economy.bypass")) return;

        double price = economyFile.getPrice(event.getPetType());

        if (price == -1) return;
        if (!LinkRetriever.getPluginLink(IVaultLink.class).isHooked()) return;

        double bal = LinkRetriever.getPluginLink(IVaultLink.class).getBalance(event.getPlayer());
        if (economyFile.getBoolean("Pay-Per-Use.Enabled")) {
            if (bal < price) {
                event.setCancelled(true);
                event.getPlayer().sendMessage(economyFile.getString("InsufficientFunds", true).replace("%price%", String.valueOf(price)));
                return;
            }

            LinkRetriever.getPluginLink(IVaultLink.class).withdrawPlayer(event.getPlayer(), price);
            event.getPlayer().sendMessage(economyFile.getString("Pay-Per-Use.Paid", true).replace("%type%", event.getPetType().getConfigName()));
            return;
        }

        PetOwner petOwner = PetOwner.getPetOwner(event.getPlayer());
        JSONArray petArray = petOwner.getOwnedPets();
        if (petArray.contains(event.getPetType().getConfigName()))
            return;

        if (bal < price) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(economyFile.getString("InsufficientFunds", true).replace("%price%", String.valueOf(price)));
            return;
        }

        petOwner.addPurchasedPet(event.getPetType().getConfigName());
        LinkRetriever.getPluginLink(IVaultLink.class).withdrawPlayer(event.getPlayer(), price);
        event.getPlayer().sendMessage(economyFile.getString("PurchaseSuccessful", true).replace("%type%", event.getPetType().getConfigName()));
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void openMenuSelector(PetInventoryOpenEvent event) {
        if (!PetCore.get().getConfiguration().getBoolean("UseVaultEconomy"))
            return;
        IStorage<ItemStack> items = new StorageList<>();
        IStorage<PetTypeStorage> types = event.getShownPetTypes().copy();
        PetOwner petOwner = PetOwner.getPetOwner(event.getPlayer());
        JSONArray petArray = petOwner.getOwnedPets();
        while (types.hasNext()) {
            PetTypeStorage storage = types.next();
            PetType type = storage.getType();
            ItemMaker maker = new ItemMaker(storage.getItem());
            String price = ((economyFile.getPrice(type) == -1) ? economyFile.getString("Price-Free") : String.valueOf(economyFile.getPrice(type)));

            if (event.getPlayer().hasPermission("Pets.economy.bypass")) price = economyFile.getString("Price-Bypass");

            if (economyFile.getBoolean("Pay-Per-Use.Enabled")) {
                for (String line : economyFile.getStringList("Pay-Per-Use.Lore-Lines"))
                    maker.addLoreLine(line.replace("%cost%", price));
            } else {
                boolean contains = petArray.contains(type.getConfigName());
                for (String line : economyFile.getStringList("Lore-Lines"))
                    maker.addLoreLine(line.replace("%cost%", price).replace("%contains%", String.valueOf(contains)));
            }
            items.add(maker.create());
            storage.setItem(maker.create());

        }
        event.setItems(items);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onName(PetNameChangeEvent event) {
        boolean cancel = false;

        if (!event.getPlayer().hasPermission("Pet.name.bypass")) {
            List<String> blocked = PetCore.get().getConfiguration().getStringList("PetToggles.Rename.Blocked-Words");
            List<String> split = Arrays.asList(event.getNewName().split(" "));
            for (String arg : split) {
                String name = ChatColor.translateAlternateColorCodes('&', arg);
                name = ChatColor.stripColor(name);
                if (blocked.contains(name.toLowerCase())) {
                    cancel = true;
                    break;
                }
            }
        }

        if (event.canUseColor())
            if (!PetCore.hasPerm(event.getPlayer(), "Pet.name.color"))
                event.setColor(false);
        if (event.canUseMagic())
            if (!PetCore.hasPerm(event.getPlayer(), "Pet.name.magic"))
                event.setMagic(false);
        if (!PetCore.hasPerm(event.getPlayer(), "Pet.name"))
            cancel = true;
        event.setCancelled(cancel);

    }
}
