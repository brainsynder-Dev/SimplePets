package simplepets.brainsynder.commands.list;

import com.google.common.collect.Lists;
import lib.brainsynder.commands.annotations.ICommand;
import lib.brainsynder.nms.Tellraw;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.plugin.SimplePets;
import simplepets.brainsynder.commands.Permission;
import simplepets.brainsynder.commands.PetSubCommand;
import simplepets.brainsynder.files.MessageFile;
import simplepets.brainsynder.files.options.MessageOption;

import java.util.List;

@ICommand(
        name = "purchased",
        usage = "<add/remove/list> <player> [type]",
        description = "Controls what pets the player has purchased"
)
@Permission(permission = "purchased", adminCommand = true, additionalPermissions = {"add", "remove", "list", "list.other"})
public class PurchasedCommand extends PetSubCommand {
    public PurchasedCommand(PetCore plugin) {
        super(plugin);
    }

    @Override
    public List<String> handleCompletions(List<String> completions, CommandSender sender, int index, String[] args) {
        if (args.length == 1) {
            if (sender.hasPermission(getPermission("add"))) completions.add("add");
            if (sender.hasPermission(getPermission("remove"))) completions.add("remove");
            if (sender.hasPermission(getPermission("list"))) completions.add("list");
            return completions;
        }

        if (args.length == 2) {
            if (args[0].contains("list")) {
                if (sender.hasPermission(getPermission("list.other"))) return getOnlinePlayers();
                return Lists.newArrayList(sender.getName());
            }
            return completions;
        }

        if ((index == 3)) {
            if (!args[0].contains("list")) return getPetTypes();
        }
        return super.handleCompletions(completions, sender, index, args);
    }

    @Override
    public void run(CommandSender sender, String[] args) {
        if (args.length == 0) {
            sendUsage(sender);
            return;
        }

        // Missing player
        if (args.length == 1) {
            sendUsage(sender);
            return;
        }
        Player target = Bukkit.getPlayerExact(args[1]);

        if (args[0].equalsIgnoreCase("list") && (!sender.hasPermission(getPermission("list.other")))){
            if (sender instanceof Player) target = (Player) sender;
        }

        if (target == null) {
            sender.sendMessage(MessageFile.getTranslation(MessageOption.PLAYER_NOT_ONLINE));
            return;
        }

        Player finalTarget = target;
        SimplePets.getUserManager().getPetUser(target).ifPresent(user -> {
            if (args[0].equalsIgnoreCase("add")) {
                if (!sender.hasPermission(getPermission("add"))){
                    sender.sendMessage(MessageFile.getTranslation(MessageOption.NO_PERMISSION));
                    return;
                }

                if (args.length == 2) {
                    sender.sendMessage(MessageFile.getTranslation(MessageOption.MISSING_PET_TYPE));
                    return;
                }

                PetType.getPetType(args[2]).ifPresent(type -> {
                    user.addOwnedPet(type);
                    sender.sendMessage(MessageFile.getTranslation(MessageOption.PURCHASE_ADD)
                            .replace("{player}", finalTarget.getName())
                            .replace("{type}", type.getName())
                    );
                });
                return;
            }

            if (args[0].equalsIgnoreCase("remove")) {
                if (!sender.hasPermission(getPermission("remove"))){
                    sender.sendMessage(MessageFile.getTranslation(MessageOption.NO_PERMISSION));
                    return;
                }
                if (args.length == 2) {
                    sender.sendMessage(MessageFile.getTranslation(MessageOption.MISSING_PET_TYPE));
                    return;
                }

                PetType.getPetType(args[2]).ifPresent(type -> {
                    user.removeOwnedPet(type);
                    sender.sendMessage(MessageFile.getTranslation(MessageOption.PURCHASE_REMOVE)
                            .replace("{player}", finalTarget.getName())
                            .replace("{type}", type.getName())
                    );
                });
                return;
            }

            if (args[0].equalsIgnoreCase("list")) {
                if (!sender.hasPermission(getPermission("list"))){
                    sender.sendMessage(MessageFile.getTranslation(MessageOption.NO_PERMISSION));
                    return;
                }

                String prefix = MessageFile.getTranslation(MessageOption.PURCHASE_LIST_PREFIX);
                if (!prefix.endsWith(" ")) prefix = prefix+" ";
                Tellraw tellraw = Tellraw.getInstance(prefix);
                user.getOwnedPets().forEach(type -> {
                    tellraw.then(type.getName()).color(ChatColor.GREEN).then(", ").color(ChatColor.of("#d1c9c9"));
                });
                tellraw.removeLastPart();
                tellraw.send(sender);
                return;
            }

            sendUsage(sender);
        });
    }
}
