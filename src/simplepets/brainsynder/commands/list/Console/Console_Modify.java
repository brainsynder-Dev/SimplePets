package simplepets.brainsynder.commands.list.Console;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import simple.brainsynder.nbt.JsonToNBT;
import simple.brainsynder.nbt.NBTException;
import simple.brainsynder.nbt.StorageTagCompound;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.commands.PetCommand;
import simplepets.brainsynder.commands.annotations.CommandDescription;
import simplepets.brainsynder.commands.annotations.CommandName;
import simplepets.brainsynder.commands.annotations.CommandUsage;
import simplepets.brainsynder.commands.annotations.Console;
import simplepets.brainsynder.player.PetOwner;

@Console
@CommandName(name = "modify")
@CommandUsage(usage = "<player> <json>")
@CommandDescription(description = "Modify the Selected Players' Pet.")
public class Console_Modify extends PetCommand {
    @Override
    public void onCommand(CommandSender sender, String[] args) {
        if (args.length == 0) {
            sendUsage(sender);
        } else {
            Player target = Bukkit.getPlayerExact(args[0]);
            if (target == null) {
                sender.sendMessage(PetCore.get().getMessages().getString("Player-Not-Found", true).replace("%player%", args[0]));
                return;
            }
            PetOwner owner = PetOwner.getPetOwner(target);

            if (!owner.hasPet()) {
                sender.sendMessage("&eSimplePets &6>> &7%player% does not have a pet.".replace('&', '§').replace("%player%", args[0]));
                return;
            }

            if (args.length == 1) {
                sendUsage(sender);
            } else {
                StorageTagCompound compound;
                String json = buildMultiArg(args, 1).replace(" ", "~");
                try {
                    compound = JsonToNBT.getTagFromJson(json);
                } catch (NBTException e) {
                    sender.sendMessage("SimplePets >> Invalid JSON text has been entered");
                    e.printStackTrace();
                    return;
                }

                owner.getPet().getVisableEntity().applyCompound(compound);
            }
        }
    }
}
