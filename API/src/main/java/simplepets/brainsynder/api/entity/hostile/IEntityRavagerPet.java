package simplepets.brainsynder.api.entity.hostile;

import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.misc.EntityPetType;
import simplepets.brainsynder.api.entity.misc.IRaider;
import simplepets.brainsynder.api.entity.misc.ISpecialRiding;
import simplepets.brainsynder.api.pet.PetType;

@EntityPetType(petType = PetType.RAVAGER)
public interface IEntityRavagerPet extends IEntityPet, ISpecialRiding, IRaider {
    boolean isChomping ();

    void setChomping (boolean chomping);
}
