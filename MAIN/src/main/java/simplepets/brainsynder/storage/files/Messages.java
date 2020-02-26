package simplepets.brainsynder.storage.files;

import org.bukkit.plugin.Plugin;
import simplepets.brainsynder.storage.files.base.FileMaker;

public class Messages extends FileMaker {
    public Messages(Plugin plugin, String fileName) {
        super(plugin, fileName);
    }

    public void loadDefaults() {
        setDefault("prefix", "&eSimplePets &6>>");
        setDefault("No-Permission", "{prefix} &cYou do not have permission.");
        setDefault("Reload-Complete", "{prefix} &7Inventory and Files have been reloaded.");
        setDefault("Unknown-commands", "{prefix} &7Unknown SimplePets commands.");
        setDefault("Player-Not-Found", "{prefix} &7%player% is not valid.");
        setDefault("Pet-Remove-Self-Removed", "{prefix} &7Your pet was removed.");
        setDefault("Pet-Remove-Self-No-Pet", "{prefix} &7You do not have a pet to remove.");
        setDefault("Pet-Remove-Other-Remover", "{prefix} &7%player%'s pet was successfully removed.");
        setDefault("Pet-Remove-Other-Target", "{prefix} &7%player% removed your pet.");
        setDefault("Pet-Remove-Other-No-Pet", "{prefix} &7%player% does not have a pet to remove.");
        setDefault("Invalid-PetType", "{prefix} &cInvalid pet type.");
        setDefault("No-Pet-Permission", "{prefix} &cYou do not have permission to this pet.");
        setDefault("Select-Pet", "{prefix} &7You have selected the &e%pet% &7Pet.");
        setDefault("Select-Pet-Other", "{prefix} &7%player% force selected the pet %pet% for you.");
        setDefault("Select-Pet-Sender", "{prefix} &7You have set %player%'s pet to %pet%");

        setDefault("Other-No-Pet-Permission", "{prefix} &7%player% does not have permission to this pet.");
        setDefault("Type-Not-Supported", "{prefix} &7This pet type is not supported in this version.");
        setDefault("Type-Not-Enabled", "{prefix} &7This pet is currently disabled.");
        setDefault("Pet-Name-Changed", "{prefix} &7Your pets name was changed to: &r%petname%");
        setDefault("Menu-Name", "&a&lSelect a Pet:");
        setDefault("Data-Menu.PetCore-Name", "&a&lPet Data Changer");
        setDefault("No-Spawning", "{prefix} &cPets are not allowed to be spawned in this region.");
        setDefault("Pet-No-Enter", "{prefix} &cPets are not allowed in this region.");
        setDefault("Pet-RenameViaChat", "{prefix} &7Type your pets new name in chat:");
        setDefault("Pet-RenameViaChat-Cancel", "{prefix} &cPet renaming has been canceled");
        setDefault("Pet-RenameViaAnvil", "{prefix} &7Type your pets new name in the anvil, then click the item on the right when complete.");
        setDefault("Pet-RenameFailure", "{prefix} &cSorry, but your pets name could not be changed to: &7{name}");
        setDefault("Player-No-Pet", "{prefix} &7%player% does not have a pet.");

        setDefault("Anvil-Rename.Name", "Rename Pet");
    }

    @Override
    public String getString(String path) {
        return super.getString(path, true);
    }
}
