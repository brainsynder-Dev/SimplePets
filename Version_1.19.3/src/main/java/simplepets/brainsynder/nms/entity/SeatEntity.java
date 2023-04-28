package simplepets.brainsynder.nms.entity;

import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import org.bukkit.event.entity.CreatureSpawnEvent;
import simplepets.brainsynder.nms.VersionTranslator;

public class SeatEntity extends ArmorStand {
    public SeatEntity(Level level, double x, double y, double z) {
        super(level, x, y, z);
        setSilent(true);
        setNoGravity(true);
        setSmall(true);
        // Marker armour stands have a size 0 hitbox, which means the rider
        // isn't raised while mounted
        setMarker(true);
        setInvisible(true);
        setInvulnerable(true);
        // don't save to disk
        persist = false;
    }

    public static boolean attach(Entity rider, Entity entity) {
        var seat = new SeatEntity(entity.level, entity.getX(), entity.getY(), entity.getZ());
        if (VersionTranslator.addEntity(entity.level, seat, CreatureSpawnEvent.SpawnReason.CUSTOM)) {
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
        } else if (getVehicle() == null) {
            // We got dismounted somehow
            ejectPassengers();
            discard();
        }
    }

    @Override
    public void setRot(float f, float f1) {
        super.setRot(f, f1);
    }

    // If a passenger has their eyes in water and this returns false, the
    // passenger ejects themselves from the vehicle.
    @Override
    public boolean rideableUnderWater() {
        if (getVehicle() != null) {
            // let vehicle decide what should happen
            return getVehicle().rideableUnderWater();
        }
        return false;
    }

    // Never let seats eject themselves if they are in water, the passenger has
    // to decide that.
    @Override
    public boolean isEyeInFluid(TagKey<Fluid> tagkey) {
        return false;
    }
}
