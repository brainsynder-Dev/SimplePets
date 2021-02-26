package simplepets.brainsynder.versions.v1_16_R3;

import lib.brainsynder.nbt.StorageTagCompound;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.CreatureSpawnEvent;
import simplepets.brainsynder.api.ISpawnUtil;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.event.entity.PetEntitySpawnEvent;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.versions.v1_16_R3.entity.EntityPet;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class SpawnerUtil implements ISpawnUtil {
    private final Map<PetType, Class<?>> petMap;

    public SpawnerUtil () {
        petMap = new HashMap<>();

        for (PetType type : PetType.values()) {
            if (type.getEntityClass() == null) continue;

            String name = type.getEntityClass().getSimpleName().replaceFirst("I", "");
            try {
                Class<?> clazz = Class.forName("simplepets.brainsynder.versions.v1_16_R3.entity.list."+name);
                petMap.put(type, clazz);
                System.out.println("Registered Pet: "+type.getName());
            }catch (ClassNotFoundException ignored) {}
        }
    }

    @Override
    public Optional<IEntityPet> spawnEntityPet(PetType type, PetUser user) {
        if (user.getUserLocation().isPresent()) return spawnEntityPet(type, user, user.getUserLocation().get());
        return Optional.empty();
    }

    @Override
    public Optional<IEntityPet> spawnEntityPet(PetType type, PetUser user, StorageTagCompound compound) {
        if (user.getUserLocation().isPresent()) return spawnEntityPet(type, user, compound, user.getUserLocation().get());
        return Optional.empty();
    }

    @Override
    public Optional<IEntityPet> spawnEntityPet(PetType type, PetUser user, Location location) {
        return spawnEntityPet(type, user, new StorageTagCompound(), location);
    }

    @Override
    public Optional<IEntityPet> spawnEntityPet(PetType type, PetUser user, StorageTagCompound compound, Location location) {
        try {
            EntityPet customEntity = (EntityPet) petMap.get(type).getDeclaredConstructor(PetType.class, PetUser.class).newInstance(type, user);

            if ((compound != null) && (!compound.hasNoTags())) customEntity.applyCompound(compound);

            customEntity.setInvisible(false);
            customEntity.setPositionRotation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
            customEntity.setPersistent();

            // Call the spawn event
            PetEntitySpawnEvent event = new PetEntitySpawnEvent(user, customEntity);
            Bukkit.getServer().getPluginManager().callEvent(event);
            if (event.isCancelled()) return Optional.empty();

            if (!location.getChunk().isLoaded()) location.getChunk().load();

            if (((CraftWorld)location.getWorld()).getHandle().addEntity(customEntity, CreatureSpawnEvent.SpawnReason.CUSTOM)) {
                user.setPet(customEntity);
                return Optional.of(customEntity);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    @Override
    public boolean isRegistered(PetType type) {
        return petMap.containsKey(type);
    }

    @Override
    public Optional<Object> getHandle(Entity entity) {
        if (entity == null) return Optional.empty();
        try {
            return Optional.of(((CraftEntity) entity).getHandle());
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
