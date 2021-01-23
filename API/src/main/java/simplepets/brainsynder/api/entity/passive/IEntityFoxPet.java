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

    default void setRollingHead(boolean value) {
        setSpecialFlag(8, value);
    }
    default void setCrouching(boolean value) {
        setSpecialFlag(4, value);
    }
    default void setSitting(boolean value) {
        setSpecialFlag(1, value);
    }
    default void setAggressive(boolean value) {
        setSpecialFlag(128, value);
    }
    default void setWalking(boolean value) {
        setSpecialFlag(64, value);
    }

    default boolean isRollingHead () {
        return getSpecialFlag(8);
    }
    default boolean isCrouching () {
        return getSpecialFlag(4);
    }
    default boolean isSitting () {
        return getSpecialFlag(1);
    }
    default boolean isAggressive () {
        return getSpecialFlag(128);
    }
    default boolean isWalking () {
        return getSpecialFlag(64);
    }
}
