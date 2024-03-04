package simplepets.brainsynder.commands.list;

import lib.brainsynder.commands.annotations.ICommand;
import lib.brainsynder.files.JsonFile;
import lib.brainsynder.json.JsonValue;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.pet.IPetConfig;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.plugin.SimplePets;
import simplepets.brainsynder.commands.Permission;
import simplepets.brainsynder.commands.PetSubCommand;
import simplepets.brainsynder.files.MessageFile;
import simplepets.brainsynder.files.options.MessageOption;
import simplepets.brainsynder.impl.PetConfiguration;
import simplepets.brainsynder.managers.InventoryManager;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@ICommand(
        name = "petconfig",
        usage = "<type> <key> <value|reset>",
        description = "Modify settings for the selected pet type."
)
@Permission(permission = "petconfig", adminCommand = true)
public class PetConfigCommand extends PetSubCommand {
    public PetConfigCommand(PetCore plugin) {
        super(plugin);
    }

    @Override
    public List<String> handleCompletions(List<String> completions, CommandSender sender, int index, String[] args) {
        if (index == 1) {
            for (PetType type : PetType.values()) {
                completions.add(type.getName());
            }
            return completions;
        }

        if (index == 2) {
            PetType.getPetType(args[0]).ifPresent(type -> {
                SimplePets.getPetConfigManager().getPetConfig(type).ifPresent(iPetConfig -> {
                    PetConfiguration.PetConfig config = (PetConfiguration.PetConfig) iPetConfig;
                    JsonFile jsonFile = config.getJSON();

                    jsonFile.getKeys().forEach(key -> {
                        boolean value = canModifyKey(key, iPetConfig);
                        if (value) completions.add(key);
                    });
                });
            });
            return completions;
        }

        if (index == 3) {
            completions.add("reset");
            PetType.getPetType(args[0]).ifPresent(type -> {
                SimplePets.getPetConfigManager().getPetConfig(type).ifPresent(iPetConfig -> {
                    PetConfiguration.PetConfig config = (PetConfiguration.PetConfig) iPetConfig;
                    JsonFile jsonFile = config.getJSON();

                    String key = args[1];

                    String defaultValue = jsonFile.getDefaultValue(key).toString();
                    String current = jsonFile.getValue(key).toString();

                    completions.add(defaultValue);
                    if (!defaultValue.equalsIgnoreCase(current)) completions.add(current);
                });
            });
            return completions;
        }
        return super.handleCompletions(completions, sender, index, args);
    }

    @Override
    public void run(CommandSender sender, String[] args) {
        if ((args.length == 0) || (args.length == 1) || (args.length == 2)) {
            sendUsage(sender);
            return;
        }

        Optional<PetType> petType = PetType.getPetType(args[0]);
        if (petType.isEmpty()) {
            sender.sendMessage(MessageFile.getTranslation(MessageOption.INVALID_PET_TYPE).replace("{arg}", args[0]));
            return;
        }
        PetType type = petType.get();

        String key = args[1];
        String value = args[2];
        modifyPetConfig(sender, type, key, value);
    }

    private boolean canModifyKey(String key, IPetConfig iPetConfig) {
        PetConfiguration.PetConfig config = (PetConfiguration.PetConfig) iPetConfig;

        JsonFile jsonFile = config.getJSON();
        if (!jsonFile.hasKey(key)) {
            return false;
        }

        JsonValue original = jsonFile.getValue(key);
        return (original.isBoolean() || original.isNumber() || original.isString());
    }

    private void modifyPetConfig(CommandSender sender, PetType type, String key, String newValue) {
        SimplePets.getPetConfigManager().getPetConfig(type).ifPresent(iPetConfig -> {
            PetConfiguration.PetConfig config = (PetConfiguration.PetConfig) iPetConfig;

            JsonFile jsonFile = config.getJSON();
            if (!jsonFile.hasKey(key)) {
                sender.sendMessage(MessageFile.getTranslation(MessageOption.CONFIG_UNKNOWN_KEY)
                        .replace("{key}", key));
                return;
            }

            JsonValue original = jsonFile.getDefaultValue(key);
            boolean updated = false, reset = false;

            if (newValue.equalsIgnoreCase("reset")) {
                jsonFile.set(key, jsonFile.getDefaultValue(key));
                updated = true;
                reset = true;
            } else if (original.isBoolean()) {
                if ("true".equalsIgnoreCase(newValue) || "false".equalsIgnoreCase(newValue)) {
                    jsonFile.set(key, Boolean.parseBoolean(newValue));
                    updated = true;
                } else {
                    sender.sendMessage(MessageFile.getTranslation(MessageOption.CONFIG_INVALID_BOOLEAN)
                            .replace("{key}", key)
                            .replace("{value}", newValue));
                    return;
                }

            } else if (original.isNumber()) {
                if (original.toString().contains(".")) {
                    try {
                        double value = Double.parseDouble(newValue);
                        jsonFile.set(key, value);
                        updated = true;
                    } catch (NumberFormatException e) {
                        sender.sendMessage(MessageFile.getTranslation(MessageOption.CONFIG_INVALID_DOUBLE)
                                .replace("{key}", key)
                                .replace("{value}", newValue));
                        return;
                    }
                }else{
                    try {
                        int value = Integer.parseInt(newValue);
                        jsonFile.set(key, value);
                        updated = true;
                    } catch (NumberFormatException e) {
                        sender.sendMessage(MessageFile.getTranslation(MessageOption.CONFIG_INVALID_INT)
                                .replace("{key}", key)
                                .replace("{value}", newValue));
                        return;
                    }
                }
            } else if (original.isString()) {
                jsonFile.set(key, newValue);
                updated = true;
            }


            if (updated) {
                jsonFile.save();

                boolean finalReset = reset;
                getPlugin().getScheduler().getImpl().runLater(() -> {
                    // Reloads the data to use the new values
                    ((PetConfiguration)PetCore.getInstance().getPetConfigManager()).reset();
                    InventoryManager.SELECTION.reloadAvailableTypes();

                    if (finalReset) {
                        sender.sendMessage(MessageFile.getTranslation(MessageOption.CONFIG_VALUE_RESET)
                                .replace("{key}", key)
                                .replace("{value}", jsonFile.getDefaultValue(key).toString())
                                .replace("{type}", type.getName()));
                    }else{
                        sender.sendMessage(MessageFile.getTranslation(MessageOption.CONFIG_VALUE_UPDATED)
                                .replace("{key}", key)
                                .replace("{value}", newValue)
                                .replace("{type}", type.getName()));
                    }
                }, 250L, TimeUnit.MILLISECONDS);
            } else {
                sender.sendMessage(MessageFile.getTranslation(MessageOption.CONFIG_UNABLE_TO_UPDATE)
                        .replace("{key}", key)
                        .replace("{value}", newValue));
            }
        });
    }
}
