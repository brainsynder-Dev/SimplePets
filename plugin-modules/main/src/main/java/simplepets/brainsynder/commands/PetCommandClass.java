package simplepets.brainsynder.commands;

import org.bsdevelopment.nbt.StorageTagCompound;
import org.bsdevelopment.pluginutils.command.CommandClass;
import org.bsdevelopment.pluginutils.command.arguments.Argument;
import org.bsdevelopment.pluginutils.command.arguments.CustomArgument;
import org.bsdevelopment.pluginutils.command.arguments.CustomArgument.CustomArgumentParser;
import org.bsdevelopment.pluginutils.command.arguments.StorageTagArgument;
import org.bsdevelopment.pluginutils.command.arguments.StringArgument;
import org.bsdevelopment.pluginutils.command.arguments.suggestions.ArgumentSuggestions;
import org.bsdevelopment.pluginutils.command.exception.ArgumentParseException;
import org.bukkit.entity.Player;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.pet.IPetConfig;
import simplepets.brainsynder.api.pet.PetData;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.plugin.SimplePets;
import simplepets.brainsynder.api.plugin.config.ConfigOption;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.utils.Utilities;

import java.util.*;

public interface PetCommandClass extends CommandClass {
    CustomArgumentParser<PetType> ALL_PET_TYPES_PARSER = info -> {
        try {
            PetType type = PetType.getPetType(info.input().toUpperCase()).orElse(PetType.UNKNOWN);
            if (type == PetType.UNKNOWN)
                throw ArgumentParseException.fromString("Unknown pet type: " + info.input());
            Optional<IPetConfig> config = SimplePets.getPetConfigManager().getPetConfig(type);

            if (config.isPresent() && !config.get().isEnabled())
                throw ArgumentParseException.fromString("That pet type is disabled: " + info.input());
            if (!type.isSupported())
                throw ArgumentParseException.fromString("That pet type is not supported: " + info.input());
            if (!SimplePets.getSpawnUtil().isRegistered(type))
                throw ArgumentParseException.fromString("That pet type is not registered: " + info.input());

            return type;
        } catch (IllegalArgumentException e) {
            throw ArgumentParseException.fromString("Invalid pet type: " + info.input());
        }
    };

    ArgumentSuggestions ALL_PET_TYPES_SUGGESTIONS = ArgumentSuggestions.of(info -> {
        List<String> suggestions = new ArrayList<>();
        for (PetType type : PetType.values()) {
            if (type == PetType.UNKNOWN) continue;
            Optional<IPetConfig> config = SimplePets.getPetConfigManager().getPetConfig(type);
            if (config.isPresent() && !config.get().isEnabled()) continue;
            if (!type.isSupported()) continue;
            if (!SimplePets.getSpawnUtil().isRegistered(type)) continue;

            suggestions.add(type.getName());
        }
        return suggestions;
    });

    Argument<PetType> ALL_PET_TYPES = new CustomArgument<>(new StringArgument("type"), ALL_PET_TYPES_PARSER)
            .replaceSuggestions(ALL_PET_TYPES_SUGGESTIONS);

    Argument<PetType> OPTIONAL_PET_TYPES = new CustomArgument<>(new StringArgument("type"), ALL_PET_TYPES_PARSER)
            .replaceSuggestions(ALL_PET_TYPES_SUGGESTIONS)
            .setOptional(true);

    Argument<PetType> ACCESSIBLE_PET_TYPES = new CustomArgument<>(new StringArgument("type"), info -> {
        try {
            PetType type = PetType.getPetType(info.input().toUpperCase()).orElse(PetType.UNKNOWN);
            if (type == PetType.UNKNOWN)
                throw ArgumentParseException.fromString("Unknown pet type: " + info.input());
            Optional<IPetConfig> config = SimplePets.getPetConfigManager().getPetConfig(type);
            if (config.isPresent() && !config.get().isEnabled())
                throw ArgumentParseException.fromString("That pet type is disabled: " + info.input());
            if (!type.isSupported())
                throw ArgumentParseException.fromString("That pet type is not supported: " + info.input());
            if (!SimplePets.getSpawnUtil().isRegistered(type))
                throw ArgumentParseException.fromString("That pet type is not registered: " + info.input());
            if (info.sender() instanceof Player player) {
                Optional<PetUser> user = SimplePets.getUserManager().getPetUser(player);
                boolean purchased = user.isPresent()
                        && user.get().getOwnedPets().contains(type)
                        && ConfigOption.UTILIZE_PURCHASED_PETS.get();
                if (!purchased && !Utilities.hasPermission(player, type.getPermission()))
                    throw ArgumentParseException.fromString("You do not have access to: " + info.input());
            }
            return type;
        } catch (IllegalArgumentException e) {
            throw ArgumentParseException.fromString("Invalid pet type: " + info.input());
        }
    }).replaceSuggestions(ArgumentSuggestions.of(info -> {
        List<String> suggestions = new ArrayList<>();
        for (PetType type : PetType.values()) {
            if (type == PetType.UNKNOWN) continue;
            Optional<IPetConfig> config = SimplePets.getPetConfigManager().getPetConfig(type);
            if (config.isPresent() && !config.get().isEnabled()) continue;
            if (!type.isSupported()) continue;
            if (!SimplePets.getSpawnUtil().isRegistered(type)) continue;
            if (info.sender() instanceof Player player) {
                Optional<PetUser> user = SimplePets.getUserManager().getPetUser(player);
                boolean purchased = user.isPresent()
                        && user.get().getOwnedPets().contains(type)
                        && ConfigOption.UTILIZE_PURCHASED_PETS.get();
                if (!purchased && !Utilities.hasPermission(player, type.getPermission())) continue;
            }
            suggestions.add(type.getName());
        }
        return suggestions;
    }));


    Argument<String> CLOUD_ADDONS = new CustomArgument<>(new StringArgument("addon"), info -> {
        List<String> cloudSuggestions = new ArrayList<>();
        PetCore.getInstance().getAddonManager().getCloudAddons().forEach(cloudData -> cloudSuggestions.add(cloudData.getName()));
        if (cloudSuggestions.contains(info.input())) return info.input();
        throw ArgumentParseException.fromString("Addon not found: " + info.input());
    }).replaceSuggestions(ArgumentSuggestions.of(info -> {
        List<String> cloudSuggestions = new ArrayList<>();
        PetCore.getInstance().getAddonManager().getCloudAddons().forEach(cloudData -> cloudSuggestions.add(cloudData.getName()));
        return cloudSuggestions;
    }));

    Argument<String> LOCAL_ADDONS = new CustomArgument<>(new StringArgument("addon"), info -> {
        List<String> localSuggestions = new ArrayList<>();
        PetCore.getInstance().getAddonManager().getLocalDataMap().keySet().forEach(localData -> localSuggestions.add(localData.getName()));
        if (localSuggestions.contains(info.input())) return info.input();
        throw ArgumentParseException.fromString("Addon not found: " + info.input());
    }).replaceSuggestions(ArgumentSuggestions.of(info -> {
        List<String> localSuggestions = new ArrayList<>();
        PetCore.getInstance().getAddonManager().getLocalDataMap().keySet().forEach(localData -> localSuggestions.add(localData.getName()));
        return localSuggestions;
    }));

    ArgumentSuggestions PET_NBT_SUGGESTIONS = ArgumentSuggestions.of(info -> {
        PetType type = info.previousArgs() != null ? info.previousArgs().get("type") : null;
        if (type == null || type == PetType.UNKNOWN) return List.of("{}");
        return petDataSuggestions(type, info.currentInput());
    });

    Argument<StorageTagCompound> PET_NBT = new StorageTagArgument("nbt")
            .replaceSuggestions(PET_NBT_SUGGESTIONS);

    static List<String> petDataSuggestions(PetType type, String current) {
        List<String> suggestions = new ArrayList<>();
        String input = (current == null) ? "" : current;

        if (!input.startsWith("{")) {
            suggestions.add("{}");
            for (PetData data : type.getPetData()) {
                if (data.isVersionSupported()) suggestions.add("{" + data.namespace());
            }
            return suggestions;
        }

        int separator = lastTopLevelSeparator(input);
        String existing = input.substring(0, separator + 1);
        String typing = input.substring(separator + 1);
        int colon = typing.indexOf(':');

        if (colon < 0) {
            Set<String> usedKeys = topLevelKeys(input);
            for (PetData data : type.getPetData()) {
                if (!data.isVersionSupported()) continue;
                String key = data.namespace();
                if (usedKeys.contains(key)) continue;
                if (!key.toLowerCase().startsWith(typing.toLowerCase())) continue;
                suggestions.add(existing + key);
            }
            return suggestions;
        }

        String key = typing.substring(0, colon);
        String typedValue = typing.substring(colon + 1).toLowerCase();
        for (PetData data : type.getPetData()) {
            if (!data.isVersionSupported() || !data.namespace().equals(key)) continue;
            for (Object entry : data.getDefaultItems().keySet()) {
                String value = String.valueOf(entry);
                if (!value.toLowerCase().startsWith(typedValue)) continue;
                suggestions.add(existing + key + ":" + value);
            }
        }
        return suggestions;
    }

    private static int lastTopLevelSeparator(String input) {
        int depth = 0;
        int separator = 0;
        boolean quoted = false;
        char quote = 0;

        for (int index = 0; index < input.length(); index++) {
            char character = input.charAt(index);
            if (quoted) {
                if (character == '\\') index++;
                else if (character == quote) quoted = false;
                continue;
            }
            if (character == '"' || character == '\'') {
                quoted = true;
                quote = character;
            } else if (character == '{' || character == '[') {
                if (character == '{' && depth == 0) separator = index;
                depth++;
            } else if (character == '}' || character == ']') {
                depth--;
            } else if (character == ',' && depth == 1) {
                separator = index;
            }
        }
        return separator;
    }

    private static Set<String> topLevelKeys(String input) {
        Set<String> keys = new LinkedHashSet<>();
        int depth = 0;
        int keyStart = -1;
        boolean quoted = false;
        char quote = 0;

        for (int index = 0; index < input.length(); index++) {
            char character = input.charAt(index);
            if (quoted) {
                if (character == '\\') index++;
                else if (character == quote) quoted = false;
                continue;
            }
            if (character == '"' || character == '\'') {
                quoted = true;
                quote = character;
            } else if (character == '{' || character == '[') {
                if (character == '{' && depth == 0) keyStart = index + 1;
                depth++;
            } else if (character == '}' || character == ']') {
                depth--;
            } else if (character == ':' && depth == 1 && keyStart >= 0) {
                String key = input.substring(keyStart, index).trim();
                if (!key.isEmpty()) keys.add(key);
                keyStart = -1;
            } else if (character == ',' && depth == 1) {
                keyStart = index + 1;
            }
        }
        return keys;
    }
}
