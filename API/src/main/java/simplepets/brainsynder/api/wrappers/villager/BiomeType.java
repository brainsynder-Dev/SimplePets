package simplepets.brainsynder.api.wrappers.villager;

import lib.brainsynder.item.ItemBuilder;
import org.bukkit.Material;

/**
 * This is used to handle the 1.14 Villager Biome Types
 */
public enum BiomeType {
    DESERT(new ItemBuilder(Material.SAND)),
    JUNGLE(new ItemBuilder(Material.VINE)),
    PLAINS(new ItemBuilder(Material.GRASS_BLOCK)),
    SAVANNA(new ItemBuilder(Material.ACACIA_LOG)),
    SNOW(new ItemBuilder(Material.SNOW_BLOCK)),
    SWAMP(new ItemBuilder(Material.SLIME_BLOCK)),
    TAIGA(new ItemBuilder(Material.PODZOL));

    private final ItemBuilder icon;
    BiomeType (ItemBuilder icon) {
        this.icon = icon;
    }

    public ItemBuilder getIcon() {
        return icon;
    }

    public static BiomeType getByName(String name) {
        for (BiomeType wrapper : values()) {
            if (wrapper.name().equalsIgnoreCase(name)) return wrapper;
        }
        return DESERT;
    }

    public static BiomeType getPrevious(BiomeType current) {
        if (current == DESERT) return TAIGA;
        return values()[(current.ordinal() - 1)];
    }
    public static BiomeType getNext(BiomeType current) {
        if (current == TAIGA) return DESERT;
        return values()[(current.ordinal() + 1)];
    }
}
