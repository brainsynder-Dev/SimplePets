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
@Permission(permission = "purchased")
public class PurchasedCommand extends PetSubCommand {
    public PurchasedCommand(PetCore plugin) {
        super(plugin);
        registerCompletion(1, Lists.newArrayList("add", "remove", "list"));
    }

    @Override
    public List<String> handleCompletions(List<String> completions, CommandSender sender, int index, String[] args) {
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

        if (target == null) {
            sender.sendMessage(MessageFile.getTranslation(MessageOption.PLAYER_NOT_ONLINE));
            return;
        }

        SimplePets.getUserManager().getPetUser(target).ifPresent(user -> {
            if (args[0].equalsIgnoreCase("add")) {
                if (args.length == 2) {
                    sender.sendMessage(MessageFile.getTranslation(MessageOption.MISSING_PET_TYPE));
                    return;
                }

                PetType.getPetType(args[2]).ifPresent(type -> {
                    user.addOwnedPet(type);
                    sender.sendMessage(MessageFile.getTranslation(MessageOption.PURCHASE_ADD)
                            .replace("{player}", target.getName())
                            .replace("{type}", type.getName())
                    );
                });
                return;
            }

            if (args[0].equalsIgnoreCase("remove")) {
                if (args.length == 2) {
                    sender.sendMessage(MessageFile.getTranslation(MessageOption.MISSING_PET_TYPE));
                    return;
                }

                PetType.getPetType(args[2]).ifPresent(type -> {
                    user.removeOwnedPet(type);
                    sender.sendMessage(MessageFile.getTranslation(MessageOption.PURCHASE_REMOVE)
                            .replace("{player}", target.getName())
                            .replace("{type}", type.getName())
                    );
                });
                return;
            }

            if (args[0].equalsIgnoreCase("list")) {
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
