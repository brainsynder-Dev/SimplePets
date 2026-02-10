package simplepets.brainsynder.nms.entity.list;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import simplepets.brainsynder.api.entity.passive.IEntitySquidPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.nms.entity.EntityPetOverride;

/**
 * NMS: {@link net.minecraft.world.entity.animal.Squid}
 */
public class EntitySquidPet extends EntityPetOverride implements IEntitySquidPet {
    public EntitySquidPet (EntityType<? extends Mob> entitytypes, PetType type, PetUser user) {
        super(entitytypes, type, user);
    }

    public EntitySquidPet(PetType type, PetUser user) {
        super(EntityType.SQUID, type, user);
    }
}
