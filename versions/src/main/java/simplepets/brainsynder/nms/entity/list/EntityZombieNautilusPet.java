package simplepets.brainsynder.nms.entity.list;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.animal.nautilus.ZombieNautilusVariants;
import org.bsdevelopment.nbt.StorageTagCompound;
import org.bsdevelopment.pluginutils.libs.json.JsonObject;
import org.bsdevelopment.pluginutils.version.VersionLimit;
import org.bukkit.craftbukkit.CraftRegistry;
import org.bukkit.craftbukkit.util.CraftNamespacedKey;
import simplepets.brainsynder.api.entity.passive.IEntityZombieNautilusPet;
import simplepets.brainsynder.api.pet.PetDataRegistry;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.api.wrappers.ZombieNautilusVariant;
import simplepets.brainsynder.nms.EntitySelector;
import simplepets.brainsynder.nms.entity.branch.EntityNautilusAbstractPet;
import simplepets.brainsynder.nms.utils.PetDataAccess;
import simplepets.brainsynder.nms.utils.VariantUtils;

/**
 * NMS: {@link net.minecraft.world.entity.animal.nautilus.ZombieNautilus }
 */
@VersionLimit(min = {1, 21, 11})
public class EntityZombieNautilusPet extends EntityNautilusAbstractPet implements IEntityZombieNautilusPet {
    private static final EntityDataAccessor<Holder<net.minecraft.world.entity.animal.nautilus.ZombieNautilusVariant>> VARIANT = SynchedEntityData.defineId(EntityZombieNautilusPet.class, EntityDataSerializers.ZOMBIE_NAUTILUS_VARIANT);
    private ZombieNautilusVariant variant = ZombieNautilusVariant.TEMPERATE;

    public EntityZombieNautilusPet(PetType type, PetUser user) {
        super(EntitySelector.ZOMBIE_NAUTILUS, type, user);
    }

    @Override
    public void populateDataAccess(PetDataAccess dataAccess) {
        super.populateDataAccess(dataAccess);
        dataAccess.define(VARIANT, VariantUtils.getDefaultOrAny(registryAccess(), ZombieNautilusVariants.TEMPERATE));
    }

    @Override
    public ZombieNautilusVariant getVariant() {
        return variant;
    }

    @Override
    public void setVariant(ZombieNautilusVariant variant) {
        this.variant = variant;

        Registry<net.minecraft.world.entity.animal.nautilus.ZombieNautilusVariant> registry = CraftRegistry.getMinecraftRegistry(Registries.ZOMBIE_NAUTILUS_VARIANT);
        entityData.set(VARIANT, registry.wrapAsHolder(registry.getValue(CraftNamespacedKey.toMinecraft(variant.getKey()))));
    }


    @Override
    public void fetchPetData(JsonObject data) {
        super.fetchPetData(data);
        data.add("variant", variant.getKey().toString());
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = super.asCompound();
        object.setEnum(PetDataRegistry.ZombieNautilus.VARIANT.namespace(), getVariant());
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey(PetDataRegistry.ZombieNautilus.VARIANT.namespace())) setVariant(object.getEnum(PetDataRegistry.ZombieNautilus.VARIANT.namespace(), ZombieNautilusVariant.class, ZombieNautilusVariant.TEMPERATE));
        super.applyCompound(object);
    }
}
