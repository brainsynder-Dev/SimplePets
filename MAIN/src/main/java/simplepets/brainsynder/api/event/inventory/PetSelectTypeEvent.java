package simplepets.brainsynder.api.event.inventory;

import org.bukkit.entity.Player;
import simplepets.brainsynder.api.event.CancellablePetEvent;
import simplepets.brainsynder.pet.PetDefault;

public class PetSelectTypeEvent extends CancellablePetEvent {

    private PetDefault petType;
    private Player player;

    public PetSelectTypeEvent(PetEventType eventType, PetDefault type, Player player) {
        super(eventType);
        this.petType = type;
        this.player = player;
    }

    public PetDefault getPetType() {return this.petType;}

    public Player getPlayer() {return this.player;}
}
