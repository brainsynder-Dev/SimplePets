package simplepets.brainsynder.nms.entity.list;

import lib.brainsynder.json.JsonObject;
import lib.brainsynder.nbt.StorageTagCompound;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.animal.PigVariant;
import net.minecraft.world.entity.animal.PigVariants;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.bukkit.craftbukkit.v1_21_R6.CraftRegistry;
import org.bukkit.craftbukkit.v1_21_R6.util.CraftNamespacedKey;
import simplepets.brainsynder.api.entity.passive.IEntityPigPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.api.wrappers.TemperatureVariant;
import simplepets.brainsynder.nms.entity.EntityAgeablePet;
import simplepets.brainsynder.nms.utils.PetDataAccess;
import simplepets.brainsynder.nms.utils.VariantUtils;

/**
 * NMS: {@link net.minecraft.world.entity.animal.Pig}
 */
public class EntityPigPet extends EntityAgeablePet implements IEntityPigPet {
    private static final EntityDataAccessor<Integer> DATA_BOOST_TIME = SynchedEntityData.defineId(EntityPigPet.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Holder<PigVariant>> VARIANT = SynchedEntityData.defineId(EntityPigPet.class, EntityDataSerializers.PIG_VARIANT);
    private TemperatureVariant variant = TemperatureVariant.TEMPERATE;

    public EntityPigPet(PetType type, PetUser user) {
        super(EntityType.PIG, type, user);
        doIndirectAttach = true;
    }

    @Override
    public void populateDataAccess(PetDataAccess dataAccess) {
        super.populateDataAccess(dataAccess);
        dataAccess.define(DATA_BOOST_TIME, 0);
        dataAccess.define(VARIANT, VariantUtils.getDefaultOrAny(registryAccess(), PigVariants.TEMPERATE));
    }

    @Override
    public void setVariant(TemperatureVariant variant) {
        this.variant = variant;

        Registry<PigVariant> registry = CraftRegistry.getMinecraftRegistry(Registries.PIG_VARIANT);
        entityData.set(VARIANT, registry.wrapAsHolder(registry.getValue(CraftNamespacedKey.toMinecraft(variant.getKey()))));
    }

    @Override
    public TemperatureVariant getVariant() {
        return variant;
    }

    @Override
    public boolean isPetSaddled() {
        return getItemBySlot(EquipmentSlot.SADDLE).is(Items.SADDLE);
    }

    @Override
    public void setPetSaddled(boolean saddled) {
        if (saddled) {
            setItemSlot(EquipmentSlot.SADDLE, Items.SADDLE.getDefaultInstance(), true);
        } else {
            setItemSlot(EquipmentSlot.SADDLE, ItemStack.EMPTY, true);
        }
    }

    @Override
    public void fetchPetData(JsonObject data) {
        super.fetchPetData(data);
        data.add("saddled", isPetSaddled());
        data.add("variant", variant.getKey().toString());
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = super.asCompound();
        object.setBoolean("saddled", isPetSaddled());
        object.setEnum("variant", getVariant());
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("saddled")) setPetSaddled(object.getBoolean("saddled"));
        if (object.hasKey("variant")) setVariant(object.getEnum("variant", TemperatureVariant.class, TemperatureVariant.TEMPERATE));
        super.applyCompound(object);
    }
}
