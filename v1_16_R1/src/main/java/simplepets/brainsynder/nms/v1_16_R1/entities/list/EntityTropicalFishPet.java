package simplepets.brainsynder.nms.v1_16_R1.entities.list;

import lib.brainsynder.nbt.StorageTagCompound;
import net.minecraft.server.v1_16_R1.*;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.entity.passive.IEntityTropicalFishPet;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.v1_16_R1.entities.EntityFishPet;
import simplepets.brainsynder.nms.v1_16_R1.utils.DataWatcherWrapper;
import simplepets.brainsynder.player.PetOwner;
import simplepets.brainsynder.wrapper.DyeColorWrapper;
import simplepets.brainsynder.wrapper.TropicalPattern;

/**
 * NMS: {@link net.minecraft.server.v1_16_R1.EntityTropicalFish}
 */
public class EntityTropicalFishPet extends EntityFishPet implements IEntityTropicalFishPet {
    private static final DataWatcherObject<Integer> VARIANT;
    public EntityTropicalFishPet(EntityTypes<? extends EntityCreature> type, World world, IPet pet) {
        super(type, world, pet);
    }
    public EntityTropicalFishPet(EntityTypes<? extends EntityCreature> type, World world) {
        super(type, world);
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
        PetCore.get().getInvLoaders().PET_DATA.update(PetOwner.getPetOwner(getOwner()));
    }

    @Override
    public DyeColorWrapper getBodyColor() {
        return getRawBodyColor(datawatcher.get(VARIANT));
    }

    @Override
    public void setBodyColor(DyeColorWrapper color) {
        datawatcher.set(VARIANT, getRawData(getPatternColor(), color, getPattern()));
        PetCore.get().getInvLoaders().PET_DATA.update(PetOwner.getPetOwner(getOwner()));
    }

    @Override
    public TropicalPattern getPattern() {
        return getRawPattern(datawatcher.get(VARIANT));
    }

    @Override
    public void setPattern(TropicalPattern pattern) {
        datawatcher.set(VARIANT, getRawData(getPatternColor(), getBodyColor(), pattern));
        PetCore.get().getInvLoaders().PET_DATA.update(PetOwner.getPetOwner(getOwner()));
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
