package simplepets.brainsynder.api.entity.passive;

import simplepets.brainsynder.api.entity.misc.*;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.wrappers.PandaVariant;

@EntityPetType(petType = PetType.PANDA)
public interface IEntityPandaPet extends IAgeablePet, ISpecialFlag, ISleeper, ISitting {

    PandaVariant getGene();

    void setGene(PandaVariant gene);

    void setSneezeProgress(int progress);

    int getSneezeProgress();

    boolean isEating();

    void setEating(boolean value);

    default void setSneezing(boolean value) {
        setSpecialFlag(2, value);
        if (!value) {
            setSneezeProgress(0);
            getPetUser().updateDataMenu();
        }
    }

    default boolean isSneezing() {
        return getSpecialFlag(2);
    }

    default boolean isPlaying() {
        return getSpecialFlag(4);
    }

    default void setPlaying(boolean value) {
        setSpecialFlag(4, value);
    }
}
