package simplepets.brainsynder.nms.entity.list;

import lib.brainsynder.json.JsonObject;
import lib.brainsynder.nbt.StorageTagCompound;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import simplepets.brainsynder.api.entity.hostile.IEntityWitherPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.nms.entity.EntityPetOverride;
import simplepets.brainsynder.nms.utils.PetDataAccess;

/**
 * NMS: {@link net.minecraft.world.entity.boss.wither.WitherBoss}
 */
public class EntityWitherPet extends EntityPetOverride implements IEntityWitherPet {
    private static final EntityDataAccessor<Integer> FIRST_HEAD_TARGET = SynchedEntityData.defineId(EntityWitherPet.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> SECOND_HEAD_TARGET = SynchedEntityData.defineId(EntityWitherPet.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> THIRD_HEAD_TARGET = SynchedEntityData.defineId(EntityWitherPet.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> INVULNERABILITY_TIME = SynchedEntityData.defineId(EntityWitherPet.class, EntityDataSerializers.INT);

    public EntityWitherPet(PetType type, PetUser user) {
        super(EntityType.WITHER, type, user);
    }

    @Override
    public void fetchPetData(JsonObject data) {
        data.add("shielded", isShielded());
        data.add("small", isSmall());
    }

    @Override
    public void populateDataAccess(PetDataAccess dataAccess) {
        super.populateDataAccess(dataAccess);
        dataAccess.define(FIRST_HEAD_TARGET, 0);
        dataAccess.define(SECOND_HEAD_TARGET, 0);
        dataAccess.define(THIRD_HEAD_TARGET, 0);
        dataAccess.define(INVULNERABILITY_TIME, 0);
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = super.asCompound();
        object.setBoolean("shielded", isShielded());
        object.setBoolean("small", isSmall());
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("shielded")) setShielded(object.getBoolean("shielded"));
        if (object.hasKey("small")) setSmall(object.getBoolean("small"));
        super.applyCompound(object);
    }

    @Override
    public boolean isShielded() {
        return this.entityData.get(INVULNERABILITY_TIME) == 1;
    }

    @Override
    public void setShielded(boolean flag) {
        this.entityData.set(INVULNERABILITY_TIME, flag ? 1 : 0);
        this.setHealth((float) (flag ? 150 : 300));
        getPetUser().updateDataMenu();
    }

    @Override
    public boolean isSmall() {
        return (entityData.get(INVULNERABILITY_TIME) == 600);
    }

    @Override
    public void setSmall(boolean var) {
        this.entityData.set(INVULNERABILITY_TIME, var ? 600 : 0);
        getPetUser().updateDataMenu();
    }
}
