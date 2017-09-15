package simplepets.brainsynder.nms.entities.v1_9_R1.list;

import com.google.common.base.Optional;
import lombok.Getter;
import net.minecraft.server.v1_9_R1.*;
import simple.brainsynder.nbt.StorageTagCompound;
import simplepets.brainsynder.nms.entities.type.IEntityHorsePet;
import simplepets.brainsynder.nms.entities.v1_9_R1.AgeableEntityPet;
import simplepets.brainsynder.pet.IPet;
import simplepets.brainsynder.wrapper.HorseArmorType;
import simplepets.brainsynder.wrapper.HorseColorType;
import simplepets.brainsynder.wrapper.HorseStyleType;

import java.util.UUID;

@Deprecated
public class EntityHorsePet extends AgeableEntityPet implements IEntityHorsePet {
    private static final DataWatcherObject<Byte> VISUAL;
    private static final DataWatcherObject<Integer> TYPE;
    private static final DataWatcherObject<Integer> STYLE;
    private static final DataWatcherObject<Optional<UUID>> OWNER;
    private static final DataWatcherObject<Integer> ARMOR;

    static {
        VISUAL = DataWatcher.a(EntityHorsePet.class, DataWatcherRegistry.a);
        TYPE = DataWatcher.a(EntityHorsePet.class, DataWatcherRegistry.b);
        STYLE = DataWatcher.a(EntityHorsePet.class, DataWatcherRegistry.b);
        OWNER = DataWatcher.a(EntityHorsePet.class, DataWatcherRegistry.m);
        ARMOR = DataWatcher.a(EntityHorsePet.class, DataWatcherRegistry.b);
    }

    @Getter
    private HorseArmorType armor;

    public EntityHorsePet(World world, IPet pet) {
        super(world, pet);
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = super.asCompound();
        object.setBoolean("Chested", isChested());
        object.setInteger("HorseArmor", this.armor.ordinal());
        object.setInteger("Color", getColor().ordinal());
        object.setBoolean("Saddled", isSaddled());
        object.setInteger("Style", getStyle().ordinal());
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("HorseArmor"))
            setArmor(HorseArmorType.values()[object.getInteger("HorseArmor")]);
        if (object.hasKey("Color"))
            setColor(HorseColorType.values()[object.getInteger("Color")]);
        if (object.hasKey("Style"))
            setStyle(HorseStyleType.values()[object.getInteger("Style")]);
        if (object.hasKey("Saddled"))
            setSaddled(object.getBoolean("Saddled"));
        if (object.hasKey("Chested"))
            setChested(object.getBoolean("Chested"));
        super.applyCompound(object);
    }

    @Override
    public void i() {
        super.i();

        this.datawatcher.register(VISUAL, Byte.valueOf((byte) 0));
        this.datawatcher.register(TYPE, Integer.valueOf(EnumHorseType.HORSE.k()));
        this.datawatcher.register(STYLE, Integer.valueOf(0));
        this.datawatcher.register(OWNER, Optional.absent());
        this.datawatcher.register(ARMOR, Integer.valueOf(EnumHorseArmor.NONE.a()));
    }

    @Override
    public void setVariant(HorseColorType var1) {

    }

    @Override
    public void setVariant(HorseColorType var1, HorseStyleType var2) {
        this.datawatcher.set(STYLE, var2.getId(var1));
    }

    public void setArmor(HorseArmorType var1) {
        this.armor = var1;
        this.datawatcher.set(ARMOR, Integer.valueOf(EnumHorseArmor.values()[var1.ordinal()].a()));
    }

    @Override
    public HorseStyleType getStyle() {
        return null;
    }

    @Override
    public void setStyle(HorseStyleType var1) {

    }

    @Override
    public HorseColorType getColor() {
        return null;
    }

    @Override
    public void setColor(HorseColorType var1) {

    }

    public boolean isChested() {
        return o(8);
    }

    public void setChested(boolean var1) {
        horseVisual(8, var1);
    }

    public boolean isArmored() {
        return false;
    }

    @Override
    public boolean isSaddled() {
        return o(4);
    }

    @Override
    public void setSaddled(boolean var1) {
        horseVisual(4, var1);
    }

    private void horseVisual(int i, boolean flag) {
        byte b0 = this.datawatcher.get(VISUAL).byteValue();
        if (flag) {
            this.datawatcher.set(VISUAL, Byte.valueOf((byte) (b0 | i)));
        } else {
            this.datawatcher.set(VISUAL, Byte.valueOf((byte) (b0 & ~i)));
        }
    }

    private boolean o(int i) {
        return (this.datawatcher.get(VISUAL) & i) != 0;
    }
}
