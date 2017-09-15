package simplepets.brainsynder.pet;

import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import simplepets.brainsynder.event.SimplePetEvent;

public class PetPreSpawnEvent extends SimplePetEvent {
    @Getter
    private Player player;
    @Getter
    private Location spawnLocation;
    @Getter
    private PetType petType;

    public PetPreSpawnEvent(Player player, Location spawnLocation, PetType type) {
        super(PetEventType.PRE_SPAWN);
        this.player = player;
        this.spawnLocation = spawnLocation;
        this.petType = type;
    }
}
