package simplepets.brainsynder.commands.sub;

import lib.brainsynder.commands.annotations.ICommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.commands.PetSubCommand;
import simplepets.brainsynder.player.PetOwner;

@ICommand(
        name = "menu",
        usage = "&r &r &6[] &7/pet menu",
        description = "Opens The Pet Selection GUI",
        style = "{usage}"
)
public class Menu_SubCommand extends PetSubCommand {
    @Override
    public void run(CommandSender sender) {
        if (sender instanceof Player) {
            PetCore.get().getInvLoaders().SELECTION.open(PetOwner.getPetOwner((Player) sender));
        }
    }
}
