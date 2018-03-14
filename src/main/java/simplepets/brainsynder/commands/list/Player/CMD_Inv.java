package simplepets.brainsynder.commands.list.Player;

import org.bukkit.entity.Player;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.commands.PetCommand;
import simplepets.brainsynder.commands.annotations.CommandDescription;
import simplepets.brainsynder.commands.annotations.CommandName;
import simplepets.brainsynder.commands.annotations.CommandUsage;
import simplepets.brainsynder.events.PetSelectionMenu;
import simplepets.brainsynder.menu.ItemStorageMenu;

@CommandName(name = "inv")
@CommandUsage(usage = "[player]")
@CommandDescription(description = "Opens the Pet Item Inventory Storage.")
public class CMD_Inv extends PetCommand<Player> {
    @Override
    public void onCommand(Player p, String[] args) {
        if (PetCore.get().getConfiguration().getBoolean("PetItemStorage.Enable")) {
            if (args.length == 0) {
                if (!ItemStorageMenu.loadFromPlayer(p)) {
                    p.sendMessage("§eSimplePets §6>> §7No Items Stored in your Pet Inventory");
                }
            } else {
                if (!p.hasPermission("Pet.inv.other")) {
                    if (!ItemStorageMenu.loadFromPlayer(p)) {
                        p.sendMessage("§eSimplePets §6>> §7No Items Stored in your Pet Inventory");
                    }
                    return;
                }

                if (!ItemStorageMenu.loadFromName(p, args[0])) {
                    p.sendMessage("§eSimplePets §6>> §7No Items Stored for " + args[0]);
                }
            }
        } else {
            PetSelectionMenu.openMenu(p, 1);
        }
    }
}
