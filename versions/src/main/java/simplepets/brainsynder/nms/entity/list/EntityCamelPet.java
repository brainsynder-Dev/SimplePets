package simplepets.brainsynder.nms.entity.list;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Pose;
import org.bsdevelopment.nbt.StorageTagCompound;
import org.bsdevelopment.pluginutils.libs.json.JsonObject;
import org.bsdevelopment.pluginutils.version.VersionLimit;
import simplepets.brainsynder.api.entity.passive.IEntityCamelPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.nms.EntitySelector;
import simplepets.brainsynder.nms.entity.branch.EntityHorseAbstractPet;
import simplepets.brainsynder.nms.utils.PetDataAccess;

import static simplepets.brainsynder.api.pet.PetDataRegistry.SITTING;

/**
 * NMS: {@link net.minecraft.world.entity.animal.camel.Camel}
 */
@VersionLimit(min = {1, 20, 0})
public class EntityCamelPet extends EntityHorseAbstractPet implements IEntityCamelPet {
    private static final EntityDataAccessor<Boolean> DASH = SynchedEntityData.defineId(EntityCamelPet.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Long> LAST_POSE_CHANGE_TICK = SynchedEntityData.defineId(EntityCamelPet.class, EntityDataSerializers.LONG);

    public EntityCamelPet(PetType type, PetUser user) {
        this(EntitySelector.CAMEL, type, user);
    }

    public EntityCamelPet(EntityType<? extends Mob> entityType, PetType type, PetUser user) {
        super(entityType, type, user);
        doIndirectAttach = false;
    }

    @Override
    public void fetchPetData(JsonObject data) {
        super.fetchPetData(data);
        data.add("sitting", isSitting());
    }

    @Override
    public void populateDataAccess(PetDataAccess dataAccess) {
        super.populateDataAccess(dataAccess);
        dataAccess.define(DASH, false);
        dataAccess.define(LAST_POSE_CHANGE_TICK, 0L);
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = super.asCompound();
        object.setBoolean(SITTING.namespace(), isSitting());
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey(SITTING.namespace())) setSitting(object.getBoolean(SITTING.namespace()));
        super.applyCompound(object);
    }

    @Override
    public boolean isSitting() {
        return (getPose() == Pose.SITTING) && (entityData.get(LAST_POSE_CHANGE_TICK) < 0L);
    }

    @Override
    public void setSitting(boolean sitting) {
        if (sitting) {
            sitDown();
        } else {
            standUp();
        }
    }


    //  ---- Implemented methods directly from the NMS Code ---- //
    public void sitDown() {
        if (!isSitting()) {
            playSound(SoundEvents.CAMEL_SIT, 1.0F, 1.0F);
            setPose(Pose.SITTING);
            entityData.set(LAST_POSE_CHANGE_TICK, -level().getGameTime());
        }
    }

    public void standUp() {
        if (isSitting()) {
            playSound(SoundEvents.CAMEL_STAND, 1.0F, 1.0F);
            setPose(Pose.STANDING);
            entityData.set(LAST_POSE_CHANGE_TICK, level().getGameTime());
        }
    }
}
