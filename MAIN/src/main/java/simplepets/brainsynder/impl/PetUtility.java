package simplepets.brainsynder.impl;

import com.google.common.collect.Lists;
import lib.brainsynder.utils.Colorize;
import org.bukkit.Bukkit;
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
            if (owner.getPetEntity(type).isPresent()) {
                commands.forEach(command -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), handlePlaceholders(owner, owner.getPetEntity(type).get(), null, command)));
                return;
            }
            commands.forEach(command -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), handlePlaceholders(owner, null, location, command)));
        });

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
     * @param owner   The owner of the pet
     * @param entity  The pet the commands are run for
     * @param text The command that is being run
     * @return | Returns the command with all the replaced placeholders
     */
    @Override
    public String handlePlaceholders(PetUser owner, IEntityPet entity, Location petLoc, String text) {
        Location ownerLoc = owner.getPlayer().getLocation();
        if ((petLoc == null) && (entity != null)) petLoc = entity.getEntity().getLocation();

        if (petLoc != null) text = text.replace("{petX}", String.valueOf(petLoc.getX()))
                .replace("{petY}", String.valueOf(petLoc.getY()))
                .replace("{petZ}", String.valueOf(petLoc.getZ()));

        text = text.replace("{ownerX}", String.valueOf(ownerLoc.getX()))
                .replace("{ownerY}", String.valueOf(ownerLoc.getY()))
                .replace("{ownerZ}", String.valueOf(ownerLoc.getZ()))
                .replace("{ownerName}", owner.getOwnerName())
                .replace("{petType}", entity.getPetType().getName())
                .replace("{petUUID}", entity.getEntity().getUniqueId().toString());
        if ((entity != null) && entity.getPetName().isPresent())
            text = text.replace("{petName}", entity.getPetName().get());
        return text;
    }

    @Override
    public String translatePetName(String name) {
        boolean color = ConfigOption.INSTANCE.RENAME_COLOR_ENABLED.getValue();
        boolean magic = ConfigOption.INSTANCE.RENAME_COLOR_MAGIC.getValue();
        if (!magic) name = name.replace("&k", "");
        if (!color) return name;

        if (ConfigOption.INSTANCE.RENAME_COLOR_HEX.getValue()) {
            name = Colorize.translateBungeeHex(name);
        } else {
            name = Colorize.translateBukkit(name);
        }
        return name;
    }
}
