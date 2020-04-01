package simplepets.brainsynder.nms.v1_13_R1.entities.branch;

import lib.brainsynder.nbt.StorageTagCompound;
import net.minecraft.server.v1_13_R1.DataWatcher;
import net.minecraft.server.v1_13_R1.DataWatcherObject;
import net.minecraft.server.v1_13_R1.EntityTypes;
import net.minecraft.server.v1_13_R1.World;
import simplepets.brainsynder.api.entity.misc.IHorseAbstract;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.v1_13_R1.entities.AgeableEntityPet;
import simplepets.brainsynder.nms.v1_13_R1.utils.DataWatcherWrapper;

import java.util.Optional;
import java.util.UUID;

public abstract class EntityHorseAbstractPet extends AgeableEntityPet implements IHorseAbstract {
    private static final DataWatcherObject<Byte> STATUS;
    private static final DataWatcherObject<Optional<UUID>> OWNER_UNIQUE_ID;

    static {
        STATUS = DataWatcher.a(EntityHorseAbstractPet.class, DataWatcherWrapper.BYTE);
        OWNER_UNIQUE_ID = DataWatcher.a(EntityHorseAbstractPet.class, DataWatcherWrapper.UUID);
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
        return getFlag(4);
    }

    public void setSaddled(boolean flag) {
        setFlag(4, flag);
    }

    @Override
    public boolean getFlag(int i) {
        return (this.datawatcher.get(STATUS) & i) != 0;
    }

    @Override
    public void setFlag(int i, boolean flag) {
        byte b0 = this.datawatcher.get(STATUS);
        if (flag) {
            this.datawatcher.set(STATUS, (byte) (b0 | i));
        } else {
            this.datawatcher.set(STATUS, (byte) (b0 & ~i));
        }
    }
}
