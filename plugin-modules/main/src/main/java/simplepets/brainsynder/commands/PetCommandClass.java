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
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.plugin.SimplePets;
import simplepets.brainsynder.api.plugin.config.ConfigOption;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.utils.Utilities;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    Argument<StorageTagCompound> PET_NBT = new StorageTagArgument("nbt")
            .replaceSuggestions(ArgumentSuggestions.of(info -> {
                List<String> suggestions = new ArrayList<>();
                suggestions.add("{}");

                // Determine target player: prefer "player" from previous args, fallback to sender
                Player player = null;
                if (info.previousArgs() != null && info.previousArgs().has("player")) {
                    player = info.previousArgs().get("player");
                } else if (info.sender() instanceof Player p) {
                    player = p;
                }
                if (player == null) return suggestions;

                // Determine pet type from previous args
                PetType type = info.previousArgs() != null ? info.previousArgs().get("type") : null;
                if (type == null || type == PetType.UNKNOWN) return suggestions;

                // Find the spawned pet and use its compound as the template suggestion
                Optional<PetUser> user = SimplePets.getUserManager().getPetUser(player);
                if (user.isEmpty()) return suggestions;

                user.get().getPetEntity(type).ifPresent(entityPet -> {
                    String compoundStr = entityPet.asCompound().toString();
                    if (!compoundStr.equals("{}")) suggestions.add(compoundStr);
                });

                return suggestions;
            }));
}
