package simplepets.brainsynder.listeners;

import lib.brainsynder.item.ItemBuilder;
import lib.brainsynder.storage.IStorage;
import lib.brainsynder.storage.StorageList;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.event.inventory.PetInventoryOpenEvent;
import simplepets.brainsynder.api.event.inventory.PetSelectTypeEvent;
import simplepets.brainsynder.api.event.pet.PetNameChangeEvent;
import simplepets.brainsynder.links.IVaultLink;
import simplepets.brainsynder.pet.PetType;
import simplepets.brainsynder.player.PetOwner;
import simplepets.brainsynder.storage.PetTypeStorage;
import simplepets.brainsynder.storage.files.EconomyFile;

import java.util.List;

public class PetEventListeners implements Listener {
    private final EconomyFile economyFile;

    public PetEventListeners() {
        economyFile = PetCore.get().getEcomony();
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onSelect(PetSelectTypeEvent event) {
        if (PetCore.get().getConfiguration().getBoolean("UseVaultEconomy")) {
            if (event.getPlayer().hasPermission("Pet.economy.bypass")) return;

            double price = economyFile.getPrice(event.getPetType());
            if (price == -1) return;

            IVaultLink vault = PetCore.get().getLinkRetriever().getPluginLink(IVaultLink.class);
            if (!vault.isHooked()) return;

            PetOwner petOwner = PetOwner.getPetOwner(event.getPlayer());
            List<PetType> petArray = petOwner.getOwnedPets();
            if (petArray.contains(event.getPetType())) return;

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
        List<PetType> petArray = petOwner.getOwnedPets();
        List<String> lore = economyFile.getStringList((economyFile.getBoolean("Pay-Per-Use.Enabled") ? "Pay-Per-Use.Lore-Lines" : "Lore-Lines"));

        while (types.hasNext()) {
            PetTypeStorage storage = types.next();
            PetType type = storage.getType();
            ItemBuilder maker = storage.getType().getItemBuilder().clone();
            String price = String.valueOf(economyFile.getPrice(type));
            if (price.equals("-1")) price = economyFile.getString("Price-Free", true);

            try {
                if (price.isEmpty()) {
                    PetCore.get().debug("Price is empty for: 'Pet." + type.getConfigName() + ".Price'");
                    items.add(maker.build());
                    continue;
                }
                Double.parseDouble(price);
            }catch (Exception e){
                PetCore.get().debug("Price is invalid for: 'Pet." + type.getConfigName() + ".Price'");
                items.add(maker.build());
                continue;
            }

            if (economyFile.getBoolean("Bypass.Hide-Price-If-Bypassed") && event.getPlayer().hasPermission("Pet.economy.bypass")) price = String.valueOf(economyFile.get("Bypass.Price"));
            boolean contains = petArray.contains(type);
            for (String line : lore)
                maker.addLore(line.replace("%cost%", price).replace("%contains%", String.valueOf(contains)));
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
