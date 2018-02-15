package simplepets.brainsynder.events;

import org.bukkit.entity.Player;
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
import simplepets.brainsynder.api.event.inventory.PetInventoryOpenEvent;
import simplepets.brainsynder.api.event.inventory.PetInventorySelectTypeEvent;
import simplepets.brainsynder.api.event.pet.PetNameChangeEvent;
import simplepets.brainsynder.files.EconomyFile;
import simplepets.brainsynder.links.IVaultLink;
import simplepets.brainsynder.pet.PetType;
import simplepets.brainsynder.player.PetOwner;
import simplepets.brainsynder.utils.LinkRetriever;

public class PetEventListeners implements Listener {
    private EconomyFile economyFile;

    public PetEventListeners() {
        economyFile = new EconomyFile();
        economyFile.loadDefaults();
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onSelect(PetInventorySelectTypeEvent event) {
        if (PetCore.get().getConfiguration().getBoolean("UseVaultEconomy")) {
            double price = economyFile.getPrice(event.getPetType());
            if (price != -1) {
                IVaultLink vault = LinkRetriever.getPluginLink(IVaultLink.class);
                if (vault.isHooked()) {
                    Player player = event.getPlayer();
                    double bal = vault.getBalance(player);
                    if (economyFile.getBoolean("Pay-Per-Use.Enabled")) {
                        if (!hasSufficientFunds(bal, price, player)) {
                            event.setCancelled(true);
                            return;
                        }
                        vault.withdrawPlayer(player, price);
                        player.sendMessage(economyFile.getString("Pay-Per-Use.Paid", true).replace("%type%", event.getPetType().getConfigName()));
                        return;
                    }
                    PetOwner petOwner = PetOwner.getPetOwner(player);
                    JSONArray petArray = petOwner.getOwnedPets();

                    if (petArray.contains(event.getPetType().getConfigName()))
                        return;

                    if (!hasSufficientFunds(bal, price, player)) {
                        event.setCancelled(true);
                        return;
                    }
                    petOwner.addPurchasedPet(event.getPetType().getConfigName());
                    vault.withdrawPlayer(player, price);
                    player.sendMessage(economyFile.getString("PurchaseSuccessful", true).replace("%type%", event.getPetType().getConfigName()));
                }
            }
        }
    }

    private boolean hasSufficientFunds(double bal, double price, Player p) {
        if (bal < price) {
            p.sendMessage(economyFile.getString("InsufficientFunds", true).replace("%price%", String.valueOf(price)));
            return false;
        } else {
            return true;
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void openMenuSelector(PetInventoryOpenEvent event) {
        if (PetCore.get().getConfiguration().getBoolean("UseVaultEconomy")) {

            IStorage<ItemStack> items = new StorageList<>();
            IStorage<PetTypeStorage> types = event.getShownPetTypes().copy();
            PetOwner petOwner = PetOwner.getPetOwner(event.getPlayer());
            JSONArray petArray = petOwner.getOwnedPets();
            while (types.hasNext()) {
                PetTypeStorage storage = types.next();
                PetType type = storage.getType();
                ItemMaker maker = new ItemMaker(storage.getItem());
                String price = ((economyFile.getPrice(type) == -1) ? economyFile.getString("Price-Free") : String.valueOf(economyFile.getPrice(type)));
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
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onName(PetNameChangeEvent event) {
        if (event.canUseColor())
            if (!PetCore.hasPerm(event.getPlayer(), "Pet.name.color"))
                event.setColor(false);
        if (event.canUseMagic())
            if (!PetCore.hasPerm(event.getPlayer(), "Pet.name.magic"))
                event.setMagic(false);
        if (!PetCore.hasPerm(event.getPlayer(), "Pet.name"))
            event.setCancelled(true);

    }
}
