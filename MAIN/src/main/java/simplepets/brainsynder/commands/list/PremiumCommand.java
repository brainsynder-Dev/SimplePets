package simplepets.brainsynder.commands.list;

import lib.brainsynder.commands.annotations.ICommand;
import org.bukkit.command.CommandSender;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.commands.Permission;
import simplepets.brainsynder.commands.PetSubCommand;
import simplepets.brainsynder.files.MessageFile;
import simplepets.brainsynder.files.options.MessageOption;

@ICommand(
        name = "premium",
        description = "Shows the information of the user who purchased the resource"
)
@Permission(permission = "premium", adminCommand = true)
public class PremiumCommand extends PetSubCommand {

    public PremiumCommand(PetCore plugin) {
        super(plugin);
    }

    @Override
    public void run(CommandSender sender, String[] args) {
        sender.sendMessage(MessageFile.getTranslation(MessageOption.PREFIX) + "§7Purchase Users ID: "+getPlugin().getPurchaseUserID());
        sender.sendMessage(MessageFile.getTranslation(MessageOption.PREFIX) + "§7Premium Resource ID: "+getPlugin().getPremiumID());
        sender.sendMessage(MessageFile.getTranslation(MessageOption.PREFIX) + "§7Download ID: "+getPlugin().getPremiumUniqueID() + "(Unique for all downloads)");
    }
}
