package simplepets.brainsynder.api.entity.hostile;

public interface IEntityZombieVillagerPet extends IEntityZombiePet {

    default boolean isShaking () { return false; }

    default void setShaking (boolean value) {}
}
