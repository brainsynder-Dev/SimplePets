package api.wrappers;

public enum ColorWrapper {
    NONE(-1, -1, 'r'),
    WHITE(0, 15, 'f'),
    ORANGE(1, 14, '6'),
    MAGENTA(2, 13, '5'),
    LIGHT_BLUE(3, 12, '9'),
    YELLOW(4, 11, 'e'),
    LIME(5, 10, 'a'),
    PINK(6, 9, 'd'),
    GRAY(7, 8, '8'),
    LIGHT_GRAY(8, 7, '7'),
    CYAN(9, 6, '3'),
    PURPLE(10, 5, '5'),
    BLUE(11, 4, '1'),
    BROWN(12, 3, '4'),
    GREEN(13, 2, '2'),
    RED(14, 1, 'c'),
    BLACK(15, 0, '0');

    private final int woolData;
    private final int dyeData;
    private final char chatChar;

    ColorWrapper(int woolData, int dyeData, char chatChar) {
        this.woolData = woolData;
        this.dyeData = dyeData;
        this.chatChar = chatChar;
    }

    public static ColorWrapper getByName(String name) {
        for (ColorWrapper wrapper : values()) {
            if (wrapper.name().equalsIgnoreCase(name)) return wrapper;
        }

        return NONE;
    }

    public static ColorWrapper getPrevious(ColorWrapper current) {
        return (current == NONE) ? BLACK : values()[current.ordinal() - 1];
    }

    public static ColorWrapper getNext(ColorWrapper current) {
        return (current == BLACK) ? NONE : values()[current.ordinal() + 1];
    }

    public static ColorWrapper getByWoolData(byte data) {
        for (ColorWrapper wrapper : values()) {
            if (wrapper.woolData == data) return wrapper;
        }

        return NONE;
    }

    public static ColorWrapper getByDyeData(byte data) {
        for (ColorWrapper wrapper : values()) {
            if (wrapper.dyeData == data) return wrapper;
        }

        return NONE;
    }

    public int getWoolData() {
        return this.woolData;
    }

    public int getDyeData() {
        return this.dyeData;
    }

    public char getChatChar() {
        return this.chatChar;
    }
}