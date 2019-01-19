package simplepets.brainsynder.links;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.links.impl.*;
import simplepets.brainsynder.player.PetOwner;

import java.util.ArrayList;
import java.util.List;

public class LinkRetriever {
    private List<IPluginLink> loaders = new ArrayList<>();

    public void initiate() {
        if (loaders != null) if (!loaders.isEmpty()) loaders.clear();
        loaders.add(new PlotSquaredLink());
        loaders.add(new WorldGuardLink());
        loaders.add(new VaultLink());
        loaders.add(new WorldBorderLink());

        loaders.forEach(pluginLink -> {
            if (pluginLink instanceof PluginLink) {
                PluginLink link = (PluginLink) pluginLink;
                if (!link.isHooked()) {
                    try {
                        Plugin dependency = Bukkit.getPluginManager().getPlugin(link.getDependencyName());
                        if (dependency != null && dependency.isEnabled()) {
                            link.setHooked(link.onHook());
                            if (link.isHooked()) {
                                PetCore.get().debug(dependency.getName() + " Successfully linked");
                            }else{
                                PetCore.get().debug(link.getDependencyName() + " Could not be linked");
                            }
                        }
                    } catch (Exception ignored) {}
                }
            }
        });
    }

    public <T extends IProtectionLink> T getProtectionLink(Class<T> clazz) {
        return getLink(clazz);
    }

    public <T extends IPluginLink> T getPluginLink(Class<T> clazz) {
        return getLink(clazz);
    }

    private <T extends IPluginLink> T getLink(Class<T> c) {
        checkLoaders();
        for (IPluginLink loader : loaders) {
            if (c.isAssignableFrom(loader.getClass())) {
                return (T) loader;
            }
        }
        return null;
    }

    public boolean canSpawnPet(Location location) {
        return canSpawnPet (null, location);
    }
    public boolean canPetEnter(Location location) {
        return canPetEnter (null, location);
    }

    public boolean canPetEnter(PetOwner owner, Location location) {
        checkLoaders();
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

    public boolean canRidePet(PetOwner owner, Location location) {
        checkLoaders();
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

    public boolean canSpawnPet(PetOwner owner, Location location) {
        checkLoaders();
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
        return (!list.contains(false)) && canPetEnter(owner, location);
    }

    private void checkLoaders() {
        if (loaders == null) initiate();
        if (loaders.isEmpty()) initiate();
    }
}
