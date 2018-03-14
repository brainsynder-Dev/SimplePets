package simplepets.brainsynder.commands.list.Player;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.commands.PetCommand;
import simplepets.brainsynder.commands.annotations.CommandDescription;
import simplepets.brainsynder.commands.annotations.CommandName;
import simplepets.brainsynder.commands.annotations.CommandUsage;
import simplepets.brainsynder.player.PetOwner;

@CommandName(name = "remove")
@CommandUsage(usage = "[player]")
@CommandDescription(description = "Remove your pet or another players.")
public class CMD_Remove extends PetCommand<Player> {
    @Override
    public void onCommand(Player p, String[] args) {
        if (args.length == 0) {
            PetOwner petOwner = PetOwner.getPetOwner(p);
            if (petOwner.hasPet()) {
                petOwner.removePet();
                p.sendMessage(PetCore.get().getMessages().getString("Pet-Remove-Self-Removed", true));
            } else {
                p.sendMessage(PetCore.get().getMessages().getString("Pet-Remove-Self-No-Pet", true));
            }
        } else {
            if (p.hasPermission("Pet.remove.other")) {
                Player tp = Bukkit.getPlayer(args[0]);
                if (tp == null) {
                    p.sendMessage(PetCore.get().getMessages().getString("Player-Not-Found", true).replace("%player%", tp.getName()));
                    return;
                }
                PetOwner petOwner = PetOwner.getPetOwner(tp);
                if (petOwner.hasPet()) {
                    p.sendMessage(PetCore.get().getMessages().getString("Pet-Remove-Other-Remover", true).replace("%player%", tp.getName()));
                    tp.sendMessage(PetCore.get().getMessages().getString("Pet-Remove-Other-Target", true).replace("%player%", p.getName()));
                    petOwner.removePet();
                } else {
                    p.sendMessage(PetCore.get().getMessages().getString("Pet-Remove-Other-No-Pet", true).replace("%player%", tp.getName()));
                }
            } else {
                p.sendMessage(PetCore.get().getMessages().getString("No-Permission", true));
            }
        }
    }
}
