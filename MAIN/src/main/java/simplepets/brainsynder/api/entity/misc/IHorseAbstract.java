package simplepets.brainsynder.api.entity.misc;

public interface IHorseAbstract extends IAgeablePet, IFlag {
    default boolean isSaddled(){
        return getFlag(4);
    }
    default void setSaddled(boolean var1) {
        setFlag(4, var1);
    }

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
