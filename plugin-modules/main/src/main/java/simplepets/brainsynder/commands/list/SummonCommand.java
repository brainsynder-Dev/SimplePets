package simplepets.brainsynder.commands.list;

import org.bsdevelopment.nbt.StorageTagCompound;
import org.bsdevelopment.nbt.io.StorageStringParser;
import org.bsdevelopment.pluginutils.chat.TellrawMessage;
import org.bsdevelopment.pluginutils.command.CommandArguments;
import org.bsdevelopment.pluginutils.command.CommandBuilder;
import org.bsdevelopment.pluginutils.command.CommandPermission;
import org.bsdevelopment.pluginutils.command.arguments.PlayerArgument;
import org.bsdevelopment.pluginutils.command.arguments.StorageTagArgument;
import org.bsdevelopment.pluginutils.command.arguments.suggestions.ArgumentSuggestions;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.ISpawnUtil;
import simplepets.brainsynder.api.SpawnResult;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.misc.IEntityControllerPet;
import simplepets.brainsynder.api.event.inventory.PetSelectTypeEvent;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.plugin.SimplePets;
import simplepets.brainsynder.api.plugin.config.ConfigOption;
import simplepets.brainsynder.api.plugin.config.MessageOption;
import simplepets.brainsynder.commands.PetCommandClass;
import simplepets.brainsynder.utils.Utilities;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class SummonCommand implements PetCommandClass {

    @Override
    public CommandBuilder build() {
        return CommandBuilder.create("summon")
                .withAliases("spawn")
                .withPermission("pet.commands.summon")
                .withDescription("Spawns a pet for yourself")
                .withSubcommand(buildAllCommand())
                .withSubcommand(buildTargetCommand())
                .withArguments(ACCESSIBLE_PET_TYPES)
                .withArguments(buildNbtArgument(false))
                .executesPlayer((player, args) -> {
                    ISpawnUtil spawner = PetCore.getInstance().getSpawnUtil();
                    if (spawner == null) return;

                    PetType type = args.get("type");

                    if (type.isInDevelopment() && !ConfigOption.PET_TOGGLES_DEV_MOBS.get()) {
                        player.sendMessage(PetCore.getInstance().getMessageFile().getTranslation(MessageOption.PET_IN_DEVELOPMENT).replace("{type}", type.getName()));
                        return;
                    }

                    StorageTagCompound compound = buildCompound(player, args);
                    if (compound == null) return;

                    Utilities.applyPetDataDefaults(type, compound);
                    StorageTagCompound finalCompound = compound;

                    PetCore.getInstance().getUserManager().getPetUser(player.getUniqueId()).ifPresent(user -> {
                        if (user.isOnPetChangeCooldown()) {
                            player.sendMessage(PetCore.getInstance().getMessageFile()
                                    .getTranslation(MessageOption.PET_ON_COOLDOWN)
                                    .replace("{seconds}", String.valueOf(user.getRemainingCooldownSeconds())));
                            return;
                        }

                        if (!user.canSpawnMorePets()) {
                            player.sendMessage(PetCore.getInstance().getMessageFile().getTranslation(MessageOption.CANT_SPAWN_MORE_PETS));
                            return;
                        }

                        PetSelectTypeEvent event = new PetSelectTypeEvent(type, user);
                        Bukkit.getServer().getPluginManager().callEvent(event);
                        if (event.isCancelled()) return;

                        SpawnResult<IEntityPet> result = spawner.spawnEntityPet(type, user, finalCompound);
                        if (result.isSuccess()) user.recordPetChange();
                        handleSpawnResult(player, result, type, finalCompound);
                    });
                });
    }

    private CommandBuilder buildTargetCommand() {
        return CommandBuilder.create("target")
                .withPermission("pet.commands.summon.other")
                .withDescription("Spawns a pet for another player")
                .withArguments(new PlayerArgument("player"))
                .withArguments(ALL_PET_TYPES)
                .withArguments(buildNbtArgument(true))
                .executes((sender, args) -> {
                    ISpawnUtil spawner = PetCore.getInstance().getSpawnUtil();
                    if (spawner == null) return;

                    Player target = args.get("player");
                    PetType type = args.get("type");

                    if (type.isInDevelopment() && !ConfigOption.PET_TOGGLES_DEV_MOBS.get()) {
                        sender.sendMessage(PetCore.getInstance().getMessageFile().getTranslation(MessageOption.PET_IN_DEVELOPMENT).replace("{type}", type.getName()));
                        return;
                    }

                    StorageTagCompound compound = buildCompound(sender, args);
                    if (compound == null) return;

                    Utilities.applyPetDataDefaults(type, compound);
                    StorageTagCompound finalCompound = compound;

                    PetCore.getInstance().getUserManager().getPetUser(target.getUniqueId()).ifPresent(user -> {
                        if (!user.canSpawnMorePets()
                                && !ConfigOption.MISC_TOGGLES_CONSOLE_BYPASS_LIMIT.get()
                                && !(sender instanceof Player)) {
                            target.sendMessage(PetCore.getInstance().getMessageFile().getTranslation(MessageOption.CANT_SPAWN_MORE_PETS));
                            return;
                        }

                        SpawnResult<IEntityPet> result = spawner.spawnEntityPet(type, user, finalCompound);
                        handleSpawnResult(sender, result, type, finalCompound);
                    });
                });
    }

    public CommandBuilder buildAllCommand() {
        return CommandBuilder.create("all")
                .withPermission("pet.commands.summon.all")
                .withDescription("Spawns all available pet types for yourself")
                .withRequirement(sender -> sender instanceof Player)
                .executesPlayer((player, args) -> {
                    ISpawnUtil spawner = PetCore.getInstance().getSpawnUtil();
                    if (spawner == null) return;

                    AtomicInteger count = new AtomicInteger(0);
                    for (PetType type : PetType.values()) {
                        SimplePets.getPetConfigManager().getPetConfig(type).ifPresent(config -> {
                            if (!config.isEnabled()) return;
                            if (!type.isSupported()) return;
                            if (!SimplePets.getSpawnUtil().isRegistered(type)) return;
                            count.getAndIncrement();
                            PetCore.getInstance().getUserManager().getPetUser(player.getUniqueId())
                                    .ifPresent(user -> spawner.spawnEntityPet(type, user));
                        });
                    }

                    player.sendMessage(PetCore.getInstance().getMessageFile().getTranslation(MessageOption.SUMMONED_ALL_PETS).replace("{count}", String.valueOf(count.get())));
                });
    }

    /**
     * Builds the optional nbt argument with suggestions based on context.
     */
    private StorageTagArgument buildNbtArgument(boolean hasPlayerArg) {
        return (StorageTagArgument) new StorageTagArgument("nbt")
                .setOptional(true)
                .withPermission(CommandPermission.of("pet.commands.summon.nbt"))
                .replaceSuggestions(ArgumentSuggestions.of(info -> {
                    List<String> suggestions = new ArrayList<>();
                    suggestions.add("{}");

                    if (info.previousArgs() == null) return suggestions;

                    Player player = hasPlayerArg && info.previousArgs().has("player") ? info.previousArgs().get("player") : (info.sender() instanceof Player p ? p : null);
                    if (player == null) return suggestions;

                    PetType type = info.previousArgs().get("type");
                    if (type == null || type == PetType.UNKNOWN) return suggestions;

                    SimplePets.getUserManager().getPetUser(player).ifPresent(user -> user.getPetEntity(type).ifPresent(entityPet -> {
                        String compoundStr = entityPet.asCompound().toString();
                        if (!compoundStr.equals("{}")) suggestions.add(compoundStr);
                    }));

                    return suggestions;
                }));
    }

    /**
     * Parses the nbt argument from args, sending an error message on failure. Returns null on failure.
     */
    private StorageTagCompound buildCompound(CommandSender sender, CommandArguments args) {
        if (!args.has("nbt") || !sender.hasPermission("pet.commands.summon.nbt")) return new StorageTagCompound();
        StorageTagCompound nbtArg = args.get("nbt");
        try {
            return StorageStringParser.getTagFromJson(nbtArg.toString());
        } catch (Exception e) {
            sender.sendMessage(PetCore.getInstance().getMessageFile().getTranslation(MessageOption.INVALID_NBT));
            return null;
        }
    }

    private void handleSpawnResult(CommandSender sender, SpawnResult<IEntityPet> result, PetType type, StorageTagCompound compound) {
        if (!result.isSuccess()) {
            if (result.isFailure()) {
                TellrawMessage.of(PetCore.getInstance().getMessageFile().getTranslation(MessageOption.FAILED_SUMMON, false).replace("{type}", type.getName())).tooltip(result.failMessage()).send(sender);
                return;
            }
            sender.sendMessage(PetCore.getInstance().getMessageFile().getTranslation(MessageOption.FAILED_SUMMON).replace("{type}", type.getName()));
            return;
        }

        if (type == PetType.ARMOR_STAND) {
            ((IEntityControllerPet) result.value()).getVisibleEntity().applyCompound(compound);
        }
        sender.sendMessage(PetCore.getInstance().getMessageFile().getTranslation(MessageOption.SUMMONED_PET).replace("{type}", type.getName()));
    }
}
