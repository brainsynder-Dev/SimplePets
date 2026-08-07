package simplepets.brainsynder.nms.entity.list;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.animal.chicken.ChickenSoundVariant;
import net.minecraft.world.entity.animal.chicken.ChickenSoundVariants;
import net.minecraft.world.entity.animal.chicken.ChickenVariant;
import net.minecraft.world.entity.animal.chicken.ChickenVariants;
import org.bsdevelopment.nbt.StorageTagCompound;
import org.bsdevelopment.pluginutils.libs.json.JsonObject;
import org.bukkit.craftbukkit.CraftRegistry;
import org.bukkit.craftbukkit.util.CraftNamespacedKey;
import simplepets.brainsynder.api.entity.passive.IEntityChickenPet;
import simplepets.brainsynder.api.pet.PetDataRegistry;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.api.wrappers.TemperatureVariant;
import simplepets.brainsynder.nms.EntitySelector;
import simplepets.brainsynder.nms.entity.EntityAgeablePet;
import simplepets.brainsynder.nms.utils.PetDataAccess;
import simplepets.brainsynder.nms.utils.VariantUtils;

/**
 * NMS: {@link net.minecraft.world.entity.animal.chicken.Chicken}
 */
public class EntityChickenPet extends EntityAgeablePet implements IEntityChickenPet {
    private static final EntityDataAccessor<Holder<ChickenVariant>> VARIANT = SynchedEntityData.defineId(EntityChickenPet.class, EntityDataSerializers.CHICKEN_VARIANT);
    private static final EntityDataAccessor<Holder<ChickenSoundVariant>> DATA_SOUND_VARIANT_ID = SynchedEntityData.defineId(EntityChickenPet.class, EntityDataSerializers.CHICKEN_SOUND_VARIANT);
    private TemperatureVariant variant = TemperatureVariant.TEMPERATE;

    public EntityChickenPet(PetType type, PetUser user) {
        super(EntitySelector.CHICKEN, type, user);
    }

    @Override
    public void populateDataAccess(PetDataAccess dataAccess) {
        super.populateDataAccess(dataAccess);
        dataAccess.define(VARIANT, VariantUtils.getDefaultOrAny(registryAccess(), ChickenVariants.TEMPERATE));

        Registry<ChickenSoundVariant> chickenSoundVariants = this.registryAccess().lookupOrThrow(Registries.CHICKEN_SOUND_VARIANT);
        dataAccess.define(DATA_SOUND_VARIANT_ID, chickenSoundVariants.get(ChickenSoundVariants.CLASSIC).or(chickenSoundVariants::getAny).orElseThrow());
    }

    @Override
    public TemperatureVariant getVariant() {
        return variant;
    }

    @Override
    public void setVariant(TemperatureVariant variant) {
        this.variant = variant;

        Registry<ChickenVariant> registry = CraftRegistry.getMinecraftRegistry(Registries.CHICKEN_VARIANT);
        entityData.set(VARIANT, registry.wrapAsHolder(registry.getValue(CraftNamespacedKey.toMinecraft(variant.getKey()))));
    }

    @Override
    public void fetchPetData(JsonObject data) {
        super.fetchPetData(data);
        data.add("variant", getVariant().name());
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound compound = super.asCompound();
        compound.setEnum(PetDataRegistry.Chicken.VARIANT.namespace(), getVariant());
        return compound;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey(PetDataRegistry.Chicken.VARIANT.namespace()))
            setVariant(object.getEnum(PetDataRegistry.Chicken.VARIANT.namespace(), TemperatureVariant.class, TemperatureVariant.TEMPERATE));
        super.applyCompound(object);
    }
}
