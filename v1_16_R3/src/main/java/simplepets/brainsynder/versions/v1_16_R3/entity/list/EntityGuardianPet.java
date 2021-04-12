package simplepets.brainsynder.versions.v1_16_R3.entity.list;

import net.minecraft.server.v1_16_R3.DataWatcher;
import net.minecraft.server.v1_16_R3.DataWatcherObject;
import net.minecraft.server.v1_16_R3.EntityInsentient;
import net.minecraft.server.v1_16_R3.EntityTypes;
import simplepets.brainsynder.api.entity.hostile.IEntityGuardianPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.versions.v1_16_R3.entity.EntityPet;
import simplepets.brainsynder.versions.v1_16_R3.utils.DataWatcherWrapper;

/**
 * NMS: {@link net.minecraft.server.v1_16_R3.EntityGuardian}
 */
public class EntityGuardianPet extends EntityPet implements IEntityGuardianPet {
    private static final DataWatcherObject<Boolean> MOVING;
    private static final DataWatcherObject<Integer> TARGET_ENTITY;

    public EntityGuardianPet(PetType type, PetUser user) {
        super(EntityTypes.GUARDIAN, type, user);
    }
    public EntityGuardianPet(EntityTypes<? extends EntityInsentient> entitytypes, PetType type, PetUser user) {
        super(entitytypes, type, user);
    }

    @Override
    protected void registerDatawatchers() {
        super.registerDatawatchers();
        this.datawatcher.register(MOVING, Boolean.FALSE);
        this.datawatcher.register(TARGET_ENTITY, 0);
    }

    static {
        MOVING = DataWatcher.a(EntityGuardianPet.class, DataWatcherWrapper.BOOLEAN);
        TARGET_ENTITY = DataWatcher.a(EntityGuardianPet.class, DataWatcherWrapper.INT);
    }
}
