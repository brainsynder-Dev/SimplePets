package simplepets.brainsynder.nms.entities.v1_12_R1.list;

import net.minecraft.server.v1_12_R1.*;
import simple.brainsynder.nbt.StorageTagCompound;
import simplepets.brainsynder.api.Size;
import simplepets.brainsynder.api.entity.passive.IEntityRabbitPet;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.entities.v1_12_R1.AgeableEntityPet;
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
        super(world);
    }
    public EntityRabbitPet(World world, IPet pet) {
        super(world, pet);
        g = new ControllerJumpRabbit(this);
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

    protected void cu() {
        super.cu();
        this.world.broadcastEntityEffect(this, (byte) 1);
    }

    private void reset() {
        this.resetDelay();
        ((ControllerJumpRabbit) this.g).a(false);
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
                this.l(false);
                this.reset();
            }

            ControllerJumpRabbit jumpController = (ControllerJumpRabbit) this.g;
            if (!jumpController.c()) {
                if (this.delay == 0) {
                    PathEntity pathentity = getNavigation().l();
                    if (pathentity != null && pathentity.e() < pathentity.d()) {
                        Vec3D vec3d = pathentity.a(this);
                        this.a(vec3d.x, vec3d.z);
                        this.de();
                    }
                }
            } else if (!jumpController.d()) {
                ((ControllerJumpRabbit) this.g).a(true);
            }
        }

        this.onGroundLastTick = this.onGround;
    }

    public void M() {
        super.M();
        if (this.delay > 0) {
            --this.delay;
        }
    }

    public void reseter() {
        this.l(true);
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
