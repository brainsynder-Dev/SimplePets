package simplepets.brainsynder.utils;

import org.bukkit.Location;
import simplepets.brainsynder.links.IPluginLink;
import simplepets.brainsynder.links.IProtectionLink;
import simplepets.brainsynder.links.impl.PlotSquaredLink;
import simplepets.brainsynder.links.impl.VaultLink;
import simplepets.brainsynder.links.impl.WorldGuardLink;
import simplepets.brainsynder.player.PetOwner;

import java.util.ArrayList;
import java.util.List;

public class LinkRetriever {
    private static List<IPluginLink> loaders = new ArrayList<>();

    public static void initiate() {
        if (loaders != null) if (!loaders.isEmpty()) loaders.clear();
        loaders.add(new PlotSquaredLink());
        loaders.add(new WorldGuardLink());
        loaders.add(new VaultLink());
    }

    public static <T extends IProtectionLink> T getProtectionLink(Class<T> clazz) {
        if (loaders == null) initiate();
        if (loaders.isEmpty()) initiate();
        for (IPluginLink loader : loaders) {
            if (clazz.isAssignableFrom(loader.getClass())) {
                return (T) loader;
            }
        }
        return null;
    }

    public static <T extends IPluginLink> T getPluginLink(Class<T> clazz) {
        if (loaders == null) initiate();
        if (loaders.isEmpty()) initiate();
        for (IPluginLink loader : loaders) {
            if (clazz.isAssignableFrom(loader.getClass())) {
                return (T) loader;
            }
        }
        return null;
    }

    public static boolean canSpawnPet(Location location) {
        return canSpawnPet (null, location);
    }
    public static boolean canPetEnter(Location location) {
        return canPetEnter (null, location);
    }

    public static boolean canPetEnter(PetOwner owner, Location location) {
        if (loaders == null) initiate();
        if (loaders.isEmpty()) initiate();
        List<Boolean> list = new ArrayList<>();
        for (IPluginLink link : loaders) {
            if (!link.isHooked()) continue;
            if (!(link instanceof IProtectionLink)) continue;
            if (owner == null) {
                list.add(((IProtectionLink) link).allowPetEntry(location));
                continue;
            }
            list.add(((IProtectionLink) link).allowPetEntry(owner, location));
        }
        return (!list.contains(false));
    }

    public static boolean canSpawnPet(PetOwner owner, Location location) {
        if (loaders == null) initiate();
        if (loaders.isEmpty()) initiate();
        List<Boolean> list = new ArrayList<>();
        for (IPluginLink link : loaders) {
            if (!link.isHooked()) continue;
            if (!(link instanceof IProtectionLink)) continue;
            if (owner == null) {
                list.add(((IProtectionLink) link).allowPetSpawn(location));
                continue;
            }
            list.add(((IProtectionLink) link).allowPetSpawn(owner, location));
        }
        return (!list.contains(false));
    }
}
