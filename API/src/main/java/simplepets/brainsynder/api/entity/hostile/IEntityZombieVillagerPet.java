package simplepets.brainsynder.api.entity.hostile;

import simplepets.brainsynder.api.entity.misc.EntityPetType;
import simplepets.brainsynder.api.entity.misc.IProfession;
import simplepets.brainsynder.api.pet.PetType;

@EntityPetType(petType = PetType.ZOMBIE_VILLAGER)
public interface IEntityZombieVillagerPet extends IEntityZombiePet, IProfession {
}
