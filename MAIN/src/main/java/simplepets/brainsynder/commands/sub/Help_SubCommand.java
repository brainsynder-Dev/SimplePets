package simplepets.brainsynder.commands.sub;

import lib.brainsynder.commands.annotations.ICommand;
import org.bukkit.command.CommandSender;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.commands.PetCommand;
import simplepets.brainsynder.commands.PetSubCommand;
import simplepets.brainsynder.commands.annotations.Permission;

@ICommand(
        name = "help",
        usage = "&r &r &6[] &7/pet help",
        description = "Collects Information about your server that we use for finding bugs",
        style = "{usage}"
)
@Permission(permission = "help")
public class Help_SubCommand extends PetSubCommand {
    private PetCommand parent;
    public Help_SubCommand(PetCommand parent) {
        this.parent = parent;
    }

    @Override
    public void run(CommandSender sender) {
        sender.sendMessage(PetCore.get().getCommands().getString("Help.Header", true));

        for (PetSubCommand command : parent.getSubCommands()) {
            if (command.hasPermission(sender)) command.sendUsage(sender);
        }
    }
}
