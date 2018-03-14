package simplepets.brainsynder.pet;

import lombok.Getter;
import org.bukkit.entity.Player;
import simplepets.brainsynder.event.CancellablePetEvent;
import simplepets.brainsynder.nms.entities.type.main.IEntityPet;

public class PetSpawnEvent extends CancellablePetEvent {
    @Getter private Player player;
    @Getter private IEntityPet entity;

    public PetSpawnEvent(Player player, IEntityPet entity) {
        super(PetEventType.SPAWN);
        this.player = player;
        this.entity = entity;
    }
}
