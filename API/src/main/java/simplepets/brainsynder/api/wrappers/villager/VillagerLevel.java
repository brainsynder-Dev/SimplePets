package simplepets.brainsynder.api.wrappers.villager;

import lib.brainsynder.item.ItemBuilder;
import org.bukkit.Material;

/**
 * This is used to handle the 1.14 Villager Types/Professions
 */
public enum VillagerLevel {
    NOVICE("8e140dc30e7bd57d0c5ff71a6818500434692f492479a5426aa231d7ad994d8a"),
    APPRENTICE("126b772329cf32f8643c4928626b6a325233ff61aa9c7725873a4bd66db3d692"),
    JOURNEYMAN("97f57e7aa8de86591bb0bc52cba30a49d931bfabbd47bbc80bdd662251392161"),
    EXPERT("ac906d688e65802569d9705b579bce56edc86ea5c36bdd6d6fc35516a77d4"),
    MASTER("a00b26a42df13c769942b01727e0a4205bbd56c61c5fbd25ce35f3d7478c73b8");

    private final ItemBuilder icon;

    VillagerLevel(String texture) {
        this.icon = new ItemBuilder(Material.PLAYER_HEAD).setTexture("http://textures.minecraft.net/texture/"+texture);
    }

    VillagerLevel(ItemBuilder icon) {
        this.icon = icon;
    }

    public static VillagerLevel getById(int id) {
        for (VillagerLevel wrapper : values()) {
            if (wrapper.ordinal() == (id-1)) {
                return wrapper;
            }
        }
        return null;
    }

    public static VillagerLevel getPrevious(VillagerLevel current) {
        if (current == NOVICE) return MASTER;
        return values()[(current.ordinal() - 1)];
    }

    public static VillagerLevel getNext(VillagerLevel current) {
        if (current == MASTER) return NOVICE;
        return values()[(current.ordinal() + 1)];
    }

    public static VillagerLevel getVillagerType(String name) {
        for (VillagerLevel wrapper : values()) {
            if (wrapper.name().equalsIgnoreCase(name)) return wrapper;
        }

        return NOVICE;
    }

    public ItemBuilder getIcon() {
        return icon;
    }
}