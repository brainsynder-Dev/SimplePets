package simplepets.brainsynder.commands.sub;

import lib.brainsynder.commands.annotations.ICommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.commands.PetSubCommand;
import simplepets.brainsynder.player.PetOwner;

@ICommand(
        name = "hat",
        usage = "&r &r &6[] &7/pet hat",
        description = "Toggles your pet on/off your head"
)
public class Hat_SubCommand extends PetSubCommand {
    @Override
    public void run(CommandSender sender) {
        if (sender instanceof Player) {
            PetOwner owner = PetOwner.getPetOwner((Player) sender);
            if (owner.hasPet()) {
                IPet pet = owner.getPet();
                if (pet.getPetType().canHat((Player) sender)) pet.toggleHat();
            }
        }
    }
}
