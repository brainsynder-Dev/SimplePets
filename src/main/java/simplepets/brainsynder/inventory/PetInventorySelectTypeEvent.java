package simplepets.brainsynder.inventory;

import lombok.Getter;
import org.bukkit.entity.Player;
import simplepets.brainsynder.event.CancellablePetEvent;
import simplepets.brainsynder.pet.PetType;

public class PetInventorySelectTypeEvent extends CancellablePetEvent {
    @Getter
    private PetType petType;
    @Getter
    private Player player;

    public PetInventorySelectTypeEvent(PetType petType, Player player) {
        super(PetEventType.INVENTORY_SELECT);
        this.petType = petType;
        this.player = player;
    }
}
