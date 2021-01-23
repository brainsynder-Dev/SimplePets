package simplepets.brainsynder.api.entity.passive;

import simplepets.brainsynder.api.entity.misc.EntityPetType;
import simplepets.brainsynder.api.entity.misc.IFlyableEntity;
import simplepets.brainsynder.api.entity.misc.IRainbow;
import simplepets.brainsynder.api.entity.misc.ITameable;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.wrappers.ParrotVariant;

@EntityPetType(petType = PetType.PARROT)
public interface IEntityParrotPet extends ITameable, IFlyableEntity, IRainbow {
    ParrotVariant getVariant();

    void setVariant(ParrotVariant variant);
}
