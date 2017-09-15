package simplepets.brainsynder.wrapper;

import lombok.Getter;

public enum RabbitColor {
    BROWN(0),
    WHITE(1),
    BLACK(2),
    BLACK_AND_WHITE(3),
    GOLD(4),
    SALT_AND_PEPPER(5),
    THE_KILLER_BUNNY(99);

    @Getter
    private int id;

    RabbitColor(int id) {
        this.id = id;
    }

    public static RabbitColor getByID(int id) {
        for (RabbitColor v : values()) {
            if (v.getId() == id) {
                return v;
            }
        }
        return null;
    }
}
