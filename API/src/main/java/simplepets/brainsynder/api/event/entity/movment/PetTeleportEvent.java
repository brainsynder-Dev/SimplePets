package simplepets.brainsynder.api.event.entity.movment;

import org.bukkit.Location;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.event.entity.PetMoveEvent;

public class PetTeleportEvent extends PetMoveEvent {
    public PetTeleportEvent(IEntityPet entity, Location targetLocation) {
        super(entity, targetLocation);
    }
}
