package simplepets.brainsynder.api.event.inventory;

import org.bukkit.entity.Player;
import simplepets.brainsynder.pet.PetType;

public class PetCommandSummonEvent extends PetSelectTypeEvent {

    public PetCommandSummonEvent(PetType type, Player p) {
        super(type, p);
    }
}
