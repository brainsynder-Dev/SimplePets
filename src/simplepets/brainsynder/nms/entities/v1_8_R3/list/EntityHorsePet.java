package simplepets.brainsynder.nms.entities.v1_8_R3.list;

import lombok.Getter;
import net.minecraft.server.v1_8_R3.World;
import simple.brainsynder.nbt.StorageTagCompound;
import simplepets.brainsynder.nms.entities.type.IEntityHorsePet;
import simplepets.brainsynder.nms.entities.v1_8_R3.AgeableEntityPet;
import simplepets.brainsynder.pet.IPet;
import simplepets.brainsynder.wrapper.HorseArmorType;
import simplepets.brainsynder.wrapper.HorseColorType;
import simplepets.brainsynder.wrapper.HorseStyleType;

/**
 * @Deprecated Will be removed when MC version 1.13 is released
 */
@Deprecated
public class EntityHorsePet extends AgeableEntityPet implements IEntityHorsePet {
    @Getter
    private HorseArmorType armor;


    public EntityHorsePet(World world, IPet pet) {
        super(world, pet);
    }

    public EntityHorsePet(World world) {
        super(world);
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = super.asCompound();
        object.setBoolean("Chested", isChested());
        object.setInteger("armor", this.armor.ordinal());
        object.setInteger("color", getColor().ordinal());
        object.setBoolean("Saddled", isSaddled());
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
        if (object.hasKey("Saddled"))
            setSaddled(object.getBoolean("Saddled"));
        if (object.hasKey("Chested"))
            setChested(object.getBoolean("Chested"));
        super.applyCompound(object);
    }

    @Override
    protected void initDatawatcher() {
        super.initDatawatcher();
        this.datawatcher.a(16, Integer.valueOf(0));
        this.datawatcher.a(19, Byte.valueOf((byte) 0));
        this.datawatcher.a(20, Integer.valueOf(0));
        this.datawatcher.a(21, String.valueOf(""));
        this.datawatcher.a(22, Integer.valueOf(0));
    }

    public int getVariant() {
        return this.datawatcher.getInt(20);
    }

    @Override
    public void setVariant(HorseColorType var1) {
        if (var1 != HorseColorType.WHITE) {
            this.setArmor(HorseArmorType.NONE);
        }

        this.datawatcher.watch(19, Byte.valueOf((byte) var1.ordinal()));
    }

    public HorseStyleType getStyle() {
        return HorseStyleType.values()[this.getVariant() >>> 8];
    }

    public void setStyle(HorseStyleType style) {
        this.datawatcher.watch(20, Integer.valueOf(this.getColor().ordinal() & 255 | style.ordinal() << 8));
    }

    public HorseColorType getColor() {
        return HorseColorType.values()[this.getVariant() & 255];
    }

    public void setColor(HorseColorType color) {
        this.datawatcher.watch(20, Integer.valueOf(color.ordinal() & 255 | this.getStyle().ordinal() << 8));
    }

    public void setVariant(HorseColorType v, HorseStyleType m) {
        this.datawatcher.watch(20, m.getId(v));
    }

    public void setArmor(HorseArmorType var1) {
        this.armor = var1;
        this.datawatcher.watch(22, var1.getId());
    }

    public boolean isChested() {
        return false;
    }

    public void setChested(boolean flag) {
        this.horseVisual(8, flag);
    }

    public boolean isArmored() {
        return false;
    }

    @Override
    public boolean isSaddled() {
        return false;
    }

    public void setSaddled(boolean flag) {
        this.horseVisual(4, flag);
    }

    private void horseVisual(int i, boolean flag) {
        int j = this.datawatcher.getInt(16);
        if (flag) {
            this.datawatcher.watch(16, j | i);
        } else {
            this.datawatcher.watch(16, j & ~i);
        }

    }

    public int getType() {
        return this.datawatcher.getByte(19);
    }
}
