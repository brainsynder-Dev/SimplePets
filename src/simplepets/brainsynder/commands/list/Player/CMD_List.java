package simplepets.brainsynder.commands.list.Player;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import simple.brainsynder.nms.ITellraw;
import simple.brainsynder.utils.Reflection;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.commands.PetCommand;
import simplepets.brainsynder.commands.annotations.CommandDescription;
import simplepets.brainsynder.commands.annotations.CommandName;
import simplepets.brainsynder.commands.annotations.CommandPermission;
import simplepets.brainsynder.pet.PetDefault;
import simplepets.brainsynder.pet.TypeManager;

@CommandName(name = "list")
@CommandPermission(permission = "list")
@CommandDescription(description = "Shows a list of pets that are in the plugin.")
public class CMD_List extends PetCommand<Player> {
    @Override
    public void onCommand(Player p, String[] args) {
        TypeManager manager = PetCore.get().getTypeManager();
        ITellraw tellraw = Reflection.getTellraw("§ePet list §6(§7" + manager.getTypes().size() + "§6)§e: ");
        int i = 1;
        for (PetDefault type : manager.getTypes()) {
            tellraw.then(type.getConfigName());
            if (type.isSupported()) {
                tellraw.color(ChatColor.GRAY);
            } else {
                tellraw.color(ChatColor.RED);
                tellraw.tooltip("§cPet is not supported", "§cin your current server version");
            }
            if ((manager.getTypes().size()) != i) {
                tellraw.then(", ");
                tellraw.color(ChatColor.YELLOW);
            }
            i++;
        }
        tellraw.send(p);
    }
}
