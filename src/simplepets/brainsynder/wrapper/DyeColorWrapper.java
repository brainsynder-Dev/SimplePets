package simplepets.brainsynder.wrapper;

import lombok.Getter;

public enum DyeColorWrapper {
    WHITE(0, 15, 'f'),
    ORANGE(1, 14, '6'),
    MAGENTA(2, 13, '5'),
    LIGHT_BLUE(3, 12, '9'),
    YELLOW(4, 11, 'e'),
    LIME(5, 10, 'a'),
    PINK(6, 9, 'd'),
    GRAY(7, 8, '8'),
    SILVER(8, 7, '7'),
    CYAN(9, 6, '3'),
    PURPLE(10, 5, '5'),
    BLUE(11, 4, '1'),
    BROWN(12, 3, '4'),
    GREEN(13, 2, '2'),
    RED(14, 1, 'c'),
    BLACK(15, 0, '0');

    @Getter
    private byte woolData;
    @Getter
    private byte dyeData;
    @Getter
    private char chatChar;

    DyeColorWrapper(int woolData, int dyeData, char chatChar) {
        this.woolData = (byte) woolData;
        this.dyeData = (byte) dyeData;
        this.chatChar = chatChar;
    }

    public static DyeColorWrapper getByName (String name) {
        for (DyeColorWrapper wrapper : values()) {
            if (wrapper.name().equalsIgnoreCase(name)) return wrapper;
        }
        return WHITE;
    }

    public static DyeColorWrapper getPrevious(DyeColorWrapper current) {
        int original = current.ordinal();
        if (original == 0) {
            return BLACK;
        }
        return values()[(original - 1)];
    }

    public static DyeColorWrapper getNext(DyeColorWrapper current) {
        int original = current.ordinal();
        if (original == 15) {
            return WHITE;
        }
        return values()[(original + 1)];
    }

    public static DyeColorWrapper getByWoolData(byte data) {
        for (DyeColorWrapper wrapper : values()) {
            if (wrapper.woolData == data)
                return wrapper;
        }
        return null;
    }

    public static DyeColorWrapper getByDyeData(byte data) {
        for (DyeColorWrapper wrapper : values()) {
            if (wrapper.dyeData == data)
                return wrapper;
        }
        return null;
    }
}
