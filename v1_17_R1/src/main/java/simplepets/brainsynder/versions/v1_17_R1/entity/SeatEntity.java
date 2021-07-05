package simplepets.brainsynder.versions.v1_17_R1.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.level.Level;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftLivingEntity;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class SeatEntity extends ArmorStand {
    public SeatEntity(Level level, double x, double y, double z) {
        super(level, x, y, z);
        setSilent(true);
        setNoGravity(true);
        setSmall(true);
        // Marker armour stands have a size 0 hitbox, which means the rider
        // isn't raised while mounted
        setMarker(true);
        ((CraftLivingEntity) getBukkitEntity()).setInvisible(true);
        setInvulnerable(true);
        // don't save to disk
        persist = false;
    }

    public static boolean attach(Entity rider, Entity entity) {
        var seat = new SeatEntity(entity.level, entity.getX(), entity.getY(), entity.getZ());
        if (entity.level.addEntity(seat, CreatureSpawnEvent.SpawnReason.CUSTOM)) {
            if (seat.startRiding(entity)) {
                if (rider.startRiding(seat)) {
                    return true;
                }
                seat.stopRiding();
            }
            seat.discard();
        }
        return false;
    }

    @Override
    public void tick() {
        super.tick();
        if (getPassengers().isEmpty()) {
            // Trash this entity when player stopped riding
            stopRiding();
            discard();
        }
    }

    @Override
    public void setRot(float f, float f1) {
        super.setRot(f, f1);
    }
}
