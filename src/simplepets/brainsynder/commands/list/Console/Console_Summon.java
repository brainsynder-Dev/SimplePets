package simplepets.brainsynder.commands.list.Console;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.commands.PetCommand;
import simplepets.brainsynder.commands.annotations.*;
import simplepets.brainsynder.pet.PetDefault;
import simplepets.brainsynder.pet.TypeManager;

@Console
@CommandName(name = "summon")
@CommandUsage(usage = "<pet> <player>")
@CommandPermission(permission = "help")
@CommandDescription(description = "Spawns a pet for the selected player.")
public class Console_Summon extends PetCommand {
    @Override
    public void onCommand(CommandSender sender, String[] args) {
        if (args.length == 0) {
            sendUsage(sender);
        } else {
            TypeManager manager = PetCore.get().getTypeManager();
            PetDefault type = manager.getItem(args[0]);
            if (type == null) {
                sender.sendMessage(PetCore.get().getMessages().getString("Invalid-PetType", true));
                return;
            }
            if (!type.isSupported()) {
                sender.sendMessage(PetCore.get().getMessages().getString("Type-Not-Supported", true));
                return;
            }
            if (!type.isEnabled()) {
                sender.sendMessage(PetCore.get().getMessages().getString("Type-Not-Enabled", true));
                return;
            }
            if (args.length == 1) {
                sendUsage(sender);
            } else {
                Player tp = Bukkit.getPlayer(args[1]);
                if (tp == null) {
                    sender.sendMessage(PetCore.get().getMessages().getString("Player-Not-Found", true)
                            .replace("%player%", args[1]));
                    return;
                }
                sender.sendMessage(PetCore.get().getMessages().getString("Select-Pet-Sender", true)
                        .replace("%pet%", type.getDisplayName())
                        .replace("%player%", tp.getName()));
                tp.sendMessage(PetCore.get().getMessages().getString("Select-Pet-Other", true)
                        .replace("%pet%", type.getDisplayName())
                        .replace("%player%", "Console"));
                type.setPet(tp);
            }
        }
    }
}
