package simplepets.brainsynder.api.event.inventory;

import org.bukkit.entity.Player;
import simplepets.brainsynder.api.event.CancellablePetEvent;
import simplepets.brainsynder.pet.PetType;

public class PetInventorySelectTypeEvent extends CancellablePetEvent {
    private PetType petType;
    private Player player;

    public PetInventorySelectTypeEvent(PetType petType, Player player) {
        super(PetEventType.INVENTORY_SELECT);
        this.petType = petType;
        this.player = player;
    }

    public PetType getPetType() {return this.petType;}

    public Player getPlayer() {return this.player;}
}
