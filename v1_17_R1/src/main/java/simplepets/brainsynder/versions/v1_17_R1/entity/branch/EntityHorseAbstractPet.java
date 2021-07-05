package simplepets.brainsynder.versions.v1_17_R1.entity.branch;

import lib.brainsynder.nbt.StorageTagCompound;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Bukkit;
import simplepets.brainsynder.api.entity.misc.IHorseAbstract;
import simplepets.brainsynder.api.event.entity.PetMoveEvent;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.versions.v1_17_R1.entity.EntityAgeablePet;

import java.util.Optional;
import java.util.UUID;

public class EntityHorseAbstractPet extends EntityAgeablePet implements IHorseAbstract {
    private static final EntityDataAccessor<Byte> STATUS;
    private static final EntityDataAccessor<Optional<UUID>> OWNER_UNIQUE_ID;

    protected boolean isJumping;
    protected float playerJumpPendingScale;
    protected int gallopSoundCounter;

    public EntityHorseAbstractPet(EntityType<? extends Mob> entitytypes, PetType type, PetUser user) {
        super(entitytypes, type, user);
    }

    @Override
    public void travel(Vec3 vec3d) {
        if (this.isAlive()) {
            if (this.isVehicle() && this.canBeControlledByRider()) {
                LivingEntity entityliving = (LivingEntity)this.getControllingPassenger();
                this.setYRot(entityliving.getYRot());
                this.yRotO = this.getYRot();
                this.setXRot(entityliving.getXRot() * 0.5F);
                this.setRot(this.getYRot(), this.getXRot());
                this.yBodyRot = this.getYRot();
                this.yHeadRot = this.yBodyRot;
                float strafe = entityliving.xxa * 0.5F;
                float forward = entityliving.zza;
                if (forward <= 0.0F) {
                    forward *= 0.25F;
                    this.gallopSoundCounter = 0;
                }

                if (this.onGround && this.playerJumpPendingScale == 0.0F) {
                    strafe = 0.0F;
                    forward = 0.0F;
                }

                PetMoveEvent moveEvent = new PetMoveEvent(this, PetMoveEvent.Cause.RIDE);
                Bukkit.getServer().getPluginManager().callEvent(moveEvent);
                if (moveEvent.isCancelled()) return;

                if (this.playerJumpPendingScale > 0.0F && !this.isJumping() && this.onGround) {
                    double d0 = this.getCustomJump() * (double)this.playerJumpPendingScale * (double)this.getBlockJumpFactor();
                    double d1;
                    if (this.hasEffect(MobEffects.JUMP)) {
                        d1 = d0 + (double)((float)(this.getEffect(MobEffects.JUMP).getAmplifier() + 1) * 0.1F);
                    } else {
                        d1 = d0;
                    }

                    Vec3 vec3d1 = this.getDeltaMovement();
                    this.setDeltaMovement(vec3d1.x, d1, vec3d1.z);
                    this.setIsJumping(true);
                    this.hasImpulse = true;
                    if (forward > 0.0F) {
                        float f2 = Mth.sin(this.getYRot() * 0.017453292F);
                        float f3 = Mth.cos(this.getYRot() * 0.017453292F);
                        this.setDeltaMovement(this.getDeltaMovement().add(-0.4F * f2 * this.playerJumpPendingScale, 0.0D, 0.4F * f3 * this.playerJumpPendingScale));
                    }

                    this.playerJumpPendingScale = 0.0F;
                }

                this.flyingSpeed = this.getSpeed() * 0.1F;
                if (this.isControlledByLocalInstance()) {
                    this.setSpeed((float)this.getAttributeValue(Attributes.MOVEMENT_SPEED));
                    super.travel(new Vec3(strafe, vec3d.y, forward));
                } else if (entityliving instanceof Player) {
                    this.setDeltaMovement(Vec3.ZERO);
                }

                if (this.onGround) {
                    this.playerJumpPendingScale = 0.0F;
                    this.setIsJumping(false);
                }

                this.calculateEntityAnimation(this, false);
                this.tryCheckInsideBlocks();
            } else {
                this.flyingSpeed = 0.02F;
                super.travel(vec3d);
            }
        }
    }

    public boolean isJumping() {
        return this.isJumping;
    }

    public void setIsJumping(boolean flag) {
        this.isJumping = flag;
    }

    public double getCustomJump() {
        return this.getAttributeValue(Attributes.JUMP_STRENGTH);
    }


    @Override
    public void setSpecialFlag(int flag, boolean value) {
        byte b0 = this.entityData.get(STATUS);
        if(value){
            this.entityData.set(STATUS, (byte) (b0 | flag));
        }else{
            this.entityData.set(STATUS, (byte) (b0 & (~flag)));
        }
    }

    @Override
    public boolean getSpecialFlag(int flag) {
        return (this.entityData.get(STATUS) & flag) != 0;
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = super.asCompound();
        object.setBoolean("saddled", isSaddled());
        object.setBoolean("eating", isEating());
        object.setBoolean("angry", isAngry());
        object.setBoolean("rearing", isRearing());
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("saddled")) setSaddled(object.getBoolean("saddled"));
        if (object.hasKey("eating")) setEating(object.getBoolean("eating"));
        if (object.hasKey("angry")) setAngry(object.getBoolean("angry"));
        if (object.hasKey("rearing")) setRearing(object.getBoolean("rearing"));
        super.applyCompound(object);
    }

    @Override
    protected void registerDatawatchers() {
        super.registerDatawatchers();
        this.entityData.define(STATUS, (byte) 0);
        this.entityData.define(OWNER_UNIQUE_ID, Optional.empty());
    }

    static {
        STATUS = SynchedEntityData.defineId(EntityHorseAbstractPet.class, EntityDataSerializers.BYTE);
        OWNER_UNIQUE_ID = SynchedEntityData.defineId(EntityHorseAbstractPet.class, EntityDataSerializers.OPTIONAL_UUID);
    }
}