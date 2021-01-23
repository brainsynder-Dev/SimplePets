package simplepets.brainsynder.api.wrappers;

public enum FoxType  {
    RED,
    WHITE;

    public static FoxType getByID(int id) {
        for (FoxType v : values()) {
            if (v.ordinal() == id) {
                return v;
            }
        }
        return RED;
    }
    public static FoxType getByName(String name) {
        for (FoxType wrapper : values()) {
            if (wrapper.name().equalsIgnoreCase(name)) return wrapper;
        }
        return RED;
    }

    public static FoxType getPrevious(FoxType current) {
        if (current == RED) return WHITE;
        return values()[(current.ordinal() - 1)];
    }
    public static FoxType getNext(FoxType current) {
        if (current == WHITE) return RED;
        return values()[(current.ordinal() + 1)];
    }
}
