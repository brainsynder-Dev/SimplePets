package simplepets.brainsynder.api.wrappers;

public enum AngerLevel {
    CALM,
    AGITATED,
    ANGRY;

    public static AngerLevel getByID(int id) {
        for (AngerLevel v : values()) {
            if (v.ordinal() == id) {
                return v;
            }
        }
        return CALM;
    }

    public static AngerLevel getByName(String name) {
        for (AngerLevel wrapper : values()) {
            if (wrapper.name().equalsIgnoreCase(name)) return wrapper;
        }
        return CALM;
    }

    public static AngerLevel getPrevious(AngerLevel current) {
        return switch (current) {
            case CALM -> ANGRY;
            case AGITATED -> CALM;
            case ANGRY -> AGITATED;
        };
    }

    public static AngerLevel getNext(AngerLevel current) {
        return switch (current) {
            case CALM -> AGITATED;
            case AGITATED -> ANGRY;
            case ANGRY -> CALM;
        };
    }
}