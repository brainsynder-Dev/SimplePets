package simplepets.brainsynder.commands.sub;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import simple.brainsynder.commands.annotations.ICommand;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.commands.PetSubCommand;
import simplepets.brainsynder.player.PetOwner;

@ICommand(
        name = "saves",
        usage = "&r &r &6[] &7/pet saves",
        description = "Opens the Menu where all of your pet saves are"
)
public class Saves_SubCommand extends PetSubCommand {
    @Override
    public void run(CommandSender sender) {
        if (sender instanceof Player) {
            PetCore.get().getInvLoaders().SAVES.open(PetOwner.getPetOwner((Player) sender));
        }
    }
}
