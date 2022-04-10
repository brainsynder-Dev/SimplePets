package simplepets.brainsynder.addon;

import lib.brainsynder.item.ItemBuilder;
import lib.brainsynder.utils.AdvString;
import lib.brainsynder.utils.Colorize;
import org.bukkit.Material;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import simplepets.brainsynder.api.Namespace;

import java.io.File;

public abstract class PetModule implements Listener {
    private boolean loaded = false;
    private boolean enabled = false;
    private boolean update = false;
    private File addonFolder = null;
    private AddonLocalData localData;

    /**
     * This method is used to generate the default values for the addons config file
     */
    public void loadDefaults (AddonConfig config) {}

    /**
     * This will be called when the addon is initialized by SimplePets
     */
    public abstract void init ();

    /**
     * This will be called when the addon is being unloading
     */
    public void cleanup (){}

    /**
     * Should the plugin enable (could be used when linking into another plugin)
     */
    public boolean shouldEnable () {
        return true;
    }

    /**
     * Will return the folder the Addon jar file is located in
     */
    public File getAddonFolder() {
        return addonFolder;
    }

    /**
     * All PetModule classes must have a Namespace Annotation to give it a name
     * This method just retrieves it
     */
    public Namespace getNamespace() {
        if (getClass().isAnnotationPresent(Namespace.class)) return getClass().getAnnotation(Namespace.class);
        throw new NullPointerException(getClass().getSimpleName() + " is missing @Namespace annotation for the module");
    }

    public ItemStack getAddonIcon (){
        StringBuilder authors = new StringBuilder();
        localData.getAuthors().forEach(s -> authors.append(s).append(", "));
        return new ItemBuilder (Material.PLAYER_HEAD)
                .setTexture("http://textures.minecraft.net/texture/" +
                        (enabled ? "78d58a7651fedae4c03efebc226c03fd791eb74a132babb974e8d838ac6882" : "2da1508d47ed73b5c515e3b93928b728e4bc6278569a79b3723ab6972ce05357"))
                .withName(Colorize.fetchColor("e1eb5b")+getNamespace().namespace() + " Module").withLore(localData.getDescription())
                .addLore("&r ", "&7Author: &e"+ AdvString.replaceLast(", ", "", authors.toString()))
                .addLore("&7Addon: &e"+localData.getName()+" (v"+localData.getVersion()+")").build();
    }

    /**
     * Does the server have the addon enabled
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Was the plugin able to load the addon from the file
     */
    public boolean isLoaded() {
        return loaded;
    }

    /**
     * does this addon have a new version available?
     */
    public boolean hasUpdate () {
        return update;
    }

    public AddonLocalData getLocalData() {
        return localData;
    }

    public void setAddonFolder(File addonFolder) {
        this.addonFolder = addonFolder;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }

    public void setHasUpdate(boolean update) {
        this.update = update;
    }

    public void setLocalData(AddonLocalData localData) {
        this.localData = localData;
    }
}
