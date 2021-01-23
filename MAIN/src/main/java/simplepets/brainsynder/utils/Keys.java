package simplepets.brainsynder.utils;

import org.bukkit.NamespacedKey;
import simplepets.brainsynder.PetCore;

public class Keys {
    public final static NamespacedKey ENTITY_OWNER;
    public final static NamespacedKey ENTITY_TYPE;

    static {
        ENTITY_OWNER = new NamespacedKey(PetCore.getInstance(), "ownerUUID");
        ENTITY_TYPE = new NamespacedKey(PetCore.getInstance(), "petType");
    }

}
