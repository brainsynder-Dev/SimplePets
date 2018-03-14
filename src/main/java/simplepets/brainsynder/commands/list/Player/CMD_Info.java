package simplepets.brainsynder.commands.list.Player;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import simple.brainsynder.nbt.*;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.commands.PetCommand;
import simplepets.brainsynder.commands.annotations.CommandDescription;
import simplepets.brainsynder.commands.annotations.CommandName;
import simplepets.brainsynder.commands.annotations.CommandPermission;
import simplepets.brainsynder.commands.annotations.CommandUsage;
import simplepets.brainsynder.nms.entities.type.main.IEntityPet;
import simplepets.brainsynder.player.PetOwner;

import java.util.Arrays;

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
            compound.getKeySet().forEach(key -> p.sendMessage("§7- §e" + key + "§6: §y" + fetchValue(compound.getTag(key))));
        }
    }

    private String fetchValue(StorageBase base) {
        if (base instanceof StorageTagByte) {
            byte tagByte = ((StorageTagByte) base).getByte();
            if ((tagByte == 0) || (tagByte == 1))
                return String.valueOf(tagByte == 1);
            return String.valueOf(tagByte);
        }
        if (base instanceof StorageTagByteArray)
            return Arrays.toString(((StorageTagByteArray) base).getByteArray());
        if (base instanceof StorageTagDouble)
            return String.valueOf(((StorageTagDouble) base).getDouble());
        if (base instanceof StorageTagFloat)
            return String.valueOf(((StorageTagFloat) base).getFloat());
        if (base instanceof StorageTagInt)
            return String.valueOf(((StorageTagInt) base).getInt());
        if (base instanceof StorageTagIntArray)
            return Arrays.toString(((StorageTagIntArray) base).getIntArray());
        if (base instanceof StorageTagLong)
            return String.valueOf(((StorageTagLong) base).getLong());
        if (base instanceof StorageTagShort)
            return String.valueOf(((StorageTagShort) base).getShort());
        if (base instanceof StorageTagString)
            return String.valueOf(((StorageTagString) base).getString());
        if (base instanceof StorageTagList) {
            StorageTagList list = (StorageTagList) base;
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < list.tagCount(); i++) {
                builder.append(fetchValue(list.get(i)));
            }
            return builder.toString();
        }

        return "";
    }
}
