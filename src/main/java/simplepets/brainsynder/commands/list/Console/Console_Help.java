package simplepets.brainsynder.commands.list.Console;

import org.bukkit.command.CommandSender;
import simplepets.brainsynder.commands.CMD_Pet;
import simplepets.brainsynder.commands.PetCommand;
import simplepets.brainsynder.commands.annotations.CommandDescription;
import simplepets.brainsynder.commands.annotations.CommandName;
import simplepets.brainsynder.commands.annotations.CommandUsage;
import simplepets.brainsynder.commands.annotations.Console;

@Console
@CommandName(name = "help")
@CommandDescription(description = "Show help on plugin commands.")
public class Console_Help extends PetCommand {
    @Override
    public void onCommand(CommandSender sender, String[] args) {
        for (PetCommand gcmd : CMD_Pet.commands) {
            String name = "", description = "", usage = "";
            if (gcmd.getClass().isAnnotationPresent(CommandName.class)) {
                name = gcmd.getClass().getAnnotation(CommandName.class).name();
            }
            if (gcmd.getClass().isAnnotationPresent(CommandUsage.class)) {
                usage = ' ' + gcmd.getClass().getAnnotation(CommandUsage.class).usage();
            }
            if (gcmd.getClass().isAnnotationPresent(CommandDescription.class)) {
                description = " - " + gcmd.getClass().getAnnotation(CommandDescription.class).description();
            }
            if (gcmd.getClass().isAnnotationPresent(Console.class)) {
                sender.sendMessage("- pet " + name + usage + description);
            }
        }
    }
}
