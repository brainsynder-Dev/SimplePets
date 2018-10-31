package simplepets.brainsynder.nms.v1_11_R1.entities.list;

import net.minecraft.server.v1_11_R1.*;
import simple.brainsynder.nbt.StorageTagCompound;
import simplepets.brainsynder.api.entity.passive.IEntityLlamaPet;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.v1_11_R1.entities.branch.EntityHorseChestedAbstractPet;
import simplepets.brainsynder.wrapper.DyeColorWrapper;
import simplepets.brainsynder.wrapper.LlamaColor;

public class EntityLlamaPet extends EntityHorseChestedAbstractPet implements IEntityLlamaPet {
    private static final DataWatcherObject<Integer> STRENGTH;
    private static final DataWatcherObject<Integer> COLOR;
    private static final DataWatcherObject<Integer> VARIANT;

    static {
        STRENGTH = DataWatcher.a(EntityLlamaPet.class, DataWatcherRegistry.b);
        COLOR = DataWatcher.a(EntityLlamaPet.class, DataWatcherRegistry.b);
        VARIANT = DataWatcher.a(EntityLlamaPet.class, DataWatcherRegistry.b);
    }

    private DyeColorWrapper color;

    public EntityLlamaPet(World world) {
        super(world);
    }


    public EntityLlamaPet(World world, IPet pet) {
        super(world, pet);
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = super.asCompound();
        object.setString("LlamaColor", getLlamaColor().name());
        if (getColor() != null)
            object.setString("color", this.color.name());
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("LlamaColor"))
            setSkinColor(LlamaColor.valueOf(String.valueOf(object.getString("LlamaColor"))));
        if (object.hasKey("color"))
            setColor(DyeColorWrapper.valueOf(String.valueOf(object.getString("color"))));
        super.applyCompound(object);
    }

    protected void registerDatawatchers() {
        super.registerDatawatchers();
        this.datawatcher.register(STRENGTH, 0);
        this.datawatcher.register(COLOR, -1);
        this.datawatcher.register(VARIANT, 0);
    }

    public void setSkinColor(LlamaColor skinColor) {
        this.datawatcher.set(VARIANT, skinColor.getId());
    }

    @Override
    public LlamaColor getLlamaColor() {
        return LlamaColor.getByID(datawatcher.get(VARIANT));
    }

    @Override
    public void setColor(DyeColorWrapper i) {
        color = i;
        datawatcher.set(COLOR, EnumColor.fromColorIndex(i.getWoolData()).getColorIndex());
    }

    public DyeColorWrapper getColor() {return this.color;}
}
