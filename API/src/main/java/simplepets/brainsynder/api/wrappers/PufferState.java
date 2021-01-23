package simplepets.brainsynder.api.wrappers;

public enum PufferState {
    SMALL,
    MEDIUM,
    LARGE;

    public static PufferState getByID(int id) {
        for (PufferState v : values()) {
            if (v.ordinal() == id) {
                return v;
            }
        }
        return null;
    }

    public static PufferState getByName(String name) {
        for (PufferState wrapper : values()) {
            if (wrapper.name().equalsIgnoreCase(name)) return wrapper;
        }
        return SMALL;
    }

    public static PufferState getPrevious(PufferState current) {
        PufferState target = SMALL;

        switch (current) {
            case SMALL:
                target = LARGE;
                break;
            case MEDIUM:
                target = SMALL;
                break;
            case LARGE:
                target = MEDIUM;
                break;
        }
        return target;
    }

    public static PufferState getNext(PufferState current) {
        PufferState target = SMALL;

        switch (current) {
            case SMALL:
                target = MEDIUM;
                break;
            case MEDIUM:
                target = LARGE;
                break;
            case LARGE:
                target = SMALL;
                break;
        }
        return target;
    }
}