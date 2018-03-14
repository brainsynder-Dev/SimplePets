package simplepets.brainsynder.storage.files;

import org.bukkit.plugin.Plugin;
import simplepets.brainsynder.storage.files.base.FileMaker;

public class Messages extends FileMaker {
    public Messages(Plugin plugin, String fileName) {
        super(plugin, fileName);
    }

    public void loadDefaults() {
        setDefault("No-Permission", "&eSimplePets &6>> &cYou do not have permission.");
        setDefault("Reload-Complete", "&eSimplePets &6>> &7Inventory and Files have been reloaded.");
        setDefault("Unknown-commands", "&eSimplePets &6>> &7Unknown SimplePets commands.");
        setDefault("Player-Not-Found", "&eSimplePets &6>> &7%player% is not valid.");
        setDefault("Pet-Remove-Self-Removed", "&eSimplePets &6>> &7Your pet was removed.");
        setDefault("Pet-Remove-Self-No-Pet", "&eSimplePets &6>> &7You do not have a pet to remove.");
        setDefault("Pet-Remove-Other-Remover", "&eSimplePets &6>> &7%player%'s pet was successfully removed.");
        setDefault("Pet-Remove-Other-Target", "&eSimplePets &6>> &7%player% removed your pet.");
        setDefault("Pet-Remove-Other-No-Pet", "&eSimplePets &6>> &7%player% does not have a pet to remove.");
        setDefault("Invalid-PetType", "&eSimplePets &6>> &cInvalid pet type.");
        setDefault("No-Pet-Permission", "&eSimplePets &6>> &cYou do not have permission to this pet.");
        setDefault("Select-Pet", "&eSimplePets &6>> &7You have selected the &e%pet% &7Pet.");
        setDefault("Select-Pet-Other", "&eSimplePets &6>> &7%player% force selected the pet %pet% for you.");
        setDefault("Select-Pet-Sender", "&eSimplePets &6>> &7You have set %player%'s pet to %pet%");

        setDefault("Other-No-Pet-Permission", "&eSimplePets &6>> &7%player% does not have permission to this pet.");
        setDefault("Type-Not-Supported", "&eSimplePets &6>> &7This pet type is not supported in this version.");
        setDefault("Type-Not-Enabled", "&eSimplePets &6>> &7This pet is currently disabled.");
        setDefault("Pet-Name-Changed", "&eSimplePets &6>> &7Your pets name was changed to: &r%petname%");
        setDefault("Menu-Name", "&a&lSelect a Pet:");
        setDefault("Data-Menu.PetCore-Name", "&a&lPet Data Changer");
        setDefault("No-Spawning", "&eSimplePets &6>> &cPets are not allowed to be spawned in this region.");
        setDefault("Pet-No-Enter", "&eSimplePets &6>> &cPets are not allowed in this region.");
        setDefault("Pet-RenameViaChat", "&eSimplePets &6>> &7Type your pets new name in chat:");
        setDefault("Pet-RenameViaAnvil", "&eSimplePets &6>> &7Type your pets new name in the anvil, then click the item on the right when complete.");
        setDefault("Pet-RenameFailure", "&eSimplePets &6>> &cSorry, but your pets name could not be changed to: &7{name}");
        setDefault("Player-No-Pet", "&eSimplePets &6>> &7%player% does not have a pet.");
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
