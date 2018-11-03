package simplepets.brainsynder.nms.v1_13_R1.entities.list;

import net.minecraft.server.v1_13_R1.DataWatcher;
import net.minecraft.server.v1_13_R1.DataWatcherObject;
import net.minecraft.server.v1_13_R1.DataWatcherRegistry;
import net.minecraft.server.v1_13_R1.World;
import simple.brainsynder.nbt.StorageTagCompound;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.Size;
import simplepets.brainsynder.api.entity.passive.IEntityHorsePet;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.v1_13_R1.entities.branch.EntityHorseAbstractPet;
import simplepets.brainsynder.nms.v1_13_R1.registry.Types;
import simplepets.brainsynder.player.PetOwner;
import simplepets.brainsynder.wrapper.HorseArmorType;
import simplepets.brainsynder.wrapper.HorseColorType;
import simplepets.brainsynder.wrapper.HorseStyleType;

@Size(width = 1.4F, length = 1.6F)
public class EntityHorsePet extends EntityHorseAbstractPet implements IEntityHorsePet {
    private static final DataWatcherObject<Integer> HORSE_VARIANT;
    private static final DataWatcherObject<Integer> HORSE_ARMOR;

    static {
        HORSE_VARIANT = DataWatcher.a(EntityHorsePet.class, DataWatcherRegistry.b);
        HORSE_ARMOR = DataWatcher.a(EntityHorsePet.class, DataWatcherRegistry.b);
    }

    public EntityHorsePet(World world) {
        super(Types.HORSE, world);
    }
    public EntityHorsePet(World world, IPet pet) {
        super(Types.HORSE, world, pet);
    }

    @Override
    protected void registerDatawatchers() {
        super.registerDatawatchers();
        this.datawatcher.register(HORSE_VARIANT, 0);
        this.datawatcher.register(HORSE_ARMOR, 0);
    }

    private int getVariant() {
        return this.datawatcher.get(HORSE_VARIANT);
    }

    @Override
    public HorseStyleType getStyle() {
        return HorseStyleType.values()[this.getVariant() >>> 8];
    }

    @Override
    public void setStyle(HorseStyleType style) {
        this.datawatcher.set(HORSE_VARIANT, this.getColor().ordinal() & 255 | style.ordinal() << 8);
        PetCore.get().getInvLoaders().PET_DATA.update(PetOwner.getPetOwner(getOwner()));
    }

    @Override
    public HorseColorType getColor() {
        return HorseColorType.values()[this.getVariant() & 255];
    }

    @Override
    public void setColor(HorseColorType color) {
        this.datawatcher.set(HORSE_VARIANT, color.ordinal() & 255 | this.getStyle().ordinal() << 8);
        PetCore.get().getInvLoaders().PET_DATA.update(PetOwner.getPetOwner(getOwner()));
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = super.asCompound();
        object.setString("armor", getArmor().name());
        object.setString("horsecolor", getColor().name());
        object.setString("style", getStyle().name());
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("armor")) setArmor(HorseArmorType.getByName(object.getString("armor")));
        if (object.hasKey("color")) setColor(HorseColorType.getByName(object.getString("color")));
        if (object.hasKey("style")) setStyle(HorseStyleType.getByName(object.getString("style")));
        super.applyCompound(object);
    }

    @Override
    public void setArmor(HorseArmorType a) {
        this.datawatcher.set(HORSE_ARMOR, a.ordinal());
    }

    @Override
    public HorseArmorType getArmor() {
        return HorseArmorType.values()[datawatcher.get(HORSE_ARMOR)];
    }
}
