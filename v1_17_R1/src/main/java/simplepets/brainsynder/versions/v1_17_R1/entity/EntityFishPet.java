package simplepets.brainsynder.versions.v1_17_R1.entity;

import net.minecraft.server.v1_16_R3.DataWatcher;
import net.minecraft.server.v1_16_R3.DataWatcherObject;
import net.minecraft.server.v1_16_R3.EntityInsentient;
import net.minecraft.server.v1_16_R3.EntityTypes;
import simplepets.brainsynder.api.entity.misc.IEntityFishPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.versions.v1_17_R1.utils.DataWatcherWrapper;

public class EntityFishPet extends EntityPet implements IEntityFishPet {
    private static final DataWatcherObject<Boolean> FROM_BUCKET;

    public EntityFishPet(EntityTypes<? extends EntityInsentient> entitytypes, PetType type, PetUser user) {
        super(entitytypes, type, user);
    }

    @Override
    protected void registerDatawatchers() {
        super.registerDatawatchers();
        this.datawatcher.register(FROM_BUCKET, false);
    }

    static {
        FROM_BUCKET = DataWatcher.a(EntityFishPet.class, DataWatcherWrapper.BOOLEAN);
    }
}
