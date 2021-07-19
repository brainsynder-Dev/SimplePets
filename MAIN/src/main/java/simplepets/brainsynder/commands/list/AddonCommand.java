package simplepets.brainsynder.commands.list;

import lib.brainsynder.commands.annotations.ICommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.plugin.SimplePets;
import simplepets.brainsynder.commands.Permission;
import simplepets.brainsynder.commands.PetSubCommand;
import simplepets.brainsynder.files.MessageFile;
import simplepets.brainsynder.files.options.MessageOption;
import simplepets.brainsynder.managers.AddonManager;
import simplepets.brainsynder.menu.inventory.AddonMenu;

import java.io.File;
import java.util.List;

@ICommand(
        name = "addon",
        usage = "[reload]",
        description = "Opens a GUI to download/toggle addons for the plugin"
)
@Permission(permission = "addon", adminCommand = true, additionalPermissions = {"reload"})
public class AddonCommand extends PetSubCommand {

    public AddonCommand(PetCore plugin) {
        super(plugin);
    }

    @Override
    public List<String> handleCompletions(List<String> completions, CommandSender sender, int index, String[] args) {
        if (!canExecute(sender)) return super.handleCompletions(completions, sender, index, args);
        if ((index == 1) && sender.hasPermission(getPermission("reload"))) completions.add("reload");
        return super.handleCompletions(completions, sender, index, args);
    }

    @Override
    public void run(CommandSender sender, String[] args) {
        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("reload") && sender.hasPermission(getPermission("reload"))) {
                AddonManager manager = PetCore.getInstance().getAddonManager();
                manager.cleanup();
                File folder = manager.getFolder();
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (!folder.exists()) return;
                        for (File file : folder.listFiles()) {
                            manager.loadAddon(file);
                        }
                        sender.sendMessage(MessageFile.getTranslation(MessageOption.PREFIX)+ ChatColor.GRAY+"All Addons have been reloaded");
                    }
                }.runTaskLater(PetCore.getInstance(), 1);
                return;
            }
        }

        if (!(sender instanceof Player)) {
            sendUsage(sender);
            return;
        }

        SimplePets.getUserManager().getPetUser((Player) sender).ifPresent(user -> {
            SimplePets.getGUIHandler().getInventory(AddonMenu.class).ifPresent(selectionMenu -> selectionMenu.open(user));
        });
    }
}
