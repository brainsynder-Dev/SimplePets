package simplepets.brainsynder.api.entity.passive;

import simplepets.brainsynder.api.entity.misc.IAgeablePet;
import simplepets.brainsynder.api.entity.misc.IFlag;
import simplepets.brainsynder.wrapper.PandaGene;

public interface IEntityPandaPet extends IAgeablePet, IFlag {

    PandaGene getGene ();
    void setGene (PandaGene gene);

    void setSitting(boolean value);
    boolean isSitting();

    void setSneezing (boolean value);
    default boolean isSneezing() {
        return getFlag(2);
    }

    default boolean isScared() {
        return getFlag(8);
    }
    default void setScared(boolean value) {
        setFlag(8, value);
    }

    default boolean isPlaying() {
        return getFlag(4);
    }
    default void setPlaying(boolean value) {
        setFlag(4, value);
    }

    default boolean isLyingOnBack() {
        return getFlag(16);
    }
    default void setLyingOnBack(boolean value) {
        setFlag(16, value);
    }

}
