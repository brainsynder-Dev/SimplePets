package simplepets.brainsynder.api.entity.passive;

import simplepets.brainsynder.api.entity.misc.EntityPetType;
import simplepets.brainsynder.api.entity.misc.IAgeablePet;
import simplepets.brainsynder.api.entity.misc.ISleeper;
import simplepets.brainsynder.api.entity.misc.ISpecialFlag;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.wrappers.FoxType;

@EntityPetType(petType = PetType.FOX)
public interface IEntityFoxPet extends IAgeablePet, ISpecialFlag, ISleeper {
    FoxType getFoxType();
    void setFoxType(FoxType type);

    /**
     * Tilts the foxes head
     */
    default void setInterested(boolean value) {
        setSpecialFlag(8, value);
    }

    /**
     * Will make the fox wag its tail (Like a dog waiting for a ball to be thrown)
     */
    default void setCrouching(boolean value) {
        setSpecialFlag(4, value);
    }

    /**
     * Really... the name says it all...
     */
    default void setSitting(boolean value) {
        setSpecialFlag(1, value);
    }

    default boolean isInterested() {
        return getSpecialFlag(8);
    }
    default boolean isCrouching () {
        return getSpecialFlag(4);
    }
    default boolean isSitting () {
        return getSpecialFlag(1);
    }
}
