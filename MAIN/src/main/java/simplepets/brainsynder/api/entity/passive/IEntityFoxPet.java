package simplepets.brainsynder.api.entity.passive;

import simplepets.brainsynder.api.entity.misc.IAgeablePet;
import simplepets.brainsynder.api.entity.misc.IFlag;
import simplepets.brainsynder.wrapper.FoxType;

public interface IEntityFoxPet extends IAgeablePet, IFlag {
    FoxType getFoxType();
    void setFoxType(FoxType type);

    void setRollingHead(boolean value);
    void setCrouching(boolean value);
    void setSitting(boolean value);
    void setSleeping(boolean value);
    void setAggressive(boolean value);
    void setWalking(boolean value);

    boolean isRollingHead ();
    boolean isCrouching ();
    boolean isSitting ();
    boolean isSleeping ();
    boolean isAggressive ();
    boolean isWalking ();
}
