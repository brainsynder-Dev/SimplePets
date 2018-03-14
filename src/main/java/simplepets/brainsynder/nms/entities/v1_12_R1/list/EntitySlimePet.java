package simplepets.brainsynder.nms.entities.v1_12_R1.list;

import net.minecraft.server.v1_12_R1.DataWatcher;
import net.minecraft.server.v1_12_R1.DataWatcherObject;
import net.minecraft.server.v1_12_R1.DataWatcherRegistry;
import net.minecraft.server.v1_12_R1.World;
import simple.brainsynder.nbt.StorageTagCompound;
import simplepets.brainsynder.nms.entities.type.IEntitySlimePet;
import simplepets.brainsynder.nms.entities.v1_12_R1.EntityPet;
import simplepets.brainsynder.pet.IPet;
import simplepets.brainsynder.utils.Size;


@Size(width = 0.6F, length = 0.6F)
public class EntitySlimePet extends EntityPet implements IEntitySlimePet {
    private static final DataWatcherObject<Integer> SIZE;

    static {
        SIZE = DataWatcher.a(EntitySlimePet.class, DataWatcherRegistry.b);
    }

    private int jumpDelay;

    public EntitySlimePet(World world) {
        super(world);
    }
    public EntitySlimePet(World world, IPet pet) {
        super(world, pet);
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

    public boolean isSmall() {
        return this.getSize() <= 1;
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
