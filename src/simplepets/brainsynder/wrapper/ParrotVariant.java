package simplepets.brainsynder.wrapper;

public enum ParrotVariant {
    RED(0),
    BLUE(1),
    GREEN(2),
    CYAN(3),
    GRAY(4);

    private int id;

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
        int original = current.id;
        if (original == 0) {
            return GRAY;
        }
        return values()[(original - 1)];
    }

    public static ParrotVariant getNext(ParrotVariant current) {
        int original = current.id;
        if (original == 4) {
            return RED;
        }
        return values()[(original + 1)];
    }
}
