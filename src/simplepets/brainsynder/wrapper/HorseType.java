package simplepets.brainsynder.wrapper;


import org.bukkit.entity.Horse.Variant;

/**
 * @Deprecated Will be removed when MC version 1.13 is released
 */
@Deprecated
public enum HorseType {
    NORMAL(Variant.HORSE, 0),
    DONKEY(Variant.DONKEY, 1),
    MULE(Variant.MULE, 2),
    ZOMBIE(Variant.UNDEAD_HORSE, 3),
    SKELETON(Variant.SKELETON_HORSE, 4);

    private Variant bukkitVariant;
    private int id;

    HorseType(Variant bukkitVariant, int id) {
        this.bukkitVariant = bukkitVariant;
        this.id = id;
    }

    public static HorseType getByName(String name) {
        for (HorseType wrapper : values()) {
            if (wrapper.name().equalsIgnoreCase(name)) return wrapper;
        }
        return NORMAL;
    }

    public static HorseType getForBukkitVariant(Variant variant) {
        HorseType[] arr$ = values();
        int len$ = arr$.length;

        for (int i$ = 0; i$ < len$; ++i$) {
            HorseType v = arr$[i$];
            if (v.getBukkitVariant().equals(variant)) {
                return v;
            }
        }

        return null;
    }

    public int getId() {
        return this.id;
    }

    public Variant getBukkitVariant() {
        return this.bukkitVariant;
    }
}
