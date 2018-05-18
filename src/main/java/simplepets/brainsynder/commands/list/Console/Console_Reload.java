package simplepets.brainsynder.commands.list.Console;

import org.bukkit.command.CommandSender;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.commands.PetCommand;
import simplepets.brainsynder.commands.annotations.CommandDescription;
import simplepets.brainsynder.commands.annotations.CommandName;
import simplepets.brainsynder.commands.annotations.CommandUsage;
import simplepets.brainsynder.commands.annotations.Console;

@Console
@CommandName(name = "reload")
@CommandUsage(usage = "[pets|sql|inv|items]")
@CommandDescription(description = "Reloads the Settings that cant AutoReload.")
public class Console_Reload extends PetCommand {
    @Override
    public void onCommand(CommandSender sender, String[] args) {
        PetCore pc = PetCore.get();
        if (args.length == 0) {
            pc.reload(2);
            pc.getItemLoaders().reloadLoaders();
            pc.getInvLoaders().reloadLoaders();
        } else {
            String type = args[0].toLowerCase();
            switch (type) {
                case "pets":
                    pc.reload(0);
                    break;
                case "sql":
                    pc.reload(1);
                    break;
                case "inv":
                    pc.getInvLoaders().reloadLoaders();
                    break;
                case "items":
                    pc.getItemLoaders().reloadLoaders();
                    break;
                default:
                    pc.reload(2);
                    pc.getItemLoaders().reloadLoaders();
                    pc.getInvLoaders().reloadLoaders();
            }
        }
        sender.sendMessage(PetCore.get().getMessages().getString("Reload-Complete", true));
    }
}
