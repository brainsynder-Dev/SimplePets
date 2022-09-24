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
    private final boolean update = false;
    private File addonFolder = null;
    private AddonLocalData localData;

    /**
     * "This function is called when the addon is loaded, and is used to set default values for the addon's configuration."
     *
     * The `config` parameter is an instance of the `AddonConfig` class, which is used to store the addon's configuration
     *
     * @param config The AddonConfig object that is being loaded.
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
     * Should the addon enable (could be used when linking into another plugin)
     */
    public boolean shouldEnable () {
        return true;
    }

    /**
     * It returns the folder where the addon jar is located
     *
     * @return The addon folder.
     */
    public File getAddonFolder() {
        return addonFolder;
    }

    /**
     * If the class has a Namespace annotation, return it. If not, throw a NullPointerException
     *
     * @return The Namespace annotation for the module.
     *
     * @throws {@link NullPointerException} The pet module is missing the {@link Namespace} annotation
     */
    public Namespace getNamespace() {
        if (getClass().isAnnotationPresent(Namespace.class)) return getClass().getAnnotation(Namespace.class);
        throw new NullPointerException(getClass().getSimpleName() + " is missing @Namespace annotation for the module");
    }

    /**
     * It returns an ItemStack that represents the addon
     *
     * @return An ItemStack
     */
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
     * Returns true if the addon module is enabled, false otherwise.
     *
     * @return The value of the enabled variable.
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Returns true if the addon module is loaded, false otherwise.
     *
     * @return The boolean value of the loaded variable.
     */
    public boolean isLoaded() {
        return loaded;
    }

    /**
     * This function returns the local data of the addon.
     *
     * @return The localData object.
     */
    public AddonLocalData getLocalData() {
        return localData;
    }

    @Deprecated
    public void setAddonFolder(File addonFolder) {
        this.addonFolder = addonFolder;
    }

    /**
     * This function sets the enabled variable to the value of the enabled parameter.
     *
     * @param enabled This is a boolean value that determines whether the user is enabled or not.
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * > This function sets the value of the loaded variable to the value of the loaded parameter
     *
     * @param loaded This is a boolean value that indicates whether the data has been loaded or not.
     */
    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }

    @Deprecated
    public void setLocalData(AddonLocalData localData) {
        this.localData = localData;
    }
}
