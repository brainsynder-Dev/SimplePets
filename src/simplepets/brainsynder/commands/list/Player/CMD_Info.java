package simplepets.brainsynder.commands.list.Player;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import simple.brainsynder.nbt.StorageTagCompound;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.commands.PetCommand;
import simplepets.brainsynder.commands.annotations.CommandDescription;
import simplepets.brainsynder.commands.annotations.CommandName;
import simplepets.brainsynder.commands.annotations.CommandPermission;
import simplepets.brainsynder.commands.annotations.CommandUsage;
import simplepets.brainsynder.nms.entities.type.main.IEntityPet;
import simplepets.brainsynder.player.PetOwner;

@CommandName(name = "info")
@CommandPermission(permission = "info")
@CommandUsage(usage = "<player>")
@CommandDescription(description = "Collects Info the Selected Players' Pet.")
public class CMD_Info extends PetCommand<Player> {
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

            p.sendMessage("§eSimplePets §6>> §7" + target.getName() + "'s Pet Data:");
            IEntityPet entity = owner.getPet().getVisableEntity();
            StorageTagCompound compound = entity.asCompound();
            compound.getKeySet().forEach(key -> p.sendMessage("§7- §e" + key + "§6: §y" + compound.getTag(key).toString()));
        }
    }
}
