package simplepets.brainsynder.api.event.inventory;

import org.bukkit.entity.Player;
import simplepets.brainsynder.api.event.CancellablePetEvent;
import simplepets.brainsynder.pet.PetDefault;
import simplepets.brainsynder.storage.PetTypeStorage;

public class PetInventorySelectTypeEvent extends PetSelectTypeEvent {

    public PetInventorySelectTypeEvent(PetDefault petType, Player player) {
        super(PetEventType.INVENTORY_SELECT, petType, player);
    }
}
