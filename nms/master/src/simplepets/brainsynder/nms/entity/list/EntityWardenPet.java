package simplepets.brainsynder.nms.entity.list;

import lib.brainsynder.ServerVersion;
import lib.brainsynder.SupportedVersion;
import lib.brainsynder.json.JsonObject;
import lib.brainsynder.math.MathUtils;
import lib.brainsynder.nbt.StorageTagCompound;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Bukkit;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.entity.hostile.IEntityWardenPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.plugin.config.ConfigOption;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.api.wrappers.AngerLevel;
import simplepets.brainsynder.nms.VersionTranslator;
import simplepets.brainsynder.nms.entity.EntityPetOverride;
import simplepets.brainsynder.nms.utils.PetDataAccess;

/**
 * NMS: {@link net.minecraft.world.entity.monster.warden.Warden}
 */
@SupportedVersion(version = ServerVersion.v1_19)
public class EntityWardenPet extends EntityPetOverride implements IEntityWardenPet {
    protected static final EntityDataAccessor<Integer> ANGER_LEVEL = SynchedEntityData.defineId(EntityWardenPet.class, EntityDataSerializers.INT);
    private boolean vibrationEffect = false;
    private int vibrationTick = 0;

    public EntityWardenPet(PetType type, PetUser user) {
        super(EntityType.WARDEN, type, user);
        if (ConfigOption.INSTANCE.PET_TOGGLES_WARDEN_ANIMATIONS.getValue()) {
            this.setPose(Pose.EMERGING);
            Bukkit.getScheduler().runTaskLater(PetCore.getInstance(), () -> this.setPose(Pose.STANDING), 135);
        }
    }

    @Override
    public void fetchPetData(JsonObject data) {
        data.add("anger", getAngerLevel().name());
        data.add("raw-anger", entityData.get(ANGER_LEVEL));
        data.add("vibration-effect", getVibrationEffect());
    }

    @Override
    public void populateDataAccess(PetDataAccess dataAccess) {
        super.populateDataAccess(dataAccess);
        dataAccess.define(ANGER_LEVEL, 0);
    }

    @Override
    public void tick() {
        super.tick();

        if (vibrationEffect) {
            if (vibrationTick <= 0) {
                VersionTranslator.getEntityLevel(this).broadcastEntityEvent(this, (byte) 61);
                this.playSound(SoundEvents.WARDEN_TENDRIL_CLICKS, 5.0F, this.getVoicePitch());
                vibrationTick = MathUtils.random(40, 60);
            }
            vibrationTick--;
        }
    }

    @Override
    public void setAngerLevel(AngerLevel level) {
        int anger = 10;
        if (level == AngerLevel.AGITATED) anger = 50;
        if (level == AngerLevel.ANGRY) anger = 90;
        entityData.set(ANGER_LEVEL, anger);
    }

    @Override
    public AngerLevel getAngerLevel() {
        int anger = entityData.get(ANGER_LEVEL);
        if (anger >= 80) return AngerLevel.ANGRY;
        if (anger <= 39) return AngerLevel.CALM;
        return AngerLevel.AGITATED;
    }

    @Override
    public void setVibrationEffect(boolean value) {
        vibrationEffect = value;
        if (!value) vibrationTick = 0;
    }

    @Override
    public boolean getVibrationEffect() {
        return vibrationEffect;
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = super.asCompound();
        object.setInteger("raw-anger", entityData.get(ANGER_LEVEL));
        object.setEnum("anger-level", getAngerLevel());
        object.setBoolean("vibration", vibrationEffect);
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("raw-anger")) entityData.set(ANGER_LEVEL, object.getInteger("raw-anger"));
        if (object.hasKey("anger-level"))
            setAngerLevel(object.getEnum("anger-level", AngerLevel.class, AngerLevel.CALM));
        if (object.hasKey("vibration")) setVibrationEffect(object.getBoolean("vibration"));
        super.applyCompound(object);
    }

    @Override
    public void travel(Vec3 vec3d) {
        // Don't move if the warden isn't standing
        if (getPose() != Pose.STANDING) return;
        super.travel(vec3d);
    }

    public void remove(Entity.RemovalReason entity_removalreason) {
        if (!ConfigOption.INSTANCE.PET_TOGGLES_WARDEN_ANIMATIONS.getValue()) {
            super.remove(entity_removalreason);
            return;
        }

        this.setPose(Pose.DIGGING);
        Bukkit.getScheduler().runTaskLater(PetCore.getInstance(), () -> super.remove(entity_removalreason), 100);
    }
}
