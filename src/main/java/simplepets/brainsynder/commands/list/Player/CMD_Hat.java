package simplepets.brainsynder.commands.list.Player;

import org.bukkit.entity.Player;
import simplepets.brainsynder.commands.PetCommand;
import simplepets.brainsynder.commands.annotations.CommandDescription;
import simplepets.brainsynder.commands.annotations.CommandName;
import simplepets.brainsynder.pet.IPet;
import simplepets.brainsynder.player.PetOwner;

@CommandName(name = "hat")
@CommandDescription(description = "Set your pet on/off your head.")
public class CMD_Hat extends PetCommand<Player> {
    @Override
    public void onCommand(Player p, String[] args) {
        PetOwner petOwner = PetOwner.getPetOwner(p);
        if (petOwner.hasPet()) {
            IPet pet = petOwner.getPet();
            if (pet.getPetType().canHat(p)) pet.hatPet();
        }
    }
}
