package simplepets.brainsynder.api.event.pet;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import simplepets.brainsynder.api.event.SimplePetEvent;
import simplepets.brainsynder.pet.PetDefault;

public class PetPreSpawnEvent extends SimplePetEvent {
    private Player player;
    private Location spawnLocation;
    private PetDefault petType;

    public PetPreSpawnEvent(Player player, Location spawnLocation, PetDefault type) {
        super(PetEventType.PRE_SPAWN);
        this.player = player;
        this.spawnLocation = spawnLocation;
        this.petType = type;
    }

    public Player getPlayer() {return this.player;}

    public Location getSpawnLocation() {return this.spawnLocation;}

    public PetDefault getPetType() {return this.petType;}
}
