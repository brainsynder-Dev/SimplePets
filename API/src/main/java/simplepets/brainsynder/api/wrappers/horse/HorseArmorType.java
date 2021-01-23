package simplepets.brainsynder.api.wrappers.horse;

public enum HorseArmorType {
    NONE(0),
    LEATHER(1),
    IRON(2),
    GOLD(3),
    DIAMOND(4);

    private final int id;

    HorseArmorType(int id) {
        this.id = id;
    }

    public static HorseArmorType getByName(String name) {
        for (HorseArmorType wrapper : values()) {
            if (wrapper.name().equalsIgnoreCase(name)) return wrapper;
        }
        return NONE;
    }

    public static HorseArmorType fromId(int id) {
        for (HorseArmorType armor : values()) {
            if (armor.getId() == id)
                return armor;
        }
        return null;
    }

    public int getId() {
        return this.id;
    }
}
