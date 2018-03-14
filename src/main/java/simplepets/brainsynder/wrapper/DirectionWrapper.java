package simplepets.brainsynder.wrapper;

public enum DirectionWrapper {
    DOWN,
    UP,
    NORTH,
    SOUTH,
    WEST,
    EAST;

    public static DirectionWrapper getPrevious(DirectionWrapper current) {
        int original = current.ordinal();
        if (original == 0) {
            return EAST;
        }
        return values()[(original - 1)];
    }

    public static DirectionWrapper getNext(DirectionWrapper current) {
        int original = current.ordinal();
        if (original == 5) {
            return DOWN;
        }
        return values()[(original + 1)];
    }

    public static DirectionWrapper fromString(String direction) {
        for (DirectionWrapper wrapper : values()) {
            if (wrapper.name().equals(direction))
                return wrapper;
        }
        return null;
    }
}
