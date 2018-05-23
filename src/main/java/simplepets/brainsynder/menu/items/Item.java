package simplepets.brainsynder.menu.items;

import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.menu.inventory.CustomInventory;
import simplepets.brainsynder.player.PetOwner;
import simplepets.brainsynder.storage.files.base.JSONFile;
import simplepets.brainsynder.utils.ItemBuilder;
import simplepets.brainsynder.utils.Utilities;

import java.io.File;

public class Item extends JSONFile {
    private boolean tried = false;
    public static final String COMMANDS = "CommandsOnClick";

    public Item(File file) {
        super(file);
    }

    public static File getLocation (PetCore core, Class<? extends Item> clazz) {
        File folder = new File(core.getDataFolder().toString() + "/Items/");
        return new File(folder, clazz.getSimpleName() + ".json");
    }


    public void onClick(PetOwner owner, CustomInventory inventory) {}

    // Should the item be added to the inventory? (Used for items that have specific uses like pages)
    public boolean addItemToInv(PetOwner owner, CustomInventory inventory) { return true; }

    @Override
    public void loadDefaults() {
        setDefault("enabled", true);
        if (getDefaultItem() != null)
        setDefault("item", getDefaultItem().toJSON());
    }

    public ItemBuilder getItemBuilder () {
        if (hasKey("item")) return ItemBuilder.fromJSON(getObject("item"));
        if (!tried) {
            reload();
            tried = true;
            return getItemBuilder();
        }

        return new ItemBuilder(Utilities.toMaterial(Utilities.Type.STAINED_GLASS_PANE, 14));
    }

    /**
     * This method is only used when generating the default file.
     *
     * {@link Item#getItemBuilder()}
     * @return
     */
    @Deprecated
    public ItemBuilder getDefaultItem (){ return null; }

    public String namespace () {
        if (hasKey("namespace")) return getString("namespace", false);
        return getClass().getSimpleName().toLowerCase();
    }

    public boolean isEnabled () {
        if (hasKey("enabled")) return getBoolean("enabled");
        return true;
    }
}
