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
import simplepets.brainsynder.storage.files.Commands;

import java.util.ArrayList;
import java.util.List;

@CommandName(name = "list")
@CommandPermission(permission = "list")
@CommandDescription(description = "Shows a list of pets that are in the plugin.")
public class CMD_List extends PetCommand<Player> {
    @Override
    public void onCommand(Player p, String[] args) {
        TypeManager manager = PetCore.get().getTypeManager();
        Commands commands = PetCore.get().getCommands();
        String list = commands.getString("List.List-Display")
                .replace("%size%", String.valueOf(manager.getTypes().size()));
        p.sendMessage(list.substring(0, list.lastIndexOf("%list%")));
        String list2 = list.substring(list.indexOf("%list%") + 6, list.length());
        int i = 1;
        if (list.contains("%list%")) {
            list.replace("%list%", "");
            ITellraw tellraw = Reflection.getTellraw("");
            for (PetDefault type : manager.getTypes()) {
                tellraw.then(type.getConfigName());
                if (type.isSupported()) {
                    tellraw.color(ChatColor.GRAY);
                } else {
                    List<String> tooltip = commands.getStringList("List.List-Pet-Not-Supported");
                    List<String> newTip = new ArrayList<>();
                    tooltip.forEach(key -> newTip.add(key.replace('&', '§')));
                    tellraw.color(ChatColor.RED);
                    tellraw.tooltip(newTip);
                }
                if ((manager.getTypes().size()) != i) {
                    tellraw.then(", ");
                    tellraw.color(ChatColor.YELLOW);
                }
                i++;
            }
            tellraw.send(p);
        }
        if (!list2.trim().isEmpty()) {
            p.sendMessage(list2);
        }
    }
}
