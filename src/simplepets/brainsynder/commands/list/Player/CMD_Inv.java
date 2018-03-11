package simplepets.brainsynder.commands.list.Player;

import org.bukkit.entity.Player;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.commands.PetCommand;
import simplepets.brainsynder.commands.annotations.CommandDescription;
import simplepets.brainsynder.commands.annotations.CommandName;
import simplepets.brainsynder.commands.annotations.CommandUsage;
import simplepets.brainsynder.menu.ItemStorageMenu;
import simplepets.brainsynder.player.PetOwner;
import simplepets.brainsynder.storage.files.Commands;

@CommandName(name = "inv")
@CommandUsage(usage = "[player]")
@CommandDescription(description = "Opens the Pet Item Inventory Storage.")
public class CMD_Inv extends PetCommand<Player> {
    @Override
    public void onCommand(Player p, String[] args) {
        if (PetCore.get().getConfiguration().getBoolean("PetItemStorage.Enable")) {
            Commands commands = PetCore.get().getCommands();
            if (args.length == 0) {
                if (!ItemStorageMenu.loadFromPlayer(p)) {
                    p.sendMessage(commands.getString("Inv.No-Pet-Items")
                            .replace("{prefix}", commands.getString("Prefix"))
                            .replace('&', '§'));
                }
            } else {
                if (!p.hasPermission("Pet.commands.inv.other")) {
                    if (!ItemStorageMenu.loadFromPlayer(p)) {
                        p.sendMessage(commands.getString("Inv.No-Pet-Items")
                                .replace("{prefix}", commands.getString("Prefix"))
                                .replace('&', '§'));
                    }
                    return;
                }

                if (!ItemStorageMenu.loadFromName(p, args[0])) {
                    p.sendMessage(commands.getString("Inv.No-Pet-Items-Other")
                            .replace("%player%", args[0])
                            .replace("{prefix}", commands.getString("Prefix"))
                            .replace('&', '§'));
                }
            }
        } else {
            PetCore.get().getInvLoaders().SELECTION.open(PetOwner.getPetOwner(p));
        }
    }
}
