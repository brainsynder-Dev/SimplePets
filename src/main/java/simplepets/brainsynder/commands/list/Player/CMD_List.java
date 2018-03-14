package simplepets.brainsynder.commands.list.Player;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import simple.brainsynder.nms.ITellraw;
import simple.brainsynder.utils.Reflection;
import simplepets.brainsynder.commands.PetCommand;
import simplepets.brainsynder.commands.annotations.CommandDescription;
import simplepets.brainsynder.commands.annotations.CommandName;
import simplepets.brainsynder.commands.annotations.CommandPermission;
import simplepets.brainsynder.pet.PetType;

@CommandName(name = "list")
@CommandPermission(permission = "list")
@CommandDescription(description = "Shows a list of pets that are in the plugin.")
public class CMD_List extends PetCommand<Player> {
    @Override
    public void onCommand(Player p, String[] args) {
        ITellraw tellraw = Reflection.getTellraw("§ePet list §6(§7" + PetType.values().length + "§6)§e: ");
        int i = 1;
        for (PetType type : PetType.values()) {
            tellraw.then(type.getConfigName());
            if (type.isSupported()) {
                tellraw.color(ChatColor.GRAY);
            } else {
                tellraw.color(ChatColor.RED);
                tellraw.tooltip("§cPet is not supported", "§cin your current server version");
            }
            if ((PetType.values().length) != i) {
                tellraw.then(", ");
                tellraw.color(ChatColor.YELLOW);
            }
            i++;
        }
        tellraw.send(p);
    }
}
