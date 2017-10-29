package simplepets.brainsynder.nms.entities.type.main;

public interface IAgeablePet extends IEntityPet {
    default boolean isBaby(){
        return false;
    }

    default void setBaby(boolean flag){}
}
