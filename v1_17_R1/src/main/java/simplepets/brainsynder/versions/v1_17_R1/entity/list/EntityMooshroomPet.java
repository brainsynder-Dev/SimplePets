package simplepets.brainsynder.versions.v1_17_R1.entity.list;

import lib.brainsynder.nbt.StorageTagCompound;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import simplepets.brainsynder.api.entity.passive.IEntityMooshroomPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.api.wrappers.MooshroomType;
import simplepets.brainsynder.versions.v1_17_R1.entity.EntityAgeablePet;

/**
 * NMS: {@link net.minecraft.server.v1_16_R3.EntityMushroomCow}
 */
public class EntityMooshroomPet extends EntityAgeablePet implements IEntityMooshroomPet {
    private static final EntityDataAccessor<String> TYPE;

    public EntityMooshroomPet(PetType type, PetUser user) {
        super(EntityType.MOOSHROOM, type, user);
    }

    @Override
    protected void registerDatawatchers() {
        super.registerDatawatchers();
        entityData.define(TYPE, MooshroomType.RED.name());
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound compound = super.asCompound();
        compound.setString("type", getMooshroomType().name());
        return compound;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("type"))
            setMooshroomType(MooshroomType.valueOf(object.getString("type")));
        super.applyCompound(object);
    }

    @Override
    public void setMooshroomType(MooshroomType type) {
        entityData.set(TYPE, type.name().toLowerCase());
    }

    @Override
    public MooshroomType getMooshroomType() {
        return MooshroomType.valueOf(entityData.get(TYPE).toUpperCase());
    }

    static {
        TYPE = SynchedEntityData.defineId(EntityMooshroomPet.class, EntityDataSerializers.STRING);
    }
}
