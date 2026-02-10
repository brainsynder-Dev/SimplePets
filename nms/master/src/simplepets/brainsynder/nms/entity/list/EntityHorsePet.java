package simplepets.brainsynder.nms.entity.list;

import lib.brainsynder.json.JsonObject;
import lib.brainsynder.nbt.StorageTagCompound;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import org.bukkit.craftbukkit.v1_21_R7.CraftRegistry;
import simplepets.brainsynder.api.entity.passive.IEntityHorsePet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.api.wrappers.horse.HorseArmorType;
import simplepets.brainsynder.api.wrappers.horse.HorseColorType;
import simplepets.brainsynder.api.wrappers.horse.HorseStyleType;
import simplepets.brainsynder.nms.VersionTranslator;
import simplepets.brainsynder.nms.entity.branch.EntityHorseAbstractPet;
import simplepets.brainsynder.nms.utils.PetDataAccess;

/**
 * NMS: {@link net.minecraft.world.entity.animal.horse.Horse}
 */
public class EntityHorsePet extends EntityHorseAbstractPet implements IEntityHorsePet {
    private static final EntityDataAccessor<Integer> HORSE_VARIANT = SynchedEntityData.defineId(EntityHorsePet.class, EntityDataSerializers.INT);
    private HorseArmorType armor = null;

    public EntityHorsePet(PetType type, PetUser user) {
        super(EntityType.HORSE, type, user);
    }

    @Override
    public void fetchPetData(JsonObject data) {
        super.fetchPetData(data);
        data.add("armor", getArmor().name());
        data.add("color", getColor().name());
        data.add("style", getStyle().name());
    }

    @Override
    public void populateDataAccess(PetDataAccess dataAccess) {
        super.populateDataAccess(dataAccess);
        dataAccess.define(HORSE_VARIANT, 0);
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
        if (!armor.isSupported()) return;
        this.armor = armor;

        if (armor == null) {
            setItemSlot(EquipmentSlot.BODY, Items.AIR.getDefaultInstance());
            return;
        }

        Registry<Item> registry = CraftRegistry.getMinecraftRegistry(Registries.ITEM);
        setItemSlot(EquipmentSlot.BODY, VersionTranslator.getRegistryValue(registry, armor.getKey()).getDefaultInstance());
    }

    @Override
    public HorseStyleType getStyle() {
        return HorseStyleType.values()[(this.getTypeVariant() & '\uff00') >> 8];
    }

    @Override
    public void setStyle(HorseStyleType style) {
        updateHorse(getColor(), style);
    }

    @Override
    public HorseColorType getColor() {
        return HorseColorType.values()[this.getTypeVariant() & 255];
    }

    @Override
    public void setColor(HorseColorType color) {
        updateHorse(color, getStyle());
    }

    private int getTypeVariant() {
        return this.entityData.get(HORSE_VARIANT);
    }

    private void updateHorse (HorseColorType colorType, HorseStyleType styleType) {
        this.entityData.set(HORSE_VARIANT, ( colorType.ordinal() & 255 | styleType.ordinal() << 8 & '\uff00' ));
    }
}
