

package simplepets.brainsynder.commands.list;

import lib.brainsynder.commands.annotations.ICommand;
import lib.brainsynder.nms.Tellraw;
import lib.brainsynder.utils.Colorize;
import lib.brainsynder.web.WebConnector;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.plugin.SimplePets;
import simplepets.brainsynder.commands.Permission;
import simplepets.brainsynder.commands.PetSubCommand;
import simplepets.brainsynder.files.MessageFile;
import simplepets.brainsynder.files.options.MessageOption;
import simplepets.brainsynder.sql.InventorySQL;
import simplepets.brainsynder.sql.PlayerSQL;

import java.util.List;

@ICommand(
        name = "database",
        description = "Shows information about the database"
)
@Permission(permission = "database", adminCommand = true, additionalPermissions = {"removenpcs", "removeduplicates", "findduplicates"})
public class DatabaseCommand extends PetSubCommand {
    public DatabaseCommand(PetCore plugin) {
        super(plugin);
    }


    @Override
    public List<String> handleCompletions(List<String> completions, CommandSender sender, int index, String[] args) {
        if (!canExecute(sender)) return super.handleCompletions(completions, sender, index, args);
        if ((index == 1)) {
            if (sender.hasPermission(getPermission("removenpcs"))) completions.add("removenpcs");
            if (sender.hasPermission(getPermission("removeduplicates"))) completions.add("removeduplicates");
            if (sender.hasPermission(getPermission("findduplicates"))) completions.add("findduplicates");
        }
        return super.handleCompletions(completions, sender, index, args);
    }

    @Override
    public void run(CommandSender sender, String[] args) {
        if (args.length == 0) {
            sendDatabaseInfo(sender);
            return;
        }

        // Clears the database of any NPC/offline UUIDs
        if (args[0].equalsIgnoreCase("removenpcs")) {
            PlayerSQL.getInstance().removeNPCs(sender);
            return;
        }

        // Clears the database of all duplicate players
        if (args[0].equalsIgnoreCase("removeduplicates")) {
            PlayerSQL.getInstance().removeDuplicates(sender);
            return;
        }

        // Fetches a list of all duplicate players in the database
        if (args[0].equalsIgnoreCase("findduplicates")) {
            PlayerSQL.getInstance().findDuplicates(triples -> {
                if (triples.isEmpty()) {
                    sender.sendMessage(MessageFile.getTranslation(MessageOption.PREFIX)+ChatColor.GRAY+" No duplicate players were found in the database.");
                    return;
                }

                StringBuilder builder = new StringBuilder();

                triples.forEach(triple -> {
                    builder.append("[Count: ").append(triple.right).append("]   '").append(triple.middle).append("'    (").append(triple.left.toString()).append(")").append("\n");
                });

                WebConnector.uploadPaste(SimplePets.getPlugin(), builder.toString(), s -> {
                    sender.sendMessage(MessageFile.getTranslation(MessageOption.PREFIX)+ChatColor.GRAY+" Here is a list of duplicated players: ");
                    sender.sendMessage(MessageFile.getTranslation(MessageOption.PREFIX)+ChatColor.GRAY+" "+s);
                });
            });
            return;
        }

        sendDatabaseInfo(sender);
    }

    private void sendDatabaseInfo (CommandSender sender) {
        PlayerSQL.getInstance().fetchRowCount(playerDataCount -> {
            if (InventorySQL.getInstance() == null) {
                sender.sendMessage(MessageFile.getTranslation(MessageOption.PREFIX)+Colorize.translateBungeeHex(" &#d1c9c9Player Data SQL &#b35349======&#de9790-------"));
                sender.sendMessage(Colorize.translateBungeeHex(" &#e1eb5b- &#d1c9c9Type: &#e3c79a" + (PlayerSQL.getInstance().isUsingSqlite() ? "SQLite" : "MySQL")));
                Tellraw raw = Tellraw.fromLegacy("&#e1eb5b - &#d1c9c9Status: ");
                if (PlayerSQL.getInstance().isUsingSqlite()) {
                    raw.then("CONNECTED").color(ChatColor.GREEN).tooltip("&7SQLite connections are kept connected");
                }else{
                    raw.then("IDLE").color("#e3aa4f").tooltip("&7MySQL connections are kept closed until they are needed","&7That's what the IDLE state is");
                }
                raw.send(sender);
                sender.sendMessage(Colorize.translateBungeeHex(" &#e1eb5b- &#d1c9c9Players In Database: &#e3c79a"+playerDataCount));
                return;
            }

            InventorySQL.getInstance().fetchRowCount(invCount -> {
                try {
                    sender.sendMessage(MessageFile.getTranslation(MessageOption.PREFIX)+Colorize.translateBungeeHex(" &#d1c9c9Player Data SQL &#b35349======&#de9790-------"));
                    sender.sendMessage(Colorize.translateBungeeHex(" &#e1eb5b- &#d1c9c9Type: &#e3c79a" + (PlayerSQL.getInstance().isUsingSqlite() ? "SQLite" : "MySQL")));
                    Tellraw raw = Tellraw.fromLegacy(" &#e1eb5b- &#d1c9c9Status: ");
                    if (PlayerSQL.getInstance().isUsingSqlite()) {
                        raw.then("CONNECTED").color(ChatColor.GREEN).tooltip("&7SQLite connections are kept connected");
                    }else{
                        raw.then("IDLE").color("#e3aa4f").tooltip("&7MySQL connections are kept closed until they are needed","&7That's what the IDLE state is");
                    }
                    raw.send(sender);
                    sender.sendMessage(Colorize.translateBungeeHex(" &#e1eb5b- &#d1c9c9Players In Database: &#e3c79a"+playerDataCount));
                    sender.sendMessage(MessageFile.getTranslation(MessageOption.PREFIX)+Colorize.translateBungeeHex(" &#d1c9c9Inventory Data SQL &#b35349====&#de9790------"));
                    sender.sendMessage(Colorize.translateBungeeHex(" &#e1eb5b- &#d1c9c9Type: &#e3c79aSQLite"));
                    Tellraw.fromLegacy(" &#e1eb5b- &#d1c9c9Status: ").then("CONNECTED").color(ChatColor.GREEN).tooltip("&7SQLite connections are kept connected").send(sender);
                    sender.sendMessage(Colorize.translateBungeeHex(" &#e1eb5b- &#d1c9c9Inventories In Database: &#e3c79a"+invCount));
                }catch (Exception e) {
                    sender.sendMessage(MessageFile.getTranslation(MessageOption.PREFIX)+ChatColor.RED + "Failed to fetch database information");
                    sender.sendMessage(ChatColor.RED + e.getMessage());
                }
            });
        });
    }
}

