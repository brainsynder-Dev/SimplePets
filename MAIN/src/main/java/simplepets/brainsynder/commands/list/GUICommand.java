package simplepets.brainsynder.commands.list;

import lib.brainsynder.commands.annotations.ICommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.plugin.SimplePets;
import simplepets.brainsynder.commands.Permission;
import simplepets.brainsynder.commands.PetSubCommand;
import simplepets.brainsynder.menu.inventory.SelectionMenu;

@ICommand(
        name = "gui",
        description = "Opens the pet gui"
)
@Permission(permission = "gui", defaultAllow = true)
public class GUICommand extends PetSubCommand {
    public GUICommand(PetCore plugin) {
        super(plugin);
    }

    @Override
    public void run(CommandSender sender) {
        SimplePets.getUserManager().getPetUser((Player) sender).ifPresent(user -> {
            SimplePets.getGUIHandler().getInventory(SelectionMenu.class).ifPresent(selectionMenu -> selectionMenu.open(user));
        });
    }
}
