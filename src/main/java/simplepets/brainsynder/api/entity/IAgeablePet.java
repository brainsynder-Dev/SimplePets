package simplepets.brainsynder.api.entity;

public interface IAgeablePet extends IEntityPet {
    default boolean isBaby(){
        return false;
    }

    default void setBaby(boolean flag){}
}
