package simplepets.brainsynder.pet;

import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import simplepets.brainsynder.event.CancellablePetEvent;

public class PetSpawnEvent extends CancellablePetEvent {
    @Getter
    private Player player;
    @Getter
    private Location spawnLocation;
    @Getter
    private PetType petType;

    public PetSpawnEvent(Player player, Location spawnLocation, PetType type) {
        super(PetEventType.SPAWN);
        this.player = player;
        this.spawnLocation = spawnLocation;
        this.petType = type;
    }
}
