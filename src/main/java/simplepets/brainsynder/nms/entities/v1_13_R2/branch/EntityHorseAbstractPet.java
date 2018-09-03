package simplepets.brainsynder.nms.entities.v1_13_R2.branch;

import net.minecraft.server.v1_13_R2.*;
import simple.brainsynder.nbt.StorageTagCompound;
import simplepets.brainsynder.api.entity.IHorseAbstract;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.entities.v1_13_R2.AgeableEntityPet;

import java.util.Optional;
import java.util.UUID;

public abstract class EntityHorseAbstractPet extends AgeableEntityPet implements IHorseAbstract {
    private static final DataWatcherObject<Byte> STATUS;
    private static final DataWatcherObject<Optional<UUID>> OWNER_UNIQUE_ID;

    static {
        STATUS = DataWatcher.a(EntityHorseAbstractPet.class, DataWatcherRegistry.a);
        OWNER_UNIQUE_ID = DataWatcher.a(EntityHorseAbstractPet.class, DataWatcherRegistry.o);
    }

    protected float jumpPower = 0.0F;
    protected boolean bA;

    public EntityHorseAbstractPet(EntityTypes<?> type, World world) {
        super(type, world);
        this.setSize(1.3964844F, 1.6F);
    }

    public EntityHorseAbstractPet(EntityTypes<?> type, World world, IPet pet) {
        super(type, world, pet);
        this.setSize(1.3964844F, 1.6F);
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = super.asCompound();
        object.setBoolean("saddled", isSaddled());
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        super.applyCompound(object);
        if (object.hasKey("saddled")) {
            setSaddled(object.getBoolean("saddled"));
        }
    }

    @Override
    protected void registerDatawatchers() {
        super.registerDatawatchers();
        this.datawatcher.register(STATUS, (byte) 0);
        this.datawatcher.register(OWNER_UNIQUE_ID, Optional.empty());
    }

    @Override
    public boolean isCollidable() {
        return false;
    }

    @Override
    public boolean isSaddled() {
        return getHorseVisual(4);
    }

    public void setSaddled(boolean flag) {
        this.setHorseVisual(4, flag);
    }

    public boolean getHorseVisual(int i) {
        return (this.datawatcher.get(STATUS) & i) != 0;
    }

    public void setHorseVisual(int i, boolean flag) {
        byte b0 = this.datawatcher.get(STATUS);
        if (flag) {
            this.datawatcher.set(STATUS, (byte) (b0 | i));
        } else {
            this.datawatcher.set(STATUS, (byte) (b0 & ~i));
        }
    }
}
