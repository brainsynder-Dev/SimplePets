package simplepets.brainsynder.nms.v1_16_R3.entities.list;

import lib.brainsynder.nbt.StorageTagCompound;
import net.minecraft.server.v1_16_R3.*;
import simplepets.brainsynder.api.Size;
import simplepets.brainsynder.api.entity.passive.IEntityCatPet;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.v1_16_R3.entities.EntityTameablePet;
import simplepets.brainsynder.nms.v1_16_R3.utils.DataWatcherWrapper;
import simplepets.brainsynder.wrapper.CatType;
import simplepets.brainsynder.wrapper.DyeColorWrapper;

/**
 * NMS: {@link net.minecraft.server.v1_16_R3.EntityCat}
 */
@Size(width = 0.9F, length = 1.3F)
public class EntityCatPet extends EntityTameablePet implements IEntityCatPet {
    private static final DataWatcherObject<Integer> TYPE;
    private static final DataWatcherObject<Boolean> SLEEPING_WITH_OWNER;
    private static final DataWatcherObject<Boolean> HEAD_UP;
    private static final DataWatcherObject<Integer> COLLAR_COLOR;

    static {
        TYPE = DataWatcher.a(EntityCatPet.class, DataWatcherWrapper.INT);
        SLEEPING_WITH_OWNER = DataWatcher.a(EntityCatPet.class, DataWatcherWrapper.BOOLEAN);
        HEAD_UP = DataWatcher.a(EntityCatPet.class, DataWatcherWrapper.BOOLEAN);
        COLLAR_COLOR = DataWatcher.a(EntityCatPet.class, DataWatcherWrapper.INT);
    }

    @Override
    protected void registerDatawatchers() {
        super.registerDatawatchers();
        datawatcher.register(TYPE, CatType.TABBY.ordinal());
        datawatcher.register(SLEEPING_WITH_OWNER, false);
        datawatcher.register(HEAD_UP, false);
        datawatcher.register(COLLAR_COLOR, (int) DyeColorWrapper.WHITE.getWoolData());
    }

    public EntityCatPet(EntityTypes<? extends EntityCreature> type, World world, IPet pet) {
        super(type, world, pet);
    }
    public EntityCatPet(EntityTypes<? extends EntityCreature> type, World world) {
        super(type, world);
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound compound = super.asCompound();
        compound.setString("type", getCatType().name());
        compound.setInteger("color", getCollarColor().ordinal());
        return compound;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("type"))
            setCatType(CatType.getByName(object.getString("type")));
        if (object.hasKey("color"))
            setCollarColor(DyeColorWrapper.getByWoolData((byte)object.getInteger("color")));
        super.applyCompound(object);
    }

    @Override
    public CatType getCatType() {
        return CatType.getByID(datawatcher.get(TYPE));
    }

    @Override
    public void setCatType(CatType type) {
        datawatcher.set(TYPE, type.ordinal());
    }

    @Override
    public DyeColorWrapper getCollarColor() {
        return DyeColorWrapper.getByWoolData((byte)((int)datawatcher.get(COLLAR_COLOR)));
    }

    @Override
    public void setCollarColor(DyeColorWrapper color) {
        datawatcher.set(COLLAR_COLOR, color.ordinal());
    }

    @Override
    public boolean isHeadUp() {
        return datawatcher.get(HEAD_UP);
    }

    @Override
    public void setHeadUp(boolean value) {
        datawatcher.set(HEAD_UP, value);
    }

    @Override
    public boolean isSleeping() {
        return datawatcher.get(SLEEPING_WITH_OWNER);
    }

    @Override
    public void setSleeping(boolean value) {
        datawatcher.set(SLEEPING_WITH_OWNER, value);
    }
}
