package simplepets.brainsynder.nms.entity.list;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.animal.frog.FrogVariant;
import net.minecraft.world.entity.animal.frog.FrogVariants;
import net.minecraft.world.phys.Vec3;
import org.bsdevelopment.nbt.StorageTagCompound;
import org.bsdevelopment.pluginutils.libs.json.JsonObject;
import org.bsdevelopment.pluginutils.utilities.MathUtil;
import org.bsdevelopment.pluginutils.version.VersionLimit;
import org.bukkit.craftbukkit.CraftRegistry;
import simplepets.brainsynder.api.entity.passive.IEntityFrogPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.api.wrappers.TemperatureVariant;
import simplepets.brainsynder.nms.EntitySelector;
import simplepets.brainsynder.nms.entity.EntityAgeablePet;
import simplepets.brainsynder.nms.helper.VersionHelper;
import simplepets.brainsynder.nms.utils.PetDataAccess;
import simplepets.brainsynder.nms.utils.VariantUtils;

import java.util.OptionalInt;

import static simplepets.brainsynder.api.pet.PetDataRegistry.Frog.*;

/**
 * NMS: {@link net.minecraft.world.entity.animal.frog.Frog}
 */
@VersionLimit(min = {1, 19, 0})
public class EntityFrogPet extends EntityAgeablePet implements IEntityFrogPet {
    private static final EntityDataAccessor<Holder<FrogVariant>> DATA_VARIANT = SynchedEntityData.defineId(EntityFrogPet.class, EntityDataSerializers.FROG_VARIANT);
    private static final EntityDataAccessor<OptionalInt> TONGUE_TARGET_ID = SynchedEntityData.defineId(EntityFrogPet.class, EntityDataSerializers.OPTIONAL_UNSIGNED_INT);
    private TemperatureVariant variant = TemperatureVariant.TEMPERATE;

    private boolean croaking = false;
    private int croakingTick = 0;

    private boolean tongue = false;
    private int tongueTick = 0;

    public EntityFrogPet(PetType type, PetUser user) {
        super(EntitySelector.FROG, type, user);
        //this.moveControl = new SmoothSwimmingMoveControl(this, 85, 10, 0.02F, 0.1F, true);
    }

    @Override
    public void fetchPetData(JsonObject data) {
        super.fetchPetData(data);
        data.add("variant", getVariant().name());
        data.add("croaking", isCroaking());
        data.add("tongue", isUsingTongue());
    }

    @Override
    public void populateDataAccess(PetDataAccess dataAccess) {
        super.populateDataAccess(dataAccess);
        dataAccess.define(DATA_VARIANT, VariantUtils.getDefaultOrAny(registryAccess(), FrogVariants.TEMPERATE));
        dataAccess.define(TONGUE_TARGET_ID, OptionalInt.empty());
    }

    @Override
    public void tick() {
        super.tick();

        if (croaking) {
            if (croakingTick <= 0) {
                setPose(Pose.STANDING);
                setPose(Pose.CROAKING);
                croakingTick = MathUtil.randomInt(120, 150);
            }
            croakingTick--;
        }

        if (tongue) {
            if (tongueTick <= 0) {
                setPose(Pose.STANDING);
                setPose(Pose.USING_TONGUE);
                tongueTick = MathUtil.randomInt(100, 150);
            }
            tongueTick--;
        }

        if (!isOnGround()) {
            setPose(Pose.LONG_JUMPING);
        } else if (!croaking && !tongue) {
            setPose(Pose.STANDING);
        }
    }

    @Override
    public void travel(Vec3 vec3d) {
        if (!isOwnerRiding() && isInWater()) {
            moveRelative(getSpeed(), vec3d);
            move(MoverType.SELF, getDeltaMovement());
            setDeltaMovement(getDeltaMovement().scale(0.9D));
        } else {
            super.travel(vec3d);
        }
    }

    public void setTongueTarget(Entity entity) {
        this.entityData.set(TONGUE_TARGET_ID, OptionalInt.of(entity.getId()));
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound compound = super.asCompound();
        compound.setEnum(VARIANT.namespace(), getVariant());
        compound.setBoolean(CROAKING.namespace(), isCroaking());
        compound.setBoolean(TONGUE.namespace(), isCroaking());
        return compound;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey(VARIANT.namespace())) setVariant(object.getEnum(VARIANT.namespace(), TemperatureVariant.class, TemperatureVariant.TEMPERATE));
        if (object.hasKey(CROAKING.namespace())) setCroaking(object.getBoolean(CROAKING.namespace()));
        if (object.hasKey(TONGUE.namespace())) setUsingTongue(object.getBoolean(TONGUE.namespace()));
        super.applyCompound(object);
    }

    @Override
    public void setVariant(TemperatureVariant variant) {
        this.variant = variant;

        Registry<FrogVariant> registry = CraftRegistry.getMinecraftRegistry(Registries.FROG_VARIANT);
        entityData.set(DATA_VARIANT, registry.wrapAsHolder(VersionHelper.VERSION_TRANSLATOR.getRegistryValue(registry, variant.getKey())));
    }

    @Override
    public TemperatureVariant getVariant() {
        return variant;
    }

    @Override
    public boolean isCroaking() {
        return croaking;
    }

    @Override
    public void setCroaking(boolean value) {
        croaking = value;
        if (croaking) setPose(Pose.CROAKING);
        if (!croaking) {
            setPose(Pose.STANDING);
            croakingTick = 0;
        }
    }

    @Override
    public boolean isUsingTongue() {
        return tongue;
    }

    @Override
    public void setUsingTongue(boolean value) {
        tongue = value;
        if (tongue) setPose(Pose.USING_TONGUE);
        if (!tongue) {
            setPose(Pose.STANDING);
            tongueTick = 0;
        }
    }
}
