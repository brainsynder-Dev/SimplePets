package simplepets.brainsynder.api.entity.misc;

public interface IHorseAbstract extends IAgeablePet, IFlag {
    boolean isSaddled();

    void setSaddled(boolean var1);

    default boolean isEating () {
        return getFlag(16);
    }
    default boolean isAngry () {
        return getFlag(32);
    }
    default void setEating (boolean value) {
        setFlag(16, value);
    }
    default void setAngry (boolean value) {
        if (value) setEating(false);
        setFlag(32, value);
    }
}
