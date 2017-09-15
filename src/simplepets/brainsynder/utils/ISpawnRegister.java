package simplepets.brainsynder.utils;

public interface ISpawnRegister extends ISpawner {
    void enablePets(String nmsClassName);

    void disablePets(String nmsClassName);
}
