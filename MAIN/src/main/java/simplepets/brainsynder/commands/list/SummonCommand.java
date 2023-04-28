package simplepets.brainsynder.commands.list;

import lib.brainsynder.commands.annotations.ICommand;
import lib.brainsynder.nbt.JsonToNBT;
import lib.brainsynder.nbt.StorageTagCompound;
import lib.brainsynder.nbt.other.NBTException;
import lib.brainsynder.nms.Tellraw;
import lib.brainsynder.optional.BiOptional;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.ISpawnUtil;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.misc.IEntityControllerPet;
import simplepets.brainsynder.api.event.inventory.PetSelectTypeEvent;
import simplepets.brainsynder.api.pet.PetData;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.plugin.SimplePets;
import simplepets.brainsynder.api.plugin.config.ConfigOption;
import simplepets.brainsynder.commands.Permission;
import simplepets.brainsynder.commands.PetSubCommand;
import simplepets.brainsynder.files.MessageFile;
import simplepets.brainsynder.files.options.MessageOption;
import simplepets.brainsynder.utils.Utilities;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@ICommand(
        name = "summon",
        usage = "<type> [player] [nbt]",
        alias = {"spawn"},
        description = "Spawns a pet for the player or selected player"
)
@Permission(permission = "summon", defaultAllow = true, additionalPermissions = {"all", "other", "nbt"})
public class SummonCommand extends PetSubCommand {
    public SummonCommand(PetCore plugin) {
        super(plugin);
    }

    @Override
    public void run(CommandSender sender, String[] args) {
        if (args.length == 0) {
            sendUsage(sender);
            return;
        }

        ISpawnUtil spawner = getPlugin().getSpawnUtil();
        if (spawner == null) return;

        if (args[0].equalsIgnoreCase("all") && sender.hasPermission(getPermission("all"))) {
            AtomicInteger integer = new AtomicInteger(0);
            for (PetType type : PetType.values()) {
                SimplePets.getPetConfigManager().getPetConfig(type).ifPresent(config -> {
                    if (!config.isEnabled()) return;
                    if (!type.isSupported()) return;
                    if (!SimplePets.getSpawnUtil().isRegistered(type)) return;
                    integer.getAndIncrement();
                    getPlugin().getUserManager().getPetUser(((Player)sender).getUniqueId()).ifPresent(user -> spawner.spawnEntityPet(type, user));
                });
            }

            sender.sendMessage(MessageFile.getTranslation(MessageOption.SUMMONED_ALL_PETS).replace("{count}", String.valueOf(integer.get())));
            return;
        }

        Optional<PetType> petType = PetType.getPetType(args[0]);
        if (!petType.isPresent()) {
            sender.sendMessage(MessageFile.getTranslation(MessageOption.INVALID_PET_TYPE).replace("{arg}", args[0]));
            return;
        }

        PetType type = petType.get();
        if (type.isInDevelopment()
                && (!ConfigOption.INSTANCE.PET_TOGGLES_DEV_MOBS.getValue())) {
            sender.sendMessage(MessageFile.getTranslation(MessageOption.PET_IN_DEVELOPMENT).replace("{type}", type.getName()));
            return;
        }

        if (!type.isSupported()) {
            sender.sendMessage(MessageFile.getTranslation(MessageOption.PET_NOT_SUPPORTED).replace("{type}", type.getName()));
            return;
        }

        if (!Utilities.hasPermission(sender, type.getPermission())) {
            sender.sendMessage(MessageFile.getTranslation(MessageOption.NO_PERMISSION));
            return;
        }

        if (!SimplePets.getSpawnUtil().isRegistered(type)) {
            sender.sendMessage(MessageFile.getTranslation(MessageOption.PET_NOT_REGISTERED).replace("{type}", type.getName()));
            return;
        }

        Player target = null;
        StorageTagCompound compound = new StorageTagCompound();
        if (args.length > 1) {
            int argStart = 1;

            if (isUsername(args[1])) {
                target = Bukkit.getPlayerExact(args[1]);
                if (target == null) {
                    sender.sendMessage(MessageFile.getTranslation(MessageOption.PLAYER_NOT_ONLINE).replace("{player}", args[1]));
                    return;
                }
                if ((!sender.hasPermission(getPermission("other"))) && (!target.getName().equals(sender.getName()))) {
                    sender.sendMessage(MessageFile.getTranslation(MessageOption.NO_PERMISSION));
                    return;
                }
                argStart++;
            }


            if (args.length > argStart) {
                String json = messageMaker(args, argStart);
                json = formatJson(json);

                // This should help fix the issue with booleans not working for the command.
                if (json.toLowerCase().contains(":true")) {
                    json = json.replaceAll("(?i):true", ":1b");
                }
                if (json.toLowerCase().contains(":false")) {
                    json = json.replaceAll("(?i):false", ":0b");
                }

                try {
                    compound = JsonToNBT.getTagFromJson(json);
                } catch (NBTException e) {
                    sender.sendMessage(MessageFile.getTranslation(MessageOption.INVALID_NBT));
                    sender.sendMessage(MessageFile.getTranslation(MessageOption.INVALID_NBT_MESSAGE).replace("{message}", e.getMessage()));
                    return;
                }
            }
        }

        if (!sender.hasPermission(getPermission("nbt"))) compound = new StorageTagCompound();

        if (target == null) {
            if (sender instanceof Player) {
                target = (Player) sender;
            }else{
                sendUsage(sender);
                return;
            }
        }

        StorageTagCompound finalCompound = compound;
        if (finalCompound.hasNoTags()) {
            for (PetData petData : type.getPetData()) {
                petData.getDefault(type).ifPresent(o -> {
                    finalCompound.set(petData.getNamespace().namespace(), o);
                });
            }
        }

        Player finalTarget = target;
        getPlugin().getUserManager().getPetUser(target.getUniqueId()).ifPresent(user -> {
            if (!user.canSpawnMorePets() && finalTarget == sender) {
                sender.sendMessage(MessageFile.getTranslation(MessageOption.CANT_SPAWN_MORE_PETS));
                return;
            }

            if ((!user.canSpawnMorePets())
                    && (!ConfigOption.INSTANCE.MISC_TOGGLES_CONSOLE_BYPASS_LIMIT.getValue())
                    && (!(sender instanceof Player))) {
                finalTarget.sendMessage(MessageFile.getTranslation(MessageOption.CANT_SPAWN_MORE_PETS));
                return;
            }

            if (finalTarget == sender) {
                // Will be treated like selecting a pet from the selection menu
                PetSelectTypeEvent event = new PetSelectTypeEvent(type, user);
                Bukkit.getServer().getPluginManager().callEvent(event);
                if (event.isCancelled()) return;
            }

            BiOptional<IEntityPet, String> entityPet = spawner.spawnEntityPet(type, user, finalCompound);
            if (!entityPet.isFirstPresent()) {
                if (entityPet.isSecondPresent()) {
                    Tellraw.fromLegacy(MessageFile.getTranslation(MessageOption.FAILED_SUMMON).replace("{type}", type.getName()))
                            .tooltip(entityPet.second().get()).send(sender);
                    return;
                }

                sender.sendMessage(MessageFile.getTranslation(MessageOption.FAILED_SUMMON).replace("{type}", type.getName()));
                return;
            }
            if (type == PetType.ARMOR_STAND) {
                ((IEntityControllerPet)entityPet.first().get()).getVisibleEntity().applyCompound(finalCompound);
            }
            sender.sendMessage(MessageFile.getTranslation(MessageOption.SUMMONED_PET).replace("{type}", type.getName()));
        });
    }
}
