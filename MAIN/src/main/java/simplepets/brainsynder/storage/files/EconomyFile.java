package simplepets.brainsynder.storage.files;

import lib.brainsynder.utils.Utilities;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.pet.PetType;
import simplepets.brainsynder.storage.files.base.FileMaker;

import java.util.Arrays;
import java.util.Collections;

public class EconomyFile extends FileMaker {
    public EconomyFile() {
        super(PetCore.get().getDataFolder(), "PetEconomy.yml");
    }

    public void loadDefaults() {
        addDefault("prefix", "&eSimplePets &6>>");
        addDefault("PurchaseSuccessful", "{prefix} &7You have Successfully Purchased the %type% Pet.", "This message will be sent if the purchase it successful\nPlaceholders:\n- {prefix} (uses the customized prefix)\n- %type% (will get what type of pet it is)\n- %price% (what price the pet is)");
        addDefault("InsufficientFunds", "{prefix} &cYou do not have enough money to buy this pet, you need to have %price%", "This message will be sent if the player does not have enough money to buy the pet\nPlaceholders:\n- {prefix} (uses the customized prefix)\n- %type% (will get what type of pet it is)\n- %price% (what price the pet is)");
        addDefault("Price-Free", "Free", "If a pet is free this will be in the place of the price in the lore\nDefault: 'Free'");
        addDefault("Bypass.Price", "BYPASSED", "If the player has the bypass permission, will be in the place of the price in the lore\nDefault: 'BYPASSED'");
        addDefault("Bypass.Hide-Price-If-Bypassed", true, "Disabling this will make the items show the price, but if the player has bypass permissions he wont have to pay\nDefault: true");
        addDefault("Pay-Per-Use.Enabled", false, "Should players have to pay each time they spawn a pet?\nDefault: false");
        addDefault("Pay-Per-Use.Paid", "{prefix} &7You have Successfully Paid for the %type% Pet.", "This message will be sent if the purchase it successful\nPlaceholders:\n- {prefix} (uses the customized prefix)\n- %type% (will get what type of pet it is)\n- %price% (what price the pet is)");
        addDefault("Pay-Per-Use.Lore-Lines", Collections.singletonList("&6Price: &e%cost%"), "These lines get added to the pet items when the GUI is opened\nPlaceholders:\n- %cost% (price of the pet)\n- %contains% (true/false if the player purchased the pet)");
        addDefault("Lore-Lines", Arrays.asList("&6Price: &e%cost%", "&6Purchased: &e%contains%"));

        addSectionHeader("Pet", Utilities.AlignText.LEFT, "Customize the price of the pets\nIf you want the pet to be free then set the price to -1\nDefault: 2000.0");
        for (PetType type : PetCore.get().getTypeManager().getTypes()) {
            addDefault("Pet." + type.getConfigName() + ".Price", 2000.0);
        }
    }

    public double getPrice(PetType type) {
        if (isSet("Pet." + type.getConfigName() + ".Price"))
            return getDouble("Pet." + type.getConfigName() + ".Price");
        return -1;
    }
}
