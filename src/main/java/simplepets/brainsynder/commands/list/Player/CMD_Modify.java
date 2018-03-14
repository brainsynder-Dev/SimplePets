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

            if (!owner.hasPet()) {
                p.sendMessage("&eSimplePets &6>> &7%player% does not have a pet.".replace('&', '§').replace("%player%", args[0]));
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
                    p.sendMessage("§eSimplePets §6>> §cInvalid JSON text has been entered");
                    p.sendMessage("§eSimplePets §6>> §cError: " + e.getMessage());
                    return;
                }

                owner.getPet().getVisableEntity().applyCompound(compound);
                p.sendMessage("§eSimplePets §6>> §7You have changed §c" + target.getName() + "'s §7Pet Data.");
            }
        }
    }
}
