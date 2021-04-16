package simplepets.brainsynder.utils;

import org.bukkit.NamespacedKey;
import simplepets.brainsynder.PetCore;

public class Keys {
    public final static NamespacedKey GUI_ITEM; // Will be added to any items that is in the GUI
    public final static NamespacedKey PET_TYPE_ITEM; // Will be added to items that are for a PetType

    public final static NamespacedKey ENTITY_OWNER;
    public final static NamespacedKey ENTITY_TYPE;

    public final static NamespacedKey BOOK_KEY;

    public final static NamespacedKey ADDON_NAME;
    public final static NamespacedKey ADDON_UPDATE;
    public final static NamespacedKey ADDON_URL;

    static {
        GUI_ITEM = new NamespacedKey(PetCore.getInstance(), "gui_item");
        PET_TYPE_ITEM = new NamespacedKey(PetCore.getInstance(), "pet_item");

        ENTITY_OWNER = new NamespacedKey(PetCore.getInstance(), "ownerUUID");
        ENTITY_TYPE = new NamespacedKey(PetCore.getInstance(), "petType");

        BOOK_KEY = new NamespacedKey(PetCore.getInstance(), "book_key");

        ADDON_NAME = new NamespacedKey(PetCore.getInstance(), "addon_name");
        ADDON_URL = new NamespacedKey(PetCore.getInstance(), "addon_url");
        ADDON_UPDATE = new NamespacedKey(PetCore.getInstance(), "addon_update_url");
    }

}
