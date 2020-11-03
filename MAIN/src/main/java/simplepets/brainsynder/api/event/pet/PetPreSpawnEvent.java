package simplepets.brainsynder.api.event.pet;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import simplepets.brainsynder.api.event.SimplePetEvent;
import simplepets.brainsynder.pet.PetType;

/**
 * This Event is called before all the checks are run for spawning a pet
 */
public class PetPreSpawnEvent extends SimplePetEvent implements Cancellable {
    private final Player player;
    private final Location spawnLocation;
    private final PetType petType;
    private boolean cancelled = false;

    public PetPreSpawnEvent(Player player, Location spawnLocation, PetType type) {
        this.player = player;
        this.spawnLocation = spawnLocation;
        this.petType = type;
    }

    public Player getPlayer() {return this.player;}

    public Location getSpawnLocation() {return this.spawnLocation;}

    public PetType getPetType() {return this.petType;}

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        cancelled = b;
    }
}
