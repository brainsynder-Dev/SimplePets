package simplepets.brainsynder.versions.v1_18_R1.entity.list;

import net.minecraft.world.entity.EntityType;
import simplepets.brainsynder.api.entity.passive.IEntityZombieHorsePet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.versions.v1_18_R1.entity.branch.EntityHorseAbstractPet;

/**
 * NMS: {@link net.minecraft.server.v1_16_R3.EntityHorseZombie}
 */
public class EntityZombieHorsePet extends EntityHorseAbstractPet implements IEntityZombieHorsePet {
    public EntityZombieHorsePet(PetType type, PetUser user) {
        super(EntityType.ZOMBIE_HORSE, type, user);
    }
}
