package simplepets.brainsynder.commands.list;

import lib.brainsynder.commands.annotations.ICommand;
import lib.brainsynder.nbt.JsonToNBT;
import lib.brainsynder.nbt.NBTException;
import lib.brainsynder.nbt.StorageTagCompound;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.ISpawnUtil;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.plugin.SimplePets;
import simplepets.brainsynder.commands.PetSubCommand;
import simplepets.brainsynder.files.MessageFile;
import simplepets.brainsynder.files.MessageOption;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@ICommand(
        name = "summon",
        usage = "<pet> [player] [nbt]",
        alias = {"spawn"},
        description = "Spawns a pet for the player or selected player"
)
public class SummonCommand extends PetSubCommand {
    public SummonCommand(PetCore plugin) {
        super(plugin);

        registerCompletion(1, getPetTypes());
        registerCompletion(2, getOnlinePlayers());
    }

    @Override
    public void run(CommandSender sender, String[] args) {
        if (args.length == 0) {
            sendUsage(sender);
            return;
        }

        ISpawnUtil spawner = getPlugin().getSpawnUtil();
        if (spawner == null) return; //TODO:

        if (args[0].equalsIgnoreCase("all")) {
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

        if (!SimplePets.getSpawnUtil().isRegistered(type)) {
            sender.sendMessage(MessageFile.getTranslation(MessageOption.PET_NOT_REGISTERED).replace("{type}", type.getName()));
            return;
        }

        StorageTagCompound compound = new StorageTagCompound();
        if (args.length > 1) {
            int argStart = 1;

            if (isUsername(args[1])) {
                Player target = Bukkit.getPlayerExact(args[1]);
                if (target == null) {
                    sender.sendMessage(MessageFile.getTranslation(MessageOption.PLAYER_NOT_ONLINE).replace("{player}", args[1]));
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

        StorageTagCompound finalCompound = compound;
        getPlugin().getUserManager().getPetUser(((Player)sender).getUniqueId()).ifPresent(user -> {
            Optional<IEntityPet> entityPet = spawner.spawnEntityPet(type, user, finalCompound);
            if (!entityPet.isPresent()) {
                sender.sendMessage(MessageFile.getTranslation(MessageOption.FAILED_SUMMON).replace("{type}", type.getName()));
                return;
            }
            sender.sendMessage(MessageFile.getTranslation(MessageOption.SUMMONED_PET).replace("{type}", type.getName()));
        });



    }

    private boolean isUsername (String string) {
        return (string.length() < 17) && (string.length() > 2) && string.replace("_", "").matches("[A-Za-z0-9]+");
    }
}
