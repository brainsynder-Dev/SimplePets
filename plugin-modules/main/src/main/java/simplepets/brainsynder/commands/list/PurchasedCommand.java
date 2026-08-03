package simplepets.brainsynder.commands.list;

import org.bsdevelopment.pluginutils.chat.TellrawMessage;
import org.bsdevelopment.pluginutils.chat.decoration.NamedTextColor;
import org.bsdevelopment.pluginutils.command.CommandBuilder;
import org.bsdevelopment.pluginutils.command.CommandPermission;
import org.bsdevelopment.pluginutils.command.arguments.PlayerArgument;
import org.bukkit.entity.Player;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.plugin.SimplePets;
import simplepets.brainsynder.api.plugin.config.MessageOption;
import simplepets.brainsynder.commands.PetCommandClass;

public class PurchasedCommand implements PetCommandClass {

    @Override
    public CommandBuilder build() {
        return CommandBuilder.create("purchased")
                .withPermission("pet.commands.purchased")
                .withDescription("Controls what pets the player has purchased")
                .withSubcommand(buildAddCommand())
                .withSubcommand(buildRemoveCommand())
                .withSubcommand(buildListCommand())
                .executes((sender, args) -> {
                    sender.sendMessage(PetCore.getInstance().getMessageFile().getTranslation(MessageOption.PREFIX)
                            + " §cUsage: /pet purchased <add|remove|list> <player> [type]");
                });
    }

    public CommandBuilder buildAddCommand() {
        return CommandBuilder.create("add")
                .withPermission("pet.commands.purchased.add")
                .withDescription("Adds a purchased pet to a player")
                .withArguments(new PlayerArgument("player"))
                .withArguments(ALL_PET_TYPES)
                .executes((sender, args) -> {
                    Player target = args.get("player");
                    PetType type = args.get("type");

                    SimplePets.getUserManager().getPetUser(target).ifPresent(user -> {
                        user.addOwnedPet(type);
                        sender.sendMessage(PetCore.getInstance().getMessageFile().getTranslation(MessageOption.PURCHASE_ADD)
                                .replace("{player}", target.getName())
                                .replace("{type}", type.getName()));
                    });
                });
    }

    public CommandBuilder buildRemoveCommand() {
        return CommandBuilder.create("remove")
                .withPermission("pet.commands.purchased.remove")
                .withDescription("Removes a purchased pet from a player")
                .withArguments(new PlayerArgument("player"))
                .withArguments(ALL_PET_TYPES)
                .executes((sender, args) -> {
                    Player target = args.get("player");
                    PetType type = args.get("type");

                    SimplePets.getUserManager().getPetUser(target).ifPresent(user -> {
                        user.removeOwnedPet(type);
                        sender.sendMessage(PetCore.getInstance().getMessageFile().getTranslation(MessageOption.PURCHASE_REMOVE)
                                .replace("{player}", target.getName())
                                .replace("{type}", type.getName()));
                    });
                });
    }

    public CommandBuilder buildListCommand() {
        return CommandBuilder.create("list")
                .withPermission("pet.commands.purchased.list")
                .withDescription("Lists the purchased pets for a player")
                .withArguments(new PlayerArgument("player")
                        .setOptional(true)
                        .withPermission(CommandPermission.of("pet.commands.purchased.list.other")))
                .executes((sender, args) -> {
                    Player target = args.has("player") ? args.get("player") : (sender instanceof Player p ? p : null);
                    if (target == null) {
                        sender.sendMessage(PetCore.getInstance().getMessageFile().getTranslation(MessageOption.PREFIX)
                                + " §cYou must specify a player when running from console.");
                        return;
                    }

                    SimplePets.getUserManager().getPetUser(target).ifPresent(user -> {
                        String prefix = PetCore.getInstance().getMessageFile().getTranslation(MessageOption.PURCHASE_LIST_PREFIX);
                        if (!prefix.endsWith(" ")) prefix = prefix + " ";
                        TellrawMessage tellraw = TellrawMessage.of(prefix);
                        user.getOwnedPets().forEach(type -> {
                            tellraw.then(type.getName()).color(NamedTextColor.GREEN).then(", ").color("#d1c9c9");
                        });
                        tellraw.removeLastPart();
                        tellraw.send(sender);
                    });
                });
    }
}
