package simplepets.brainsynder.nms.v1_14_R1.entities.list;

import lib.brainsynder.nbt.StorageTagCompound;
import net.minecraft.server.v1_14_R1.*;
import org.bukkit.craftbukkit.v1_14_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.Size;
import simplepets.brainsynder.api.entity.passive.IEntityHorsePet;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.v1_14_R1.entities.branch.EntityHorseAbstractPet;
import simplepets.brainsynder.nms.v1_14_R1.utils.DataWatcherWrapper;
import simplepets.brainsynder.player.PetOwner;
import simplepets.brainsynder.wrapper.HorseArmorType;
import simplepets.brainsynder.wrapper.HorseColorType;
import simplepets.brainsynder.wrapper.HorseStyleType;

/**
 * NMS: {@link net.minecraft.server.v1_14_R1.EntityHorse}
 */
@Size(width = 1.4F, length = 1.6F)
public class EntityHorsePet extends EntityHorseAbstractPet implements IEntityHorsePet {
    private static final DataWatcherObject<Integer> HORSE_VARIANT;
    private HorseArmorType armor = null;
    // private static final DataWatcherObject<Integer> HORSE_ARMOR; | Was removed?

    static {
        HORSE_VARIANT = DataWatcher.a(EntityHorsePet.class, DataWatcherWrapper.INT);
    }

    public EntityHorsePet(EntityTypes<? extends EntityCreature> type, World world) {
        super(type, world);
    }
    public EntityHorsePet(EntityTypes<? extends EntityCreature> type, World world, IPet pet) {
        super(type, world, pet);
    }

    @Override
    protected void registerDatawatchers() {
        super.registerDatawatchers();
        this.datawatcher.register(HORSE_VARIANT, 0);
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
    public void setArmor(HorseArmorType armor) {
        this.armor = armor;
        org.bukkit.Material material = org.bukkit.Material.AIR;
        switch (armor) {
            case IRON:
                material = org.bukkit.Material.IRON_HORSE_ARMOR;
                break;
            case GOLD:
                material = org.bukkit.Material.GOLDEN_HORSE_ARMOR;
                break;
            case DIAMOND:
                material = org.bukkit.Material.DIAMOND_HORSE_ARMOR;
                break;
        }
        ItemStack stack = new ItemStack(material);
        this.setSlot(EnumItemSlot.CHEST, CraftItemStack.asNMSCopy(stack));
        this.a(EnumItemSlot.CHEST, 0.0F);
    }

    @Override
    public HorseArmorType getArmor() {
        if (armor == null) return HorseArmorType.NONE;
        return armor;
    }
}
