package simplepets.brainsynder.storage.files;

import org.bukkit.plugin.java.JavaPlugin;
import simplepets.brainsynder.storage.files.base.FileMaker;

import java.util.ArrayList;
import java.util.Arrays;

public class Commands extends FileMaker {
    public Commands(JavaPlugin plugin, String fileName) {
        super(plugin, fileName);
    }

    public void loadDefaults() {
        setDefault("Prefix", "&eSimplePets &6>>");
        // Info command
        setDefault("Info.Pet-Data-Header", "{prefix} &7%player%'s Pet Data:");
        setDefault("Info.Pet-Data-Values", "&7- &e%key%&6: &r%value%");
        setDefault("Info.Excluded-Data-Values", new ArrayList<>());
        // Help command
        setDefault("Help.Command-Display-Player", "{prefix} &7/pet %name% %usage% - %description%");
        setDefault("Help.Command-Display-Console", "- pet %name% %usage% - %description%");
        // List command
        setDefault("List.List-Display", "&ePet list &6(&7%size%&6)&e: %list%");
        setDefault("List.List-Pet-Not-Supported", Arrays.asList("&cPet is not supported", "&cin your current version."));
        // Modify command
        setDefault("Modify.Invalid-JSON", "{prefix} &cInvalid JSON has been entered.");
        setDefault("Modify.Invalid-JSON-Error-Player", "{prefix} &cError: %error%");
        setDefault("Modify.Pet-Modified", "{prefix} &7You have changed &c%player%'s &7Pet Data.");
        // Inventory command
        setDefault("Inv.No-Pet-Items", "{prefix} &7No items stored in your Pet Inventory.");
        setDefault("Inv.No-Pet-Items-Other", "{prefix} &7No items stored for %player%.");
    }

    @Override
    public String getString(String path) {
        return super.getString(path)
                .replace('&', '§')
                .replace("{prefix}", super.getString("Prefix"));
    }
}
