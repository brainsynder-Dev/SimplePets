package simplepets.brainsynder.commands.list.Player;

import org.bukkit.entity.Player;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.commands.PetCommand;
import simplepets.brainsynder.commands.annotations.CommandDescription;
import simplepets.brainsynder.commands.annotations.CommandName;
import simplepets.brainsynder.commands.annotations.CommandPermission;
import simplepets.brainsynder.menu.inventory.InvLoaders;
import simplepets.brainsynder.menu.items.ItemLoaders;

@CommandName(name = "reload")
@CommandPermission(permission = "reload")
@CommandDescription(description = "Reloads the Settings that cant AutoReload.")
public class CMD_Reload extends PetCommand<Player> {
    @Override
    public void onCommand(Player p, String[] args) {
        PetCore.get().reload();
        ItemLoaders.reloadLoaders();
        InvLoaders.reloadLoaders();
        p.sendMessage(PetCore.get().getMessages().getString("Reload-Complete", true));
    }
}
