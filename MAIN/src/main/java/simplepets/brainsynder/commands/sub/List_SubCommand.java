package simplepets.brainsynder.commands.sub;

import lib.brainsynder.commands.annotations.ICommand;
import lib.brainsynder.nms.Tellraw;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.commands.PetSubCommand;
import simplepets.brainsynder.commands.annotations.Permission;
import simplepets.brainsynder.pet.PetDefault;
import simplepets.brainsynder.pet.TypeManager;
import simplepets.brainsynder.storage.files.Commands;

import java.util.ArrayList;
import java.util.List;

@ICommand(
        name = "list",
        usage = "&r &r &6[] &7/pet list",
        description = "Lists all the pets that your version can use"
)
@Permission(permission = "list")
public class List_SubCommand extends PetSubCommand {
    @Override
    public void run(CommandSender sender) {
        TypeManager manager = PetCore.get().getTypeManager();
        Commands commands = PetCore.get().getCommands();

        String list = commands.getString("List.List-Display").replace("%size%", String.valueOf(manager.getTypes().size()));
        sender.sendMessage(list.substring(0, list.lastIndexOf("%list%")));

        if (list.contains("%list%")) fetchList(sender, commands, manager);

        // Checks if anything is after the '%list%' placeholder
        String after = list.substring(list.indexOf("%list%") + 6);
        if (!after.trim().isEmpty()) sender.sendMessage(after);
    }

    private void fetchList (CommandSender sender, Commands commands, TypeManager manager) {
        int i = 1;
        StringBuilder builder = new StringBuilder();
        Tellraw tellraw = Tellraw.getInstance("");
        for (PetDefault type : manager.getTypes()) {
            tellraw.then(type.getConfigName());
            if (type.isSupported()) {
                builder.append(type.getConfigName());
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
                builder.append(", ");
                tellraw.color(ChatColor.YELLOW);
            }
            i++;
        }

        if (sender instanceof Player){
            tellraw.send((Player) sender);
        }else{
            sender.sendMessage(builder.toString());
        }
    }
}
