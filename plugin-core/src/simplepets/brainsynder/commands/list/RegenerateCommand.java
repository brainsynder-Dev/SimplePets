package simplepets.brainsynder.commands.list;

import com.google.common.collect.Lists;
import lib.brainsynder.commands.annotations.ICommand;
import org.bukkit.command.CommandSender;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.plugin.SimplePets;
import simplepets.brainsynder.commands.Permission;
import simplepets.brainsynder.commands.PetSubCommand;
import simplepets.brainsynder.files.MessageFile;
import simplepets.brainsynder.files.options.MessageOption;
import simplepets.brainsynder.impl.PetConfiguration;
import simplepets.brainsynder.managers.InventoryManager;
import simplepets.brainsynder.managers.ItemManager;

import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@ICommand(
    name = "regenerate",
    usage = "<selector> [type]",
    description = "Regenerates the a file/folder back to default (ignores plugin addons)"
)
@Permission(permission = "regenerate", adminCommand = true)
public class RegenerateCommand extends PetSubCommand {
    private final File petsFolder, inventoryFolder, itemFolder, particleFolder;

    public RegenerateCommand(PetCore plugin) {
        super(plugin);
        petsFolder = new File(plugin.getDataFolder() + File.separator + "Pets");
        inventoryFolder = new File(plugin.getDataFolder() + File.separator + "Inventories");
        itemFolder = new File(plugin.getDataFolder() + File.separator + "Items");
        particleFolder = new File(plugin.getDataFolder() + File.separator + "Particles");
    }

    @Override
    public List<String> handleCompletions(List<String> completions, CommandSender sender, int index, String[] args) {
        if (index == 1) {
            completions.addAll(Lists.newArrayList("pets", "inventories", "items", "particles", "type"));
            return completions;
        }
        if (args[0].equalsIgnoreCase("type") && (index == 2)) {
            completions.addAll(getPetTypes());
            return completions;
        }
        return completions;
    }

    @Override
    public void run(CommandSender sender, String[] args) {
        if (args.length == 0) {
            sendUsage(sender);
            return;
        }

        if (args[0].equalsIgnoreCase("pets")) {
            deleteFiles(petsFolder);
            getPlugin().getScheduler().getImpl().runLater(() -> {
                ((PetConfiguration) SimplePets.getPetConfigManager()).reset();
                sender.sendMessage(MessageFile.getTranslation(MessageOption.PET_FILES_REGEN));
            }, 100L, TimeUnit.MILLISECONDS);
            return;
        }

        if (args[0].equalsIgnoreCase("inventories")) {
            deleteFiles(inventoryFolder);
            getPlugin().getScheduler().getImpl().runLater(() -> {
                ((InventoryManager) getPlugin().getGUIHandler()).initiate();
                sender.sendMessage(MessageFile.getTranslation(MessageOption.INV_FILES_REGEN));
            }, 100L, TimeUnit.MILLISECONDS);
            return;
        }

        if (args[0].equalsIgnoreCase("items")) {
            deleteFiles(itemFolder);
            getPlugin().getScheduler().getImpl().runLater(() -> {
                ((ItemManager) getPlugin().getItemHandler()).initiate();
                sender.sendMessage(MessageFile.getTranslation(MessageOption.ITEM_FILES_REGEN));
            }, 100L, TimeUnit.MILLISECONDS);
            return;
        }

        if (args[0].equalsIgnoreCase("particles")) {
            deleteFiles(particleFolder);
            getPlugin().getScheduler().getImpl().runLater(() -> {
                getPlugin().getParticleHandler().reload(getPlugin());
                sender.sendMessage(MessageFile.getTranslation(MessageOption.PARTICLE_FILES_REGEN));
            }, 100L, TimeUnit.MILLISECONDS);
            return;
        }

        if (args[0].equalsIgnoreCase("type")) {
            if (args.length == 1) {
                sendUsage(sender);
                return;
            }

            Optional<PetType> optional = PetType.getPetType(args[1]);
            if (!optional.isPresent()) {
                sender.sendMessage(MessageFile.getTranslation(MessageOption.INVALID_PET_TYPE).replace("{arg}", args[1]));
                return;
            }

            PetType type = optional.get();
            File petFile = new File(petsFolder, type.getName() + ".json");
            petFile.delete();
            getPlugin().getScheduler().getImpl().runLater(() -> {
                ((PetConfiguration) SimplePets.getPetConfigManager()).reset(type);
                sender.sendMessage(MessageFile.getTranslation(MessageOption.PET_TYPE_FILE_REGEN).replace("{type}", type.getName()));
            }, 50L, TimeUnit.MILLISECONDS);
        }

        sendUsage(sender);
    }

    private void deleteFiles(File folder) {
        Lists.newArrayList(folder.listFiles()).forEach(file -> {
            if (file.isFile()) file.delete();
        });
    }
}
