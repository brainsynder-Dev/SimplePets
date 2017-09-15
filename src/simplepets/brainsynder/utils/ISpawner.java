package simplepets.brainsynder.utils;

import org.bukkit.Location;
import simplepets.brainsynder.nms.entities.type.main.IEntityPet;
import simplepets.brainsynder.pet.IPet;

public interface ISpawner {
    IEntityPet spawnEntityPet(IPet pet, String className);

    IEntityPet spawnEntityPet(Location location, IPet pet, String className);
}
