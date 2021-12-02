package simplepets.brainsynder.versions.v1_18_R1.entity.list;

import lib.brainsynder.nbt.StorageTagCompound;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_18_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import simplepets.brainsynder.api.entity.passive.IEntityHorsePet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.api.wrappers.horse.HorseArmorType;
import simplepets.brainsynder.api.wrappers.horse.HorseColorType;
import simplepets.brainsynder.api.wrappers.horse.HorseStyleType;
import simplepets.brainsynder.versions.v1_18_R1.entity.branch.EntityHorseAbstractPet;

/**
 * NMS: {@link net.minecraft.server.v1_16_R3.EntityHorse}
 */
public class EntityHorsePet extends EntityHorseAbstractPet implements IEntityHorsePet {
    private static final EntityDataAccessor<Integer> HORSE_VARIANT;
    private HorseArmorType armor = null;

    public EntityHorsePet(PetType type, PetUser user) {
        super(EntityType.HORSE, type, user);
    }

    @Override
    protected void registerDatawatchers() {
        super.registerDatawatchers();
        this.entityData.define(HORSE_VARIANT, 0);
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = super.asCompound();
        object.setString("armor", getArmor().name());
        object.setString("color", getColor().name());
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
    public HorseArmorType getArmor() {
        if (armor == null) return HorseArmorType.NONE;
        return armor;
    }

    @Override
    public void setArmor(HorseArmorType armor) {
        this.armor = armor;
        Material material = Material.AIR;
        switch (armor) {
            case LEATHER:
                material = Material.LEATHER_HORSE_ARMOR;
                break;
            case IRON:
                material = Material.IRON_HORSE_ARMOR;
                break;
            case GOLD:
                material = Material.GOLDEN_HORSE_ARMOR;
                break;
            case DIAMOND:
                material = Material.DIAMOND_HORSE_ARMOR;
                break;
        }
        ItemStack stack = new ItemStack(material);
        this.setItemSlot(EquipmentSlot.CHEST, CraftItemStack.asNMSCopy(stack));
        this.setDropChance(EquipmentSlot.CHEST, 0.0F);
    }

    @Override
    public HorseStyleType getStyle() {
        return HorseStyleType.values()[this.getVariant() >>> 8];
    }

    @Override
    public void setStyle(HorseStyleType style) {
        this.entityData.set(HORSE_VARIANT, this.getColor().ordinal() & 255 | style.ordinal() << 8);
    }

    @Override
    public HorseColorType getColor() {
        return HorseColorType.values()[this.getVariant() & 255];
    }

    @Override
    public void setColor(HorseColorType color) {
        this.entityData.set(HORSE_VARIANT, color.ordinal() & 255 | this.getStyle().ordinal() << 8);

    }

    private int getVariant() {
        return this.entityData.get(HORSE_VARIANT);
    }

    static {
        HORSE_VARIANT = SynchedEntityData.defineId(EntityHorsePet.class, EntityDataSerializers.INT);
    }
}
