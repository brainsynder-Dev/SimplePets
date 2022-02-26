package simplepets.brainsynder.commands.list;

import lib.brainsynder.commands.annotations.ICommand;
import lib.brainsynder.nbt.JsonToNBT;
import lib.brainsynder.nbt.StorageTagCompound;
import lib.brainsynder.nbt.other.NBTException;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.entity.misc.IEntityControllerPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.plugin.SimplePets;
import simplepets.brainsynder.commands.Permission;
import simplepets.brainsynder.commands.PetSubCommand;
import simplepets.brainsynder.files.MessageFile;
import simplepets.brainsynder.files.options.MessageOption;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@ICommand(
        name = "modify",
        usage = "[player] <type> <nbt>",
        description = "Modify the Selected Players' Pet."
)
@Permission(permission = "modify", additionalPermissions = {"other"})
public class ModifyCommand extends PetSubCommand {
    public ModifyCommand(PetCore plugin) {
        super(plugin);
    }

    @Override
    public void run(CommandSender sender, String[] args) {
        if ((args.length == 0) || (args.length == 1) || (args.length == 2)) {
            sendUsage(sender);
            return;
        }
        AtomicInteger argStart = new AtomicInteger (0);

        Player target = null;
        if (isUsername(args[argStart.get()]) && sender.hasPermission(getPermission("other"))) {
            Player selected = Bukkit.getPlayerExact(args[argStart.get()]);
            if (selected != null) {
                target = selected;
                if (!sender.hasPermission(getPermission("other"))) {
                    sender.sendMessage(MessageFile.getTranslation(MessageOption.NO_PERMISSION));
                    return;
                }
                argStart.getAndIncrement();
            }
        }

        if (target == null) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED+"You must be a player to run this command for yourself.");
            }else{
                target = (Player) sender;
            }
        }

        SimplePets.getUserManager().getPetUser(target).ifPresent(user -> {

            Optional<PetType> petType = PetType.getPetType(args[argStart.get()]);
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
            String json = messageMaker(args, argStart.get()+1).replace(" ", "~");
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
                if (entityPet instanceof IEntityControllerPet) entityPet = ((IEntityControllerPet)entityPet).getVisibleEntity();
                try {
                    entityPet.applyCompound(compound);
                    sender.sendMessage(MessageFile.getTranslation(MessageOption.MODIFY_APPLIED).replace("{type}", type.getName()));
                }catch (Exception e) {
                    sender.sendMessage(MessageFile.getTranslation(MessageOption.INVALID_NBT));

                    String errorMessage = MessageFile.getTranslation(MessageOption.INVALID_NBT_MESSAGE)
                            .replace("{message}", e.getMessage().replaceAll("(?i):0b", ":false").replaceAll("(?i):1b", ":true"));
                    if (!errorMessage.isEmpty()) sender.sendMessage(errorMessage);
                }
            });
        });


    }
}
