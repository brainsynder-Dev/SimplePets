package simplepets.brainsynder.commands.list;

import lib.brainsynder.commands.annotations.ICommand;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.plugin.SimplePets;
import simplepets.brainsynder.commands.Permission;
import simplepets.brainsynder.commands.PetSubCommand;
import simplepets.brainsynder.managers.AddonManager;
import simplepets.brainsynder.menu.inventory.AddonMenu;

import java.util.List;

@ICommand(
        name = "addon",
        description = "Opens a GUI to download/toggle addons for the plugin"
)
@Permission(permission = "addon", adminCommand = true)
public class AddonCommand extends PetSubCommand {

    public AddonCommand(PetCore plugin) {
        super(plugin);
    }

    @Override
    public List<String> handleCompletions(List<String> completions, CommandSender sender, int index, String[] args) {
        if (!canExecute(sender)) return super.handleCompletions(completions, sender, index, args);
        AddonManager manager = getPlugin().getAddonManager();

        if (index == 1) {
            manager.getLoadedAddons().forEach(addon -> completions.add(addon.getNamespace().namespace()));
        }

        return super.handleCompletions(completions, sender, index, args);
    }

    @Override
    public void run(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "You need to be a player to run this command.");
            return;
        }

        SimplePets.getUserManager().getPetUser((Player) sender).ifPresent(user -> {
            SimplePets.getGUIHandler().getInventory(AddonMenu.class).ifPresent(selectionMenu -> selectionMenu.open(user));
        });
    }
}
