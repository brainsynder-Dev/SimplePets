package simplepets.brainsynder.commands.list.Player;

import org.bukkit.entity.Player;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.commands.PetCommand;
import simplepets.brainsynder.commands.annotations.CommandDescription;
import simplepets.brainsynder.commands.annotations.CommandName;
import simplepets.brainsynder.commands.annotations.CommandPermission;
import simplepets.brainsynder.commands.annotations.CommandUsage;

@CommandName(name = "reload")
@CommandUsage(usage = "[pets|sql|inv|items]")
@CommandPermission(permission = "reload")
@CommandDescription(description = "Reloads the Settings that cant AutoReload.")
public class CMD_Reload extends PetCommand<Player> {
    @Override
    public void onCommand(Player p, String[] args) {
        PetCore pc = PetCore.get();
        if (args.length == 0) {
            pc.reload(2);
            pc.getItemLoaders().reloadLoaders();
            pc.getInvLoaders().reloadLoaders();
        } else {
            String type = args[0].toLowerCase();
            switch (type) {
                case "pets":
                    pc.reload(0);
                    break;
                case "sql":
                    pc.reload(1);
                    break;
                case "inv":
                    pc.getInvLoaders().reloadLoaders();
                    break;
                case "items":
                    pc.getItemLoaders().reloadLoaders();
                    break;
                default:
                    pc.reload(2);
                    pc.getItemLoaders().reloadLoaders();
                    pc.getInvLoaders().reloadLoaders();
            }
        }
        p.sendMessage(PetCore.get().getMessages().getString("Reload-Complete", true));
    }
}
