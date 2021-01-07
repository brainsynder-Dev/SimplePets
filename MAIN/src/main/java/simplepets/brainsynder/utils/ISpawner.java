package simplepets.brainsynder.utils;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.pet.IPet;

public interface ISpawner {
    void init ();

    IEntityPet spawnEntityPet(IPet pet, String className);

    IEntityPet spawnEntityPet(Location location, IPet pet, String className);

    Object getHandle(Entity entity);
}
