package simplepets.brainsynder.api.inventory;

import lib.brainsynder.files.JsonFile;
import lib.brainsynder.item.ItemBuilder;
import lib.brainsynder.json.JsonObject;
import lib.brainsynder.nbt.StorageTagTools;
import lib.brainsynder.utils.DyeColorWrapper;
import simplepets.brainsynder.api.Namespace;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.plugin.SimplePets;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.debug.DebugLevel;

import java.io.File;
import java.util.Optional;

public abstract class Item extends JsonFile {
    private boolean tried = false;

    public Item(File file) {
        super(file);
    }

    /**
     * This method gets called when the user clicks this item
     *
     * @param user - User clicking the item
     * @param inventory - The inventory they are clicking in
     */
    public void onClick(PetUser user, CustomInventory inventory) {
        onClick(user, inventory, null);
    }
    public void onShiftClick(PetUser user, CustomInventory inventory) {
        onShiftClick(user, inventory, null);
    }

    /**
     * This method gets called when the user clicks this item
     *
     * @param user - User clicking the item
     * @param inventory - The inventory they are clicking in
     */
    public abstract void onClick(PetUser user, CustomInventory inventory, IEntityPet pet);
    public void onShiftClick(PetUser user, CustomInventory inventory, IEntityPet pet) {}

    /**
     * Should the item be added to the inventory
     *      EG: Does the user have permission?
     *
     * @param user - User getting checked
     * @param inventory - Inventory it is viewed in
     * @return
     *      true - Item can be added
     *      false - ITem will NOT be added
     */
    public boolean addItemToInv(PetUser user, CustomInventory inventory) { return true; }

    @Override
    public void loadDefaults() { // Generates the default files for the item
        setDefault("enabled", true);
        if (getDefaultItem() != null)
        setDefault("item", StorageTagTools.toJsonObject(getDefaultItem().toCompound()));
    }

    /**
     * This will fetch the customized item from the file (This method is used in the GUIs)
     *
     * @return
     *      - The customized item from the file
     *      IF there is an error it will either return the {@link Item#getDefaultItem()} or red glass pain
     */
    public ItemBuilder getItemBuilder () {
        if (hasKey("item")) {
            try {
                return ItemBuilder.fromCompound(StorageTagTools.fromJsonObject((JsonObject) getValue("item")));
            } catch (IllegalArgumentException ex) {
                SimplePets.getDebugLogger().debug(DebugLevel.ERROR, "Error thrown when creating item for " + getClass().getSimpleName() + ".");
                // new RuntimeException(ex).printStackTrace();
                return getDefaultItem();
            }
        }

        if (!tried) {
            reload();
            tried = true;
            return getItemBuilder();
        }

        if (getDefaultItem() != null) return getDefaultItem();

        return lib.brainsynder.nms.DataConverter.getColoredMaterial(lib.brainsynder.nms.DataConverter.MaterialType.STAINED_GLASS_PANE, DyeColorWrapper.RED);
    }

    /**
     * This method is only used when generating the default file.
     *
     * {@link Item#getItemBuilder()}
     * @return
     */
    @Deprecated
    public abstract ItemBuilder getDefaultItem ();

    public Optional<Namespace> getItemData () {
        if (getClass().isAnnotationPresent(Namespace.class)) return Optional.of(getClass().getAnnotation(Namespace.class));
        return Optional.empty();
    }

    /**
     * If the item enabled?
     */
    public boolean isEnabled () {
        if (hasKey("enabled")) return getBoolean("enabled");
        return true;
    }
}