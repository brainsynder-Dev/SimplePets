package simplepets.brainsynder.nms.entity.list;

import lib.brainsynder.ServerVersion;
import lib.brainsynder.SupportedVersion;
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

/**
 * NMS: {@link net.minecraft.world.entity.animal.camel.Camel}
 */
@SupportedVersion(version = ServerVersion.v1_20)
public class EntityCamelPet extends EntityHorseAbstractPet implements IEntityCamelPet {
    private static final EntityDataAccessor<Boolean> DASH;
    private static final EntityDataAccessor<Long> LAST_POSE_CHANGE_TICK;

    public EntityCamelPet(PetType type, PetUser user) {
        this(EntityType.CAMEL, type, user);
    }

    public EntityCamelPet(EntityType<? extends Mob> llama, PetType type, PetUser user) {
        super(llama, type, user);
        doIndirectAttach = false;
    }

    @Override
    protected void registerDatawatchers() {
        super.registerDatawatchers();
        entityData.define(DASH, false);
        entityData.define(LAST_POSE_CHANGE_TICK, 0L);
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = super.asCompound();

        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        // if (object.hasKey("skin")) setSkinColor(LlamaColor.getByName(object.getString("skin")));
        super.applyCompound(object);
    }


    static {
        DASH = SynchedEntityData.defineId(EntityCamelPet.class, EntityDataSerializers.BOOLEAN);
        LAST_POSE_CHANGE_TICK = SynchedEntityData.defineId(EntityCamelPet.class, EntityDataSerializers.LONG);
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

    @Override
    public boolean isDashing() {
        return entityData.get(DASH);
    }

    @Override
    public void setDashing(boolean dashing) {
        entityData.set(DASH, dashing);
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
