package simplepets.brainsynder.nms.entities.type.main;

public interface IAgeablePet extends IEntityPet {
    boolean isBaby();

    void setBaby(boolean flag);
}
