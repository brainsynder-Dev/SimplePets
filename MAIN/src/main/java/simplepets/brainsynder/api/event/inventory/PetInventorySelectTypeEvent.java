package simplepets.brainsynder.api.event.inventory;

import org.bukkit.entity.Player;
import simplepets.brainsynder.pet.PetDefault;

public class PetInventorySelectTypeEvent extends PetSelectTypeEvent {

    public PetInventorySelectTypeEvent(PetDefault petType, Player player) {
        super(PetEventType.INVENTORY_SELECT, petType, player);
    }
}
