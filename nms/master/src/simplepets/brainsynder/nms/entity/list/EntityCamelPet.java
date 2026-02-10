package simplepets.brainsynder.nms.entity.list;

import lib.brainsynder.ServerVersion;
import lib.brainsynder.SupportedVersion;
import lib.brainsynder.json.JsonObject;
import lib.brainsynder.nbt.StorageTagCompound;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Pose;
import simplepets.brainsynder.api.entity.passive.IEntityCamelPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.nms.VersionTranslator;
import simplepets.brainsynder.nms.entity.branch.EntityHorseAbstractPet;
import simplepets.brainsynder.nms.utils.PetDataAccess;

/**
 * NMS: {@link net.minecraft.world.entity.animal.camel.Camel}
 */
@SupportedVersion(version = ServerVersion.v1_20)
public class EntityCamelPet extends EntityHorseAbstractPet implements IEntityCamelPet {
    private static final EntityDataAccessor<Boolean> DASH = SynchedEntityData.defineId(EntityCamelPet.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Long> LAST_POSE_CHANGE_TICK = SynchedEntityData.defineId(EntityCamelPet.class, EntityDataSerializers.LONG);

    public EntityCamelPet(PetType type, PetUser user) {
        this(EntityType.CAMEL, type, user);
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
        object.setBoolean("sitting", isSitting());
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("sitting")) setSitting(object.getBoolean("sitting"));
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
            entityData.set(LAST_POSE_CHANGE_TICK, -VersionTranslator.getEntityLevel(this).getGameTime());
        }
    }

    public void standUp() {
        if (isSitting()) {
            playSound(SoundEvents.CAMEL_STAND, 1.0F, 1.0F);
            setPose(Pose.STANDING);
            entityData.set(LAST_POSE_CHANGE_TICK, VersionTranslator.getEntityLevel(this).getGameTime());
        }
    }
}
