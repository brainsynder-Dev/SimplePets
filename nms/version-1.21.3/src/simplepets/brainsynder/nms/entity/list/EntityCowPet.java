package simplepets.brainsynder.nms.entity.list;

import net.minecraft.world.entity.EntityType;
import simplepets.brainsynder.api.entity.passive.IEntityCowPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.nms.entity.EntityAgeablePet;

/**
 * NMS: {@link net.minecraft.world.entity.animal.Cow}
 */
public class EntityCowPet extends EntityAgeablePet implements IEntityCowPet {
    public EntityCowPet(PetType type, PetUser user) {
        super(EntityType.COW, type, user);
    }
}
