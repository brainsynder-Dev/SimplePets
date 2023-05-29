package simplepets.brainsynder.nms.entity.list;

import lib.brainsynder.nbt.StorageTagCompound;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.animal.horse.Markings;
import net.minecraft.world.entity.animal.horse.Variant;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import simplepets.brainsynder.api.entity.passive.IEntityHorsePet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.api.wrappers.horse.HorseArmorType;
import simplepets.brainsynder.api.wrappers.horse.HorseColorType;
import simplepets.brainsynder.api.wrappers.horse.HorseStyleType;
import simplepets.brainsynder.nms.VersionTranslator;
import simplepets.brainsynder.nms.entity.branch.EntityHorseAbstractPet;

/**
 * NMS: {@link net.minecraft.world.entity.animal.horse.Horse}
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
        this.setItemSlot(EquipmentSlot.CHEST, VersionTranslator.toNMSStack(stack));
        this.setDropChance(EquipmentSlot.CHEST, 0.0F);
    }

    @Override
    public HorseStyleType getStyle() {
        return HorseStyleType.values()[(this.getTypeVariant() & '\uff00') >> 8];
    }

    @Override
    public void setStyle(HorseStyleType style) {
        setVariantAndMarkings(getVariant(), Markings.byId(style.ordinal()));
    }

    @Override
    public HorseColorType getColor() {
        return HorseColorType.values()[this.getTypeVariant() & 255];
    }

    @Override
    public void setColor(HorseColorType color) {
        setVariantAndMarkings(net.minecraft.world.entity.animal.horse.Variant.byId(color.ordinal()), getMarkings());
    }

    private int getTypeVariant() {
        return this.entityData.get(HORSE_VARIANT);
    }

    // ----- START NMS METHODS ----- //

    /*

    Note: It seems the data is getting set but the entity is not
          getting updated for the actual user...

          I am not sure if it is just the owner or all players...

     */

    @Deprecated
    private void setVariantAndMarkings(Variant var0, Markings var1) {
        this.setTypeVariant(var0.getId() & 255 | var1.getId() << 8 & '\uff00');
    }

    @Deprecated
    private void setTypeVariant(int var0) {
        this.entityData.set(HORSE_VARIANT, var0);
    }

    @Deprecated
    private Variant getVariant() {
        return Variant.byId(this.getTypeVariant() & 255);
    }

    @Deprecated
    private Markings getMarkings() {
        return Markings.byId((this.getTypeVariant() & '\uff00') >> 8);
    }

    // ----- END NMS METHODS ----- //

    static {
        HORSE_VARIANT = SynchedEntityData.defineId(EntityHorsePet.class, EntityDataSerializers.INT);
    }
}
