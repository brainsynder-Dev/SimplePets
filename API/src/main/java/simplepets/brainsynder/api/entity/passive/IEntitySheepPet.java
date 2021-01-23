package simplepets.brainsynder.api.entity.passive;


import simplepets.brainsynder.api.entity.misc.EntityPetType;
import simplepets.brainsynder.api.entity.misc.IAgeablePet;
import simplepets.brainsynder.api.entity.misc.IColorable;
import simplepets.brainsynder.api.entity.misc.IRainbow;
import simplepets.brainsynder.api.pet.PetType;

@EntityPetType(petType = PetType.SHEEP)
public interface IEntitySheepPet extends IAgeablePet, IColorable, IRainbow {
    boolean isSheared();

    void setSheared(boolean flag);
}
