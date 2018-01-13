package simplepets.brainsynder.utils;

import org.bukkit.Location;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.pet.IPet;

public interface ISpawner {
    IEntityPet spawnEntityPet(IPet pet, String className);

    IEntityPet spawnEntityPet(Location location, IPet pet, String className);
}
