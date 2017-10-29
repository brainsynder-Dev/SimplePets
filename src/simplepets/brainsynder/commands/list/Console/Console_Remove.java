package simplepets.brainsynder.commands.list.Console;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.commands.PetCommand;
import simplepets.brainsynder.commands.annotations.*;
import simplepets.brainsynder.player.PetOwner;

@Console
@CommandName(name = "remove")
@CommandUsage(usage = "<player>")
@CommandPermission(permission = "help")
@CommandDescription(description = "Remove the selected players' pet.")
public class Console_Remove extends PetCommand {
    @Override
    public void onCommand(CommandSender sender, String[] args) {
        if (args.length == 0) {
            sendUsage(sender);
        } else {
            Player tp = Bukkit.getPlayerExact(args[0]);
            if (tp == null) {
                sender.sendMessage(PetCore.get().getMessages().getString("Player-Not-Found", true)
                        .replace("%player%", args[0]));
                return;
            }
            PetOwner petOwner = PetOwner.getPetOwner(tp);
            if (petOwner.hasPet()) {
                sender.sendMessage(PetCore.get().getMessages().getString("Pet-Remove-Other-Remover", true)
                        .replace("%player%", tp.getName()));
                tp.sendMessage(PetCore.get().getMessages().getString("Pet-Remove-Other-Target", true)
                        .replace("%player%", "Console"));
                petOwner.removePet();
            } else {
                sender.sendMessage(PetCore.get().getMessages().getString("Pet-Remove-Other-No-Pet", true)
                        .replace("%player%", tp.getName()));
            }
        }
    }
}
