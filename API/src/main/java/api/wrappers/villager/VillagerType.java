package api.wrappers.villager;

import lib.brainsynder.item.ItemBuilder;
import org.bukkit.Material;

/**
 * This is used to handle the 1.14 Villager Types/Professions
 */
public enum VillagerType {
    NONE(new ItemBuilder(Material.EMERALD)),
    ARMORER(new ItemBuilder(Material.IRON_CHESTPLATE)),
    BUTCHER(new ItemBuilder(Material.COOKED_CHICKEN)),
    CARTOGRAPHER(new ItemBuilder(Material.MAP)),
    CLERIC(new ItemBuilder(Material.ENCHANTED_BOOK)),
    FARMER(new ItemBuilder(Material.IRON_HOE)),
    FISHERMAN(new ItemBuilder(Material.FISHING_ROD)),
    FLETCHER(new ItemBuilder(Material.ARROW)),
    LEATHERWORKER(new ItemBuilder(Material.LEATHER)),
    LIBRARIAN(new ItemBuilder(Material.BOOK)),
    MASON(new ItemBuilder(Material.BRICK)),
    NITWIT(new ItemBuilder(Material.INK_SAC)),
    SHEPHERD(new ItemBuilder(Material.WHITE_WOOL)),
    TOOLSMITH(new ItemBuilder(Material.IRON_PICKAXE)),
    WEAPONSMITH(new ItemBuilder(Material.IRON_SWORD));

    private final ItemBuilder icon;

    VillagerType(ItemBuilder icon) {
        this.icon = icon;
    }

    public static VillagerType getById(int id) {
        for (VillagerType wrapper : values()) {
            if (wrapper.ordinal() == id) {
                return wrapper;
            }
        }
        return null;
    }

    public static VillagerType getPrevious(VillagerType current) {
        if (current == NONE) return WEAPONSMITH;
        return values()[(current.ordinal() - 1)];
    }

    public static VillagerType getNext(VillagerType current) {
        if (current == WEAPONSMITH) return NONE;
        return values()[(current.ordinal() + 1)];
    }

    public static VillagerType getVillagerType(String name) {
        for (VillagerType wrapper : values()) {
            if (wrapper.name().equalsIgnoreCase(name)) return wrapper;
        }

        return FARMER;
    }

    public ItemBuilder getIcon() {
        return icon;
    }
}