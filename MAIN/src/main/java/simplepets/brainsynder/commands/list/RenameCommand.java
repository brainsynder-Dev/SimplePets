package simplepets.brainsynder.commands.list;

import lib.brainsynder.commands.annotations.ICommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.plugin.SimplePets;
import simplepets.brainsynder.commands.PetSubCommand;
import simplepets.brainsynder.files.MessageFile;
import simplepets.brainsynder.files.options.MessageOption;
import simplepets.brainsynder.utils.RenameType;

import java.util.Optional;

@ICommand(
        name = "rename",
        usage = "<type> [name]",
        description = "Renames the selected pet type"
)
public class RenameCommand extends PetSubCommand {
    public RenameCommand(PetCore plugin) {
        super(plugin);

        registerCompletion(1, getPetTypes());
    }

    @Override
    public void run(CommandSender sender, String[] args) {
        if (args.length == 0) {
            sendUsage(sender);
            return;
        }

        SimplePets.getUserManager().getPetUser((Player) sender).ifPresent(user -> {

            Optional<PetType> petType = PetType.getPetType(args[0]);
            if (!petType.isPresent()) {
                sender.sendMessage(MessageFile.getTranslation(MessageOption.INVALID_PET_TYPE).replace("{arg}", args[1]));
                return;
            }

            PetType type = petType.get();

            if (!SimplePets.getSpawnUtil().isRegistered(type)) {
                sender.sendMessage(MessageFile.getTranslation(MessageOption.PET_NOT_REGISTERED).replace("{type}", type.getName()));
                return;
            }

            RenameType rename = getPlugin().getConfiguration().getEnum("RenamePet.Type", RenameType.class);
            switch (rename) {
                case CHAT:
                    getPlugin().getRenameManager().renameViaChat(user, type);
                    break;
                case COMMAND:
                    if (args.length == 1) {
                        sendUsage(sender);
                        return;
                    }
                    user.setPetName(args[1], type);
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
