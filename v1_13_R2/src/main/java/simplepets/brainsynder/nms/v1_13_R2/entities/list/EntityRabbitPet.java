package simplepets.brainsynder.nms.v1_13_R2.entities.list;

import net.minecraft.server.v1_13_R2.*;
import simple.brainsynder.nbt.StorageTagCompound;
import simplepets.brainsynder.api.Size;
import simplepets.brainsynder.api.entity.passive.IEntityRabbitPet;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.v1_13_R2.entities.AgeableEntityPet;
import simplepets.brainsynder.nms.v1_13_R2.registry.Types;
import simplepets.brainsynder.wrapper.RabbitType;

@Size(width = 0.6F, length = 0.7F)
public class EntityRabbitPet extends AgeableEntityPet implements IEntityRabbitPet {
    private static final DataWatcherObject<Integer> RABBIT_TYPE;

    static {
        RABBIT_TYPE = DataWatcher.a(EntityRabbitPet.class, DataWatcherRegistry.b);
    }

    private boolean onGroundLastTick = false;
    private int delay = 0;

    public EntityRabbitPet(World world) {
        super(Types.RABBIT, world);
    }
    public EntityRabbitPet(World world, IPet pet) {
        super(Types.RABBIT, world, pet);
        h = new ControllerJumpRabbit(this);
    }

    @Override
    protected void registerDatawatchers() {
        super.registerDatawatchers();
        this.datawatcher.register(RABBIT_TYPE, 0);
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("type"))
            setRabbitType(RabbitType.getByName(object.getString("type")));
        super.applyCompound(object);
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = super.asCompound();
        object.setString("type", getRabbitType().name());
        return object;
    }

    public RabbitType getRabbitType() {
        return RabbitType.getByID(this.datawatcher.get(RABBIT_TYPE));
    }

    public void setRabbitType(RabbitType type) {
        this.datawatcher.set(RABBIT_TYPE, type.getId());
    }

    protected void cH() {
        super.cH();
        double d0 = this.moveController.c();
        if (d0 > 0.0D) {
            double d1 = this.motX * this.motX + this.motZ * this.motZ;
            if (d1 < 0.010000000000000002D) {
                this.a(0.0F, 0.0F, 1.0F, 0.1F);
            }
        }

        if (!this.world.isClientSide) {
            this.world.broadcastEntityEffect(this, (byte)1);
        }
    }

    private void reset() {
        this.resetDelay();
        ((ControllerJumpRabbit) this.h).a(false);
    }

    private void resetDelay() {
        if (this.moveController.c() < 2.2D) {
            this.delay = 5;
        } else {
            this.delay = 1;
        }
    }

    @Override
    public void repeatTask() {
        super.repeatTask();
        if (this.onGround) {
            if (!this.onGroundLastTick) {
                this.o(false);
                this.reset();
            }

            ControllerJumpRabbit jumpController = (ControllerJumpRabbit) this.h;
            if (!jumpController.c()) {
                if (this.delay == 0) {
                    PathEntity pathentity = getNavigation().m();
                    if (pathentity != null && pathentity.e() < pathentity.d()) {
                        Vec3D vec3d = pathentity.a(this);
                        this.a(vec3d.x, vec3d.z);
                        this.de();
                    }
                }
            } else if (!jumpController.d()) {
                ((ControllerJumpRabbit) this.h).a(true);
            }
        }

        this.onGroundLastTick = this.onGround;
    }

    public void mobTick() {
        super.mobTick();
        if (this.delay > 0) {
            --this.delay;
        }
    }

    public void reseter() {
        this.o(true);
    }

    private static class ControllerJumpRabbit extends ControllerJump {
        private EntityRabbitPet rabbitPet;
        private boolean d = false;

        public ControllerJumpRabbit(EntityRabbitPet entity) {
            super(entity);
            this.rabbitPet = entity;
        }

        public boolean c() {
            return this.a;
        }

        public boolean d() {
            return this.d;
        }

        public void a(boolean flag) {
            this.d = flag;
        }

        public void b() {
            if (this.a) {
                this.rabbitPet.reseter();
                this.a = false;
            }

        }
    }
}
