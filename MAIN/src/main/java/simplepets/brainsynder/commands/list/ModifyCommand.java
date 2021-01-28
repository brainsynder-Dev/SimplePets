package simplepets.brainsynder.commands.list;

import lib.brainsynder.commands.annotations.ICommand;
import lib.brainsynder.nbt.JsonToNBT;
import lib.brainsynder.nbt.NBTException;
import lib.brainsynder.nbt.StorageTagCompound;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.plugin.SimplePets;
import simplepets.brainsynder.commands.PetSubCommand;
import simplepets.brainsynder.files.MessageFile;
import simplepets.brainsynder.files.options.MessageOption;

import java.util.Optional;

@ICommand(
        name = "modify",
        usage = "<player> <type> <json>",
        description = "Modify the Selected Players' Pet."
)
public class ModifyCommand extends PetSubCommand {
    public ModifyCommand(PetCore plugin) {
        super(plugin);

        registerCompletion(1, getOnlinePlayers());
        registerCompletion(2, getPetTypes());
        //registerCompletion(3, Arrays.asList("{}"));
    }

    @Override
    public void run(CommandSender sender, String[] args) {
        if ((args.length == 0) || (args.length == 1) || (args.length == 2)) {
            sendUsage(sender);
            return;
        }

        Player target = Bukkit.getPlayerExact(args[0]);
        if (target == null) {
            sender.sendMessage("player not online");
            return;
        }

        SimplePets.getUserManager().getPetUser(target).ifPresent(user -> {

            Optional<PetType> petType = PetType.getPetType(args[1]);
            if (!petType.isPresent()) {
                sender.sendMessage(MessageFile.getTranslation(MessageOption.INVALID_PET_TYPE).replace("{arg}", args[1]));
                return;
            }

            PetType type = petType.get();

            if (!SimplePets.getSpawnUtil().isRegistered(type)) {
                sender.sendMessage(MessageFile.getTranslation(MessageOption.PET_NOT_REGISTERED).replace("{type}", type.getName()));
                return;
            }

            StorageTagCompound compound;
            String json = messageMaker(args, 2).replace(" ", "~");
            json = formatJson(json);


            // This should help fix the issue with booleans not working for the command.
            if (json.toLowerCase().contains(":true")){
                json = json.replaceAll("(?i):true", ":1b");
            }if (json.toLowerCase().contains(":false")){
                json = json.replaceAll("(?i):false", ":0b");
            }

            try {
                compound = JsonToNBT.getTagFromJson(json);
            } catch (NBTException e) {
                sender.sendMessage(MessageFile.getTranslation(MessageOption.INVALID_NBT));

                String message = MessageFile.getTranslation(MessageOption.INVALID_NBT_MESSAGE)
                        .replace("{message}", e.getMessage().replaceAll("(?i):0b", ":false").replaceAll("(?i):1b", ":true"));
                if (!message.isEmpty()) sender.sendMessage(message);
                return;
            }

            String message = MessageFile.getTranslation(MessageOption.MODIFY_COMPOUND).replace("{compound}", compound.toString());
            if (!message.isEmpty()) sender.sendMessage(message.replaceAll("(?i):0b", ":false").replaceAll("(?i):1b", ":true"));
            user.getPetEntity(type).ifPresent(entityPet -> {
                sender.sendMessage(MessageFile.getTranslation(MessageOption.MODIFY_APPLIED).replace("{type}", type.getName()));
                entityPet.applyCompound(compound);
            });
        });


    }
}
