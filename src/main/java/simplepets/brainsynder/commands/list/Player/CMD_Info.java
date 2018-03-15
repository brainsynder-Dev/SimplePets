package simplepets.brainsynder.commands.list.Player;

import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import simple.brainsynder.nbt.*;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.commands.PetCommand;
import simplepets.brainsynder.commands.annotations.CommandDescription;
import simplepets.brainsynder.commands.annotations.CommandName;
import simplepets.brainsynder.commands.annotations.CommandPermission;
import simplepets.brainsynder.commands.annotations.CommandUsage;
import simplepets.brainsynder.player.PetOwner;
import simplepets.brainsynder.storage.files.Commands;

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
            Commands commands = PetCore.get().getCommands();
            Player target = Bukkit.getPlayerExact(args[0]);
            if (target == null) {
                p.sendMessage(PetCore.get().getMessages().getString("Player-Not-Found", true).replace("%player%", args[0]));
                return;
            }
            PetOwner owner = PetOwner.getPetOwner(target);

            if (!owner.hasPet()) {
                p.sendMessage(PetCore.get().getMessages().getString("Player-No-Pet", true).replace("%player%", args[0]));
                return;
            }

            if (args.length > 1) {
                if (args[1].equalsIgnoreCase("debug")) {
                    // This will stay hard coded just in case they want to use this as a reference
                    p.sendMessage("§eSimplePets §6>> §7" + target.getName() + "'s Pet Data:");
                    IEntityPet entity = owner.getPet().getVisableEntity();
                    StorageTagCompound compound = entity.asCompound();
                    compound.getKeySet().forEach(key -> p.sendMessage("§7- §e" + key + "§6: §e" + fetchValue(compound.getTag(key))));
                    return;
                }
            }
            p.sendMessage(commands.getString("Info.Pet-Data-Header")
            .replace("%player%", args[0]));
            IEntityPet entity = owner.getPet().getVisableEntity();
            StorageTagCompound compound = entity.asCompound();
            compound.getKeySet().forEach(key -> {
                if (!commands.getStringList("Info.Excluded-Data-Values").contains(key)) {
                    p.sendMessage(commands.getString("Info.Pet-Data-Values")
                    .replace("%key%", WordUtils.capitalize(key.toLowerCase()))
                    .replace("%value%", WordUtils.capitalize(fetchValue(compound.getTag(key)).toLowerCase())));
                }
            });
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
