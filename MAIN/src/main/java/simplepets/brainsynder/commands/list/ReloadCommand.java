package simplepets.brainsynder.commands.list;

import com.google.common.collect.Lists;
import lib.brainsynder.commands.annotations.ICommand;
import org.bukkit.command.CommandSender;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.commands.Permission;
import simplepets.brainsynder.commands.PetSubCommand;
import simplepets.brainsynder.files.MessageFile;
import simplepets.brainsynder.files.options.MessageOption;
import simplepets.brainsynder.managers.InventoryManager;
import simplepets.brainsynder.managers.ItemManager;

import java.util.List;

@ICommand(
        name = "reload",
        usage = "[selector]",
        description = "Reloads a selected file/folder"
)
@Permission(permission = "reload", adminCommand = true)
public class ReloadCommand extends PetSubCommand {
    public ReloadCommand(PetCore plugin) {
        super(plugin);
    }

    @Override
    public List<String> handleCompletions(List<String> completions, CommandSender sender, int index, String[] args) {
        if (index == 1) {
            completions.addAll(Lists.newArrayList("config", "messages", "inventories", "particles", "pets"));
        }
        return completions;
    }

    public void run(CommandSender sender, String[] args) {
        String type = "";
        if (args.length > 0) {
            type = args[0].toLowerCase();
        }

        switch (type) {
            case "config":
                getPlugin().getConfiguration().reload();
                getPlugin().getConfiguration().initValues();
                sender.sendMessage(MessageFile.getTranslation(MessageOption.CONFIG_RELOADED));
                break;
            case "messages":
                MessageFile.getFile().reload();
                sender.sendMessage(MessageFile.getTranslation(MessageOption.MESSAGES_RELOADED));
                break;
            case "inventories":
                ((InventoryManager) getPlugin().getGUIHandler()).initiate();
                ((ItemManager) getPlugin().getItemHandler()).initiate();
                sender.sendMessage(MessageFile.getTranslation(MessageOption.INVENTORIES_RELOADED));
                break;
            case "particles":
                getPlugin().getParticleHandler().reload(getPlugin());
                sender.sendMessage(MessageFile.getTranslation(MessageOption.PARTICLES_RELOADED));
                break;
            case "pets":
                getPlugin().reloadPetConfigManager();
                InventoryManager.SELECTION.reloadAvailableTypes();
                sender.sendMessage(MessageFile.getTranslation(MessageOption.PETS_RELOADED));
                break;
            default:
                getPlugin().getConfiguration().reload();
                getPlugin().getConfiguration().initValues();
                MessageFile.getFile().reload();
                getPlugin().getParticleHandler().reload(getPlugin());
                getPlugin().reloadPetConfigManager();
                ((InventoryManager) getPlugin().getGUIHandler()).initiate();
                ((ItemManager) getPlugin().getItemHandler()).initiate();
                sender.sendMessage(MessageFile.getTranslation(MessageOption.ALL_RELOADED));
                break;
        }


    }
}
