package simplepets.brainsynder.api.event.inventory;

import org.bukkit.entity.Player;
import simplepets.brainsynder.api.event.CancellablePetEvent;
import simplepets.brainsynder.pet.PetDefault;

public class PetInventorySelectTypeEvent extends CancellablePetEvent {
    private PetDefault petType;
    private Player player;

    public PetInventorySelectTypeEvent(PetDefault petType, Player player) {
        super(PetEventType.INVENTORY_SELECT);
        this.petType = petType;
        this.player = player;
    }

    public PetDefault getPetType() {return this.petType;}

    public Player getPlayer() {return this.player;}
}
