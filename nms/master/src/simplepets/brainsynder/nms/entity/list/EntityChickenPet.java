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
import net.minecraft.world.entity.animal.chicken.ChickenVariant;
import net.minecraft.world.entity.animal.chicken.ChickenVariants;
import org.bukkit.craftbukkit.v1_21_R7.CraftRegistry;
import org.bukkit.craftbukkit.v1_21_R7.util.CraftNamespacedKey;
import simplepets.brainsynder.api.entity.passive.IEntityChickenPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.api.wrappers.TemperatureVariant;
import simplepets.brainsynder.nms.entity.EntityAgeablePet;
import simplepets.brainsynder.nms.utils.PetDataAccess;
import simplepets.brainsynder.nms.utils.VariantUtils;

/**
 * NMS: {@link net.minecraft.world.entity.animal.chicken.Chicken}
 */
public class EntityChickenPet extends EntityAgeablePet implements IEntityChickenPet {
    private static final EntityDataAccessor<Holder<ChickenVariant>> VARIANT = SynchedEntityData.defineId(EntityChickenPet.class, EntityDataSerializers.CHICKEN_VARIANT);
    private TemperatureVariant variant = TemperatureVariant.TEMPERATE;

    public EntityChickenPet(PetType type, PetUser user) {
        super(EntityType.CHICKEN, type, user);
    }

    @Override
    public void populateDataAccess(PetDataAccess dataAccess) {
        super.populateDataAccess(dataAccess);
        dataAccess.define(VARIANT, VariantUtils.getDefaultOrAny(registryAccess(), ChickenVariants.TEMPERATE));
    }

    @Override
    public void setVariant(TemperatureVariant variant) {
        this.variant = variant;

        Registry<ChickenVariant> registry = CraftRegistry.getMinecraftRegistry(Registries.CHICKEN_VARIANT);
        entityData.set(VARIANT, registry.wrapAsHolder(registry.getValue(CraftNamespacedKey.toMinecraft(variant.getKey()))));
    }

    @Override
    public TemperatureVariant getVariant() {
        return variant;
    }

    @Override
    public void fetchPetData(JsonObject data) {
        super.fetchPetData(data);
        data.add("variant", getVariant().name());
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound compound = super.asCompound();
        compound.setEnum("variant", getVariant());
        return compound;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("variant")) setVariant(object.getEnum("variant", TemperatureVariant.class, TemperatureVariant.TEMPERATE));
        super.applyCompound(object);
    }
}
