package simplepets.brainsynder.versions.v1_16_R3.entity.list;

import lib.brainsynder.nbt.StorageTagCompound;
import net.minecraft.server.v1_16_R3.*;
import simplepets.brainsynder.api.entity.passive.IEntityRabbitPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.api.wrappers.RabbitType;
import simplepets.brainsynder.versions.v1_16_R3.entity.EntityAgeablePet;
import simplepets.brainsynder.versions.v1_16_R3.entity.controller.ControllerJumpRabbit;
import simplepets.brainsynder.versions.v1_16_R3.entity.controller.ControllerMoveRabbit;
import simplepets.brainsynder.versions.v1_16_R3.utils.DataWatcherWrapper;

/**
 * NMS: {@link net.minecraft.server.v1_16_R3.EntityRabbit}
 */
public class EntityRabbitPet extends EntityAgeablePet implements IEntityRabbitPet {
    private static final DataWatcherObject<Integer> RABBIT_TYPE;
    private int jumpTicks;
    private int jumpDuration;
    private boolean onGroundLastTick = false;
    private int ticksUntilJump = 0;

    public EntityRabbitPet(PetType type, PetUser user) {
        super(EntityTypes.RABBIT, type, user);
        bi = new ControllerJumpRabbit(this);
        moveController = new ControllerMoveRabbit(this);
    }

    @Override
    protected void registerDatawatchers() {
        super.registerDatawatchers();
        this.datawatcher.register(RABBIT_TYPE, 0);
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
        return RabbitType.getByID(this.datawatcher.get(RABBIT_TYPE));
    }

    @Override
    public void setRabbitType(RabbitType type) {
        this.datawatcher.set(RABBIT_TYPE, type.getId());
    }

    @Override
    public void tick() {
        super.tick();
        this.onGroundLastTick = this.onGround;
    }

    @Override
    public void mobTick() {
        super.mobTick();
        if (this.ticksUntilJump > 0) {
            --this.ticksUntilJump;
        }

        if (this.onGround) {
            if (!this.onGroundLastTick) {
                this.setJumping(false);
                scheduleJump();
            }

            ControllerJumpRabbit controller = (ControllerJumpRabbit)this.getControllerJump();
            if (!controller.isActive()) {
                if (this.moveController.b() && this.ticksUntilJump == 0) {
                    PathEntity pathentity = this.navigation.k(); // Translation: navigation.getCurrentPath()

                    // Translation: getTargetX, getTargetY, getTargetZ
                    Vec3D vec3d = new Vec3D(this.moveController.d(), this.moveController.e(), this.moveController.f());
                    if (pathentity != null && pathentity.f() < pathentity.e()) {
                        vec3d = pathentity.a(this); // Translation: getNodePosition ()
                    }

                    this.lookTowards(vec3d.x, vec3d.z);
                    this.reseter();
                }
            } else if (!controller.d()) {
                ((ControllerJumpRabbit) getControllerJump()).a(true);
            }
        }

        this.onGroundLastTick = this.onGround;
    }

    private void scheduleJump() {
        this.doScheduleJump();
        ((ControllerJumpRabbit)getControllerJump()).a(false);
    }

    private void doScheduleJump() {
        if (this.moveController.c() < 2.2D) {
            this.ticksUntilJump = 10;
        } else {
            this.ticksUntilJump = 1;
        }
        ((ControllerJumpRabbit)this.getControllerJump()).a(false);
    }

    private void lookTowards(double d0, double d1) {
        this.yaw = (float)(MathHelper.d(d1 - this.locZ(), d0 - this.locX()) * 57.2957763671875D) - 90.0F;
    }

    @Override
    public void movementTick() {
        super.movementTick();
        if (jumpTicks != jumpDuration) {
            ++this.jumpTicks;
        } else if (jumpDuration != 0) {
            jumpTicks = 0;
            jumpDuration = 0;
            setJumping(false);
        }
    }

    @Override
    protected void jump() {
        super.jump();
        // Translation: moveControl.getSpeed()
        double speed = this.moveController.c();
        if (speed > 0.0D) {
            // Translation: squaredHorizontalLength(velocity)
            double length = c(this.getMot());
            if (length < 0.01D) {
                // Translation: updateVelocity
                this.a(0.1F, new Vec3D(0.0D, 0.0D, 1.0D));
            }
        }

        if (!this.world.isClientSide) this.world.broadcastEntityEffect(this, (byte)1);
    }

    public void reseter() {
        this.setJumping(true);
        this.jumpDuration = 10;
        this.jumpTicks = 0;
    }

    public void setSpeed(double speed) {
        this.getNavigation().a(speed);
        this.moveController.a(this.moveController.d(), this.moveController.e(), this.moveController.f(), speed);
    }


    static {
        RABBIT_TYPE = DataWatcher.a(EntityRabbitPet.class, DataWatcherWrapper.INT);
    }
}
