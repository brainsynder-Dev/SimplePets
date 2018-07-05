package simplepets.brainsynder.commands.list.Player;

import org.bukkit.entity.Player;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.commands.PetCommand;
import simplepets.brainsynder.commands.annotations.CommandDescription;
import simplepets.brainsynder.commands.annotations.CommandName;
import simplepets.brainsynder.player.PetOwner;

@CommandName(name = "saves")
@CommandDescription(description = "Opens the Menu where all of your pet saves are.")
public class CMD_Saves extends PetCommand<Player> {
    @Override
    public void onCommand(Player p, String[] args) {
        PetCore.get().getInvLoaders().SAVES.open(PetOwner.getPetOwner(p));
    }
}
