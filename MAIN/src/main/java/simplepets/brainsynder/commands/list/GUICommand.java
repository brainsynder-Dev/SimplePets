package simplepets.brainsynder.commands.list;

import lib.brainsynder.commands.annotations.ICommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.plugin.SimplePets;
import simplepets.brainsynder.commands.Permission;
import simplepets.brainsynder.commands.PetSubCommand;
import simplepets.brainsynder.files.MessageFile;
import simplepets.brainsynder.files.options.MessageOption;
import simplepets.brainsynder.menu.inventory.SelectionMenu;

@ICommand(
        name = "gui",
        usage = "[player]",
        description = "Opens the pet gui"
)
@Permission(permission = "gui", defaultAllow = true, additionalPermissions = {"other"})
public class GUICommand extends PetSubCommand {
    public GUICommand(PetCore plugin) {
        super(plugin);
    }

    @Override
    public void run(CommandSender sender, String[] args) {
        if ((args.length > 0) && sender.hasPermission(getPermission("other"))) {
            String selector = args[0];
            Player target = Bukkit.getPlayerExact(selector);
            if (target == null) {
                sender.sendMessage(MessageFile.getTranslation(MessageOption.PLAYER_NOT_ONLINE));
                return;
            }

            // Will open the selection gui for the selected player
            SimplePets.getUserManager().getPetUser(target).ifPresent(user -> {
                SimplePets.getGUIHandler().getInventory(SelectionMenu.class).ifPresent(selectionMenu -> selectionMenu.open(user));
            });
            return;
        }
        SimplePets.getUserManager().getPetUser((Player) sender).ifPresent(user -> {
            SimplePets.getGUIHandler().getInventory(SelectionMenu.class).ifPresent(selectionMenu -> selectionMenu.open(user));
        });
    }
}
