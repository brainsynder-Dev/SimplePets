package simplepets.brainsynder.nms.entity.list;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.animal.cow.CowSoundVariant;
import net.minecraft.world.entity.animal.cow.CowSoundVariants;
import net.minecraft.world.entity.animal.cow.CowVariant;
import net.minecraft.world.entity.animal.cow.CowVariants;
import org.bsdevelopment.nbt.StorageTagCompound;
import org.bsdevelopment.pluginutils.libs.json.JsonObject;
import org.bukkit.craftbukkit.CraftRegistry;
import org.bukkit.craftbukkit.util.CraftNamespacedKey;
import simplepets.brainsynder.api.entity.passive.IEntityCowPet;
import simplepets.brainsynder.api.pet.PetDataRegistry;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.api.wrappers.TemperatureVariant;
import simplepets.brainsynder.nms.EntitySelector;
import simplepets.brainsynder.nms.entity.EntityAgeablePet;
import simplepets.brainsynder.nms.utils.PetDataAccess;
import simplepets.brainsynder.nms.utils.VariantUtils;

/**
 * NMS: {@link net.minecraft.world.entity.animal.cow.Cow}
 */
public class EntityCowPet extends EntityAgeablePet implements IEntityCowPet {
    private static final EntityDataAccessor<Holder<CowVariant>> VARIANT = SynchedEntityData.defineId(EntityCowPet.class, EntityDataSerializers.COW_VARIANT);
    private static final EntityDataAccessor<Holder<CowSoundVariant>> DATA_SOUND_VARIANT_ID = SynchedEntityData.defineId(EntityCowPet.class, EntityDataSerializers.COW_SOUND_VARIANT);
    private TemperatureVariant variant = TemperatureVariant.TEMPERATE;

    public EntityCowPet(PetType type, PetUser user) {
        super(EntitySelector.COW, type, user);
    }

    @Override
    public void populateDataAccess(PetDataAccess dataAccess) {
        super.populateDataAccess(dataAccess);
        dataAccess.define(VARIANT, VariantUtils.getDefaultOrAny(registryAccess(), CowVariants.TEMPERATE));

        Registry<CowSoundVariant> cowSoundVariants = this.registryAccess().lookupOrThrow(Registries.COW_SOUND_VARIANT);
        dataAccess.define(DATA_SOUND_VARIANT_ID, cowSoundVariants.get(CowSoundVariants.CLASSIC).or(cowSoundVariants::getAny).orElseThrow());
    }

    @Override
    public TemperatureVariant getVariant() {
        return variant;
    }

    @Override
    public void setVariant(TemperatureVariant variant) {
        this.variant = variant;

        Registry<CowVariant> registry = CraftRegistry.getMinecraftRegistry(Registries.COW_VARIANT);
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
        compound.setEnum(PetDataRegistry.Cow.VARIANT.namespace(), getVariant());
        return compound;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey(PetDataRegistry.Cow.VARIANT.namespace()))
            setVariant(object.getEnum(PetDataRegistry.Cow.VARIANT.namespace(), TemperatureVariant.class, TemperatureVariant.TEMPERATE));
        super.applyCompound(object);
    }
}
