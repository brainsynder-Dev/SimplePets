package simplepets.brainsynder.commands.list;

import lib.brainsynder.commands.annotations.ICommand;
import lib.brainsynder.nms.Tellraw;
import lib.brainsynder.utils.Colorize;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.commands.Permission;
import simplepets.brainsynder.commands.PetSubCommand;
import simplepets.brainsynder.files.MessageFile;
import simplepets.brainsynder.files.options.MessageOption;
import simplepets.brainsynder.sql.InventorySQL;
import simplepets.brainsynder.sql.PlayerSQL;

@ICommand(
        name = "database",
        description = "Shows information about the database"
)
@Permission(permission = "database", adminCommand = true)
public class DatabaseCommand extends PetSubCommand {
    public DatabaseCommand(PetCore plugin) {
        super(plugin);
    }

    @Override
    public void run(CommandSender sender) {
        PlayerSQL.getInstance().fetchRowCount(playerDataCount -> {
            InventorySQL.getInstance().fetchRowCount(invCount -> {
                try {
                    sendMessage(sender, MessageFile.getTranslation(MessageOption.PREFIX)+Colorize.translateBungeeHex(" &#d1c9c9Player Data SQL &#b35349======&#de9790-------"));
                    sendMessage(sender, Colorize.translateBungeeHex(" &#e1eb5b- &#d1c9c9Type: &#e3c79a" + (PlayerSQL.getInstance().isUsingSqlite() ? "SQLite" : "MySQL")));
                    Tellraw raw = Tellraw.fromLegacy(" &#e1eb5b- &#d1c9c9Status: ");
                    if (PlayerSQL.getInstance().isUsingSqlite()) {
                        raw.then("CONNECTED").color(ChatColor.GREEN).tooltip("&7SQLite connections are kept connected");
                    }else{
                        raw.then("IDLE").color("#e3aa4f").tooltip("&7MySQL connections are kept closed until they are needed","&7That's what the IDLE state is");
                    }
                    raw.send(sender);
                    sendMessage(sender, Colorize.translateBungeeHex(" &#e1eb5b- &#d1c9c9Players In Database: &#e3c79a"+playerDataCount));
                    sendMessage(sender, MessageFile.getTranslation(MessageOption.PREFIX)+Colorize.translateBungeeHex(" &#d1c9c9Inventory Data SQL &#b35349====&#de9790------"));
                    sendMessage(sender, Colorize.translateBungeeHex(" &#e1eb5b- &#d1c9c9Type: &#e3c79aSQLite"));
                    Tellraw.fromLegacy(" &#e1eb5b- &#d1c9c9Status: ").then("CONNECTED").color(ChatColor.GREEN).tooltip("&7SQLite connections are kept connected").send(sender);
                    sendMessage(sender, Colorize.translateBungeeHex(" &#e1eb5b- &#d1c9c9Inventories In Database: &#e3c79a"+invCount));
                }catch (Exception e) {
                    sendMessage(sender, MessageFile.getTranslation(MessageOption.PREFIX)+ChatColor.RED + "Failed to fetch database information");
                    sendMessage(sender, ChatColor.RED + e.getMessage());
                }
            });
        });
    }
}
