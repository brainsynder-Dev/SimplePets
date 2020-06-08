package simplepets.brainsynder.api.event.inventory;

import org.bukkit.entity.Player;
import simplepets.brainsynder.pet.PetType;

public class PetInventorySelectTypeEvent extends PetSelectTypeEvent {

    public PetInventorySelectTypeEvent(PetType petType, Player player) {
        super(PetEventType.INVENTORY_SELECT, petType, player);
    }
}
