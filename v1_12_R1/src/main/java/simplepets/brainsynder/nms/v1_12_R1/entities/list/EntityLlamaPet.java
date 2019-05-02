package simplepets.brainsynder.nms.v1_12_R1.entities.list;

import net.minecraft.server.v1_12_R1.DataWatcher;
import net.minecraft.server.v1_12_R1.DataWatcherObject;
import net.minecraft.server.v1_12_R1.EnumColor;
import net.minecraft.server.v1_12_R1.World;
import simple.brainsynder.nbt.StorageTagCompound;
import simplepets.brainsynder.api.Size;
import simplepets.brainsynder.api.entity.passive.IEntityLlamaPet;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.v1_12_R1.entities.branch.EntityHorseChestedAbstractPet;
import simplepets.brainsynder.nms.v1_12_R1.utils.DataWatcherWrapper;
import simplepets.brainsynder.wrapper.DyeColorWrapper;
import simplepets.brainsynder.wrapper.EntityWrapper;
import simplepets.brainsynder.wrapper.LlamaColor;

@Size(width = 0.9F, length = 1.87F)
public class EntityLlamaPet extends EntityHorseChestedAbstractPet implements IEntityLlamaPet {
    private static final DataWatcherObject<Integer> STRENGTH;
    private static final DataWatcherObject<Integer> COLOR;
    private static final DataWatcherObject<Integer> VARIANT;

    static {
        STRENGTH = DataWatcher.a(EntityLlamaPet.class, DataWatcherWrapper.INT);
        COLOR = DataWatcher.a(EntityLlamaPet.class, DataWatcherWrapper.INT);
        VARIANT = DataWatcher.a(EntityLlamaPet.class, DataWatcherWrapper.INT);
    }

    public EntityLlamaPet(World world) {
        super(world);
    }
    public EntityLlamaPet(World world, IPet pet) {
        super(world, pet);
    }

    protected void registerDatawatchers() {
        super.registerDatawatchers();
        this.datawatcher.register(STRENGTH, 0);
        this.datawatcher.register(COLOR, -1);
        this.datawatcher.register(VARIANT, 0);
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = super.asCompound();
        if (getLlamaColor() != null) object.setString("llamacolor", getLlamaColor().name());
        if (getColor() != null) object.setString("color", getColor().name());
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("llamacolor")) setSkinColor(LlamaColor.getByName(object.getString("llamacolor")));
        if (object.hasKey("color")) setColor(DyeColorWrapper.getByName(object.getString("color")));
        super.applyCompound(object);
    }

    public void setSkinColor(LlamaColor skinColor) {
        this.datawatcher.set(VARIANT, skinColor.ordinal());
    }

    @Override
    public LlamaColor getLlamaColor() {
        return LlamaColor.getByID(getDataWatcher().get(VARIANT));
    }

    @Override
    public DyeColorWrapper getColor() {
        DyeColorWrapper color = DyeColorWrapper.getByWoolData((byte) ((int) getDataWatcher().get(COLOR)));
        if (color == null) {
            color = DyeColorWrapper.getByDyeData((byte) ((int) getDataWatcher().get(COLOR)));
        }
        return color;
    }

    @Override
    public void setColor(DyeColorWrapper i) {
        datawatcher.set(COLOR, EnumColor.fromColorIndex(i.getWoolData()).getColorIndex());
    }

    @Override
    public EntityWrapper getPetEntityType() {
        return EntityWrapper.LLAMA;
    }
}
