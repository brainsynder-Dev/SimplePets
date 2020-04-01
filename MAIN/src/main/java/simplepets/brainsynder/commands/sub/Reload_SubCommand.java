package simplepets.brainsynder.commands.sub;

import lib.brainsynder.commands.annotations.ICommand;
import org.bukkit.command.CommandSender;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.commands.PetSubCommand;
import simplepets.brainsynder.commands.annotations.Permission;

import java.util.Arrays;

@ICommand(
        name = "reload",
        usage = "&r &r &6[] &7/pet reload [pets|sql|inv|items]",
        description = "Reloads the Settings that cant AutoReload"
)
@Permission(permission = "reload")
public class Reload_SubCommand extends PetSubCommand {

    public Reload_SubCommand() {
        registerCompletion(1, Arrays.asList("pets","sql","inv","items"));
    }

    @Override
    public void run(CommandSender sender, String[] args) {
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
