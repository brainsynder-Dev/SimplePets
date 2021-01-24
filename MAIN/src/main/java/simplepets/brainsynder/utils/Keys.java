package simplepets.brainsynder.utils;

import org.bukkit.NamespacedKey;
import simplepets.brainsynder.PetCore;

public class Keys {
    public final static NamespacedKey ENTITY_OWNER;
    public final static NamespacedKey ENTITY_TYPE;
    public final static NamespacedKey BOOK_KEY;

    static {
        ENTITY_OWNER = new NamespacedKey(PetCore.getInstance(), "ownerUUID");
        ENTITY_TYPE = new NamespacedKey(PetCore.getInstance(), "petType");
        BOOK_KEY = new NamespacedKey(PetCore.getInstance(), "book_key");
    }

}
