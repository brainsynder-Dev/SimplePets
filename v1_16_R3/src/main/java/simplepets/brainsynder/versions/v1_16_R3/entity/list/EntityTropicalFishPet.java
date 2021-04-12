package simplepets.brainsynder.versions.v1_16_R3.entity.list;

import lib.brainsynder.nbt.StorageTagCompound;
import lib.brainsynder.utils.DyeColorWrapper;
import net.minecraft.server.v1_16_R3.DataWatcher;
import net.minecraft.server.v1_16_R3.DataWatcherObject;
import net.minecraft.server.v1_16_R3.EntityTypes;
import simplepets.brainsynder.api.entity.passive.IEntityTropicalFishPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.api.wrappers.TropicalPattern;
import simplepets.brainsynder.versions.v1_16_R3.entity.EntityFishPet;
import simplepets.brainsynder.versions.v1_16_R3.utils.DataWatcherWrapper;

/**
 * NMS: {@link net.minecraft.server.v1_16_R3.EntityTropicalFish}
 */
public class EntityTropicalFishPet extends EntityFishPet implements IEntityTropicalFishPet {
    private static final DataWatcherObject<Integer> VARIANT;

    public EntityTropicalFishPet(PetType type, PetUser user) {
        super(EntityTypes.TROPICAL_FISH, type, user);
    }

    @Override
    protected void registerDatawatchers() {
        super.registerDatawatchers();
        this.datawatcher.register(VARIANT, 0);
    }

    @Override
    public DyeColorWrapper getPatternColor() {
        return getRawPatternColor(datawatcher.get(VARIANT));
    }

    @Override
    public void setPatternColor(DyeColorWrapper color) {
        datawatcher.set(VARIANT, getRawData(color, getBodyColor(), getPattern()));
    }

    @Override
    public DyeColorWrapper getBodyColor() {
        return getRawBodyColor(datawatcher.get(VARIANT));
    }

    @Override
    public void setBodyColor(DyeColorWrapper color) {
        datawatcher.set(VARIANT, getRawData(getPatternColor(), color, getPattern()));
    }

    @Override
    public TropicalPattern getPattern() {
        return getRawPattern(datawatcher.get(VARIANT));
    }

    @Override
    public void setPattern(TropicalPattern pattern) {
        datawatcher.set(VARIANT, getRawData(getPatternColor(), getBodyColor(), pattern));
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

    static {
        VARIANT = DataWatcher.a(EntityTropicalFishPet.class, DataWatcherWrapper.INT);
    }
}
