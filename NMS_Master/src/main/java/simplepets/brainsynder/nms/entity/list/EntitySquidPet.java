package simplepets.brainsynder.nms.entity.list;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import simplepets.brainsynder.api.entity.passive.IEntitySquidPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.nms.entity.EntityPet;

/**
 * NMS: {@link net.minecraft.server.v1_16_R3.EntitySquid}
 */
public class EntitySquidPet extends EntityPet implements IEntitySquidPet {
    public EntitySquidPet (EntityType<? extends Mob> entitytypes, PetType type, PetUser user) {
        super(entitytypes, type, user);
    }

    public EntitySquidPet(PetType type, PetUser user) {
        super(EntityType.SQUID, type, user);
    }
}
