package simplepets.brainsynder.commands.list;

import org.bsdevelopment.pluginutils.PluginUtilities;
import org.bsdevelopment.pluginutils.command.CommandBuilder;
import org.bsdevelopment.pluginutils.command.arguments.CustomArgument;
import org.bsdevelopment.pluginutils.command.arguments.StringArgument;
import org.bsdevelopment.pluginutils.command.arguments.suggestions.ArgumentSuggestions;
import org.bsdevelopment.pluginutils.command.exception.ArgumentParseException;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.plugin.SimplePets;
import simplepets.brainsynder.api.plugin.config.MessageOption;
import simplepets.brainsynder.commands.PetCommandClass;
import simplepets.brainsynder.impl.PetConfiguration;
import simplepets.brainsynder.managers.InventoryManager;
import simplepets.brainsynder.managers.ItemManager;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class RegenerateCommand implements PetCommandClass {

    @Override
    public CommandBuilder build() {
        return CommandBuilder.create("regenerate")
                .withPermission("pet.commands.regenerate")
                .withDescription("Regenerates a file/folder back to default (ignores plugin addons)")
                .withSubcommand(buildPetsCommand())
                .withSubcommand(buildInventoriesCommand())
                .withSubcommand(buildItemsCommand())
                .withSubcommand(buildParticlesCommand())
                .withSubcommand(buildTypeCommand())
                .executes((sender, args) -> {
                    sender.sendMessage(PetCore.getInstance().getMessageFile().getTranslation(MessageOption.PREFIX)
                            + " §cUsage: /pet regenerate <pets|inventories|items|particles|type> [petType]");
                });
    }

    public CommandBuilder buildPetsCommand() {
        return CommandBuilder.create("pets")
                .withPermission("pet.commands.regenerate")
                .withDescription("Regenerates all pet config files")
                .executes((sender, args) -> {
                    PetCore plugin = PetCore.getInstance();
                    File petsFolder = new File(plugin.getDataFolder() + File.separator + "Pets");
                    deleteFiles(petsFolder);
                    PluginUtilities.getScheduler().runTaskLater(() -> {
                        ((PetConfiguration) SimplePets.getPetConfigManager()).reset();
                        sender.sendMessage(plugin.getMessageFile().getTranslation(MessageOption.PET_FILES_REGEN));
                    }, 2);
                });
    }

    public CommandBuilder buildInventoriesCommand() {
        return CommandBuilder.create("inventories")
                .withPermission("pet.commands.regenerate")
                .withDescription("Regenerates all inventory files")
                .executes((sender, args) -> {
                    PetCore plugin = PetCore.getInstance();
                    File inventoryFolder = new File(plugin.getDataFolder() + File.separator + "Inventories");
                    deleteFiles(inventoryFolder);
                    PluginUtilities.getScheduler().runTaskLater(() -> {
                        ((InventoryManager) plugin.getGUIHandler()).initiate();
                        sender.sendMessage(plugin.getMessageFile().getTranslation(MessageOption.INV_FILES_REGEN));
                    }, 2);
                });
    }

    public CommandBuilder buildItemsCommand() {
        return CommandBuilder.create("items")
                .withPermission("pet.commands.regenerate")
                .withDescription("Regenerates all item files")
                .executes((sender, args) -> {
                    PetCore plugin = PetCore.getInstance();
                    File itemFolder = new File(plugin.getDataFolder() + File.separator + "Items");
                    deleteFiles(itemFolder);
                    PluginUtilities.getScheduler().runTaskLater(() -> {
                        ((ItemManager) plugin.getItemHandler()).initiate();
                        sender.sendMessage(plugin.getMessageFile().getTranslation(MessageOption.ITEM_FILES_REGEN));
                    }, 2);
                });
    }

    public CommandBuilder buildParticlesCommand() {
        return CommandBuilder.create("particles")
                .withPermission("pet.commands.regenerate")
                .withDescription("Regenerates all particle files")
                .executes((sender, args) -> {
                    PetCore plugin = PetCore.getInstance();
                    File particleFolder = new File(plugin.getDataFolder() + File.separator + "Particles");
                    deleteFiles(particleFolder);
                    PluginUtilities.getScheduler().runTaskLater(() -> {
                        plugin.getParticleHandler().reload(plugin);
                        sender.sendMessage(plugin.getMessageFile().getTranslation(MessageOption.PARTICLE_FILES_REGEN));
                    }, 2);
                });
    }

    public CommandBuilder buildTypeCommand() {
        return CommandBuilder.create("type")
                .withPermission("pet.commands.regenerate")
                .withDescription("Regenerates the config file for a specific pet type")
                .withArguments(new CustomArgument<>(new StringArgument("petType"), info -> {
                    Optional<PetType> optional = PetType.getPetType(info.input());
                    if (optional.isEmpty())
                        throw ArgumentParseException.fromString("Invalid pet type: " + info.input());
                    return optional.get();
                }).replaceSuggestions(ArgumentSuggestions.of(info -> {
                    List<String> list = new ArrayList<>();
                    for (PetType type : PetType.values()) {
                        if (type == PetType.UNKNOWN) continue;
                        list.add(type.getName());
                    }
                    return list;
                })))
                .executes((sender, args) -> {
                    PetCore plugin = PetCore.getInstance();
                    PetType type = args.get("petType");
                    File petsFolder = new File(plugin.getDataFolder() + File.separator + "Pets");
                    File petFile = new File(petsFolder, type.getName() + ".json");
                    petFile.delete();
                    PluginUtilities.getScheduler().runTaskLater(() -> {
                        ((PetConfiguration) SimplePets.getPetConfigManager()).reset(type);
                        sender.sendMessage(plugin.getMessageFile().getTranslation(MessageOption.PET_TYPE_FILE_REGEN)
                                .replace("{type}", type.getName()));
                    }, 1);
                });
    }

    private void deleteFiles(File folder) {
        if (!folder.exists()) return;
        File[] files = folder.listFiles();
        if (files == null) return;
        Arrays.stream(files).filter(File::isFile).forEach(File::delete);
    }
}
