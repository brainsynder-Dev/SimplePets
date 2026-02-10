package simplepets.brainsynder.api.wrappers;

import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.inventory.ItemType;

public enum NautilusArmorType {
    NONE("barrier"),
    COPPER("copper_nautilus_armor"),
    IRON("iron_nautilus_armor"),
    GOLD("golden_nautilus_armor"),
    DIAMOND("diamond_nautilus_armor"),
    NETHERITE("netherite_nautilus_armor");

    private final String rawMaterial;

    NautilusArmorType(String rawMaterial) {
        this.rawMaterial = rawMaterial;
    }

    public NamespacedKey getKey () {
        return NamespacedKey.minecraft(rawMaterial);
    }

    public ItemType itemType() {
        return Registry.ITEM.get(getKey ());
    }

    public static NautilusArmorType getByName(String name) {
        for (NautilusArmorType wrapper : values()) {
            if (wrapper.name().equalsIgnoreCase(name)) return wrapper;
        }
        return NONE;
    }

    public static NautilusArmorType fromId(int id) {
        for (NautilusArmorType armor : values()) {
            if (armor.ordinal() == id)
                return armor;
        }
        return null;
    }

    public static NautilusArmorType getPrevious(NautilusArmorType current) {
        return (current == NONE) ? NETHERITE : values()[current.ordinal() - 1];
    }

    public static NautilusArmorType getNext(NautilusArmorType current) {
        return (current == NETHERITE) ? NONE : values()[current.ordinal() + 1];
    }
}
