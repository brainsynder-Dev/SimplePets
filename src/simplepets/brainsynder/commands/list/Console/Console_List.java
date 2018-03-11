package simplepets.brainsynder.commands.list.Console;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.commands.PetCommand;
import simplepets.brainsynder.commands.annotations.CommandDescription;
import simplepets.brainsynder.commands.annotations.CommandName;
import simplepets.brainsynder.commands.annotations.Console;
import simplepets.brainsynder.pet.PetDefault;
import simplepets.brainsynder.pet.TypeManager;
import simplepets.brainsynder.storage.files.Commands;

@Console
@CommandName(name = "list")
@CommandDescription(description = "Shows a list of pets that are in the plugin.")
public class Console_List extends PetCommand {
    @Override
    public void onCommand(CommandSender sender, String[] args) {
        TypeManager manager = PetCore.get().getTypeManager();
        Commands commands = PetCore.get().getCommands();
        String list = commands.getString("List.List-Display")
                .replace("{prefix}", commands.getString("Prefix"))
                .replace("%size%", String.valueOf(manager.getTypes().size()))
                .replace('&', '§');
        int i = 1;
        StringBuilder sb2 = new StringBuilder();
        for (PetDefault type : manager.getTypes()) {
            sb2.append(type.getConfigName());
            if (type.isSupported()) {
                sb2.append(ChatColor.GRAY);
            } else {
                sb2.append(ChatColor.RED);
            }
            if ((manager.getTypes().size()) != i) {
                sb2.append(", ");
                sb2.append(ChatColor.YELLOW);
            }
            i++;
        }
        String list2 = sb2.toString();
        sender.sendMessage(list.replace("%list%", list2));
    }
}
