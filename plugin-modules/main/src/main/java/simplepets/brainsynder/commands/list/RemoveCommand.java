package simplepets.brainsynder.commands.list;

import org.bsdevelopment.pluginutils.command.CommandBuilder;
import org.bsdevelopment.pluginutils.command.arguments.PlayerArgument;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.plugin.SimplePets;
import simplepets.brainsynder.api.plugin.config.MessageOption;
import simplepets.brainsynder.commands.PetCommandClass;

import java.util.concurrent.atomic.AtomicInteger;

public class RemoveCommand implements PetCommandClass {

    @Override
    public CommandBuilder build() {
        return CommandBuilder.create("remove")
                .withPermission("pet.commands.remove")
                .withDescription("Remove your pet")
                .withSubcommand(buildTargetCommand())
                .withArguments(OPTIONAL_PET_TYPES)
                .executesPlayer((player, args) -> {
                    PetType type = args.has("type") ? args.get("type") : null;
                    removePets(player, player, type);
                });
    }

    private CommandBuilder buildTargetCommand() {
        return CommandBuilder.create("target")
                .withPermission("pet.commands.remove.other")
                .withDescription("Remove a pet from another player")
                .withArguments(new PlayerArgument("player"))
                .withArguments(OPTIONAL_PET_TYPES)
                .executes((sender, args) -> {
                    Player target = args.get("player");
                    PetType type = args.has("type") ? args.get("type") : null;
                    removePets(sender, target, type);
                });
    }

    private void removePets(CommandSender sender, Player target, PetType type) {
        if (type != null) {
            SimplePets.getUserManager().getPetUser(target).ifPresent(user -> {
                user.removePet(type);
                sender.sendMessage(PetCore.getInstance().getMessageFile().getTranslation(MessageOption.REMOVED_PET).replace("{type}", type.getName()));
            });
        } else {
            AtomicInteger count = new AtomicInteger(0);
            SimplePets.getUserManager().getPetUser(target).ifPresent(user -> {
                for (PetType petType : PetType.values()) {
                    if (user.removePet(petType)) count.incrementAndGet();
                }
            });
            sender.sendMessage(PetCore.getInstance().getMessageFile().getTranslation(MessageOption.REMOVED_ALL_PETS).replace("{count}", String.valueOf(count.get())));
        }
    }
}
