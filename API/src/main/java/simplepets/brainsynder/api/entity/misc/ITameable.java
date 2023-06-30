package simplepets.brainsynder.api.entity.misc;

public interface ITameable extends IAgeablePet, ISitting {
    boolean isTamed();

    void setTamed(boolean flag);
}
