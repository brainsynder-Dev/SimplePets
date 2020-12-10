package simplepets.brainsynder.storage.files;

import org.bukkit.plugin.Plugin;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.storage.files.base.FileMaker;

import java.util.Arrays;

public class Messages extends FileMaker {
    public Messages(Plugin plugin, String fileName) {
        super(plugin.getDataFolder(), fileName);
    }

    public void loadDefaults() {
        addDefault("prefix", "&eSimplePets &6>>");
        addDefault("No-Permission", "{prefix} &cYou do not have permission.");
        addDefault("Reload-Complete", "{prefix} &7Inventory and Files have been reloaded.");
        addDefault("Unknown-commands", "{prefix} &7Unknown SimplePets commands.");
        addDefault("Player-Not-Found", "{prefix} &7%player% is not Validate.");
        addDefault("Pet-Remove-Self-Removed", "{prefix} &7Your pet was removed.");
        addDefault("Pet-Remove-Self-No-Pet", "{prefix} &7You do not have a pet to remove.");
        addDefault("Pet-Remove-Other-Remover", "{prefix} &7%player%'s pet was successfully removed.");
        addDefault("Pet-Remove-Other-Target", "{prefix} &7%player% removed your pet.");
        addDefault("Pet-Remove-Other-No-Pet", "{prefix} &7%player% does not have a pet to remove.");
        addDefault("Invalid-PetType", "{prefix} &cInvalid pet type.");
        addDefault("No-Pet-Permission", "{prefix} &cYou do not have permission to this pet.");
        addDefault("Select-Pet", "{prefix} &7You have selected the &e%pet% &7Pet.");
        addDefault("Select-Pet-Other", "{prefix} &7%player% force selected the pet %pet% for you.");
        addDefault("Select-Pet-Sender", "{prefix} &7You have set %player%'s pet to %pet%");

        addDefault("Other-No-Pet-Permission", "{prefix} &7%player% does not have permission to this pet.");
        addDefault("Type-Not-Supported", "{prefix} &7This pet type is not supported in this version.");
        addDefault("Type-Not-Enabled", "{prefix} &7This pet is currently disabled.");
        addDefault("Pet-Name-Changed", "{prefix} &7Your pets name was changed to: &r%petname%");
        addDefault("Menu-Name", "&a&lSelect a Pet:");
        addDefault("Data-Menu.PetCore-Name", "&a&lPet Data Changer");
        addDefault("No-Spawning", "{prefix} &cPets are not allowed to be spawned in this region.");
        addDefault("Pet-No-Enter", "{prefix} &cPets are not allowed in this region.");
        addDefault("Pet-RenameViaChat", "{prefix} &7Type your pets new name in chat:");
        addDefault("Pet-RenameViaChat-Cancel", "{prefix} &cPet renaming has been canceled");
        addDefault("Pet-RenameViaAnvil", "{prefix} &7Type your pets new name in the anvil, then click the item on the right when complete.");
        addDefault("Pet-RenameFailure", "{prefix} &cSorry, but your pets name could not be changed to: &7{name}");
        addDefault("Player-No-Pet", "{prefix} &7%player% does not have a pet.");

        addDefault("Anvil-Rename.Name", "Rename Pet");
        addDefault("ArmorMenu.ClickToRemove", Arrays.asList("&c ", "&c&lCLICK TO REMOVE"));
        addDefault("ArmorMenu.Restricted", "&cThis pet can not have items added or removed!");

        addDefault("Data-Menu.True", "&etrue");
        addDefault("Data-Menu.False", "&efalse");
    }

    public static String getTrueOrFalse(boolean b) {
        return b ? PetCore.get().getMessages().getString("Data-Menu.True") : PetCore.get().getMessages().getString("Data-Menu.False");
    }

//    @Override
//    public String getString(String path) {
//        return super.getString(path, true);
//    }
}
