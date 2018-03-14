package simplepets.brainsynder.files;

import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.pet.PetType;

import java.util.Arrays;
import java.util.Collections;

public class EconomyFile extends FileMaker {
    public EconomyFile() {
        super(PetCore.get(), "PetEconomy.yml");
    }

    public void loadDefaults() {
        if (!isSet("PurchaseSuccessful"))
            set("PurchaseSuccessful", "&eSimplePets &6>> &7You have Successfully Purchased the %type% Pet.", "If you want a pet to be free, Simply set the price to -1", "o__o", "This message is sent when a player successfully buys a pet");
        if (!isSet("InsufficientFunds"))
            set("InsufficientFunds", "&eSimplePets &6>> &cYou do not have enough money to buy this pet, you need to have %price%", "Ohhh noo they dont have enough money...");
        if (!isSet("Price-Free"))
            set("Price-Free", "Free", "If a pet price is -1 this will show up instead");
        if (!isSet("Price-Bypass"))
            set("Price-Bypass", "Bypassed");
        if (!isSet("Pay-Per-Use.Enabled"))
            set("Pay-Per-Use.Enabled", false,
                    "Enabled -- Do users have to pay each time to spawn a pet",
                    "Paid -- Message is sent when a player buys a pet",
                    "Lore-Lines -- The lore that will be given to an item if Enabled = true"
            );
        if (!isSet("Pay-Per-Use.Paid"))
            set("Pay-Per-Use.Paid", "&eSimplePets &6>> &7You have Successfully Paid for the %type% Pet.");
        if (!isSet("Pay-Per-Use.Lore-Lines"))
            set("Pay-Per-Use.Lore-Lines", Collections.singletonList("&6Price: &e%cost%"));
        if (!isSet("Lore-Lines"))
            set("Lore-Lines", Arrays.asList("&6Price: &e%cost%", "&6Purchased: &e%contains%"));
        for (PetType type : PetType.values()) {
            if (!isSet("Pet." + type.getConfigName() + ".Price")) {
                set("Pet." + type.getConfigName() + ".Price", 2000.0);
            }
        }
    }

    public double getPrice(PetType type) {
        if (isSet("Pet." + type.getConfigName() + ".Price"))
            return getDouble("Pet." + type.getConfigName() + ".Price");
        return -1;
    }

    @Override
    public void set(String tag, Object data, String... comments) {
        try {
            super.set(tag, data, comments);
        } catch (Exception e) {
            super.set(tag, data);
        }
    }
}
