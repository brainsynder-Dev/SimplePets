package simplepets.brainsynder.commands.list.Player;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import simple.brainsynder.nbt.JsonToNBT;
import simple.brainsynder.nbt.NBTException;
import simple.brainsynder.nbt.StorageTagCompound;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.commands.PetCommand;
import simplepets.brainsynder.commands.annotations.CommandDescription;
import simplepets.brainsynder.commands.annotations.CommandName;
import simplepets.brainsynder.commands.annotations.CommandPermission;
import simplepets.brainsynder.commands.annotations.CommandUsage;
import simplepets.brainsynder.player.PetOwner;

@CommandName(name = "modify")
@CommandPermission(permission = "modify")
@CommandUsage(usage = "<player> <json>")
@CommandDescription(description = "Modify the Selected Players' Pet.")
public class CMD_Modify extends PetCommand<Player> {
    @Override
    public void onCommand(Player p, String[] args) {
        if (args.length == 0) {
            sendUsage(p);
        } else {
            Player target = Bukkit.getPlayerExact(args[0]);
            if (target == null) {
                p.sendMessage(PetCore.get().getMessages().getString("Player-Not-Found", true).replace("%player%", args[0]));
                return;
            }
            PetOwner owner = PetOwner.getPetOwner(target);
            if (owner == null) return;
            if (!owner.hasPet()) {
                p.sendMessage(PetCore.get().getMessages().getString("Player-No-Pet", true).replace("%player%", args[0]));
                return;
            }

            if (args.length == 1) {
                sendUsage(p);
            } else {
                StorageTagCompound compound;
                String json = buildMultiArg(args, 1).replace(" ", "~");
                try {
                    compound = JsonToNBT.getTagFromJson(json);
                } catch (NBTException e) {
                    p.sendMessage(PetCore.get().getCommands().getString("Modify.Invalid-JSON"));
                    p.sendMessage(PetCore.get().getCommands().getString("Modify.Invalid-JSON-Error-Player")
                            .replace("%error%", e.getMessage()));
                    return;
                }

                owner.getPet().getVisableEntity().applyCompound(compound);
                p.sendMessage(PetCore.get().getCommands().getString("Modify.Pet-Modified")
                        .replace("%player%", target.getName()));
            }
        }
    }
}
