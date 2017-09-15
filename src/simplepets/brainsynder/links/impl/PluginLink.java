package simplepets.brainsynder.links.impl;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.links.IPluginLink;

public abstract class PluginLink<T extends Plugin> implements IPluginLink<T> {
    protected PluginLink<T> instance = this;
    protected boolean hooked;
    private T dependency;
    private String dependencyName;

    public PluginLink(String dependencyName) {
        this.dependencyName = dependencyName;
        if (this.dependency == null && !this.hooked) {
            try {
                this.dependency = (T) Bukkit.getPluginManager().getPlugin(this.getDependencyName());
                if (this.dependency != null && this.dependency.isEnabled()) {
                    this.onHook();
                    this.hooked = true;
                    PetCore.get().debug(this.dependency.getName() + " Successfully linked");
                }
            } catch (Exception var4) {
            }
        }
    }

    public abstract void onHook();

    public abstract void onUnhook();

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
            onHook();
            PetCore.get().debug(this.dependency.getName() + " Successfully linked");
            hooked = true;
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