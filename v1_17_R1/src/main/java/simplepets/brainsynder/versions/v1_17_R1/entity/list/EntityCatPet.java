package simplepets.brainsynder.versions.v1_17_R1.entity.list;

import lib.brainsynder.nbt.StorageTagCompound;
import lib.brainsynder.utils.DyeColorWrapper;
import net.minecraft.server.v1_16_R3.DataWatcher;
import net.minecraft.server.v1_16_R3.DataWatcherObject;
import net.minecraft.server.v1_16_R3.EntityTypes;
import simplepets.brainsynder.api.entity.passive.IEntityCatPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.api.wrappers.CatType;
import simplepets.brainsynder.versions.v1_17_R1.entity.EntityTameablePet;
import simplepets.brainsynder.versions.v1_17_R1.utils.DataWatcherWrapper;

/**
 * NMS: {@link net.minecraft.server.v1_16_R3.EntityCat}
 */
public class EntityCatPet extends EntityTameablePet implements IEntityCatPet {
    private static final DataWatcherObject<Integer> TYPE;
    private static final DataWatcherObject<Boolean> SLEEPING_WITH_OWNER;
    private static final DataWatcherObject<Boolean> HEAD_UP;
    private static final DataWatcherObject<Integer> COLLAR_COLOR;

    public EntityCatPet(PetType type, PetUser user) {
        super(EntityTypes.CAT, type, user);
    }

    @Override
    protected void registerDatawatchers() {
        super.registerDatawatchers();
        datawatcher.register(TYPE, CatType.TABBY.ordinal());
        datawatcher.register(SLEEPING_WITH_OWNER, false);
        datawatcher.register(HEAD_UP, false);
        datawatcher.register(COLLAR_COLOR, DyeColorWrapper.WHITE.getWoolData());
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound compound = super.asCompound();
        compound.setEnum("type", getCatType());
        compound.setEnum("collar", getCollarColor());
        compound.setBoolean("sleeping", isPetSleeping());
        compound.setBoolean("head_up", isHeadUp());
        return compound;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("type")) setCatType(object.getEnum("type", CatType.class, CatType.TABBY));
        if (object.hasKey("collar")) setCollarColor(object.getEnum("collar", DyeColorWrapper.class, DyeColorWrapper.WHITE));
        if (object.hasKey("sleeping")) setPetSleeping(object.getBoolean("sleeping", false));
        if (object.hasKey("head_up")) setHeadUp(object.getBoolean("head_up", false));
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
    public boolean isPetSleeping() {
        return datawatcher.get(SLEEPING_WITH_OWNER);
    }

    @Override
    public void setPetSleeping(boolean sleeping) {
        datawatcher.set(SLEEPING_WITH_OWNER, sleeping);
    }

    static {
        TYPE = DataWatcher.a(EntityCatPet.class, DataWatcherWrapper.INT);
        SLEEPING_WITH_OWNER = DataWatcher.a(EntityCatPet.class, DataWatcherWrapper.BOOLEAN);
        HEAD_UP = DataWatcher.a(EntityCatPet.class, DataWatcherWrapper.BOOLEAN);
        COLLAR_COLOR = DataWatcher.a(EntityCatPet.class, DataWatcherWrapper.INT);
    }
}
