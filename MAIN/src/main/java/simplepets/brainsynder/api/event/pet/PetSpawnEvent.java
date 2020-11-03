package simplepets.brainsynder.api.event.pet;

import org.bukkit.entity.Player;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.event.CancellablePetEvent;

public class PetSpawnEvent extends CancellablePetEvent {
    private final Player player;
    private final IEntityPet entity;

    public PetSpawnEvent(Player player, IEntityPet entity) {
        this.player = player;
        this.entity = entity;
    }

    public Player getPlayer() {return this.player;}

    public IEntityPet getEntity() {return this.entity;}
}
