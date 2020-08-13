package simplepets.brainsynder.nms.v1_16_R2.pathfinders;

import lib.brainsynder.math.MathUtils;
import net.minecraft.server.v1_16_R2.PathfinderGoal;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.passive.IEntityParrotPet;
import simplepets.brainsynder.api.event.pet.PetMoveEvent;
import simplepets.brainsynder.nms.v1_16_R2.entities.EntityPet;
import simplepets.brainsynder.player.PetOwner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PathFinderGoalWalkToPlayer extends PathfinderGoal {
    public IEntityPet pet;
    protected double speed;
    public PetOwner owner;
    private boolean isFirst;
    private double teleportDistance = 10.0;
    private double stopDistance = 3.0;
    private List<Double> ints = Arrays.asList(1.9, -1.9);

    PathFinderGoalWalkToPlayer(IEntityPet entitycreature, Player p, double speed) {
        this.pet = entitycreature;
        this.speed = speed;
        isFirst = true;
        owner = PetOwner.getPetOwner(p);
        if (owner.getPet().getVisableEntity().isBig()) {
            ints = Arrays.asList(2.9, -2.9);
            stopDistance = PetCore.get().getConfig().getDouble("Pathfinding.Stopping-Distance");
            teleportDistance = PetCore.get().getConfig().getDouble("Pathfinding.Min-Distance-For-Teleport");
        }
    }

    @Override
    public boolean a() {
        if (pet == null) return false;
        if (owner == null) return false;
        if (owner.getPlayer() == null) return false;
        if (!owner.getPlayer().isOnline()) return false;
        if (owner.getPlayer().isInsideVehicle()) return false;
        if (!owner.hasPet()) return false;
        Location start = owner.getPlayer().getLocation();
        Entity petEntity = pet.getEntity();
        if (petEntity.getWorld().getName().equals(start.getWorld().getName())) {
            if ((petEntity.getLocation().distance(start) >= teleportDistance)) {
                petEntity.teleport(start);
                pet.setWalkToLocation(getWalkToLocation(start));
                return false;
            }
        } else {
            petEntity.teleport(start);
            pet.setWalkToLocation(getWalkToLocation(start));
            return false;
        }

        if (isFirst) {
            if (pet.getWalkToLocation() == null)
                pet.setWalkToLocation(getWalkToLocation(start));
            isFirst = false;
            this.c();
            return true;
        }
        if ((pet.getEntity().getLocation().distance(start) >= stopDistance))
            pet.setWalkToLocation(getWalkToLocation(start));
        this.c();
        return pet.getWalkToLocation() != null;
    }

    @Override
    public boolean b() { // Executed when the mob isn't working with its path
        return !((EntityPet) pet).getNavigation().m();
    }

    @Override
    public void c() {
        if (owner.hasPet()) {
            if (owner.getPet().getEntity() != null) {
                if (pet == null) {
                    if (pet.getEntity() != null) pet.getEntity().remove();
                    return;
                }

                if (pet.getOwner() == null) {
                    if (pet.getEntity() != null) pet.getEntity().remove();
                    return;
                }

                try {
                    PetMoveEvent event = new PetMoveEvent(pet, PetMoveEvent.Cause.WALK);
                    Bukkit.getServer().getPluginManager().callEvent(event);
                } catch (Throwable ignored) {
                }
            }
            Location location = pet.getWalkToLocation();
            ((EntityPet) pet).getNavigation().a(location.getX(), location.getY(), location.getZ(), speed);
        }
    }

    private Location getWalkToLocation (Location start) {
        int x = MathUtils.random(ints.size());
        int z = MathUtils.random(ints.size());

        if (pet instanceof IEntityParrotPet) {
            if (owner.getPlayer().isFlying()) {
                List<Block> checks = new ArrayList<>();
                Location clone = start.clone();
                for (int i = 1; i < ((int) stopDistance); i++) {
                    Block below = clone.subtract(0, i, 0).getBlock();
                    if ((below == null) || (below.getType() == Material.AIR)) checks.add(below);
                }
                if (!checks.isEmpty()) {
                    return new Location(start.getWorld(), start.getX() + x, start.getY(), start.getZ() + z);
                }
            }
        }

        return new Location(start.getWorld(), start.getX() + x, start.getY(), start.getZ() + z);
    }
}

