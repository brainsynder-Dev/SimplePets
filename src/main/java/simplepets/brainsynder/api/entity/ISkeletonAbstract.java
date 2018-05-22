package simplepets.brainsynder.api.entity;

public interface ISkeletonAbstract extends IEntityPet {
    default boolean isArmsSwinging() { return false; }

    default void setArmsSwinging(boolean flag) {}
}
