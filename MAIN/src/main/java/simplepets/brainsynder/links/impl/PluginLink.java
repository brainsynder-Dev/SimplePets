package simplepets.brainsynder.links.impl;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.links.IPluginLink;

public abstract class PluginLink<T extends Plugin> implements IPluginLink<T> {
    protected PluginLink<T> instance = this;
    boolean hooked;
    private T dependency;
    private String dependencyName;

    public PluginLink(String dependencyName) {
        this.dependencyName = dependencyName;
    }

    public void setHooked(boolean hooked) {
        this.hooked = hooked;
    }

    @Override
    public void onUnhook() {
        dependency = null;
        hooked = false;
        instance = null;
        dependencyName = null;
    }

    public void setDependency(T dependency) {
        this.dependency = dependency;
    }

    public T getDependency() {
        if (this.dependency == null) {
            throw new RuntimeException(getDependencyName() + " Soft-Dependency is NULL!");
        } else {
            return this.dependency;
        }
    }

    public boolean isHooked() {
        if (this.dependency != null) return true;
        this.dependency = (T) Bukkit.getPluginManager().getPlugin(this.getDependencyName());
        boolean var = (this.dependency != null && this.dependency.isEnabled());
        if (var) {
            if (onHook()) {
                hooked = true;
                PetCore.get().debug(dependency.getName() + " Successfully linked");
            }else{
                hooked = false;
                PetCore.get().debug(getDependencyName() + " Could not be linked");
            }
        }
        return var;
    }

    public String getDependencyName() {
        if (this.dependencyName == null) {
            throw new RuntimeException("Dependency name is NULL!");
        } else {
            return this.dependencyName;
        }
    }
}