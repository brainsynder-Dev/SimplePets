package simplepets.brainsynder.storage.files;

import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.pet.PetType;
import simplepets.brainsynder.storage.files.base.FileMaker;

import java.util.Arrays;
import java.util.Collections;

public class EconomyFile extends FileMaker {
    public EconomyFile() {
        super(PetCore.get(), "PetEconomy.yml");
    }

    public void loadDefaults() {
        setDefault("PurchaseSuccessful", "&eSimplePets &6>> &7You have Successfully Purchased the %type% Pet.");
        setDefault("InsufficientFunds", "&eSimplePets &6>> &cYou do not have enough money to buy this pet, you need to have %price%");
        setDefault("Price-Free", "Free");
        setDefault("Pay-Per-Use.Enabled", false);
        setDefault("Pay-Per-Use.Paid", "&eSimplePets &6>> &7You have Successfully Paid for the %type% Pet.");
        setDefault("Pay-Per-Use.Lore-Lines", Collections.singletonList("&6Price: &e%cost%"));
        setDefault("Lore-Lines", Arrays.asList("&6Price: &e%cost%", "&6Purchased: &e%contains%"));
        for (PetType type : PetType.values()) {
            setDefault("Pet." + type.getConfigName() + ".Price", 2000.0);
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
