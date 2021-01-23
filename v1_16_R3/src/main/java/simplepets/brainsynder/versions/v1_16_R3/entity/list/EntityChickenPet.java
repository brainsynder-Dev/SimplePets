package simplepets.brainsynder.versions.v1_16_R3.entity.list;

import net.minecraft.server.v1_16_R3.EntityTypes;
import simplepets.brainsynder.api.entity.passive.IEntityChickenPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.versions.v1_16_R3.entity.EntityAgeablePet;

public class EntityChickenPet extends EntityAgeablePet implements IEntityChickenPet {
    public EntityChickenPet(PetType type, PetUser user) {
        super(EntityTypes.CHICKEN, type, user);
    }
}
