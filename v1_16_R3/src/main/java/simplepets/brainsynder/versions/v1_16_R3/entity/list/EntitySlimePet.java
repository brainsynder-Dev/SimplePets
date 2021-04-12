package simplepets.brainsynder.versions.v1_16_R3.entity.list;

import lib.brainsynder.nbt.StorageTagCompound;
import net.minecraft.server.v1_16_R3.DataWatcher;
import net.minecraft.server.v1_16_R3.DataWatcherObject;
import net.minecraft.server.v1_16_R3.EntityInsentient;
import net.minecraft.server.v1_16_R3.EntityTypes;
import simplepets.brainsynder.api.entity.hostile.IEntitySlimePet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.versions.v1_16_R3.entity.EntityPet;
import simplepets.brainsynder.versions.v1_16_R3.utils.DataWatcherWrapper;

/**
 * NMS: {@link net.minecraft.server.v1_16_R3.EntitySlime}
 */
public class EntitySlimePet extends EntityPet implements IEntitySlimePet {
    private static final DataWatcherObject<Integer> SIZE;
    private int jumpDelay;

    public EntitySlimePet(PetType type, PetUser user) {
        this(EntityTypes.SLIME, type, user);
    }

    public EntitySlimePet(EntityTypes<? extends EntityInsentient> entitytypes, PetType type, PetUser user) {
        super(entitytypes, type, user);
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
    public void movementTick() {
        super.movementTick();
        if (this.onGround && (jumpDelay-- <= 0) && (passengers.size() == 0) && (!getEntity().isInsideVehicle())) {
            jumpDelay = this.random.nextInt(15) + 10;
            this.getControllerJump().b();
        }
    }

    static {
        SIZE = DataWatcher.a(EntitySlimePet.class, DataWatcherWrapper.INT);
    }
}
