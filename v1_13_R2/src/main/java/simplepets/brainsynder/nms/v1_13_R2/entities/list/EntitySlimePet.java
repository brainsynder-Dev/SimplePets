package simplepets.brainsynder.nms.v1_13_R2.entities.list;

import lib.brainsynder.nbt.StorageTagCompound;
import net.minecraft.server.v1_13_R2.DataWatcher;
import net.minecraft.server.v1_13_R2.DataWatcherObject;
import net.minecraft.server.v1_13_R2.EntityTypes;
import net.minecraft.server.v1_13_R2.World;
import simplepets.brainsynder.api.Size;
import simplepets.brainsynder.api.entity.hostile.IEntitySlimePet;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.v1_13_R2.entities.EntityPet;
import simplepets.brainsynder.nms.v1_13_R2.utils.DataWatcherWrapper;

/**
 * NMS: {@link net.minecraft.server.v1_13_R2.EntitySlime}
 */
@Size(width = 0.6F, length = 0.6F)
public class EntitySlimePet extends EntityPet implements IEntitySlimePet {
    private static final DataWatcherObject<Integer> SIZE;

    static {
        SIZE = DataWatcher.a(EntitySlimePet.class, DataWatcherWrapper.INT);
    }

    private int jumpDelay;

    public EntitySlimePet(EntityTypes<?> type, World world) {
        super(type, world);
    }
    public EntitySlimePet(EntityTypes<?> type, World world, IPet pet) {
        super(type, world, pet);
        jumpDelay = random.nextInt(15) + 10;
    }

    @Override
    protected void registerDatawatchers() {
        super.registerDatawatchers();
        this.datawatcher.register(SIZE, 2);
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = super.asCompound();
        object.setInteger("size", getSize());
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("size")) setSize(object.getInteger("size"));
        super.applyCompound(object);
    }

    public int getSize() {
        return this.datawatcher.get(SIZE);
    }

    public void setSize(int i) {
        this.datawatcher.set(SIZE, i);
    }

    @Override
    public void repeatTask() {
        super.repeatTask();
        if (this.onGround && (jumpDelay-- <= 0) && (passengers.size() == 0) && (!getEntity().isInsideVehicle())) {
            jumpDelay = this.random.nextInt(15) + 10;
            this.getControllerJump().a();
        }
    }
}
