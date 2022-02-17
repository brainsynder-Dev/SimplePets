package simplepets.brainsynder.commands.list;

import lib.brainsynder.commands.annotations.ICommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.plugin.SimplePets;
import simplepets.brainsynder.api.plugin.config.ConfigOption;
import simplepets.brainsynder.commands.Permission;
import simplepets.brainsynder.commands.PetSubCommand;
import simplepets.brainsynder.files.MessageFile;
import simplepets.brainsynder.files.options.MessageOption;
import simplepets.brainsynder.utils.RenameType;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@ICommand(
        name = "rename",
        usage = "[player] <type> [name]",
        description = "Renames the selected pet type"
)
@Permission(permission = "rename", defaultAllow = true, additionalPermissions = {"other"})
public class RenameCommand extends PetSubCommand {
    public RenameCommand(PetCore plugin) {
        super(plugin);
    }

    @Override
    public List<String> handleCompletions(List<String> completions, CommandSender sender, int index, String[] args) {
        if (index == 1) {
            if (sender.hasPermission(getPermission("other"))) {
                completions.addAll(getOnlinePlayers());
            }
            completions.addAll(getPetTypes(sender));
            return completions;
        }

        if (index == 2) {
            Optional<PetType> optional = PetType.getPetType(args[0]);
            // IS not a pet but is a player... should be...
            if (!optional.isPresent()) {
                Player target = Bukkit.getPlayer(args[0]);
                if ((target != null) && sender.hasPermission(getPermission("other"))) {
                    completions.addAll(getPetTypes(sender));
                }
            }
            return completions;
        }
        return super.handleCompletions(completions, sender, index, args);
    }

    @Override
    public void run(CommandSender sender, String[] args) {
        if (args.length == 0) {
            sendUsage(sender);
            return;
        }
        AtomicInteger index = new AtomicInteger(0);

        Player target = null;
        if (isUsername(args[index.get()]) && sender.hasPermission(getPermission("other"))) {
            Player selected = Bukkit.getPlayerExact(args[index.get()]);
            if (selected != null) {
                target = selected;
                if (!sender.hasPermission(getPermission("other"))) {
                    sender.sendMessage(MessageFile.getTranslation(MessageOption.NO_PERMISSION));
                    return;
                }
                index.getAndIncrement();
            }
        }

        if (target == null) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED+"You must be a player to run this command for yourself.");
            }else{
                target = (Player) sender;
            }
        }

        Player finalTarget = target;
        SimplePets.getUserManager().getPetUser(target).ifPresent(user -> {

            Optional<PetType> petType = PetType.getPetType(args[index.get()]);
            if (!petType.isPresent()) {
                sender.sendMessage(MessageFile.getTranslation(MessageOption.INVALID_PET_TYPE).replace("{arg}", args[index.get()]));
                return;
            }

            PetType type = petType.get();

            if (!SimplePets.getSpawnUtil().isRegistered(type)) {
                sender.sendMessage(MessageFile.getTranslation(MessageOption.PET_NOT_REGISTERED).replace("{type}", type.getName()));
                return;
            }

            index.getAndIncrement();
            if ((args.length >= 2) && (!sender.getName().equals(finalTarget.getName()))) {
                user.setPetName(messageMaker(args, index.get()), type);
                return;
            }

            RenameType rename = RenameType.getType(ConfigOption.INSTANCE.RENAME_TYPE.getValue(), RenameType.ANVIL);
            switch (rename) {
                case CHAT:
                    getPlugin().getRenameManager().renameViaChat(user, type);
                    break;
                case COMMAND:
                    if (args.length == 1) {
                        sendUsage(sender);
                        return;
                    }
                    user.setPetName(messageMaker(args, index.get()), type);
                    break;
                case ANVIL:
                    getPlugin().getRenameManager().renameViaAnvil(user, type);
                    break;
                case SIGN:
                    getPlugin().getRenameManager().renameViaSign(user, type);
                    break;
            }
        });


    }
}
