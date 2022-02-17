package simplepets.brainsynder.commands.list;

import com.google.common.collect.Lists;
import lib.brainsynder.commands.annotations.ICommand;
import lib.brainsynder.utils.Colorize;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import simplepets.brainsynder.api.plugin.config.ConfigOption;
import simplepets.brainsynder.commands.Permission;
import simplepets.brainsynder.commands.PetSubCommand;
import simplepets.brainsynder.commands.PetsCommand;

import java.util.List;

@ICommand(
        name = "help",
        description = "Lists all the commands for the plugin"
)
@Permission(permission = "help", defaultAllow = true)
public class HelpCommand extends PetSubCommand {
    private final PetsCommand parent;

    public HelpCommand(PetsCommand parent) {
        super(parent.getPlugin());
        this.parent = parent;
    }

    @Override
    public void run(CommandSender sender) {
        List<PetSubCommand> adminCommands = Lists.newArrayList();
        parent.getSubCommands().forEach(sub -> {
            if (sub.getClass().isAnnotationPresent(Permission.class)) {
                Permission permission = sub.getClass().getAnnotation(Permission.class);
                if (permission.adminCommand() && sender.hasPermission(permission.permission())) {
                    adminCommands.add(sub);
                    return;
                }
                if (!permission.defaultAllow() && !sender.hasPermission(permission.permission())) return;
                if (ConfigOption.INSTANCE.MISC_TOGGLES_IGNORE_ALLOWS_DEFAULT.getValue()) {
                    if (!sender.hasPermission(permission.permission())) return;
                }
            }
            sub.sendUsage(sender);
        });

        if (adminCommands.isEmpty()) return;
        sender.sendMessage(ChatColor.RESET.toString());
        sender.sendMessage(Colorize.translateBungeeHex("&r &r &#e1eb5b[] &#d1c9c9----- &#b35349&lADMIN COMMANDS&r&#d1c9c9 ----- &#e1eb5b[]"));
        adminCommands.forEach(petSubCommand -> petSubCommand.sendUsage(sender));
    }
}
