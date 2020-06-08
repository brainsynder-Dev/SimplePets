package simplepets.brainsynder.api.entity.misc;

public interface IHorseAbstract extends IAgeablePet, IFlag {
    default boolean isSaddled(){
        return getSpecialFlag(4);
    }
    default void setSaddled(boolean var1) {
        setSpecialFlag(4, var1);
    }

    default boolean isEating () {
        return getSpecialFlag(16);
    }
    default boolean isAngry () {
        return getSpecialFlag(32);
    }
    default void setEating (boolean value) {
        setSpecialFlag(16, value);
    }
    default void setAngry (boolean value) {
        if (value) setEating(false);
        setSpecialFlag(32, value);
    }
}
