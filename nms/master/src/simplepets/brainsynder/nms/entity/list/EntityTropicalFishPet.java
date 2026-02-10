package simplepets.brainsynder.nms.entity.list;

import lib.brainsynder.json.JsonObject;
import lib.brainsynder.nbt.StorageTagCompound;
import lib.brainsynder.utils.DyeColorWrapper;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import simplepets.brainsynder.api.entity.passive.IEntityTropicalFishPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.api.wrappers.TropicalPattern;
import simplepets.brainsynder.nms.entity.EntityFishPet;
import simplepets.brainsynder.nms.utils.PetDataAccess;

/**
 * NMS: {@link net.minecraft.world.entity.animal.TropicalFish}
 */
public class EntityTropicalFishPet extends EntityFishPet implements IEntityTropicalFishPet {
    private static final EntityDataAccessor<Integer> VARIANT = SynchedEntityData.defineId(EntityTropicalFishPet.class, EntityDataSerializers.INT);

    public EntityTropicalFishPet(PetType type, PetUser user) {
        super(EntityType.TROPICAL_FISH, type, user);
    }

    @Override
    public void fetchPetData(JsonObject data) {
        super.fetchPetData(data);
        data.add("body", getBodyColor().name());
        data.add("pattern", getPattern().name());
        data.add("color", getPatternColor().name());
    }

    @Override
    public void populateDataAccess(PetDataAccess dataAccess) {
        super.populateDataAccess(dataAccess);
        dataAccess.define(VARIANT, 0);
    }

    @Override
    public DyeColorWrapper getPatternColor() {
        return getRawPatternColor(entityData.get(VARIANT));
    }

    @Override
    public void setPatternColor(DyeColorWrapper color) {
        entityData.set(VARIANT, getRawData(color, getBodyColor(), getPattern()));
    }

    @Override
    public DyeColorWrapper getBodyColor() {
        return getRawBodyColor(entityData.get(VARIANT));
    }

    @Override
    public void setBodyColor(DyeColorWrapper color) {
        entityData.set(VARIANT, getRawData(getPatternColor(), color, getPattern()));
    }

    @Override
    public TropicalPattern getPattern() {
        return getRawPattern(entityData.get(VARIANT));
    }

    @Override
    public void setPattern(TropicalPattern pattern) {
        entityData.set(VARIANT, getRawData(getPatternColor(), getBodyColor(), pattern));
    }

    private DyeColorWrapper getRawPatternColor(int data) {
        return DyeColorWrapper.getByWoolData((byte)(data >> 24 & 255));
    }
    private DyeColorWrapper getRawBodyColor(int data) {
        return DyeColorWrapper.getByWoolData((byte)(data >> 16 & 255));
    }
    private TropicalPattern getRawPattern(int data) {
        return TropicalPattern.fromData(data & '\uffff');
    }
    private int getRawData(DyeColorWrapper patternColor, DyeColorWrapper bodyColor, TropicalPattern type) {
        return patternColor.getWoolData() << 24 | bodyColor.getWoolData() << 16 | type.getDataValue();
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound compound = super.asCompound();
        compound.setString("body", getBodyColor().name());
        compound.setString("pattern", getPattern().name());
        compound.setString("color", getPatternColor().name());
        return compound;
    }

    @Override
    public void applyCompound(StorageTagCompound compound) {
        super.applyCompound(compound);
        if (compound.hasKey("body"))
            setBodyColor(DyeColorWrapper.getByName(compound.getString("body")));
        if (compound.hasKey("pattern"))
            setPattern(TropicalPattern.getByName(compound.getString("pattern")));
        if (compound.hasKey("color"))
            setPatternColor(DyeColorWrapper.getByName(compound.getString("color")));
    }
}
