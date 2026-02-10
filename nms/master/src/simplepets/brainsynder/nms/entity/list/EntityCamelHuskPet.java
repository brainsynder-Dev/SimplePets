package simplepets.brainsynder.nms.entity.list;

import lib.brainsynder.ServerVersion;
import lib.brainsynder.SupportedVersion;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import simplepets.brainsynder.api.entity.passive.IEntityCamelHuskPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;

/**
 * NMS: {@link net.minecraft.world.entity.animal.camel.CamelHusk}
 */
@SupportedVersion(version = ServerVersion.v1_21_11)
public class EntityCamelHuskPet extends EntityCamelPet implements IEntityCamelHuskPet {
    public EntityCamelHuskPet(PetType type, PetUser user) {
        this(EntityType.CAMEL_HUSK, type, user);
    }

    public EntityCamelHuskPet(EntityType<? extends Mob> entityType, PetType type, PetUser user) {
        super(entityType, type, user);
        doIndirectAttach = false;
    }
}
