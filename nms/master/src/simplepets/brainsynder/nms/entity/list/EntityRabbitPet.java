package simplepets.brainsynder.nms.entity.list;

import lib.brainsynder.json.JsonObject;
import lib.brainsynder.nbt.StorageTagCompound;
import lib.brainsynder.reflection.Reflection;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;
import simplepets.brainsynder.api.entity.passive.IEntityRabbitPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.api.wrappers.RabbitType;
import simplepets.brainsynder.nms.entity.EntityAgeablePet;
import simplepets.brainsynder.nms.entity.controller.ControllerJumpRabbit;
import simplepets.brainsynder.nms.entity.controller.ControllerMoveRabbit;
import simplepets.brainsynder.nms.utils.PetDataAccess;

import java.lang.reflect.InvocationTargetException;

/**
 * NMS: {@link net.minecraft.world.entity.animal.Rabbit}
 */
public class EntityRabbitPet extends EntityAgeablePet implements IEntityRabbitPet {
    private static final EntityDataAccessor<Integer> RABBIT_TYPE = SynchedEntityData.defineId(EntityRabbitPet.class, EntityDataSerializers.INT);
    private int jumpTicks;
    private int jumpDuration;
    private boolean onGroundLastTick = false;
    private int ticksUntilJump = 0;

    public EntityRabbitPet(PetType type, PetUser user) {
        super(EntityType.RABBIT, type, user);
        jumpControl = new ControllerJumpRabbit(this);
        moveControl = new ControllerMoveRabbit(this);
    }

    @Override
    public void fetchPetData(JsonObject data) {
        super.fetchPetData(data);
        data.add("variant", getRabbitType().name());
    }

    @Override
    public void populateDataAccess(PetDataAccess dataAccess) {
        super.populateDataAccess(dataAccess);
        dataAccess.define(RABBIT_TYPE, 0);
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("variant")) setRabbitType(object.getEnum("variant", RabbitType.class, RabbitType.BROWN));
        super.applyCompound(object);
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = super.asCompound();
        object.setEnum("variant", getRabbitType());
        return object;
    }

    @Override
    public RabbitType getRabbitType() {
        return RabbitType.getByID(this.entityData.get(RABBIT_TYPE));
    }

    @Override
    public void setRabbitType(RabbitType type) {
        this.entityData.set(RABBIT_TYPE, type.getId());
    }

    @Override
    public void tick() {
        super.tick();
        this.onGroundLastTick = this.onGround;
    }

    @Override
    protected void customServerAiStep(ServerLevel worldserver) {
        if (this.ticksUntilJump > 0) {
            --this.ticksUntilJump;
        }

        if (this.onGround) {
            if (!this.onGroundLastTick) {
                this.setJumping(false);
                scheduleJump();
            }

            ControllerJumpRabbit controller = (ControllerJumpRabbit)this.getJumpControl();
            if (!controller.wantJump()) {
                if (this.moveControl.hasWanted() && this.ticksUntilJump == 0) {
                    Path pathentity = getPath(); // Translation: navigation.getCurrentPath()

                    // Translation: getTargetX, getTargetY, getTargetZ
                    Vec3 vec3d = new Vec3(this.moveControl.getWantedX(), this.moveControl.getWantedY(), this.moveControl.getWantedZ());
                    if (pathentity != null && !pathentity.isDone()) {
                        vec3d = pathentity.getNextEntityPos(this); // Translation: getNodePosition ()
                    }

                    this.lookTowards(vec3d.x, vec3d.z);
                    this.reseter();
                }
            } else if (!controller.canJump()) {
                ((ControllerJumpRabbit) jumpControl).setCanJump(true);
            }
        }

        this.onGroundLastTick = this.onGround;
    }

    private void scheduleJump() {
        this.doScheduleJump();
        ((ControllerJumpRabbit)jumpControl).setCanJump(false);
    }

    private void doScheduleJump() {
        if (this.moveControl.getSpeedModifier() < 2.2D) {
            this.ticksUntilJump = 10;
        } else {
            this.ticksUntilJump = 1;
        }
        ((ControllerJumpRabbit)this.jumpControl).setCanJump(false);
    }

    private void lookTowards(double d0, double d1) {
        setYRot((float)(Mth.atan2(d1 - getZ(), d0 - getX()) * 57.2957763671875D) - 90.0F);
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (jumpTicks != jumpDuration) {
            ++this.jumpTicks;
        } else if (jumpDuration != 0) {
            jumpTicks = 0;
            jumpDuration = 0;
            setJumping(false);
        }
    }

    public void setSpeedModifier(double d0) {
        getNavigation().setSpeedModifier(d0);
        this.moveControl.setWantedPosition(this.moveControl.getWantedX(), this.moveControl.getWantedY(), this.moveControl.getWantedZ(), d0);
    }

    private Path getPath() {
        try {
            return navigation.getPath();
        } catch (NoSuchMethodError ex) {
            try {
                return (Path) Reflection.getMethod(PathNavigation.class, "getPath").invoke(navigation);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public void reseter() {
        this.setJumping(true);
        this.jumpDuration = 10;
        this.jumpTicks = 0;
    }
}
