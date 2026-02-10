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
import net.minecraft.world.entity.animal.CowVariant;
import net.minecraft.world.entity.animal.CowVariants;
import org.bukkit.craftbukkit.v1_21_R5.CraftRegistry;
import org.bukkit.craftbukkit.v1_21_R5.util.CraftNamespacedKey;
import simplepets.brainsynder.api.entity.passive.IEntityCowPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.api.wrappers.TemperatureVariant;
import simplepets.brainsynder.nms.entity.EntityAgeablePet;
import simplepets.brainsynder.nms.utils.PetDataAccess;
import simplepets.brainsynder.nms.utils.VariantUtils;

/**
 * NMS: {@link net.minecraft.world.entity.animal.Cow}
 */
public class EntityCowPet extends EntityAgeablePet implements IEntityCowPet {
    private static final EntityDataAccessor<Holder<CowVariant>> VARIANT = SynchedEntityData.defineId(EntityCowPet.class, EntityDataSerializers.COW_VARIANT);
    private TemperatureVariant variant = TemperatureVariant.TEMPERATE;

    public EntityCowPet(PetType type, PetUser user) {
        super(EntityType.COW, type, user);
    }

    @Override
    public void populateDataAccess(PetDataAccess dataAccess) {
        super.populateDataAccess(dataAccess);
        dataAccess.define(VARIANT, VariantUtils.getDefaultOrAny(registryAccess(), CowVariants.TEMPERATE));
    }

    @Override
    public void setVariant(TemperatureVariant variant) {
        this.variant = variant;

        Registry<CowVariant> registry = CraftRegistry.getMinecraftRegistry(Registries.COW_VARIANT);
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
