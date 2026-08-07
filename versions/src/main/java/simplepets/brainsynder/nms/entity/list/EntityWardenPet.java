package simplepets.brainsynder.nms.entity.list;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.phys.Vec3;
import org.bsdevelopment.nbt.StorageTagCompound;
import org.bsdevelopment.pluginutils.PluginUtilities;
import org.bsdevelopment.pluginutils.libs.json.JsonObject;
import org.bsdevelopment.pluginutils.utilities.MathUtil;
import org.bsdevelopment.pluginutils.version.VersionLimit;
import simplepets.brainsynder.api.entity.hostile.IEntityWardenPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.plugin.config.ConfigOption;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.api.wrappers.WardenAnger;
import simplepets.brainsynder.nms.EntitySelector;
import simplepets.brainsynder.nms.entity.EntityPetOverride;
import simplepets.brainsynder.nms.utils.PetDataAccess;

import static simplepets.brainsynder.api.pet.PetDataRegistry.Warden.ANGER;
import static simplepets.brainsynder.api.pet.PetDataRegistry.Warden.VIBRATION;

/**
 * NMS: {@link net.minecraft.world.entity.monster.warden.Warden}
 */
@VersionLimit(min = {1, 19, 0})
public class EntityWardenPet extends EntityPetOverride implements IEntityWardenPet {
    protected static final EntityDataAccessor<Integer> ANGER_LEVEL = SynchedEntityData.defineId(EntityWardenPet.class, EntityDataSerializers.INT);
    private boolean vibrationEffect = false;
    private int vibrationTick = 0;

    public EntityWardenPet(PetType type, PetUser user) {
        super(EntitySelector.WARDEN, type, user);
        if (ConfigOption.PET_TOGGLES_WARDEN_ANIMATIONS.get()) {
            this.setPose(Pose.EMERGING);
            PluginUtilities.getScheduler().runTaskLater(() -> this.setPose(Pose.STANDING), 135);
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
                level().broadcastEntityEvent(this, (byte) 61);
                this.playSound(SoundEvents.WARDEN_TENDRIL_CLICKS, 5.0F, this.getVoicePitch());
                vibrationTick = MathUtil.randomInt(40, 60);
            }
            vibrationTick--;
        }
    }

    @Override
    public void setAngerLevel(WardenAnger level) {
        int anger = 10;
        if (level == WardenAnger.AGITATED) anger = 50;
        if (level == WardenAnger.ANGRY) anger = 90;
        entityData.set(ANGER_LEVEL, anger);
    }

    @Override
    public WardenAnger getAngerLevel() {
        int anger = entityData.get(ANGER_LEVEL);
        if (anger >= 80) return WardenAnger.ANGRY;
        if (anger <= 39) return WardenAnger.CALM;
        return WardenAnger.AGITATED;
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
        object.setEnum(ANGER.namespace(), getAngerLevel());
        object.setBoolean(VIBRATION.namespace(), vibrationEffect);
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("raw-anger")) entityData.set(ANGER_LEVEL, object.getInteger("raw-anger"));
        if (object.hasKey(ANGER.namespace()))
            setAngerLevel(object.getEnum(ANGER.namespace(), WardenAnger.class, WardenAnger.CALM));
        if (object.hasKey(VIBRATION.namespace())) setVibrationEffect(object.getBoolean(VIBRATION.namespace()));
        super.applyCompound(object);
    }

    @Override
    public void travel(Vec3 vec3d) {
        // Don't move if the warden isn't standing
        if (getPose() != Pose.STANDING) return;
        super.travel(vec3d);
    }

    public void remove(RemovalReason entity_removalreason) {
        if (!ConfigOption.PET_TOGGLES_WARDEN_ANIMATIONS.get()) {
            super.remove(entity_removalreason);
            return;
        }

        this.setPose(Pose.DIGGING);
        PluginUtilities.getScheduler().runTaskLater(() -> super.remove(entity_removalreason), 100);
    }
}
