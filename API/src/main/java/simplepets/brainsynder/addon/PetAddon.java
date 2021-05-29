package simplepets.brainsynder.addon;

import lib.brainsynder.item.ItemBuilder;
import lib.brainsynder.utils.Colorize;
import org.bukkit.Material;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import simplepets.brainsynder.api.Namespace;

import java.io.File;
import java.util.List;

public abstract class PetAddon implements Listener {
    private final boolean loaded = false;
    private final boolean enabled = false;
    private final boolean update = false;
    private final File addonFolder = null;

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
     * All PetAddon classes must have a Namespace Annotation to give it a name
     * This method just retrieves it
     */
    public Namespace getNamespace() {
        if (getClass().isAnnotationPresent(Namespace.class)) return getClass().getAnnotation(Namespace.class);
        throw new NullPointerException(getClass().getSimpleName() + " is missing @Namespace annotation for the addon");
    }

    public ItemStack getAddonIcon (){
        return new ItemBuilder (Material.PLAYER_HEAD)
                .setTexture("http://textures.minecraft.net/texture/" +
                        (enabled ? "78d58a7651fedae4c03efebc226c03fd791eb74a132babb974e8d838ac6882" : "2da1508d47ed73b5c515e3b93928b728e4bc6278569a79b3723ab6972ce05357"))
                .withName(Colorize.fetchColor("e1eb5b")+getNamespace().namespace()).withLore(getDescription())
                .addLore("&r ", "&7Author: &e"+getAuthor()).build();
    }

    /**
     * What version is this addon
     */
    public abstract double getVersion ();

    /**
     * What version of SimplePets was the compiled for
     *      Get this from the pom.xml, plugin.yml, or from /version SimplePets
     *   Include the full version
     *      Example: 5.0 (build 10)
     */
    public String getSupportedVersion () {
        return "";
    }

    /**
     * Who made the addon?
     */
    public abstract String getAuthor ();

    /**
     * Please describe the addon a bit
     */
    public abstract List<String> getDescription ();

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
}
