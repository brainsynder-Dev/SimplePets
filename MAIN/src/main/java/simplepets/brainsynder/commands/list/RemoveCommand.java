package simplepets.brainsynder.commands.list;

import lib.brainsynder.commands.annotations.ICommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.commands.Permission;
import simplepets.brainsynder.commands.PetSubCommand;
import simplepets.brainsynder.files.MessageFile;
import simplepets.brainsynder.files.options.MessageOption;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@ICommand(
        name = "remove",
        usage = "[player] [type]",
        description = "Remove your pet or another players"
)
@Permission(permission = "remove", defaultAllow = true, additionalPermissions = {"other"})
public class RemoveCommand extends PetSubCommand {
    public RemoveCommand(PetCore plugin) {
        super(plugin);
    }

    @Override
    public void run(CommandSender sender, String[] args) {
        if (args.length == 0) {
            if (!(sender instanceof Player)) {
                sendUsage(sender);
                return;
            }

            AtomicInteger integer = new AtomicInteger(0);

            getPlugin().getUserManager().getPetUser((Player)sender).ifPresent(user -> {
                for (PetType type : PetType.values())
                if (user.removePet(type)) integer.incrementAndGet();
            });

            sender.sendMessage(MessageFile.getTranslation(MessageOption.REMOVED_ALL_PETS).replace("{count}", String.valueOf(integer.get())));
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
        if (args.length == index.get()) {
            AtomicInteger integer = new AtomicInteger(0);

            getPlugin().getUserManager().getPetUser(finalTarget).ifPresent(user -> {
                for (PetType type : PetType.values())
                    if (user.removePet(type)) integer.incrementAndGet();
            });

            sender.sendMessage(MessageFile.getTranslation(MessageOption.REMOVED_ALL_PETS).replace("{count}", String.valueOf(integer.get())));

            return;
        }

        Optional<PetType> petType = PetType.getPetType(args[index.get()]);
        if (!petType.isPresent()) {
            sender.sendMessage(MessageFile.getTranslation(MessageOption.INVALID_PET_TYPE).replace("{arg}", args[index.get()]));
            return;
        }

        PetType type = petType.get();

        getPlugin().getUserManager().getPetUser(finalTarget).ifPresent(user -> {
            if (!user.removePet(type)) {
                sender.sendMessage(MessageFile.getTranslation(MessageOption.REMOVED_PET).replace("{type}", type.getName()));
                return;
            }

            sender.sendMessage(MessageFile.getTranslation(MessageOption.REMOVED_PET).replace("{type}", type.getName()));
        });
    }
}
