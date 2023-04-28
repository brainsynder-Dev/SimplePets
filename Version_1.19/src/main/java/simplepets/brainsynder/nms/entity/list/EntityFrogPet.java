package simplepets.brainsynder.nms.entity.list;

import lib.brainsynder.ServerVersion;
import lib.brainsynder.SupportedVersion;
import lib.brainsynder.math.MathUtils;
import lib.brainsynder.nbt.StorageTagCompound;
import net.minecraft.core.Registry;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.phys.Vec3;
import org.bukkit.craftbukkit.v1_19_R1.util.CraftNamespacedKey;
import simplepets.brainsynder.api.entity.passive.IEntityFrogPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.api.wrappers.FrogVariant;
import simplepets.brainsynder.nms.entity.EntityAgeablePet;

import java.util.OptionalInt;

/**
 * NMS: {@link net.minecraft.world.entity.animal.frog.Frog}
 */
@SupportedVersion(version = ServerVersion.v1_19)
public class EntityFrogPet extends EntityAgeablePet implements IEntityFrogPet {
    private static final EntityDataAccessor<net.minecraft.world.entity.animal.FrogVariant> DATA_VARIANT;
    private static final EntityDataAccessor<OptionalInt> TONGUE_TARGET_ID;

    private boolean croaking = false;
    private int croakingTick = 0;

    private boolean tongue = false;
    private int tongueTick = 0;

    public EntityFrogPet(PetType type, PetUser user) {
        super(EntityType.FROG, type, user);
        //this.moveControl = new SmoothSwimmingMoveControl(this, 85, 10, 0.02F, 0.1F, true);
    }

    @Override
    public void tick() {
        super.tick();

        if (croaking) {
            if (croakingTick <= 0) {
                setPose(Pose.STANDING);
                setPose(Pose.CROAKING);
                croakingTick = MathUtils.random(120, 150);
            }
            croakingTick--;
        }

        if (tongue) {
            if (tongueTick <= 0) {
                setPose(Pose.STANDING);
                setPose(Pose.USING_TONGUE);
                tongueTick = MathUtils.random(100, 150);
            }
            tongueTick--;
        }

        if (!isOnGround()) {
            setPose(Pose.LONG_JUMPING);
        } else {
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

    @Override
    protected void registerDatawatchers() {
        super.registerDatawatchers();
        entityData.define(DATA_VARIANT, net.minecraft.world.entity.animal.FrogVariant.TEMPERATE);
        entityData.define(TONGUE_TARGET_ID, OptionalInt.empty());
    }

    public void setTongueTarget(Entity entity) {
        this.entityData.set(TONGUE_TARGET_ID, OptionalInt.of(entity.getId()));
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound compound = super.asCompound();
        compound.setEnum("variant", getVariant());
        compound.setBoolean("croaking", isCroaking());
        compound.setBoolean("tongue", isCroaking());
        return compound;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("variant")) setVariant(object.getEnum("variant", FrogVariant.class, FrogVariant.TEMPERATE));
        if (object.hasKey("croaking")) setCroaking(object.getBoolean("croaking"));
        if (object.hasKey("tongue")) setUsingTongue(object.getBoolean("tongue"));
        super.applyCompound(object);
    }

    static {
        DATA_VARIANT = SynchedEntityData.defineId(EntityFrogPet.class, EntityDataSerializers.FROG_VARIANT);
        TONGUE_TARGET_ID = SynchedEntityData.defineId(EntityFrogPet.class, EntityDataSerializers.OPTIONAL_UNSIGNED_INT);
    }

    @Override
    public void setVariant(FrogVariant variant) {
        entityData.set(DATA_VARIANT, Registry.FROG_VARIANT.get(CraftNamespacedKey.toMinecraft(variant.getKey())));
    }

    @Override
    public FrogVariant getVariant() {
        return FrogVariant.getByName(org.bukkit.Registry.FROG_VARIANT.get(CraftNamespacedKey.fromMinecraft(net.minecraft.core.Registry.FROG_VARIANT.getKey(entityData.get(DATA_VARIANT)))).name());
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
