package simplepets.brainsynder.api.entity.misc;

public interface IEntityControlledPet extends IBurnablePet {

    default boolean isFrozen() { return false; }

    default void setFrozen() {}

}
