package simplepets.brainsynder.commands.list;

import org.bsdevelopment.pluginutils.command.CommandBuilder;
import org.bsdevelopment.pluginutils.command.arguments.PlayerArgument;
import org.bsdevelopment.pluginutils.command.arguments.StringArgument;
import org.bukkit.entity.Player;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.plugin.SimplePets;
import simplepets.brainsynder.api.plugin.config.ConfigOption;
import simplepets.brainsynder.api.plugin.config.MessageOption;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.commands.PetCommandClass;
import simplepets.brainsynder.utils.RenameType;

public class RenameCommand implements PetCommandClass {

    @Override
    public CommandBuilder build() {
        return CommandBuilder.create("rename")
                .withPermission("pet.commands.rename")
                .withDescription("Renames the selected pet type")
                .withArguments(ALL_PET_TYPES)
                .withArguments(new StringArgument("name").setOptional(true))
                .executesPlayer((player, args) -> {
                    PetType type = args.get("type");
                    RenameType rename = RenameType.getType(ConfigOption.RENAME_TYPE.get(), RenameType.ANVIL);

                    SimplePets.getUserManager().getPetUser(player).ifPresent(user -> {
                        if (args.has("name")) {
                            user.setPetName(args.get("name"), type);
                            return;
                        }

                        if ((rename == RenameType.COMMAND) && (!args.has("name"))) {
                            player.sendMessage(PetCore.getInstance().getMessageFile().getTranslation(MessageOption.PREFIX) + " §cUsage: /pet rename <type> <name>");
                        } else {
                            dispatch(rename, user, type);
                        }
                    });
                })
                .withSubcommand(CommandBuilder.create("target")
                        .withPermission("pet.commands.rename.other")
                        .withArguments(new PlayerArgument("player"))
                        .withArguments(ALL_PET_TYPES)
                        .withArguments(new StringArgument("name").setOptional(true))
                        .executes((sender, args) -> {
                            Player target = args.get("player");
                            PetType type = args.get("type");
                            RenameType rename = RenameType.getType(ConfigOption.RENAME_TYPE.get(), RenameType.ANVIL);

                            SimplePets.getUserManager().getPetUser(target).ifPresent(user -> {
                                if (args.has("name")) {
                                    user.setPetName(args.get("name"), type);
                                    return;
                                }

                                if ((!args.has("name")) && ((rename == RenameType.COMMAND) || !(sender instanceof Player))) {
                                    sender.sendMessage(PetCore.getInstance().getMessageFile().getTranslation(MessageOption.PREFIX) + " §cUsage: /pet rename target <player> <type> <name>");
                                } else {
                                    dispatch(rename, user, type);
                                }
                            });
                        })
                );
    }

    private void dispatch(RenameType rename, PetUser user, PetType type) {
        switch (rename) {
            case CHAT -> PetCore.getInstance().getRenameManager().renameViaChat(user, type);
            case ANVIL -> PetCore.getInstance().getRenameManager().renameViaAnvil(user, type);
            case SIGN -> PetCore.getInstance().getRenameManager().renameViaSign(user, type);
            case DIALOG -> PetCore.getInstance().getRenameManager().renameViaDialog(user, type);
        }
    }
}
