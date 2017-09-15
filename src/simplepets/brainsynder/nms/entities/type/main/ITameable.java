package simplepets.brainsynder.nms.entities.type.main;

public interface ITameable extends IAgeablePet {
    boolean isTamed();

    void setTamed(boolean flag);

    boolean isSitting();

    void setSitting(boolean flag);
}
