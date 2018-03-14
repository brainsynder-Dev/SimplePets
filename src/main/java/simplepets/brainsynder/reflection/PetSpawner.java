package simplepets.brainsynder.reflection;

import org.bukkit.Location;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.errors.SimplePetsException;
import simplepets.brainsynder.nms.entities.type.main.IEntityPet;
import simplepets.brainsynder.pet.Pet;
import simplepets.brainsynder.utils.ISpawner;

public class PetSpawner {
    public static IEntityPet spawnPet(Pet pet, Class<? extends IEntityPet> clazz) {
        ISpawner spawner = PetCore.get().getSpawner();
        String entity = clazz.getSimpleName().replaceFirst("I", "");
        if (spawner == null) {
            throw new SimplePetsException("An error occurred when trying to retrieve the PetSpawner Class");
        }
        return spawner.spawnEntityPet(pet, entity);
    }

    public static IEntityPet spawnPet(Location location, Pet pet, Class<? extends IEntityPet> clazz) {
        ISpawner spawner = PetCore.get().getSpawner();
        String entity = clazz.getSimpleName().replaceFirst("I", "");
        if (spawner == null) {
            throw new SimplePetsException("An error occurred when trying to retrieve the PetSpawner Class");
        }
        return spawner.spawnEntityPet(location, pet, entity);
    }
}
