package simplepets.brainsynder.wrapper;

import lombok.Getter;
import org.bukkit.entity.Horse;

public enum HorseStyleType {
    NONE(0, Horse.Style.NONE, new Integer[]{0, 1, 2, 3, 4, 5, 6}),
    WHITE(1, Horse.Style.WHITE, new Integer[]{256, 257, 258, 259, 260, 261, 262}),
    WHITEFIELD(2, Horse.Style.WHITEFIELD, new Integer[]{512, 513, 514, 515, 516, 517, 518}),
    WHITE_DOTS(3, Horse.Style.WHITE_DOTS, new Integer[]{768, 769, 770, 771, 772, 773, 774}),
    BLACK_DOTS(4, Horse.Style.BLACK_DOTS, new Integer[]{1024, 1025, 1026, 1027, 1028, 1029, 1030});


    private HorseColorType[] a;
    private Integer[] b;
    private Horse.Style bukkitStyle;
    @Getter
    private int id;

    HorseStyleType(int id, Horse.Style bukkitStyle, Integer[] i) {
        this.a = new HorseColorType[]{HorseColorType.WHITE, HorseColorType.CREAMY, HorseColorType.CHESTNUT, HorseColorType.BROWN, HorseColorType.BLACK, HorseColorType.GRAY, HorseColorType.DARKBROWN};
        this.bukkitStyle = bukkitStyle;
        this.b = i;
        this.id = id;
    }

    public static HorseStyleType getByName(String name) {
        for (HorseStyleType wrapper : values()) {
            if (wrapper.name().equalsIgnoreCase(name)) return wrapper;
        }
        return NONE;
    }

    public static HorseStyleType getForBukkitStyle(Horse.Style style) {
        HorseStyleType[] arr$ = values();
        int len$ = arr$.length;

        for (int i$ = 0; i$ < len$; ++i$) {
            HorseStyleType v = arr$[i$];
            if (v.getBukkitStyle().equals(style)) {
                return v;
            }
        }

        return null;
    }

    public static HorseStyleType getByID(int id) {
        for (HorseStyleType v : values()) {
            if (v.getId() == id) {
                return v;
            }
        }
        return null;
    }

    public int getId(HorseColorType v) {
        for (int i = 0; i < HorseColorType.values().length; ++i) {
            if (this.a[i] == v) {
                return this.b[i];
            }
        }

        return -1;
    }

    public Horse.Style getBukkitStyle() {
        return this.bukkitStyle;
    }
}