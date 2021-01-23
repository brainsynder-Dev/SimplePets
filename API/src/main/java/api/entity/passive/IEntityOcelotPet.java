package api.entity.passive;

import simplepets.brainsynder.api.entity.misc.EntityPetType;
import simplepets.brainsynder.api.entity.misc.ITameable;
import simplepets.brainsynder.api.pet.PetType;

@EntityPetType(petType = PetType.OCELOT)
public interface IEntityOcelotPet extends ITameable {
    default int getCatType() { return 0; }

    default void setCatType(int i) {}
}
