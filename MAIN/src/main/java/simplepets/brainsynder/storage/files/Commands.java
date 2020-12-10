package simplepets.brainsynder.storage.files;

import org.bukkit.plugin.java.JavaPlugin;
import simplepets.brainsynder.storage.files.base.FileMaker;

import java.util.ArrayList;
import java.util.Arrays;

public class Commands extends FileMaker {
    public Commands(JavaPlugin plugin, String fileName) {
        super(plugin.getDataFolder(), fileName);
    }

    public void loadDefaults() {
        addDefault("prefix", "&eSimplePets &6>>");
        // Info command
        addDefault("Info.Pet-Data-Header", "{prefix} &7%player%'s Pet Data:");
        addDefault("Info.Pet-Data-Values", "&7- &e%key%&6: &r%value%");
        addDefault("Info.Excluded-Data-Values", new ArrayList<>());
        // Help command
        addDefault("Help.Header", "&6&m-------&e SimplePets Commands &6&m-------");
        // List command
        addDefault("List.List-Display", "&ePet list &6(&7%size%&6)&e: %list%");
        addDefault("List.List-Pet-Not-Supported", Arrays.asList("&cPet is not supported", "&cin your current version."));
        // Modify command
        addDefault("Modify.Invalid-JSON", "{prefix} &cInvalid JSON has been entered.");
        addDefault("Modify.Invalid-JSON-Error-Player", "{prefix} &cError: %error%");
        addDefault("Modify.Pet-Modified", "{prefix} &7You have changed &c%player%'s &7Pet Data.");
        // Inventory command
        addDefault("Inv.No-Pet-Items", "{prefix} &7No items stored in your Pet Inventory.");
        addDefault("Inv.No-Pet-Items-Other", "{prefix} &7No items stored for %player%.");
    }

    @Override
    public String getString(String path) {
        return super.getString(path, true);
    }
}
