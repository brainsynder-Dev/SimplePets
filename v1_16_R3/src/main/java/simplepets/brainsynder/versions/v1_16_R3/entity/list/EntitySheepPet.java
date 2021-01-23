package simplepets.brainsynder.versions.v1_16_R3.entity.list;

import lib.brainsynder.nbt.StorageTagCompound;
import lib.brainsynder.utils.DyeColorWrapper;
import net.minecraft.server.v1_16_R3.DataWatcher;
import net.minecraft.server.v1_16_R3.DataWatcherObject;
import net.minecraft.server.v1_16_R3.EntityTypes;
import simplepets.brainsynder.api.entity.passive.IEntitySheepPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.versions.v1_16_R3.entity.EntityAgeablePet;
import simplepets.brainsynder.versions.v1_16_R3.utils.DataWatcherWrapper;

public class EntitySheepPet extends EntityAgeablePet implements IEntitySheepPet {
    private static final DataWatcherObject<Byte> DYE_COLOR;
    private DyeColorWrapper color = DyeColorWrapper.WHITE;
    private boolean rainbow = false;
    private int toggle = 0;

    public EntitySheepPet(PetType type, PetUser user) {
        super(EntityTypes.SHEEP, type, user);
    }

    @Override
    protected void registerDatawatchers() {
        super.registerDatawatchers();
        this.datawatcher.register(DYE_COLOR, (byte) 0);
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = super.asCompound();
        if (!rainbow) object.setString("color", getColor().name());
        object.setBoolean("sheared", isSheared());
        object.setBoolean("rainbow", rainbow);
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("rainbow")) rainbow = object.getBoolean("rainbow", false);
        if (object.hasKey("color")) setColor(DyeColorWrapper.getByName(object.getString("color")));
        if (object.hasKey("sheared")) setSheared(object.getBoolean("sheared", false));
        super.applyCompound(object);
    }

    @Override
    public DyeColorWrapper getColor() {
        return color;
    }

    @Override
    public void setColor(DyeColorWrapper color) {
        this.color = color;
        if (!isSheared()) datawatcher.set(DYE_COLOR, (byte)color.getWoolData());
    }

    @Override
    public boolean isSheared() {
        byte data = this.datawatcher.get(DYE_COLOR);
        return (data & 0xF0) != 0;
    }

    public void setSheared(boolean flag) {
        byte data = this.datawatcher.get(DYE_COLOR);
        if (flag) {
            this.datawatcher.set(DYE_COLOR, (byte) (data | 0x10));
        } else {
            this.datawatcher.set(DYE_COLOR, (byte) (data & 0xFFFFFFEF));
            setColor(color);
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (rainbow) {
            if (toggle == 4) {
                setColor(DyeColorWrapper.getNext(getColor()));
                toggle = 0;
            }
            toggle++;
        }
    }

    @Override
    public boolean isRainbow() {
        return rainbow;
    }

    @Override
    public void setRainbow(boolean rainbow) {
        this.rainbow = rainbow;
    }

    static {
        DYE_COLOR = DataWatcher.a(EntitySheepPet.class, DataWatcherWrapper.BYTE);
    }
}
