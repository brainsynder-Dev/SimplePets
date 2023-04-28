package simplepets.brainsynder.addon.presets;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lib.brainsynder.item.ItemBuilder;
import lib.brainsynder.utils.Colorize;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import simplepets.brainsynder.addon.AddonConfig;
import simplepets.brainsynder.addon.AddonPermissions;
import simplepets.brainsynder.addon.PermissionData;
import simplepets.brainsynder.addon.PetModule;
import simplepets.brainsynder.api.event.inventory.PetInventoryAddPetItemEvent;
import simplepets.brainsynder.api.event.inventory.PetSelectTypeEvent;
import simplepets.brainsynder.api.inventory.handler.InventoryType;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

/**
 * This preset addon class, is for making addons for any type of plugin that handles economy (EG: Vault/TokenManager)
 */
public abstract class EconomyModule extends PetModule {
    private final Map<PetType, Double> priceMap = Maps.newHashMap();

    private String prefix, bypassPerm, freePrice, bypassPrice, insufficientFunds, successfulPayment, paid, boolTrue, boolFalse;
    private List<String> lore;
    private boolean hidePrice, payPerUse;
    private Map<PetType, String> typePermissions;

    @Override
    public void init() {
        PermissionData parent = new PermissionData(bypassPerm).setDescription("This is the master permission, Will ignore all individual bypass permissions listed below");

        typePermissions.forEach((type, s) -> {
            AddonPermissions.register(this, parent, new PermissionData(s).setDescription("This is a bypass permission for the "+type.getName()+" pet, who ever has this permission will not have to pay for this pet"));
        });
    }

    @Override
    public void cleanup() {
        if (priceMap != null) priceMap.clear();
        if (typePermissions != null) typePermissions.clear();

        typePermissions = null;
        bypassPerm = null;
    }

    @Override
    public void loadDefaults(AddonConfig config) {
        this.typePermissions = Maps.newHashMap();

        String bypassPermission = "pet."+getNamespace().namespace().toLowerCase().replace(" ", "_")+".bypass";

        config.addDefault("Hide-Price-If-Bypassed", true,
                "Disabling this will make the items show the price, but if the player has bypass permissions he wont have to pay\n" +
                        "Default: true");

        config.addDefault("Pay-Per-Use-Enabled", false,
                "Should players have to pay each time they spawn a pet?\nDefault: false");


        config.addDefault("Price.Free", "Free",
                "If a pet is free this will be in the place of the price in the lore\n" +
                        "Default: 'Free'");
        config.addDefault("Price.Bypassed", "BYPASSED",
                "If the player has the bypass permission, will be in the place of the price in the lore\n" +
                        "Default: 'BYPASSED'");

        config.addComment("Boolean", "Here is where you can set the translations for the 2 boolean values (true/false)");
        config.addDefault("Boolean.true", "&#92fc98true");
        config.addDefault("Boolean.false", "&#fa7d7dfalse");

        config.addDefault("Messages.prefix", "&eSimplePets &6>>");

        config.addDefault("Messages.PurchaseSuccessful.One-Time-Purchase",
                "{prefix} &7You have Successfully Purchased the {type} Pet.",
                "This message will be sent if the purchase it successful\n" +
                        "Placeholders:\n" +
                        "- {prefix} (uses the customized prefix)\n" +
                        "- {type} (will get what type of pet it is)\n" +
                        "- {price} (what price the pet is)");
        config.addDefault("Messages.PurchaseSuccessful.Pay-Per-Use",
                "{prefix} &7You have Successfully Paid for the {type} Pet.",
                "This message will be sent if the purchase it successful while Pay-Per-Use is set to true\n" +
                        "Placeholders:\n" +
                        "- {prefix} (uses the customized prefix)\n" +
                        "- {type} (will get what type of pet it is)\n" +
                        "- {price} (what price the pet is)");

        config.addDefault("Messages.InsufficientFunds",
                "{prefix} &cYou do not have enough money to buy this pet, you need to have {price}",
                "This message will be sent if the player does not have enough money to buy the pet\n" +
                        "Placeholders:\n" +
                        "- {prefix} (uses the customized prefix)\n" +
                        "- {type} (will get what type of pet it is)\n" +
                        "- {price} (what price the pet is)");

        config.addDefault("Messages.Lore-Lines.One-Time-Purchase", Lists.newArrayList("&#ffbf5ePrice: &#99ffac{price}", "&#ffbf5ePurchased: {purchased}"),
                "These Lore Lines will only be used if 'Pay-Per-Use' is set to false\n" +
                        "Placeholders:\n" +
                        "- {price} (price of the pet)\n" +
                        "- {purchased} (true/false if the player purchased the pet)");
        config.addDefault("Messages.Lore-Lines.Pay-Per-Use", Collections.singletonList("&#ffbf5ePrice: &#99ffac{price}"),
                "These lines get added to the pet items when the GUI is opened\n" +
                        "Placeholders:\n" +
                        "- {price} (price of the pet)\n" +
                        "- {purchased} (true/false if the player purchased the pet)");

        config.addDefault("bypass_permissions.parent", bypassPermission,
                "This is the master permission, Will ignore all individual bypass permissions listed below");

        for (PetType type : PetType.values()) {
            if (type == PetType.UNKNOWN) continue;
            if (!type.isSupported()) continue;
            config.addDefault("type." + type.getName(), getDefaultPrice(), "The price of the " + type.getName() + " pet");
            config.addDefault("bypass_permissions.type."+type.getName(), bypassPermission+"."+type.getName(), "This is a bypass permission for the "+type.getName()+" pet, who ever has this permission will now have to pay for this pet");

            typePermissions.put(type, config.getString("bypass_permissions.type."+type.getName(), bypassPermission+"."+type.getName()));
            priceMap.put(type, config.getDouble("type." + type.getName(), getDefaultPrice()));
        }


        hidePrice = config.getBoolean("Hide-Price-If-Bypassed", true);
        payPerUse = config.getBoolean("Pay-Per-Use-Enabled", false);

        boolTrue = config.getString("Boolean.true", "&#92fc98true");
        boolFalse = config.getString("Boolean.false", "&#fa7d7dfalse");

        prefix = config.getString("Messages.prefix", "&eSimplePets &6>>");
        bypassPerm = config.getString("bypass_permission", bypassPermission);
        bypassPrice = String.valueOf(config.get("Price.Bypassed", "BYPASSED"));
        freePrice = config.getString("Price.Free", "Free");
        insufficientFunds = config.getString("Messages.InsufficientFunds", "{prefix} &cYou do not have enough money to buy this pet, you need to have {price}");
        paid = config.getString("Messages.PurchaseSuccessful.Pay-Per-Use", "{prefix} &7You have Successfully paid for the {type} Pet.");
        successfulPayment = config.getString("Messages.PurchaseSuccessful.One-Time-Purchase", "{prefix} &7You have Successfully Purchased the {type} Pet.");

        lore = config.getStringList((config.getBoolean("Pay-Per-Use-Enabled") ? "Messages.Lore-Lines.Pay-Per-Use" : "Messages.Lore-Lines.One-Time-Purchase"));
    }

    public abstract int getDefaultPrice ();
    public void fetchBalance (UUID uuid, Consumer<Double> balanceConsumer) {
        balanceConsumer.accept(getBalance(uuid));
    }
    public abstract double getBalance (UUID uuid);
    public abstract void withdraw (UUID uuid, double amount);

    private String var (boolean value) {
        return value ? boolTrue : boolFalse;
    }

    // I don't remember why I was adding this method...
    public boolean isPetFree (PetType type) {
        return (priceMap.getOrDefault(type, 2000.0D) <= 0);
    }

    @EventHandler
    public void onInventoryOpen(PetInventoryAddPetItemEvent event) {
        if (!isEnabled()) return;
        if (event.getInventory().getInventoryType() != InventoryType.SUMMON_GUI) return;
        PetUser user = event.getUser();
        List<PetType> petArray = user.getOwnedPets();

        PetType type = event.getType();
        ItemBuilder maker = ItemBuilder.fromItem(event.getItem());
        String price = String.valueOf(priceMap.getOrDefault(type, 2000.0));
        if (isPetFree(type)) price = freePrice;

        if (hidePrice && (AddonPermissions.hasPermission(this, event.getUser().getPlayer(), typePermissions.get(type)))) price = bypassPrice;
        boolean contains = petArray.contains(type);
        for (String line : lore)
            maker.addLore(line.replace("{price}", price).replace("{purchased}", String.valueOf(var(contains))));

        event.setItem(maker.build());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onSelect(PetSelectTypeEvent event) {
        if (!isEnabled()) return;
        if (AddonPermissions.hasPermission(this, event.getUser().getPlayer(), typePermissions.get(event.getPetType()))) return;

        double price = priceMap.getOrDefault(event.getPetType(), 2000.0);
        if (price <= 0) return; // The pet is free, return

        PetUser user = event.getUser();
        // If player already owns the pet ignore
        if (user.getOwnedPets().contains(event.getPetType())) return;

        fetchBalance(user.getPlayer().getUniqueId(), bal -> {
            // Checks the players balance (if they have a balance that is lower then the price)
            if (bal < price) {
                event.setCancelled(true);
                user.getPlayer().sendMessage(Colorize.translateBungeeHex(insufficientFunds
                        .replace("{price}", String.valueOf(price))
                        .replace("{type}", event.getPetType().getName())
                        .replace("{prefix}", prefix)
                ));
                return;
            }

            // Checks if PayPerUse is enabled, if it is dont add the pet to the players purchased list
            if (payPerUse) {
                withdraw(user.getPlayer().getUniqueId(), price);
                user.getPlayer().sendMessage(Colorize.translateBungeeHex(paid
                        .replace("{price}", String.valueOf(price))
                        .replace("{type}", event.getPetType().getName())
                        .replace("{prefix}", prefix)
                ));
                return;
            }

            // withdraw money, and add pet to the players purchased list
            user.addOwnedPet(event.getPetType());
            withdraw(user.getPlayer().getUniqueId(), price);
            user.getPlayer().sendMessage(Colorize.translateBungeeHex(successfulPayment
                    .replace("{price}", String.valueOf(price))
                    .replace("{type}", event.getPetType().getName())
                    .replace("{prefix}", prefix)
            ));
            user.updateSelectionMenu();
        });
    }
}
