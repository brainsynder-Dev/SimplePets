package simplepets.brainsynder.api.event.pet;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import simplepets.brainsynder.api.event.SimplePetEvent;
import simplepets.brainsynder.pet.PetType;

public class PetPreSpawnEvent extends SimplePetEvent {
    private final Player player;
    private final Location spawnLocation;
    private final PetType petType;

    public PetPreSpawnEvent(Player player, Location spawnLocation, PetType type) {
        super(PetEventType.PRE_SPAWN);
        this.player = player;
        this.spawnLocation = spawnLocation;
        this.petType = type;
    }

    public Player getPlayer() {return this.player;}

    public Location getSpawnLocation() {return this.spawnLocation;}

    public PetType getPetType() {return this.petType;}
}
