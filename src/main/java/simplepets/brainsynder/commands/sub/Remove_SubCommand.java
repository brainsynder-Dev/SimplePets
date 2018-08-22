package simplepets.brainsynder.commands.sub;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import simple.brainsynder.commands.annotations.ICommand;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.commands.PetSubCommand;
import simplepets.brainsynder.player.PetOwner;

@ICommand(
        name = "remove",
        usage = "&r &r &6[] &7/pet remove [player]",
        description = "Remove your pet or another players"
)
public class Remove_SubCommand extends PetSubCommand {
    public Remove_SubCommand () {
        registerCompletion(1, (commandSender, list, s) -> {
            list.addAll(getOnlinePlayers());
            return commandSender.hasPermission("Pet.commands.remove.other");
        });
    }

    @Override
    public void run(CommandSender sender, String[] args) {
        if (args.length == 0) {
            if (sender instanceof Player) {
                remove((Player) sender);
            }else{
                sendUsage(sender);
            }
            return;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage(PetCore.get().getMessages().getString("Player-Not-Found", true).replace("%player%", args[0]));
            return;
        }

        if (target.getName().equals(sender.getName())) {
            remove(target);
            return;
        }

        if (!sender.hasPermission("Pet.commands.remove.other")) {
            sender.sendMessage(PetCore.get().getMessages().getString("No-Permission", true));
            return;
        }

        PetOwner petOwner = PetOwner.getPetOwner(target);
        if (petOwner == null) return;
        if (petOwner.hasPet()) {
            sender.sendMessage(PetCore.get().getMessages().getString("Pet-Remove-Other-Remover", true).replace("%player%", target.getName()));
            target.sendMessage(PetCore.get().getMessages().getString("Pet-Remove-Other-Target", true).replace("%player%", sender.getName()));
            petOwner.removePet();
        } else {
            sender.sendMessage(PetCore.get().getMessages().getString("Pet-Remove-Other-No-Pet", true).replace("%player%", target.getName()));
        }

    }

    private void remove (Player player) {
        PetOwner petOwner = PetOwner.getPetOwner(player);
        if (petOwner == null) return;
        if (petOwner.hasPet()) {
            petOwner.removePet();
            player.sendMessage(PetCore.get().getMessages().getString("Pet-Remove-Self-Removed", true));
        } else {
            player.sendMessage(PetCore.get().getMessages().getString("Pet-Remove-Self-No-Pet", true));
        }

    }
}
