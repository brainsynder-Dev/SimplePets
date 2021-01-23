package simplepets.brainsynder.api.entity.misc;

public interface ITameable extends IAgeablePet {
    boolean isTamed();

    void setTamed(boolean flag);

    boolean isSitting();

    void setSitting(boolean flag);
}
