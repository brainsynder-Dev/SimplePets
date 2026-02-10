package simplepets.brainsynder.nms.entity.list;

import lib.brainsynder.ServerVersion;
import lib.brainsynder.SupportedVersion;
import lib.brainsynder.json.JsonObject;
import lib.brainsynder.nbt.StorageTagCompound;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.nautilus.ZombieNautilusVariant;
import net.minecraft.world.entity.animal.nautilus.ZombieNautilusVariants;
import org.bukkit.craftbukkit.v1_21_R7.CraftRegistry;
import org.bukkit.craftbukkit.v1_21_R7.util.CraftNamespacedKey;
import simplepets.brainsynder.api.entity.passive.IEntityZombieNautilusPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.api.wrappers.ZombieNautilusPetVariant;
import simplepets.brainsynder.nms.entity.branch.EntityNautilusAbstractPet;
import simplepets.brainsynder.nms.utils.PetDataAccess;
import simplepets.brainsynder.nms.utils.VariantUtils;

/**
 * NMS: {@link net.minecraft.world.entity.animal.nautilus.ZombieNautilus }
 */
@SupportedVersion(version = ServerVersion.v1_21_11)
public class EntityZombieNautilusPet extends EntityNautilusAbstractPet implements IEntityZombieNautilusPet {
    private static final EntityDataAccessor<Holder<ZombieNautilusVariant>> VARIANT = SynchedEntityData.defineId(EntityZombieNautilusPet.class, EntityDataSerializers.ZOMBIE_NAUTILUS_VARIANT);
    private ZombieNautilusPetVariant variant = ZombieNautilusPetVariant.TEMPERATE;

    public EntityZombieNautilusPet(PetType type, PetUser user) {
        super(EntityType.ZOMBIE_NAUTILUS, type, user);
    }

    @Override
    public void populateDataAccess(PetDataAccess dataAccess) {
        super.populateDataAccess(dataAccess);
        dataAccess.define(VARIANT, VariantUtils.getDefaultOrAny(registryAccess(), ZombieNautilusVariants.TEMPERATE));
    }

    @Override
    public ZombieNautilusPetVariant getVariant() {
        return variant;
    }

    @Override
    public void setVariant(ZombieNautilusPetVariant variant) {
        this.variant = variant;

        Registry<ZombieNautilusVariant> registry = CraftRegistry.getMinecraftRegistry(Registries.ZOMBIE_NAUTILUS_VARIANT);
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
        object.setEnum("variant", getVariant());
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("variant")) setVariant(object.getEnum("variant", ZombieNautilusPetVariant.class, ZombieNautilusPetVariant.TEMPERATE));
        super.applyCompound(object);
    }
}
