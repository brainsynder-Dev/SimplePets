package simplepets.brainsynder.api.wrappers;

public enum ParrotVariant {
    RED(0),
    BLUE(1),
    GREEN(2),
    CYAN(3),
    GRAY(4);

    private final int id;

    ParrotVariant(int id) {
        this.id = id;
    }


    public static ParrotVariant getById(int id) {
        for (ParrotVariant wrapper : values()) {
            if (wrapper.id == id) {
                return wrapper;
            }
        }
        return null;
    }

    public static ParrotVariant getByName (String name) {
        for (ParrotVariant wrapper : values()) {
            if (wrapper.name().equalsIgnoreCase(name)) return wrapper;
        }
        return RED;
    }


    public static ParrotVariant getPrevious(ParrotVariant current) {
        return current.id == 0 ? GRAY : values()[(current.id - 1)];
    }

    public static ParrotVariant getNext(ParrotVariant current) {
        return current.id == 4 ? RED : values()[(current.id + 1)];
    }
}
