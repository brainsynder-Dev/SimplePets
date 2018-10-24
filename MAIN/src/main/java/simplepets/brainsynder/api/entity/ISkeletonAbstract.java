package simplepets.brainsynder.api.entity;

public interface ISkeletonAbstract extends IEntityPet {
    default boolean isArmsRaised() { return false; }

    default void setArmsRaised(boolean flag) {}
}
