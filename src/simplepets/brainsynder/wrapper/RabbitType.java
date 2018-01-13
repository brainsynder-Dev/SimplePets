package simplepets.brainsynder.wrapper;

import lombok.Getter;

public enum RabbitType {
    BROWN(0),
    WHITE(1),
    BLACK(2),
    BLACK_AND_WHITE(3),
    GOLD(4),
    SALT_AND_PEPPER(5),
    THE_KILLER_BUNNY(99);

    @Getter
    private int id;

    RabbitType(int id) {
        this.id = id;
    }

    public static RabbitType getByID(int id) {
        for (RabbitType v : values()) {
            if (v.getId() == id) {
                return v;
            }
        }
        return null;
    }

    public static RabbitType getByName(String name) {
        for (RabbitType wrapper : values()) {
            if (wrapper.name().equalsIgnoreCase(name)) return wrapper;
        }
        return BROWN;
    }
}
