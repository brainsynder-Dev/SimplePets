package simplepets.brainsynder.storage.files;

import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.pet.PetDefault;
import simplepets.brainsynder.storage.files.base.FileMaker;

import java.util.Arrays;
import java.util.Collections;

public class EconomyFile extends FileMaker {
    public EconomyFile() {
        super(PetCore.get(), "PetEconomy.yml");
    }

    public void loadDefaults() {
        setDefault("prefix", "&eSimplePets &6>>");
        setDefault("PurchaseSuccessful", "{prefix} &7You have Successfully Purchased the %type% Pet.");
        setDefault("InsufficientFunds", "{prefix} &cYou do not have enough money to buy this pet, you need to have %price%");
        setDefault("Price-Free", "Free");
        setDefault("Bypass.Price", "BYPASSED");
        setDefault("Bypass.Hide-Price-If-Bypassed", true);
        setDefault("Pay-Per-Use.Enabled", false);
        setDefault("Pay-Per-Use.Paid", "{prefix} &7You have Successfully Paid for the %type% Pet.");
        setDefault("Pay-Per-Use.Lore-Lines", Collections.singletonList("&6Price: &e%cost%"));
        setDefault("Lore-Lines", Arrays.asList("&6Price: &e%cost%", "&6Purchased: &e%contains%"));

        for (PetDefault type : PetCore.get().getTypeManager().getTypes()) {
            setDefault("Pet." + type.getConfigName() + ".Price", 2000.0);
        }
    }

    public double getPrice(PetDefault type) {
        if (isSet("Pet." + type.getConfigName() + ".Price"))
            return getDouble("Pet." + type.getConfigName() + ".Price");
        return -1;
    }

    @Override
    public String getString(String path) {
        return super.getString(path, true);
    }
}
