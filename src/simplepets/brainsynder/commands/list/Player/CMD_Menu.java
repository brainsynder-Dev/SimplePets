package simplepets.brainsynder.commands.list.Player;

import org.bukkit.entity.Player;
import simplepets.brainsynder.commands.PetCommand;
import simplepets.brainsynder.commands.annotations.CommandDescription;
import simplepets.brainsynder.commands.annotations.CommandName;
import simplepets.brainsynder.menu.inventory.InvLoaders;
import simplepets.brainsynder.player.PetOwner;

@CommandName(name = "menu")
@CommandDescription(description = "Opens the pet selection menu.")
public class CMD_Menu extends PetCommand<Player> {
    @Override
    public void onCommand(Player p, String[] args) {
        InvLoaders.SELECTION.open(PetOwner.getPetOwner(p));
    }
}
