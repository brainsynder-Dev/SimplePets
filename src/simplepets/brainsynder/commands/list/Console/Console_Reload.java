package simplepets.brainsynder.commands.list.Console;

import org.bukkit.command.CommandSender;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.commands.PetCommand;
import simplepets.brainsynder.commands.annotations.CommandDescription;
import simplepets.brainsynder.commands.annotations.CommandName;
import simplepets.brainsynder.commands.annotations.Console;
import simplepets.brainsynder.menu.items.ItemLoaders;

@Console
@CommandName(name = "reload")
@CommandDescription(description = "Reloads the Settings that cant AutoReload.")
public class Console_Reload extends PetCommand {
    @Override
    public void onCommand(CommandSender sender, String[] args) {
        PetCore.get().reload();
        ItemLoaders.reloadLoaders();
        sender.sendMessage(PetCore.get().getMessages().getString("Reload-Complete", true));
    }
}
