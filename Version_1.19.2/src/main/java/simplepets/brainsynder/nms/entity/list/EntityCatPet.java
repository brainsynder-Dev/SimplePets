package simplepets.brainsynder.nms.entity.list;

import lib.brainsynder.nbt.StorageTagCompound;
import lib.brainsynder.utils.DyeColorWrapper;
import net.minecraft.core.Registry;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.CatVariant;
import simplepets.brainsynder.api.entity.passive.IEntityCatPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.api.wrappers.CatType;
import simplepets.brainsynder.nms.entity.EntityTameablePet;

/**
 * NMS: {@link net.minecraft.world.entity.animal.Cat}
 */
public class EntityCatPet extends EntityTameablePet implements IEntityCatPet {
    private static final EntityDataAccessor<CatVariant> TYPE;
    private static final EntityDataAccessor<Boolean> SLEEPING_WITH_OWNER;
    private static final EntityDataAccessor<Boolean> HEAD_UP;
    private static final EntityDataAccessor<Integer> COLLAR_COLOR;

    public EntityCatPet(PetType type, PetUser user) {
        super(EntityType.CAT, type, user);
    }

    @Override
    protected void registerDatawatchers() {
        super.registerDatawatchers();
        entityData.define(TYPE, CatVariant.TABBY);
        entityData.define(SLEEPING_WITH_OWNER, false);
        entityData.define(HEAD_UP, false);
        entityData.define(COLLAR_COLOR, DyeColorWrapper.WHITE.getWoolData());
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
        if (object.hasKey("collar"))
            setCollarColor(object.getEnum("collar", DyeColorWrapper.class, DyeColorWrapper.WHITE));
        if (object.hasKey("sleeping")) setPetSleeping(object.getBoolean("sleeping", false));
        if (object.hasKey("head_up")) setHeadUp(object.getBoolean("head_up", false));
        super.applyCompound(object);
    }

    @Override
    public CatType getCatType() {
        return CatType.getByID(Registry.CAT_VARIANT.getId(entityData.get(TYPE)));
    }

    @Override
    public void setCatType(CatType type) {
        entityData.set(TYPE, Registry.CAT_VARIANT.byId(type.ordinal()));
    }

    @Override
    public DyeColorWrapper getCollarColor() {
        return DyeColorWrapper.getByWoolData((byte) ((int) entityData.get(COLLAR_COLOR)));
    }

    @Override
    public void setCollarColor(DyeColorWrapper color) {
        entityData.set(COLLAR_COLOR, color.ordinal());
    }

    @Override
    public boolean isHeadUp() {
        return entityData.get(HEAD_UP);
    }

    @Override
    public void setHeadUp(boolean value) {
        entityData.set(HEAD_UP, value);
    }

    @Override
    public boolean isPetSleeping() {
        return entityData.get(SLEEPING_WITH_OWNER);
    }

    @Override
    public void setPetSleeping(boolean sleeping) {
        entityData.set(SLEEPING_WITH_OWNER, sleeping);
    }

    static {
        TYPE = SynchedEntityData.defineId(EntityCatPet.class, EntityDataSerializers.CAT_VARIANT);
        SLEEPING_WITH_OWNER = SynchedEntityData.defineId(EntityCatPet.class, EntityDataSerializers.BOOLEAN);
        HEAD_UP = SynchedEntityData.defineId(EntityCatPet.class, EntityDataSerializers.BOOLEAN);
        COLLAR_COLOR = SynchedEntityData.defineId(EntityCatPet.class, EntityDataSerializers.INT);
    }
}
