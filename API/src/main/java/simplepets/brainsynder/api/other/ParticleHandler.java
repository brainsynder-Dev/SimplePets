package simplepets.brainsynder.api.other;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public interface ParticleHandler {
    void sendParticle (Reason reason, Player player, Location location);

    enum Reason {
        SPAWN, FAILED, RENAME, REMOVE, TELEPORT, TASK_FAILED
    }
}
