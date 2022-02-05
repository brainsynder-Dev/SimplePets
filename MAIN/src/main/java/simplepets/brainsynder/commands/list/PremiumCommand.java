package simplepets.brainsynder.commands.list;

import lib.brainsynder.commands.annotations.ICommand;
import org.bukkit.command.CommandSender;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.commands.Permission;
import simplepets.brainsynder.commands.PetSubCommand;
import simplepets.brainsynder.files.MessageFile;
import simplepets.brainsynder.files.options.MessageOption;
import simplepets.brainsynder.utils.Premium;

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
        sender.sendMessage(MessageFile.getTranslation(MessageOption.PREFIX) + "§7Purchase Users ID: "+ Premium.USER_ID);
        sender.sendMessage(MessageFile.getTranslation(MessageOption.PREFIX) + "§7Premium Resource ID: "+Premium.USER_ID);
        sender.sendMessage(MessageFile.getTranslation(MessageOption.PREFIX) + "§7Download ID: "+Premium.UNIQUE_DOWNLOAD_ID + "(Unique for all downloads)");
    }
}
