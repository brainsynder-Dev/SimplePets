package simplepets.brainsynder.api.wrappers.villager;

import lib.brainsynder.item.ItemBuilder;
import org.bukkit.Material;

/**
 * This is used to handle the 1.14 Villager Types/Professions
 */
public enum VillagerType {
    NONE("41b830eb4082acec836bc835e40a11282bb51193315f91184337e8d3555583"),
    ARMORER("c29676ee6da5a3521cb478463779b8711e31887cd9a6d9fdecfcbc55083e5515"),
    BUTCHER("73bd1e78664d76aab13706f7ce91f75a52cec017d60446fb43bd8bf2042dd3b3"),
    CARTOGRAPHER("afc6ac9d85554bbe56a6668de0a54cb1eff93e14f4bcdf7245a744cc37721f0a"),
    CLERIC("261c40f8da409eef4432ce1063f50052017075038cf3a9a65485ea621c549f40"),
    FARMER("d01e035a3d8d6126072bcbe52a97913ace93552a99995b5d4070d6783a31e909"),
    FISHERMAN("79a9ebdd2c1bf2d0a61c3b98c0bc4277444a1b8fedbb16cca4faaca9f7ec0592"),
    FLETCHER("5d1c1bc63a2d59c93d07ed1ab6dae7bdd4c2e9f1a77b16dc7fb106c3545b6e47"),
    LEATHERWORKER(new ItemBuilder(Material.LEATHER)),
    LIBRARIAN("e698477d7d532748af4e09115ef16bb95994f0e58b090e6d3064458bb9ae1687"),
    MASON("2c02c3ffd5705ab488b305d57ff0168e26de70fd3f739e839661ab947dff37b1"),
    NITWIT("4541e8ebc881c8a02fe4af44f2928ea9188b539fd561b5b3fd731896c33bb524"),
    SHEPHERD("11955168ef53f7120c089dafe3e6e437e95240555d8c3accf944d6c56b740475"),
    TOOLSMITH("16ec61097e11bfe6f10aaa12e5c0a54c829bdbd9d9d7a32fc627e6b5a931e77"),
    WEAPONSMITH(new ItemBuilder(Material.IRON_SWORD));

    private final ItemBuilder icon;

    VillagerType(String texture) {
        this.icon = new ItemBuilder(Material.PLAYER_HEAD).setTexture("http://textures.minecraft.net/texture/"+texture);
    }

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