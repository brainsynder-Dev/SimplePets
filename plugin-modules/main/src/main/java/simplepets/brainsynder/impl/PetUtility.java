package simplepets.brainsynder.impl;

import com.google.common.collect.Lists;
import org.bsdevelopment.pluginutils.text.Colorize;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.pet.CommandReason;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.plugin.SimplePets;
import simplepets.brainsynder.api.plugin.config.ConfigOption;
import simplepets.brainsynder.api.plugin.utils.IPetUtilities;
import simplepets.brainsynder.api.user.PetUser;

import java.util.List;

public class PetUtility implements IPetUtilities {
    public void runPetCommands(CommandReason reason, PetUser owner, PetType type) {
        runPetCommands(reason, owner, type, null);
    }

    public void runPetCommands(CommandReason reason, PetUser owner, PetType type, Location location) {
        SimplePets.getPetConfigManager().getPetConfig(type).ifPresent(config -> {
            List<String> commands = config.getCommands().getOrDefault(reason, Lists.newArrayList());
            IEntityPet pet = owner.getPetEntity(type).orElse(null);

            commands.forEach(command -> Bukkit.dispatchCommand(
                    Bukkit.getConsoleSender(),
                    handleCommand(owner, pet, pet != null ? null : location, command)
            ));
        });
    }

    private String handleCommand(PetUser owner, IEntityPet entity, Location petLoc, String command) {
        String text = handlePlaceholders(owner, entity, petLoc, command);
        if (text == null) return null;
        if (text.startsWith("/")) text = text.substring(1);
        if (text.contains("{petName}")) text = text.replace("{petName}", safePetName(entity));
        return text;
    }

    private String safePetName(IEntityPet entity) {
        if (entity == null || entity.getPetName().isEmpty()) return "Pet";

        String name = ChatColor.stripColor(entity.getPetName().get());
        name = name.replace("\r", "").replace("\n", "").replace("\t", "");
        name = name.replaceAll("[;&|`$<>\\\\]", "").replaceAll("\\s+", "_");
        name = name.replaceAll("[^A-Za-z0-9_\\-]", "_").replaceAll("_+", "_");

        if (name.isBlank()) name = "Pet";
        if (name.length() > 32) name = name.substring(0, 32);
        return name;
    }

    /**
     * This method replaces all the placeholders with the correct replacements
     * <p>
     * List of Placeholders
     * <ul>
     *     <li>{petX} - The pets X coordinate</li>
     *     <li>{petY} - The pets Y coordinate</li>
     *     <li>{petZ} - The pets Z coordinate</li>
     *     <li>{ownerX} - The pet owners X coordinate</li>
     *     <li>{ownerY} - The pet owners Y coordinate</li>
     *     <li>{ownerZ} - The pet owners Z coordinate</li>
     *     <li>{ownerName} - The pet owners name</li>
     *     <li>{petName} - The pets current name</li>
     *     <li>{petType} - The type of pet</li>
     *     <li>{petUUID} - The UUID of pet</li>
     * </ul>
     *
     * @param owner  The owner of the pet
     * @param entity The pet the commands are run for
     * @param text   The command that is being run
     * @return | Returns the command with all the replaced placeholders
     */
    @Override
    public String handlePlaceholders(PetUser owner, IEntityPet entity, Location petLoc, String text) {
        if (text == null || owner == null) return text;

        var player = owner.getPlayer();
        Location ownerLoc = player != null ? player.getLocation() : null;

        if (petLoc == null && entity != null && entity.getEntity() != null) petLoc = entity.getEntity().getLocation();

        String petType = "Unknown", petUuid = "Unknown", petName = "Pet";
        if (entity != null && entity.getEntity() != null) {
            var type = entity.getPetType();
            if (type != null) petType = type.getName();
            petUuid = entity.getEntity().getUniqueId().toString();
            if (entity.getPetName().isPresent()) petName = entity.getPetName().get();
        }

        if (petLoc != null) text = text.replace("{petX}", String.valueOf(petLoc.getX()))
                .replace("{petY}", String.valueOf(petLoc.getY()))
                .replace("{petZ}", String.valueOf(petLoc.getZ()));

        if (ownerLoc != null) text = text.replace("{ownerX}", String.valueOf(ownerLoc.getX()))
                .replace("{ownerY}", String.valueOf(ownerLoc.getY()))
                .replace("{ownerZ}", String.valueOf(ownerLoc.getZ()));

        return text.replace("{ownerName}", owner.getOwnerName())
                .replace("{petType}", petType)
                .replace("{petUUID}", petUuid)
                .replace("{petName}", petName);
    }

    @Override
    public String translatePetName(String name) {
        boolean color = ConfigOption.RENAME_COLOR_ENABLED.get();
        boolean magic = ConfigOption.RENAME_COLOR_MAGIC.get();
        if (!magic) name = name.replace("&k", "");
        if (!color) return name;

        if (ConfigOption.RENAME_COLOR_HEX.get()) {
            name = Colorize.translateBungeeHex(name);
        } else {
            name = Colorize.translateBukkit(name);
        }
        return name;
    }
}
