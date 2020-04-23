package simplepets.brainsynder.wrapper.villager;

import lib.brainsynder.ServerVersion;
import lib.brainsynder.item.ItemBuilder;
import org.bukkit.Material;
import simplepets.brainsynder.wrapper.ProfessionWrapper;

/**
 * This is used to handle the 1.14 Villager Types/Professions
 */
public enum VillagerType {
    NONE(0, ServerVersion.v1_14_R1, new ItemBuilder(Material.EMERALD)),
    ARMORER(1, ServerVersion.v1_14_R1, new ItemBuilder(Material.IRON_CHESTPLATE)),
    BUTCHER(2, ServerVersion.v1_14_R1, new ItemBuilder(Material.COOKED_CHICKEN)),
    CARTOGRAPHER(3, ServerVersion.v1_14_R1, new ItemBuilder(Material.MAP)),
    CLERIC(4, ServerVersion.v1_14_R1, new ItemBuilder(Material.ENCHANTED_BOOK)),
    FARMER(5, ServerVersion.v1_14_R1, new ItemBuilder(Material.IRON_HOE)),
    FISHERMAN(6, ServerVersion.v1_14_R1, new ItemBuilder(Material.FISHING_ROD)),
    FLETCHER(7, ServerVersion.v1_14_R1, new ItemBuilder(Material.ARROW)),
    LEATHERWORKER(8, ServerVersion.v1_14_R1, new ItemBuilder(Material.LEATHER)),
    LIBRARIAN(9, ServerVersion.v1_14_R1, new ItemBuilder(Material.BOOK)),
    MASON(10, ServerVersion.v1_14_R1, new ItemBuilder(Material.BRICK)),
    NITWIT(11, ServerVersion.v1_14_R1, lib.brainsynder.nms.DataConverter.getColoredMaterial(lib.brainsynder.nms.DataConverter.MaterialType.INK_SACK, 2)),
    SHEPHERD(12, ServerVersion.v1_14_R1, lib.brainsynder.nms.DataConverter.getColoredMaterial(lib.brainsynder.nms.DataConverter.MaterialType.WOOL, 0)),
    TOOLSMITH(13, ServerVersion.v1_14_R1, new ItemBuilder(Material.IRON_PICKAXE)),
    WEAPONSMITH(14, ServerVersion.v1_14_R1, new ItemBuilder(Material.IRON_SWORD));

    private final int id;
    private final ServerVersion version;
    private final ItemBuilder icon;

    VillagerType(int id, ServerVersion version, ItemBuilder icon) {
        this.id = id;
        this.icon = icon;
        this.version = version;
    }

    public static VillagerType fromProfession (ProfessionWrapper wrapper) {
        if (wrapper == ProfessionWrapper.PRIEST) return CLERIC;
        if (wrapper == ProfessionWrapper.BLACKSMITH) return TOOLSMITH;
        return valueOf(wrapper.name());
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

    public boolean isSupported() {
        return ServerVersion.isEqualNew(version);
    }

    public int getId() {
        return id;
    }
}