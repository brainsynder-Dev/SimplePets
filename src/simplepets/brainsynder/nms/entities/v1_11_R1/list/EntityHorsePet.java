package simplepets.brainsynder.nms.entities.v1_11_R1.list;

import net.minecraft.server.v1_11_R1.*;
import simple.brainsynder.nbt.StorageTagCompound;
import simplepets.brainsynder.api.entity.passive.IEntityHorsePet;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.entities.v1_11_R1.branch.EntityHorseAbstractPet;
import simplepets.brainsynder.wrapper.HorseArmorType;
import simplepets.brainsynder.wrapper.HorseColorType;
import simplepets.brainsynder.wrapper.HorseStyleType;

public class EntityHorsePet extends EntityHorseAbstractPet implements IEntityHorsePet {
    private static final DataWatcherObject<Integer> STYLE;
    private static final DataWatcherObject<Integer> ARMOR;

    static {
        STYLE = DataWatcher.a(EntityHorsePet.class, DataWatcherRegistry.b);
        ARMOR = DataWatcher.a(EntityHorsePet.class, DataWatcherRegistry.b);
    }

    private HorseArmorType armor = HorseArmorType.NONE;


    public EntityHorsePet(World world) {
        super(world);
    }

    public EntityHorsePet(World world, IPet pet) {
        super(world, pet);
    }

    public int getVariant() {
        return this.datawatcher.get(STYLE);
    }

    public void setVariant(HorseColorType variant) {
    }

    public HorseStyleType getStyle() {
        return HorseStyleType.values()[this.getVariant() >>> 8];
    }

    public void setStyle(HorseStyleType style) {
        this.datawatcher.set(STYLE, this.getColor().ordinal() & 255 | style.ordinal() << 8);
    }

    public HorseColorType getColor() {
        return HorseColorType.values()[this.getVariant() & 255];
    }

    public void setColor(HorseColorType color) {
        this.datawatcher.set(STYLE, color.ordinal() & 255 | this.getStyle().ordinal() << 8);
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = super.asCompound();
        object.setInteger("armor", this.armor.ordinal());
        object.setInteger("color", getColor().ordinal());
        object.setInteger("Style", getStyle().ordinal());
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("armor"))
            setArmor(HorseArmorType.values()[object.getInteger("armor")]);
        if (object.hasKey("color"))
            setColor(HorseColorType.values()[object.getInteger("color")]);
        if (object.hasKey("Style"))
            setStyle(HorseStyleType.values()[object.getInteger("Style")]);
        super.applyCompound(object);
    }

    public void setArmor(HorseArmorType a) {
        this.armor = a;
        this.datawatcher.set(ARMOR, EnumHorseArmor.values()[a.ordinal()].a());
    }

    @Override
    protected void registerDatawatchers() {
        super.registerDatawatchers();
        this.datawatcher.register(STYLE, 0);
        this.datawatcher.register(ARMOR, EnumHorseArmor.NONE.a());
    }

    public HorseArmorType getArmor() {return this.armor;}
}
