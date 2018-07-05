package simplepets.brainsynder.listeners;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.json.simple.JSONArray;
import simple.brainsynder.storage.IStorage;
import simple.brainsynder.storage.StorageList;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.event.inventory.PetInventoryOpenEvent;
import simplepets.brainsynder.api.event.inventory.PetSelectTypeEvent;
import simplepets.brainsynder.api.event.pet.PetNameChangeEvent;
import simplepets.brainsynder.links.IVaultLink;
import simplepets.brainsynder.pet.PetDefault;
import simplepets.brainsynder.player.PetOwner;
import simplepets.brainsynder.storage.PetTypeStorage;
import simplepets.brainsynder.storage.files.EconomyFile;
import simplepets.brainsynder.utils.ItemBuilder;

import java.util.List;

public class PetEventListeners implements Listener {
    private EconomyFile economyFile;

    public PetEventListeners() {
        economyFile = PetCore.get().getEcomony();
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onSelect(PetSelectTypeEvent event) {
        if (PetCore.get().getConfiguration().getBoolean("UseVaultEconomy")) {
            if (event.getPlayer().hasPermission("Pets.economy.bypass")) return;

            double price = economyFile.getPrice(event.getPetType());
            if (price == -1) return;

            IVaultLink vault = PetCore.get().getLinkRetriever().getPluginLink(IVaultLink.class);
            if (!vault.isHooked()) return;

            PetOwner petOwner = PetOwner.getPetOwner(event.getPlayer());
            JSONArray petArray = petOwner.getOwnedPets();
            if (petArray.contains(event.getPetType().getConfigName())) return;

            double bal = vault.getBalance(event.getPlayer());
            if (bal < price) {
                event.setCancelled(true);
                event.getPlayer().sendMessage(economyFile.getString("InsufficientFunds", true).replace("%price%", String.valueOf(price)));
                return;
            }

            if (economyFile.getBoolean("Pay-Per-Use.Enabled")) {
                vault.withdrawPlayer(event.getPlayer(), price);
                event.getPlayer().sendMessage(economyFile.getString("Pay-Per-Use.Paid", true).replace("%type%", event.getPetType().getConfigName()));
                return;
            }

            petOwner.addPurchasedPet(event.getPetType().getConfigName());
            vault.withdrawPlayer(event.getPlayer(), price);
            event.getPlayer().sendMessage(economyFile.getString("PurchaseSuccessful", true).replace("%type%", event.getPetType().getConfigName()));
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void openMenuSelector(PetInventoryOpenEvent event) {
        if (!PetCore.get().getConfiguration().getBoolean("UseVaultEconomy")) return;

        IStorage<ItemStack> items = new StorageList<>();
        IStorage<PetTypeStorage> types = event.getShownPetTypes().copy();
        PetOwner petOwner = PetOwner.getPetOwner(event.getPlayer());
        JSONArray petArray = petOwner.getOwnedPets();
        while (types.hasNext()) {
            PetTypeStorage storage = types.next();
            PetDefault type = storage.getType();
            ItemBuilder maker = ItemBuilder.fromJSON(storage.getType().getItemBuilder().toJSON());
            String price = ((economyFile.getPrice(type) == -1) ? economyFile.getString("Price-Free") : String.valueOf(economyFile.getPrice(type)));

            if (event.getPlayer().hasPermission("Pets.economy.bypass")) price = economyFile.getString("Price-Bypass");
            if (economyFile.getBoolean("Pay-Per-Use.Enabled")) {
                for (String line : economyFile.getStringList("Pay-Per-Use.Lore-Lines"))
                    maker.addLore(line.replace("%cost%", price));
            } else {
                boolean contains = petArray.contains(type.getConfigName());
                for (String line : economyFile.getStringList("Lore-Lines"))
                    maker.addLore(line.replace("%cost%", price).replace("%contains%", String.valueOf(contains)));
            }
            items.add(maker.build());
            storage.setItem(maker);
        }

        event.setItems(items);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onName(PetNameChangeEvent event) {
        boolean cancel = false;

        if (!event.getPlayer().hasPermission("Pet.name.bypass")) {
            List<String> blocked = PetCore.get().getConfiguration().getStringList("RenamePet.Blocked-Words");
            if (!blocked.isEmpty()) {
                String[] split = event.getNewName().split(" ");
                for (String arg : split) {
                    String name = ChatColor.translateAlternateColorCodes('&', arg);
                    name = ChatColor.stripColor(name);
                    if (blocked.contains(name.toLowerCase())) {
                        cancel = true;
                        break;
                    }
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
