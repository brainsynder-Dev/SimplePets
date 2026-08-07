package simplepets.brainsynder.nms.entity.list;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.bsdevelopment.nbt.StorageTagCompound;
import org.bsdevelopment.pluginutils.libs.json.JsonObject;
import simplepets.brainsynder.api.entity.passive.IEntityBeePet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.nms.EntitySelector;
import simplepets.brainsynder.nms.entity.EntityAgeablePet;
import simplepets.brainsynder.nms.utils.PetDataAccess;

import static simplepets.brainsynder.api.pet.PetDataRegistry.Bee.*;

/**
 * NMS: {@link net.minecraft.world.entity.animal.bee.Bee}
 */
public class EntityBeePet extends EntityAgeablePet implements IEntityBeePet {
    private static final EntityDataAccessor<Byte> FLAGS = SynchedEntityData.defineId(EntityBeePet.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Long> ANGER = SynchedEntityData.defineId(EntityBeePet.class, EntityDataSerializers.LONG);

    public EntityBeePet(PetType type, PetUser user) {
        super(EntitySelector.BEE, type, user);
        this.moveControl = new FlyingMoveControl(this, 20, true);
    }

    @Override
    public void fetchPetData(JsonObject data) {
        super.fetchPetData(data);
        data.add("angry", isAngry());
        data.add("missing-stinger", hasStung());
        data.add("flipped", isFlipped());
        data.add("nectar", hasNectar());
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        FlyingPathNavigation navigation = new FlyingPathNavigation(this, level);
        navigation.setCanOpenDoors(false);
        navigation.setCanFloat(false);
        return navigation;
    }

    @Override
    public void populateDataAccess(PetDataAccess dataAccess) {
        super.populateDataAccess(dataAccess);
        dataAccess.define(FLAGS, (byte) 4);
        dataAccess.define(ANGER, 0L);
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = super.asCompound();
        object.setBoolean(ANGRY.namespace(), isAngry());
        object.setBoolean(NECTAR.namespace(), hasNectar());
        object.setBoolean(STINGER.namespace(), hasStung());
        object.setBoolean(FLIPPED.namespace(), isFlipped());
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey(ANGRY.namespace())) setAngry(object.getBoolean(ANGRY.namespace()));
        if (object.hasKey(NECTAR.namespace())) setHasNectar(object.getBoolean(NECTAR.namespace()));
        if (object.hasKey(STINGER.namespace())) setHasStung(object.getBoolean(STINGER.namespace()));
        if (object.hasKey(FLIPPED.namespace())) setFlipped(object.getBoolean(FLIPPED.namespace()));
        super.applyCompound(object);
    }

    @Override
    public boolean isAngry() {
        return entityData.get(ANGER) > 0;
    }

    @Override
    public void setAngry(boolean angry) {
        entityData.set(ANGER, (angry) ? 25562256L : 0L);
    }

    @Override
    public void setSpecialFlag(int flag, boolean value) {
        byte flagByte = entityData.get(FLAGS);
        if (value) {
            flagByte = (byte)(flagByte | flag);
        } else {
            flagByte = (byte)(flagByte & ~flag);
        }

        if (flagByte != entityData.get(FLAGS)) this.entityData.set(FLAGS, flagByte);
    }

    @Override
    public boolean getSpecialFlag(int flag) {
        return (this.entityData.get(FLAGS) & flag) != 0;
    }

    @Override
    public void travel(Vec3 vec3) {
        if (isOwnerRiding()) {
            super.travel(vec3);
            calculateEntityAnimation(false);
            return;
        }
        if (this.isInWater()) {
            this.moveRelative(0.02F, vec3);
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(0.800000011920929D));
        } else if (this.isInLava()) {
            this.moveRelative(0.02F, vec3);
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(0.5D));
        } else {
            if (this.onGround) {
                setDeltaMovement(getDeltaMovement().x, 0.15, getDeltaMovement().z);
            } else {
                double phase = (getId() % 20) * (Math.PI / 10.0);
                double bob = Math.sin(level().getGameTime() * 0.1 + phase) * 0.03;
                setDeltaMovement(getDeltaMovement().x, bob, getDeltaMovement().z);
            }
            this.moveRelative(this.getSpeed(), vec3);
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(0.8100000262260437D));
        }

        calculateEntityAnimation(false);
    }
}
