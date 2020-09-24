package simplepets.brainsynder.menu.items;

import lib.brainsynder.files.JsonFile;
import lib.brainsynder.item.ItemBuilder;
import lib.brainsynder.json.JsonObject;
import lib.brainsynder.nbt.StorageTagTools;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.menu.inventory.CustomInventory;
import simplepets.brainsynder.player.PetOwner;

import java.io.File;

public class Item extends JsonFile {
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
        setDefault("item", StorageTagTools.toJsonObject(getDefaultItem().toCompound()));
    }

    public ItemBuilder getItemBuilder () {
        if (hasKey("item")) {
            try {
                return ItemBuilder.fromCompound(StorageTagTools.fromJsonObject((JsonObject) getValue("item")));
            } catch (IllegalArgumentException ex) {
                PetCore.get().getLogger().warning("Error thrown when creating item for " + namespace() + ".");
                return getDefaultItem();
            }

        }
        if (!tried) {
            reload();
            tried = true;
            return getItemBuilder();
        }

        return lib.brainsynder.nms.DataConverter.getColoredMaterial(lib.brainsynder.nms.DataConverter.MaterialType.STAINED_GLASS_PANE, 14);
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
        if (hasKey("namespace")) return getString("namespace");
        return getClass().getSimpleName().toLowerCase();
    }

    public boolean isEnabled () {
        if (hasKey("enabled")) return getBoolean("enabled");
        return true;
    }
}
