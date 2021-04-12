package simplepets.brainsynder.commands.list;

import lib.brainsynder.commands.annotations.ICommand;
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
        usage = "[type]",
        description = "Remove your pet or another players"
)
@Permission(permission = "remove", defaultAllow = true, additionalPermissions = {"other"})
public class RemoveCommand extends PetSubCommand {
    public RemoveCommand(PetCore plugin) {
        super(plugin);
    }

    @Override
    public void run(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            //TODO: not player message
            return;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            AtomicInteger integer = new AtomicInteger(0);

            getPlugin().getUserManager().getPetUser(player).ifPresent(user -> {
                for (PetType type : PetType.values())
                if (user.removePet(type)) integer.incrementAndGet();
            });

            sender.sendMessage(MessageFile.getTranslation(MessageOption.REMOVED_ALL_PETS).replace("{count}", String.valueOf(integer.get())));
            return;
        }


        Optional<PetType> petType = PetType.getPetType(args[0]);
        if (!petType.isPresent()) {
            sender.sendMessage(MessageFile.getTranslation(MessageOption.INVALID_PET_TYPE).replace("{arg}", args[0]));
            return;
        }

        PetType type = petType.get();

        getPlugin().getUserManager().getPetUser(((Player)sender).getUniqueId()).ifPresent(user -> {
            if (!user.removePet(type)) {
                sender.sendMessage(MessageFile.getTranslation(MessageOption.REMOVED_PET).replace("{type}", type.getName()));
                return;
            }

            sender.sendMessage(MessageFile.getTranslation(MessageOption.REMOVED_PET).replace("{type}", type.getName()));
        });
    }
}
