package simplepets.brainsynder.nms.entity.list;

import lib.brainsynder.ServerVersion;
import lib.brainsynder.SupportedVersion;
import lib.brainsynder.nbt.StorageTagCompound;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import simplepets.brainsynder.api.entity.hostile.IEntityWardenPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.nms.entity.EntityPet;

/**
 * NMS: {@link net.minecraft.world.entity.monster.warden.Warden}
 */
@SupportedVersion(version = ServerVersion.v1_19)
public class EntityWardenPet extends EntityPet implements IEntityWardenPet {
    protected static final EntityDataAccessor<Integer> TOP_ANGER;

    public EntityWardenPet(PetType type, PetUser user) {
        super(EntityType.BLAZE, type, user);
    }

    @Override
    protected void registerDatawatchers() {
        super.registerDatawatchers();
        this.entityData.define(TOP_ANGER, 0);
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = super.asCompound();
        object.setInteger("anger", entityData.get(TOP_ANGER));
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("anger")) entityData.set(TOP_ANGER, object.getInteger("anger"));
        super.applyCompound(object);
    }

    static {
        TOP_ANGER = SynchedEntityData.defineId(EntityWardenPet.class, EntityDataSerializers.INT);
    }
}
