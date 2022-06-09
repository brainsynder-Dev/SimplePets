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
import simplepets.brainsynder.api.wrappers.AngerLevel;
import simplepets.brainsynder.nms.entity.EntityPet;

/**
 * NMS: {@link net.minecraft.world.entity.monster.warden.Warden}
 */
@SupportedVersion(version = ServerVersion.v1_19)
public class EntityWardenPet extends EntityPet implements IEntityWardenPet {
    protected static final EntityDataAccessor<Integer> ANGER_LEVEL;

    public EntityWardenPet(PetType type, PetUser user) {
        super(EntityType.WARDEN, type, user);
    }

    @Override
    protected void registerDatawatchers() {
        super.registerDatawatchers();
        this.entityData.define(ANGER_LEVEL, 0);
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
    public StorageTagCompound asCompound() {
        StorageTagCompound object = super.asCompound();
        object.setInteger("raw-anger", entityData.get(ANGER_LEVEL));
        object.setEnum("anger-level", getAngerLevel());
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("raw-anger")) entityData.set(ANGER_LEVEL, object.getInteger("raw-anger"));
        if (object.hasKey("anger-level")) setAngerLevel(object.getEnum("anger-level", AngerLevel.class, AngerLevel.CALM));
        super.applyCompound(object);
    }

    static {
        ANGER_LEVEL = SynchedEntityData.defineId(EntityWardenPet.class, EntityDataSerializers.INT);
    }
}
