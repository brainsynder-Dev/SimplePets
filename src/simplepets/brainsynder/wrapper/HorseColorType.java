package simplepets.brainsynder.wrapper;

import lombok.Getter;
import org.bukkit.entity.Horse.Color;

public enum HorseColorType {
    WHITE(0, Color.WHITE),
    CREAMY(1, Color.CREAMY),
    CHESTNUT(2, Color.CHESTNUT),
    BROWN(3, Color.BROWN),
    BLACK(4, Color.BLACK),
    GRAY(5, Color.GRAY),
    DARKBROWN(6, Color.DARK_BROWN);

    @Getter
    private int id;
    private Color bukkitColour;

    HorseColorType(int id, Color bukkitColour) {
        this.bukkitColour = bukkitColour;
        this.id = id;
    }

    public static HorseColorType getByName(String name) {
        for (HorseColorType wrapper : values()) {
            if (wrapper.name().equalsIgnoreCase(name)) return wrapper;
        }
        return WHITE;
    }

    public static HorseColorType getByID(int id) {
        for (HorseColorType v : values()) {
            if (v.getId() == id) {
                return v;
            }
        }
        return null;
    }

    public static HorseColorType getForBukkitColour(Color colour) {
        for (HorseColorType v : values()) {
            if (v.getBukkitColour().equals(colour)) {
                return v;
            }
        }
        return null;
    }

    public Color getBukkitColour() {
        return this.bukkitColour;
    }
}