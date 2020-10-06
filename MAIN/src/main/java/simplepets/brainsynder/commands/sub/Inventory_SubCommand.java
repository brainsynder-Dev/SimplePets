package simplepets.brainsynder.commands.sub;

import lib.brainsynder.commands.annotations.ICommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.commands.PetSubCommand;
import simplepets.brainsynder.menu.ItemStorageMenu;
import simplepets.brainsynder.player.PetOwner;
import simplepets.brainsynder.storage.files.Commands;
import simplepets.brainsynder.utils.AdditionalData;

@AdditionalData(otherPermissions = "Pet.commands.inv.other")
@ICommand(
        name = "inv",
        alias = {"inventory"},
        usage = "&r &r &6[] &7/pet inv [player]",
        description = "Opens the Item Storage GUI"
)
public class Inventory_SubCommand extends PetSubCommand {
    public Inventory_SubCommand() {
        registerCompletion(1, (commandSender, list, s) -> {
            list.addAll(getOnlinePlayers());
            return commandSender.hasPermission("Pet.commands.inv.other");
        });
    }

    @Override
    public void run(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
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
                            p.sendMessage(commands.getString("Inv.No-Pet-Items"));
                        }
                        return;
                    }

                    ItemStorageMenu.loadFromName(p, args[0]);
                }
            } else {
                PetCore.get().getInvLoaders().SELECTION.open(PetOwner.getPetOwner(p));
            }
        }
    }
}
