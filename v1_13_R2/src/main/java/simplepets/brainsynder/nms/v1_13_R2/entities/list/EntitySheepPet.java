package simplepets.brainsynder.nms.v1_13_R2.entities.list;

import lib.brainsynder.nbt.StorageTagCompound;
import net.minecraft.server.v1_13_R2.DataWatcher;
import net.minecraft.server.v1_13_R2.DataWatcherObject;
import net.minecraft.server.v1_13_R2.EntityTypes;
import net.minecraft.server.v1_13_R2.World;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.Size;
import simplepets.brainsynder.api.entity.passive.IEntitySheepPet;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.v1_13_R2.entities.AgeableEntityPet;
import simplepets.brainsynder.nms.v1_13_R2.utils.DataWatcherWrapper;
import simplepets.brainsynder.player.PetOwner;
import simplepets.brainsynder.wrapper.DyeColorWrapper;

/**
 * NMS: {@link net.minecraft.server.v1_13_R2.EntitySheep}
 */
@Size(width = 0.9F, length = 1.3F)
public class EntitySheepPet extends AgeableEntityPet implements IEntitySheepPet {
    private static final DataWatcherObject<Byte> DYE_COLOR;
    private DyeColorWrapper color = DyeColorWrapper.WHITE;
    private boolean rainbow = false;
    private int toggle = 0;

    static {
        DYE_COLOR = DataWatcher.a(EntitySheepPet.class, DataWatcherWrapper.BYTE);
    }

    public EntitySheepPet(EntityTypes<?> type, World world) {
        super(type, world);
    }

    public EntitySheepPet(EntityTypes<?> type, World world, IPet pet) {
        super(type, world, pet);
    }

    @Override
    protected void registerDatawatchers() {
        super.registerDatawatchers();
        this.datawatcher.register(DYE_COLOR, (byte) 0);
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = super.asCompound();
        if (!rainbow)
            object.setString("color", getColor().name());
        object.setBoolean("sheared", isSheared());
        object.setBoolean("rainbow", rainbow);
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("rainbow")) rainbow = object.getBoolean("rainbow");
        if (object.hasKey("color"))
            setColor(DyeColorWrapper.getByName(object.getString("color")));
        if (object.hasKey("sheared"))
            setSheared(object.getBoolean("sheared"));

        super.applyCompound(object);
    }

    @Override
    public DyeColorWrapper getColor() {
        return color;
    }

    @Override
    public void setColor(DyeColorWrapper color) {
        this.color = color;
        if (!isSheared()) datawatcher.set(DYE_COLOR, color.getWoolData());
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
        PetCore.get().getInvLoaders().PET_DATA.update(PetOwner.getPetOwner(getOwner()));
    }

    @Override
    public void repeatTask() {
        super.repeatTask();
        if (rainbow) {
            if (toggle == 4) {
                setColor(DyeColorWrapper.getNext(getColor()));
                PetCore.get().getInvLoaders().PET_DATA.update(PetOwner.getPetOwner(getOwner()));
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
}
