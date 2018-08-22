package simplepets.brainsynder.commands.sub;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import simple.brainsynder.commands.annotations.ICommand;
import simple.brainsynder.nbt.JsonToNBT;
import simple.brainsynder.nbt.NBTException;
import simple.brainsynder.nbt.StorageTagCompound;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.commands.PetSubCommand;
import simplepets.brainsynder.commands.annotations.Permission;
import simplepets.brainsynder.player.PetOwner;

import java.util.Arrays;

@ICommand(
        name = "modify",
        usage = "&r &r &6[] &7/pet modify <player> <json>",
        description = "Modify the Selected Players' Pet."
)
@Permission(permission = "modify")
public class Modify_SubCommand extends PetSubCommand {
    public Modify_SubCommand() {
        registerCompletion(1, getOnlinePlayers());
        registerCompletion(2, Arrays.asList("{}"));
    }

    @Override
    public void run(CommandSender sender, String[] args) {
        if (args.length == 0) {
            sendUsage(sender);
            return;
        }

        Player target = Bukkit.getPlayerExact(args[0]);
        if (target == null) {
            sender.sendMessage(PetCore.get().getMessages().getString("Player-Not-Found", true).replace("%player%", args[0]));
            return;
        }
        PetOwner owner = PetOwner.getPetOwner(target);
        if (owner == null) return;
        if (!owner.hasPet()) {
            sender.sendMessage(PetCore.get().getMessages().getString("Player-No-Pet", true).replace("%player%", args[0]));
            return;
        }

        if (args.length == 1) {
            sendUsage(sender);
            return;
        }

        StorageTagCompound compound;
        String json = messageMaker(args, 1).replace(" ", "~");
        try {
            compound = JsonToNBT.getTagFromJson(json);
        } catch (NBTException e) {
            sender.sendMessage(PetCore.get().getCommands().getString("Modify.Invalid-JSON"));
            sender.sendMessage(PetCore.get().getCommands().getString("Modify.Invalid-JSON-Error-Player")
                    .replace("%error%", e.getMessage()));
            return;
        }

        owner.getPet().getVisableEntity().applyCompound(compound);
        sender.sendMessage(PetCore.get().getCommands().getString("Modify.Pet-Modified")
                .replace("%player%", target.getName()));

    }
}
