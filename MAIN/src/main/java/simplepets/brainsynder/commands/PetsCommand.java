package simplepets.brainsynder.commands;

import com.google.common.collect.Lists;
import lib.brainsynder.commands.ParentCommand;
import lib.brainsynder.commands.annotations.ICommand;
import lib.brainsynder.utils.Colorize;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.commands.list.*;

import java.util.List;

@ICommand(
        name = "pet",
        alias = {"pets", "simplepets", "simplepet"},
        description = "Main SimplePets Command/Opens The Pet Selection GUI"
)
public class PetsCommand extends ParentCommand<PetSubCommand> {
    private final PetCore plugin;

    public PetsCommand(PetCore plugin) {
        this.plugin = plugin;
        registerSub(new SummonCommand(plugin));
        registerSub(new ModifyCommand(plugin));
        registerSub(new RemoveCommand(plugin));
        registerSub(new ListCommand(plugin));
        registerSub(new GUICommand(plugin));
        registerSub(new RenameCommand(plugin));
        registerSub(new PurchasedCommand (plugin));
        registerSub(new PermissionsCommand(this));
        registerSub(new RegenerateCommand (plugin));
        registerSub(new DebugCommand (this));
        registerSub(new AddonCommand (plugin));
        registerSub(new DatabaseCommand (plugin));

        ReportCommand reportCommand = new ReportCommand(plugin);
        plugin.getServer().getPluginManager().registerEvents(reportCommand, plugin);
        registerSub(reportCommand);
    }

    public PetCore getPlugin() {
        return plugin;
    }

    @Override
    public void run(CommandSender sender, String[] args) {
        List<PetSubCommand> adminCommands = Lists.newArrayList();

        getSubCommands().forEach(sub -> {
            if (sub.getClass().isAnnotationPresent(Permission.class)) {
                Permission permission = sub.getClass().getAnnotation(Permission.class);
                if (permission.adminCommand() && sender.hasPermission(permission.permission())) {
                    adminCommands.add(sub);
                    return;
                }
                if (!permission.defaultAllow() && !sender.hasPermission(permission.permission())) return;
            }
            sub.sendUsage(sender);
        });

        if (adminCommands.isEmpty()) return;
        sender.sendMessage(ChatColor.RESET.toString());
        sender.sendMessage(Colorize.translateBungeeHex("&r &r &#e1eb5b[] &#d1c9c9----- &#b35349&lADMIN COMMANDS&r&#d1c9c9 ----- &#e1eb5b[]"));
        adminCommands.forEach(petSubCommand -> petSubCommand.sendUsage(sender));
    }
}
