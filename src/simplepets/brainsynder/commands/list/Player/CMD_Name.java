package simplepets.brainsynder.commands.list.Player;

import org.bukkit.entity.Player;
import simplepets.brainsynder.commands.PetCommand;
import simplepets.brainsynder.commands.annotations.CommandDescription;
import simplepets.brainsynder.commands.annotations.CommandName;
import simplepets.brainsynder.player.PetOwner;

@CommandName(name = "name")
@CommandDescription(description = "Change your pets name.")
public class CMD_Name extends PetCommand<Player> {
    @Override
    public void onCommand(Player p, String[] args) {
        PetOwner petOwner = PetOwner.getPetOwner(p);
        petOwner.renamePet();
    }
}
