package simplepets.brainsynder.versions.v1_18_R1.entity;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import simplepets.brainsynder.api.entity.misc.IEntityFishPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;

public class EntityFishPet extends EntityPet implements IEntityFishPet {
    private static final EntityDataAccessor<Boolean> FROM_BUCKET;

    public EntityFishPet(EntityType<? extends Mob> entitytypes, PetType type, PetUser user) {
        super(entitytypes, type, user);
    }

    @Override
    protected void registerDatawatchers() {
        super.registerDatawatchers();
        this.entityData.define(FROM_BUCKET, false);
    }

    static {
        FROM_BUCKET = SynchedEntityData.defineId(EntityFishPet.class, EntityDataSerializers.BOOLEAN);
    }
}
