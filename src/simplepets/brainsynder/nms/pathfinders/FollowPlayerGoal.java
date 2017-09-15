package simplepets.brainsynder.nms.pathfinders;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import simple.brainsynder.math.MathUtils;
import simplepets.brainsynder.nms.entities.type.main.IEntityPet;
import simplepets.brainsynder.player.PetOwner;

import java.util.Arrays;
import java.util.List;

public class FollowPlayerGoal {
    @Getter
    private IEntityPet pet;
    @Getter
    private double speed;
    private PetOwner owner;
    private boolean isFirst;
    @Getter
    @Setter
    private Location location = null;
    private double teleportDistance = 10.0;
    private double stopDistance = 3.0;
    private List<Double> ints = Arrays.asList(1.9, -1.9);

    public FollowPlayerGoal(IEntityPet entityPet, Player p, double speed) {
        this.pet = entityPet;
        this.speed = speed;
        isFirst = true;
        owner = PetOwner.getPetOwner(p);
        if (owner.getPet().getVisableEntity().isBig()) {
            ints = Arrays.asList(2.9, -2.9);
            stopDistance = 7.0;
            teleportDistance = 20.0;
        }
    }

    public boolean canRun() {
        if (pet == null) return false;
        if (!owner.getPlayer().isOnline()) return false;
        if (owner.getPlayer().isInsideVehicle()) return false;
        if (!owner.hasPet()) return false;

        Location start = owner.getPlayer().getLocation();
        if (pet.getEntity().getWorld().getName().equals(start.getWorld().getName())) {
            if ((pet.getEntity().getLocation().distance(start) >= teleportDistance)) {
                pet.getEntity().teleport(start);
            }
        } else {
            pet.getEntity().teleport(start);
            return false;
        }
        int x = MathUtils.random(ints.size());
        int z = MathUtils.random(ints.size());

        if (isFirst) {
            location = new Location(start.getWorld(), start.getX() + x, start.getY(), start.getZ() + z);
            isFirst = false;
            return true;
        }
        if ((pet.getEntity().getLocation().distance(start) >= stopDistance)) {
            location = new Location(start.getWorld(), start.getX() + x, start.getY(), start.getZ() + z);
        }
        return location != null;
    }
}
