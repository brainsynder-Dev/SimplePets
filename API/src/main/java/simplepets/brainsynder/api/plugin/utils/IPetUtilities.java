package simplepets.brainsynder.api.plugin.utils;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.metadata.MetadataValue;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.pet.CommandReason;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;

public interface IPetUtilities {
    void runPetCommands(CommandReason reason, PetUser owner, PetType type);

    void runPetCommands(CommandReason reason, PetUser owner, PetType type, Location location);

    String handlePlaceholders(PetUser owner, IEntityPet entity, Location petLoc, String text);

    String translatePetName (String name);

    default boolean isVanished(Player player) {
        if (player == null) return false;
        for (MetadataValue meta : player.getMetadata("vanished")) {
            if (meta.asBoolean()) return true;
        }
        return false;
    }
}
