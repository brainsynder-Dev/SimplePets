package simplepets.brainsynder.files;

import org.bukkit.plugin.Plugin;

public class Messages extends FileMaker {
    public Messages(Plugin plugin, String fileName) {
        super(plugin, fileName);
    }

    public void loadDefaults() {
        if (!isSet("No-Permission"))
            set("No-Permission", "&eSimplePets &6>> &cYou do not have permission.");
        if (!isSet("Reload-Complete"))
            set("Reload-Complete", "&eSimplePets &6>> &7Inventory and Files have been reloaded.");
        if (!isSet("Unknown-commands"))
            set("Unknown-commands", "&eSimplePets &6>> &7Unknown SimplePets commands.");
        if (!isSet("Player-Not-Found"))
            set("Player-Not-Found", "&eSimplePets &6>> &7%player% is not valid.");
        if (!isSet("Pet-Remove-Self-Removed"))
            set("Pet-Remove-Self-Removed", "&eSimplePets &6>> &7Your pet was removed.", "Message is sent if player removes their pet via", "/pet remove");
        if (!isSet("Pet-Remove-Self-No-Pet"))
            set("Pet-Remove-Self-No-Pet", "&eSimplePets &6>> &7You do not have a pet to remove.");
        if (!isSet("Pet-Remove-Other-Remover"))
            set("Pet-Remove-Other-Remover", "&eSimplePets &6>> &7%player%'s pet was successfully removed.");
        if (!isSet("Pet-Remove-Other-Target"))
            set("Pet-Remove-Other-Target", "&eSimplePets &6>> &7%player% removed your pet.");
        if (!isSet("Pet-Remove-Other-No-Pet"))
            set("Pet-Remove-Other-No-Pet", "&eSimplePets &6>> &7%player% does not have a pet to remove.");
        if (!isSet("Invalid-PetType"))
            set("Invalid-PetType", "&eSimplePets &6>> &cInvalid pet type.");
        if (!isSet("No-Pet-Permission"))
            set("No-Pet-Permission", "&eSimplePets &6>> &cYou do not have permission to this pet.");
        if (!isSet("Select-Pet"))
            set("Select-Pet", "&eSimplePets &6>> &7You have selected the &e%pet% &7Pet.");
        if (!isSet("Select-Pet-Other"))
            set("Select-Pet-Other", "&eSimplePets &6>> &7%player% force selected the pet %pet% for you.");
        if (!isSet("Select-Pet-Sender"))
            set("Select-Pet-Sender", "&eSimplePets &6>> &7You have set %player%'s pet to %pet%");

        if (!isSet("Other-No-Pet-Permission"))
            set("Other-No-Pet-Permission", "&eSimplePets &6>> &7%player% does not have permission to this pet.");
        if (!isSet("Type-Not-Supported"))
            set("Type-Not-Supported", "&eSimplePets &6>> &7This pet type is not supported in this version.");
        if (!isSet("Type-Not-Enabled"))
            set("Type-Not-Enabled", "&eSimplePets &6>> &7This pet is currently disabled.");
        if (!isSet("Pet-Name-Changed"))
            set("Pet-Name-Changed", "&eSimplePets &6>> &7Your pets name was changed to: &r%petname%");
        if (!isSet("Menu-Name"))
            set("Menu-Name", "&a&lSelect a Pet:");
        if (!isSet("Data-Menu.PetCore-Name"))
            set("Data-Menu.PetCore-Name", "&a&lPet Data Changer");
        if (!isSet("No-Spawning"))
            set("No-Spawning", "&eSimplePets &6>> &cPets are not allowed to be spawned in this region.", "Message sent to the player when they", "Try to spawn a pet in a WorldGuard region", "That is not added to the region list");
        if (!isSet("Pet-No-Enter"))
            set("Pet-No-Enter", "&eSimplePets &6>> &cPets are not allowed in this region.", "Message sent to the player when their", "pet tries to enter a WorldGuard region", "That is not added to the region list");
        if (!isSet("Pet-RenameViaChat"))
            set("Pet-RenameViaChat", "&eSimplePets &6>> &7Type your pets new name in chat (Type 'cancel' to cancel):");
        if (!isSet("Pet-RenameViaChat-Cancel"))
            set("Pet-RenameViaChat-Cancel", "&eSimplePets &6>> &cPet renaming has been canceled");
        if (!isSet("Pet-RenameViaAnvil"))
            set("Pet-RenameViaAnvil", "&eSimplePets &6>> &7Type your pets new name in the anvil, then click the item on the right when complete.");
        if (!isSet("Pet-RenameFailure"))
            set("Pet-RenameFailure", "&eSimplePets &6>> &cSorry, but your pets name could not be changed to: &7{name}");
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
