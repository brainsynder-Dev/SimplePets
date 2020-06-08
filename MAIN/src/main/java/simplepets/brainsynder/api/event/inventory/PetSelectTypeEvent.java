package simplepets.brainsynder.api.event.inventory;

import org.bukkit.entity.Player;
import simplepets.brainsynder.api.event.CancellablePetEvent;
import simplepets.brainsynder.pet.PetType;

public class PetSelectTypeEvent extends CancellablePetEvent {

    private final PetType petType;
    private final Player player;

    public PetSelectTypeEvent(PetEventType eventType, PetType type, Player player) {
        super(eventType);
        this.petType = type;
        this.player = player;
    }

    public PetType getPetType() {return this.petType;}

    public Player getPlayer() {return this.player;}
}
