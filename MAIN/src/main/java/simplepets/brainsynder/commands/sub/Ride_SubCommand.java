package simplepets.brainsynder.commands.sub;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import simple.brainsynder.commands.annotations.ICommand;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.commands.PetSubCommand;
import simplepets.brainsynder.player.PetOwner;

@ICommand(
        name = "ride",
        usage = "&r &r &6[] &7/pet ride",
        description = "Toggles if you are riding your pet"
)
public class Ride_SubCommand extends PetSubCommand {
    @Override
    public void run(CommandSender sender) {
        if (sender instanceof Player) {
            PetOwner owner = PetOwner.getPetOwner((Player) sender);
            if (owner.hasPet()) {
                IPet pet = owner.getPet();
                if (pet.getPetType().canMount((Player) sender)) pet.toggleRiding(false);
            }
        }
    }
}
