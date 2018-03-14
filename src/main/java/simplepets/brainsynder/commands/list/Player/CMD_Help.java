package simplepets.brainsynder.commands.list.Player;

import org.bukkit.entity.Player;
import simplepets.brainsynder.commands.CMD_Pet;
import simplepets.brainsynder.commands.PetCommand;
import simplepets.brainsynder.commands.annotations.*;

@CommandName(name = "help")
@CommandUsage(usage = "<pet> [player]")
@CommandPermission(permission = "help")
@CommandDescription(description = "Show help on plugin commands.")
public class CMD_Help extends PetCommand<Player> {
    @Override
    public void onCommand(Player p, String[] args) {
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
            if (!gcmd.getClass().isAnnotationPresent(Console.class)) {
                if (gcmd.getClass().isAnnotationPresent(CommandPermission.class)) {
                    if (!p.hasPermission("Pet." + gcmd.getClass().getAnnotation(CommandPermission.class).permission()))
                        continue;
                }
                p.sendMessage("§eSimplePets §6>> §7" + ("/pet " + name + usage + description));
            }
        }
    }
}
