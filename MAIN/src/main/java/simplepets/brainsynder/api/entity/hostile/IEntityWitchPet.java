package simplepets.brainsynder.api.entity.hostile;

import simplepets.brainsynder.api.entity.IEntityPet;

public interface IEntityWitchPet extends IEntityPet {
    default void setDrinkingPotion(boolean flag) {}
    default boolean isDrinkingPotion() { return false; }
}
