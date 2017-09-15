package simplepets.brainsynder.commands.list.Player;

import org.bukkit.entity.Player;
import simplepets.brainsynder.commands.PetCommand;
import simplepets.brainsynder.commands.annotations.CommandDescription;
import simplepets.brainsynder.commands.annotations.CommandName;
import simplepets.brainsynder.commands.annotations.CommandPermission;
import simplepets.brainsynder.nms.VersionNMS;

@CommandName(name = "register")
@CommandPermission(permission = "register")
@CommandDescription(description = "Re-Registers the pets (Attempted fix for Vanished Pets).")
public class CMD_Register extends PetCommand {
    @Override
    public void onPlayerCommand(Player p, String[] args) {
        VersionNMS.registerPets();
        p.sendMessage("§eSimplePets §6>> §7Re-Registered pets.");
    }
}
