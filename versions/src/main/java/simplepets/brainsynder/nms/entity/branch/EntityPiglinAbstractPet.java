package simplepets.brainsynder.nms.entity.branch;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import org.bsdevelopment.nbt.StorageTagCompound;
import org.bsdevelopment.pluginutils.libs.json.JsonObject;
import simplepets.brainsynder.api.entity.misc.IShaking;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.nms.entity.EntityPetOverride;
import simplepets.brainsynder.nms.utils.PetDataAccess;

import static simplepets.brainsynder.api.pet.PetDataRegistry.SHAKE;

/**
 * NMS: {@link net.minecraft.world.entity.monster.piglin.AbstractPiglin}
 */
public abstract class EntityPiglinAbstractPet extends EntityPetOverride implements IShaking {
    private static final EntityDataAccessor<Boolean> IMMUNE_TO_ZOMBIFICATION = SynchedEntityData.defineId(EntityPiglinAbstractPet.class, EntityDataSerializers.BOOLEAN);

    public EntityPiglinAbstractPet(EntityType<? extends Mob> entitytypes, PetType type, PetUser user) {
        super(entitytypes, type, user);
        setShaking(false);
    }

    @Override
    public void fetchPetData(JsonObject data) {
        super.fetchPetData(data);
        data.add("shaking", isShaking());
    }

    @Override
    public void populateDataAccess(PetDataAccess dataAccess) {
        super.populateDataAccess(dataAccess);
        dataAccess.define(IMMUNE_TO_ZOMBIFICATION, false);
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = super.asCompound();
        object.setBoolean(SHAKE.namespace(), isShaking());
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey(SHAKE.namespace())) setShaking(object.getBoolean(SHAKE.namespace()));
        super.applyCompound(object);
    }

    @Override
    public boolean isShaking() {
        return !entityData.get(IMMUNE_TO_ZOMBIFICATION);
    }

    @Override
    public void setShaking(boolean shaking) {
        entityData.set(IMMUNE_TO_ZOMBIFICATION, !shaking);
    }
}
