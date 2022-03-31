package simplepets.brainsynder.nms.entity.list;

import lib.brainsynder.ServerVersion;
import lib.brainsynder.SupportedVersion;
import lib.brainsynder.nbt.StorageTagCompound;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import simplepets.brainsynder.api.entity.passive.IEntityFrogPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.api.wrappers.FrogVariant;
import simplepets.brainsynder.nms.VersionTranslator;
import simplepets.brainsynder.nms.entity.EntityAgeablePet;

/**
 * NMS: {@link net.minecraft.world.entity.animal.frog.Frog}
 */
@SupportedVersion(version = ServerVersion.v1_19)
public class EntityFrogPet extends EntityAgeablePet implements IEntityFrogPet {
    private static final EntityDataAccessor<Integer> DATA_VARIANT;

    public EntityFrogPet(PetType type, PetUser user) {
        super(VersionTranslator.fetchEntityType("FROG"), type, user);
    }

    @Override
    protected void registerDatawatchers() {
        super.registerDatawatchers();
        entityData.define(DATA_VARIANT, 0);
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound compound = super.asCompound();
        compound.setEnum("variant", getVariant());
        compound.setBoolean("croaking", isCroaking());
        return compound;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("variant")) setVariant(object.getEnum("variant", FrogVariant.class, FrogVariant.TEMPERATE));
        if (object.hasKey("croaking")) setCroaking(object.getBoolean("croaking"));
        super.applyCompound(object);
    }

    static {
        DATA_VARIANT = SynchedEntityData.defineId(EntityFrogPet.class, EntityDataSerializers.INT);
    }

    @Override
    public void setVariant(FrogVariant variant) {
        entityData.set(DATA_VARIANT, variant.ordinal());
    }

    @Override
    public FrogVariant getVariant() {
        return FrogVariant.getByID(entityData.get(DATA_VARIANT));
    }

    @Override
    public boolean isCroaking() {
        return false;
    }

    @Override
    public void setCroaking(boolean value) {

    }
}
