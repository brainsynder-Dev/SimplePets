package simplepets.brainsynder.api.entity.misc;

public interface IHorseAbstract extends IAgeablePet, ISpecialFlag, IJump, ISaddle {
    default boolean isEating() {
        return getSpecialFlag(16);
    }

    default boolean isAngry() {
        return getSpecialFlag(32);
    }

    default boolean isRearing() {
        return getSpecialFlag(64);
    }

    default void setEating(boolean value) {
        setSpecialFlag(16, value);
    }

    default void setAngry(boolean value) {
        if (value) setEating(false);
        setSpecialFlag(32, value);
    }

    default void setRearing(boolean value) {
        setSpecialFlag(64, value);
    }
}
