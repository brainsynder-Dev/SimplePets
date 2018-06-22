package simplepets.brainsynder.commands.list.Player;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.event.inventory.PetCommandSummonEvent;
import simplepets.brainsynder.api.event.inventory.PetSelectTypeEvent;
import simplepets.brainsynder.commands.PetCommand;
import simplepets.brainsynder.commands.annotations.CommandDescription;
import simplepets.brainsynder.commands.annotations.CommandName;
import simplepets.brainsynder.commands.annotations.CommandUsage;
import simplepets.brainsynder.pet.PetDefault;
import simplepets.brainsynder.pet.TypeManager;

@CommandName(name = "summon")
@CommandUsage(usage = "<pet> [player]")
@CommandDescription(description = "Spawns a pet for the player or selected player.")
public class CMD_Summon extends PetCommand<Player> {
    @Override
    public void onCommand(Player p, String[] args) {
        if (args.length == 0) {
            sendUsage(p);
        } else {
            TypeManager manager = PetCore.get().getTypeManager();
            PetDefault type = manager.getType(args[0]);
            if (type == null) {
                p.sendMessage(PetCore.get().getMessages().getString("Invalid-PetType", true));
                return;
            }
            if (!type.isSupported()) {
                p.sendMessage(PetCore.get().getMessages().getString("Type-Not-Supported", true));
                return;
            }
            if (!type.isEnabled()) {
                p.sendMessage(PetCore.get().getMessages().getString("Type-Not-Enabled", true));
                return;
            }
            if (args.length == 1) {
                if (!type.hasPermission(p)) {
                    p.sendMessage(PetCore.get().getMessages().getString("No-Permission", true));
                    return;
                }
                PetCommandSummonEvent event = new PetCommandSummonEvent(type, p);
                Bukkit.getPluginManager().callEvent(event);
                if (event.isCancelled()) return;
                p.sendMessage(PetCore.get().getMessages().getString("Select-Pet", true).replace("%pet%", type.getDisplayName()));
                type.setPet(p);
            } else {
                if (args[1].equalsIgnoreCase(p.getName())) {
                    if (!type.hasPermission(p)) {
                        p.sendMessage(PetCore.get().getMessages().getString("No-Permission", true));
                        return;
                    }
                    p.sendMessage(PetCore.get().getMessages().getString("Select-Pet", true).replace("%pet%", type.getDisplayName()));
                    type.setPet(p);
                    return;
                }
                if (!p.hasPermission("Pet.commands.summon.other")) {
                    p.sendMessage(PetCore.get().getMessages().getString("No-Permission", true));
                    return;
                }
                Player tp = Bukkit.getPlayer(args[1]);
                if (tp == null) {
                    p.sendMessage(PetCore.get().getMessages().getString("Player-Not-Found", true).replace("%player%", args[1]));
                    return;
                }
                if (!type.hasPermission(tp)) {
                    p.sendMessage(PetCore.get().getMessages().getString("Other-No-Pet-Permission", true)
                    .replace("%player%", tp.getName()));
                    return;
                }
                PetCommandSummonEvent event = new PetCommandSummonEvent(type, p); // Change to tp if needed, reason why is in case of the sender wanting to buy the pet for the player, or in case of abuse.
                Bukkit.getPluginManager().callEvent(event);
                if (event.isCancelled()) return;
                p.sendMessage(PetCore.get().getMessages().getString("Select-Pet-Sender", true)
                        .replace("%pet%", type.getDisplayName())
                        .replace("%player%", tp.getName()));
                tp.sendMessage(PetCore.get().getMessages().getString("Select-Pet-Other", true)
                        .replace("%pet%", type.getDisplayName())
                        .replace("%player%", p.getName()));
                type.setPet(tp);
            }
        }
    }
}
