package simplepets.brainsynder.nms.v1_13_R1.entities.list;

import net.minecraft.server.v1_13_R1.DataWatcher;
import net.minecraft.server.v1_13_R1.DataWatcherObject;
import net.minecraft.server.v1_13_R1.EntityTypes;
import net.minecraft.server.v1_13_R1.World;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.entity.passive.IEntityTropicalFishPet;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.v1_13_R1.entities.EntityFishPet;
import simplepets.brainsynder.nms.v1_13_R1.utils.DataWatcherWrapper;
import simplepets.brainsynder.player.PetOwner;
import simplepets.brainsynder.wrapper.DyeColorWrapper;
import simplepets.brainsynder.wrapper.TropicalPattern;

public class EntityTropicalFishPet extends EntityFishPet implements IEntityTropicalFishPet {
    private static final DataWatcherObject<Integer> VARIANT;
    public EntityTropicalFishPet(EntityTypes<?> type, World world, IPet pet) {
        super(type, world, pet);
    }
    public EntityTropicalFishPet(EntityTypes<?> type, World world) {
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

    // WHY DID MOJANG HAVE TO DO THIS!!!
    // WHY DID THEY NOT JUST ADD DIFFERENT DATAWATCHERS, INSTEAD OF ONE
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

    static {
        VARIANT = DataWatcher.a(EntityTropicalFishPet.class, DataWatcherWrapper.INT);
    }
}
