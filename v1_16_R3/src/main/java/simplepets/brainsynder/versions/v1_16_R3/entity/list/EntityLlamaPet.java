package simplepets.brainsynder.versions.v1_16_R3.entity.list;

import lib.brainsynder.nbt.StorageTagCompound;
import net.minecraft.server.v1_16_R3.*;
import simplepets.brainsynder.api.entity.passive.IEntityLlamaPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.api.wrappers.ColorWrapper;
import simplepets.brainsynder.api.wrappers.LlamaColor;
import simplepets.brainsynder.versions.v1_16_R3.entity.branch.EntityDonkeyAbstractPet;
import simplepets.brainsynder.versions.v1_16_R3.utils.DataWatcherWrapper;

public class EntityLlamaPet extends EntityDonkeyAbstractPet implements IEntityLlamaPet {
    private static final DataWatcherObject<Integer> STRENGTH;
    private static final DataWatcherObject<Integer> COLOR;
    private static final DataWatcherObject<Integer> VARIANT;

    public EntityLlamaPet(PetType type, PetUser user) {
        this(EntityTypes.LLAMA, type, user);
    }

    public EntityLlamaPet(EntityTypes<? extends EntityInsentient> llama, PetType type, PetUser user) {
        super(llama, type, user);
    }

    @Override
    protected void registerDatawatchers() {
        super.registerDatawatchers();
        this.datawatcher.register(STRENGTH, 0);
        this.datawatcher.register(COLOR, -1);
        this.datawatcher.register(VARIANT, 0);
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = super.asCompound();
        object.setString("variant", getLlamaColor().name());
        object.setString("color", getColor().name());
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("variant")) setSkinColor(LlamaColor.getByName(object.getString("variant")));
        if (object.hasKey("color"))
            setColor(ColorWrapper.getByName(object.getString("color")));
        super.applyCompound(object);
    }

    @Override
    public void setSkinColor(LlamaColor skinColor) {
        this.datawatcher.set(VARIANT, skinColor.ordinal());
    }

    @Override
    public LlamaColor getLlamaColor() {
        return LlamaColor.getByID(getDataWatcher().get(VARIANT));
    }

    @Override
    public ColorWrapper getColor() {
        ColorWrapper color = ColorWrapper.getByWoolData((byte) ((int) getDataWatcher().get(COLOR)));
        if (color == null) color = ColorWrapper.getByDyeData((byte) ((int) getDataWatcher().get(COLOR)));
        return color;
    }

    @Override
    public void setColor(ColorWrapper color) {
        if (color == ColorWrapper.NONE) {
            datawatcher.set(COLOR, -1);
            return;
        }
        datawatcher.set(COLOR, EnumColor.fromColorIndex(color.getWoolData()).getColorIndex());
    }

    static {
        STRENGTH = DataWatcher.a(EntityLlamaPet.class, DataWatcherWrapper.INT);
        COLOR = DataWatcher.a(EntityLlamaPet.class, DataWatcherWrapper.INT);
        VARIANT = DataWatcher.a(EntityLlamaPet.class, DataWatcherWrapper.INT);
    }
}
