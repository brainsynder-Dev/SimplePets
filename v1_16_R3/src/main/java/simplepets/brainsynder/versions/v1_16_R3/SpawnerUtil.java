package simplepets.brainsynder.versions.v1_16_R3;

import lib.brainsynder.nbt.StorageTagCompound;
import lib.brainsynder.storage.RandomCollection;
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
import simplepets.brainsynder.utils.debug.Debug;
import simplepets.brainsynder.utils.debug.DebugBuilder;
import simplepets.brainsynder.utils.debug.DebugLevel;
import simplepets.brainsynder.versions.v1_16_R3.entity.EntityPet;

import java.util.*;

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
            }catch (ClassNotFoundException ignored) {
                Debug.debug(DebugBuilder.build(getClass()).setLevel(DebugLevel.MODERATE).setMessages(
                        "Failed to register the '"+type.getName()+"' pet (Missing '"+name+"' class for your version) [Will not effect your server]"
                ));
            }
        }
    }

    @Override
    public Optional<IEntityPet> spawnEntityPet(PetType type, PetUser user) {
        if (user.getUserLocation().isPresent()) return spawnEntityPet(type, user, getRandomLocation(type, user.getUserLocation().get()));
        return Optional.empty();
    }

    @Override
    public Optional<IEntityPet> spawnEntityPet(PetType type, PetUser user, StorageTagCompound compound) {
        if (user.getUserLocation().isPresent()) return spawnEntityPet(type, user, compound, getRandomLocation(type, user.getUserLocation().get()));
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

    private Location getRandomLocation (PetType type, Location center) {
        List<Location> locationList = circle(center, modifyInt(type, 4), 1, false, false);
        return RandomCollection.fromCollection(locationList).next();
    }

    private int modifyInt(PetType type, int number) {
        return (type.isLargePet() ? (number + number) : number);
    }

    private List<Location> circle(Location loc, double radius, double height, boolean hollow, boolean sphere) {
        ArrayList circleblocks = new ArrayList();
        double cx = loc.getX();
        double cy = loc.getY();
        double cz = loc.getZ();

        for (double x = cx - radius; x <= cx + radius; ++x) {
            for (double z = cz - radius; z <= cz + radius; ++z) {
                for (double y = sphere ? cy - radius : cy; y < (sphere ? cy + radius : cy + height); ++y) {
                    double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z) + (sphere ? (cy - y) * (cy - y) : 0.0D);
                    if (dist < radius * radius && (!hollow || dist >= (radius - 1.0D) * (radius - 1.0D))) {
                        Location l = new Location(loc.getWorld(), x, y, z);
                        circleblocks.add(l);
                    }
                }
            }
        }

        return circleblocks;
    }
}
