package simplepets.brainsynder.api.event.inventory;

import org.bukkit.entity.Player;
import simplepets.brainsynder.pet.PetDefault;

public class PetCommandSummonEvent extends PetSelectTypeEvent {

    public PetCommandSummonEvent(PetDefault type, Player p) {
        super(PetEventType.SUMMON, type, p);
    }
}
