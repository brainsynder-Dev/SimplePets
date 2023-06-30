

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
import simplepets.brainsynder.sql.SQLData;

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
            PetCore.getInstance().getSqlHandler().removeNPCs().whenComplete((biOptional, throwable) -> {
                int rawCount = biOptional.first().orElse(0);
                int totalCount = biOptional.second().orElse(0);

                if (rawCount == 0) {
                    // No duplicates...
                    sender.sendMessage(MessageFile.getTranslation(MessageOption.PREFIX) + ChatColor.GRAY + " No NPC account entries found that needed to be deleted");
                    return;
                }

                sender.sendMessage(MessageFile.getTranslation(MessageOption.PREFIX) + ChatColor.GRAY + " Number of NPC accounts found: " + rawCount);
                sender.sendMessage(MessageFile.getTranslation(MessageOption.PREFIX) + ChatColor.GRAY + " Number of accounts actually deleted: " + totalCount);
            });
            return;
        }

        // Clears the database of all duplicate players
        if (args[0].equalsIgnoreCase("removeduplicates")) {
            PetCore.getInstance().getSqlHandler().removeDuplicates().whenComplete((biOptional, throwable) -> {
                int rawCount = biOptional.first().orElse(0);
                int totalCount = biOptional.second().orElse(0);

                if (rawCount == 0) {
                    // No duplicates...
                    sender.sendMessage(MessageFile.getTranslation(MessageOption.PREFIX) + ChatColor.GRAY + " No duplicate entries found that needed to be deleted");
                    return;
                }

                sender.sendMessage(MessageFile.getTranslation(MessageOption.PREFIX) + ChatColor.GRAY + " Number of duplicate accounts found: " + rawCount);
                sender.sendMessage(MessageFile.getTranslation(MessageOption.PREFIX) + ChatColor.GRAY + " Number of duplicate entries actually deleted: " + totalCount);
            });
            return;
        }

        // Fetches a list of all duplicate players in the database
        if (args[0].equalsIgnoreCase("findduplicates")) {
            sender.sendMessage(MessageFile.getTranslation(MessageOption.PREFIX) + ChatColor.GRAY + " Finding any duplicates in the database...");
            PetCore.getInstance().getSqlHandler().findDuplicates().whenComplete((triples, throwable) -> {
                if (triples.isEmpty()) {
                    sender.sendMessage(MessageFile.getTranslation(MessageOption.PREFIX) + ChatColor.GRAY + " No duplicate players were found in the database.");
                    return;
                }

                StringBuilder builder = new StringBuilder();

                triples.forEach(triple -> {
                    builder.append("[Count: ").append(triple.right).append("]   '").append(triple.middle).append("'    (").append(triple.left.toString()).append(")").append("\n");
                });

                WebConnector.uploadPaste(SimplePets.getPlugin(), builder.toString(), s -> {
                    sender.sendMessage(MessageFile.getTranslation(MessageOption.PREFIX) + ChatColor.GRAY + " Here is a list of duplicated players: ");
                    sender.sendMessage(MessageFile.getTranslation(MessageOption.PREFIX) + ChatColor.GRAY + " " + s);
                });
            });
            return;
        }

        sendDatabaseInfo(sender);
    }

    private void sendDatabaseInfo(CommandSender sender) {
        PetCore.getInstance().getSqlHandler().getRowCount().whenComplete((playerDataCount, throwable) -> {
            sender.sendMessage(MessageFile.getTranslation(MessageOption.PREFIX) + Colorize.translateBungeeHex(" &#d1c9c9Player Data SQL &#b35349======&#de9790-------"));
            sender.sendMessage(Colorize.translateBungeeHex(" &#e1eb5b- &#d1c9c9Type: &#e3c79a" + (SQLData.USE_SQLITE ? "SQLite" : "MySQL")));
            Tellraw raw = Tellraw.fromLegacy("&#e1eb5b - &#d1c9c9Status: ");
            if (SQLData.USE_SQLITE) {
                raw.then("CONNECTED").color(ChatColor.GREEN).tooltip("&7SQLite connections are kept connected");
            } else {
                raw.then("IDLE").color("#e3aa4f").tooltip("&7MySQL connections are kept closed until they are needed", "&7That's what the IDLE state is");
            }
            raw.send(sender);
            sender.sendMessage(Colorize.translateBungeeHex(" &#e1eb5b- &#d1c9c9Players In Database: &#e3c79a" + playerDataCount));
        });
    }
}

