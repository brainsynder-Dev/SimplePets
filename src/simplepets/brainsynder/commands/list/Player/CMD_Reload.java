package simplepets.brainsynder.commands.list.Player;

import org.bukkit.entity.Player;
import simple.brainsynder.storage.IStorage;
import simple.brainsynder.storage.StorageList;
import simple.brainsynder.utils.ObjectPager;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.commands.PetCommand;
import simplepets.brainsynder.commands.annotations.CommandDescription;
import simplepets.brainsynder.commands.annotations.CommandName;
import simplepets.brainsynder.commands.annotations.CommandPermission;
import simplepets.brainsynder.pet.PetType;
import simplepets.brainsynder.utils.LoaderRetriever;

import java.util.ArrayList;
import java.util.List;

@CommandName(name = "reload")
@CommandPermission(permission = "reload")
@CommandDescription(description = "Reloads the Settings that cant AutoReload.")
public class CMD_Reload extends PetCommand<Player> {
    @Override
    public void onCommand(Player p, String[] args) {
        IStorage<Integer> slots = new StorageList<>();
        List<String> _allowed_ = PetCore.get().getConfiguration().getStringList("AvailableSlots");
        int size = 1;
        for (String s : _allowed_) {
            try {
                int slot = Integer.parseInt(s);
                if (slot <= 0) {
                    PetCore.get().debug("SimplePets Error: Invalid Slot number '" + slot + "' Value must be from 1-54");
                    continue;
                }
                if (slot >= 55) {
                    PetCore.get().debug("SimplePets Error: Invalid Slot number '" + slot + "' Value must be from 1-54");
                    continue;
                }
                slots.add((slot - 1));
            } catch (NumberFormatException e) {
                PetCore.get().debug("SimplePets Error: Invalid Slot number '" + s + "' Value must be from 1-54");
            }
            size++;
        }
        PetCore.get().setAvailableSlots(slots);
        List<PetType> types = new ArrayList<>();
        for (PetType type : PetType.values()) {
            if (type.isSupported()) {
                if (type.isEnabled()) {
                    types.add(type);
                }
            }
        }
        PetCore.get().reload();
        PetCore.get().petTypes = new ObjectPager<>(size, types);
        LoaderRetriever.reloadLoaders();
        p.sendMessage(PetCore.get().getMessages().getString("Reload-Complete", true));
    }
}
