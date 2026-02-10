package simplepets.brainsynder.nms.entity.list;

import net.minecraft.world.entity.EntityType;
import simplepets.brainsynder.api.entity.passive.IEntityChickenPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.nms.entity.EntityAgeablePet;

/**
 * NMS: {@link net.minecraft.world.entity.animal.Chicken}
 */
public class EntityChickenPet extends EntityAgeablePet implements IEntityChickenPet {
    public EntityChickenPet(PetType type, PetUser user) {
        super(EntityType.CHICKEN, type, user);
    }
}
