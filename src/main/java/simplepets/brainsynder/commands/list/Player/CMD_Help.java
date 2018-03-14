package simplepets.brainsynder.commands.list.Player;

import org.bukkit.entity.Player;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.commands.PetCommand;
import simplepets.brainsynder.commands.annotations.*;
import simplepets.brainsynder.storage.files.Commands;

@CommandName(name = "help")
@CommandUsage(usage = "<pet> [player]")
@CommandPermission(permission = "help")
@CommandDescription(description = "Show help on plugin commands.")
public class CMD_Help extends PetCommand<Player> {
    @Override
    public void onCommand(Player p, String[] args) {
        for (PetCommand gcmd : PetCore.get().getSPCommand().commands) {
            String name = "", description = "", usage = "";
            if (gcmd.getClass().isAnnotationPresent(CommandName.class)) {
                name = gcmd.getClass().getAnnotation(CommandName.class).name();
            }
            if (gcmd.getClass().isAnnotationPresent(CommandUsage.class)) {
                usage = gcmd.getClass().getAnnotation(CommandUsage.class).usage();
            }
            if (gcmd.getClass().isAnnotationPresent(CommandDescription.class)) {
                description = gcmd.getClass().getAnnotation(CommandDescription.class).description();
            }
            if (!gcmd.getClass().isAnnotationPresent(Console.class)) {
                if (gcmd.getClass().isAnnotationPresent(CommandPermission.class)) {
                    if (!p.hasPermission("Pet.commands." + gcmd.getClass().getAnnotation(CommandPermission.class).permission()))
                        continue;
                }
                Commands commands = PetCore.get().getCommands();
                p.sendMessage(commands.getString("Help.Command-Display-Player")
                .replace("%name%", name)
                .replace("%usage%", usage)
                .replace("%description%", description));
            }
        }
    }
}
