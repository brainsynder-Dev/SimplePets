package simplepets.brainsynder.utils;

import org.bukkit.NamespacedKey;
import simplepets.brainsynder.PetCore;

public class Keys {
    public final static NamespacedKey ENTITY_OWNER;
    public final static NamespacedKey ENTITY_TYPE;
    public final static NamespacedKey BOOK_KEY;
    public final static NamespacedKey ADDON_NAME;
    public final static NamespacedKey ADDON_UPDATE;
    public final static NamespacedKey ADDON_URL;

    static {
        ENTITY_OWNER = new NamespacedKey(PetCore.getInstance(), "ownerUUID");
        ENTITY_TYPE = new NamespacedKey(PetCore.getInstance(), "petType");
        BOOK_KEY = new NamespacedKey(PetCore.getInstance(), "book_key");
        ADDON_NAME = new NamespacedKey(PetCore.getInstance(), "addon_name");
        ADDON_URL = new NamespacedKey(PetCore.getInstance(), "addon_url");
        ADDON_UPDATE = new NamespacedKey(PetCore.getInstance(), "addon_update_url");
    }

}
