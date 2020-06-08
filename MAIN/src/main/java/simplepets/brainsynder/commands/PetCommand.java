package simplepets.brainsynder.commands;

import lib.brainsynder.commands.ParentCommand;
import lib.brainsynder.commands.annotations.ICommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.event.inventory.PetCommandSummonEvent;
import simplepets.brainsynder.commands.annotations.Permission;
import simplepets.brainsynder.commands.sub.*;
import simplepets.brainsynder.pet.PetType;
import simplepets.brainsynder.pet.TypeManager;
import simplepets.brainsynder.player.PetOwner;

@ICommand(
        name = "pet",
        alias = {"pets", "simplepets", "simplepet"},
        description = "Main SimplePets Command/Opens The Pet Selection GUI"
)
public class PetCommand extends ParentCommand<PetSubCommand> {
    public PetCommand() {
        registerSub(new Debug_SubCommand());
        registerSub(new Generator_SubCommand(this));
        registerSub(new Hat_SubCommand());
        registerSub(new Help_SubCommand(this));
        registerSub(new Info_SubCommand());
        if (PetCore.get().getConfiguration().getBoolean("PetItemStorage.Enable"))
            registerSub(new Inventory_SubCommand());
        registerSub(new List_SubCommand());
        registerSub(new Menu_SubCommand());
        registerSub(new Modify_SubCommand());
        registerSub(new Name_SubCommand());
        registerSub(new Reload_SubCommand());
        registerSub(new Remove_SubCommand());
        registerSub(new Ride_SubCommand());
        registerSub(new Saves_SubCommand());
        registerSub(new Summon_SubCommand());

    }

    @Override
    public void run(CommandSender sender, String[] args) {
        if (args.length == 0) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                if (args.length == 0) {
                    PetCore.get().getInvLoaders().SELECTION.open(PetOwner.getPetOwner(p));
                } else {
                    TypeManager manager = PetCore.get().getTypeManager();
                    PetType type = manager.getType(args[0]);

                    if (type == null) {
                        sender.sendMessage(PetCore.get().getMessages().getString("Invalid-PetType", true));
                        return;
                    }

                    if (!type.isSupported()) {
                        sender.sendMessage(PetCore.get().getMessages().getString("Type-Not-Supported", true));
                        return;
                    }

                    if (!type.isEnabled()) {
                        sender.sendMessage(PetCore.get().getMessages().getString("Type-Not-Enabled", true));
                        return;
                    }

                    if (!type.hasPermission(p)) {
                        p.sendMessage(PetCore.get().getMessages().getString("No-Permission", true));
                        return;
                    }

                    PetCommandSummonEvent event = new PetCommandSummonEvent(type, p);
                    Bukkit.getPluginManager().callEvent(event);
                    if (event.isCancelled()) return;
                    p.sendMessage(PetCore.get().getMessages().getString("Select-Pet", true).replace("%pet%", type.getDisplayName()));
                    type.setPet(p);
                }
            } else {
                sendHelp(sender, false);
            }
        }
    }

    public boolean needsPermission() {
        return getClass().isAnnotationPresent(Permission.class);
    }

    public boolean hasPermission(CommandSender sender) {
        if (getClass().isAnnotationPresent(Permission.class)) {
            return sender.hasPermission(getPermission());
        }
        return true;
    }

    public String getPermission() {
        if (getClass().isAnnotationPresent(Permission.class)) {
            return "Pet.commands." + getClass().getAnnotation(Permission.class).permission();
        }
        return null;
    }
}
