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

@Console
@CommandName(name = "list")
@CommandDescription(description = "Shows a list of pets that are in the plugin.")
public class Console_List extends PetCommand {
    @Override
    public void onCommand(CommandSender sender, String[] args) {
        StringBuilder builder = new StringBuilder();
        TypeManager manager = PetCore.get().getTypeManager();
        builder.append("§ePet list §6(§7").append(manager.getTypes().size()).append("§6)§e: ");
        int i = 1;
        for (PetDefault type : manager.getTypes()) {
            builder.append(type.getConfigName());
            if (type.isSupported()) {
                builder.append(ChatColor.GRAY);
            } else {
                builder.append(ChatColor.RED);
            }
            if ((manager.getTypes().size()) != i) {
                builder.append(", ");
                builder.append(ChatColor.YELLOW);
            }
            i++;
        }
        sender.sendMessage(builder.toString());
    }
}
