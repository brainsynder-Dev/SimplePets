package simplepets.brainsynder.api.wrappers;

public enum SnifferState {
    IDLING("87ad920a66e38cc3426a5bff084667e8772116915e298098567c139f222e2c42"),
    FEELING_HAPPY("87ad920a66e38cc3426a5bff084667e8772116915e298098567c139f222e2c42"),
    SCENTING("87ad920a66e38cc3426a5bff084667e8772116915e298098567c139f222e2c42"),
    SNIFFING("87ad920a66e38cc3426a5bff084667e8772116915e298098567c139f222e2c42"),
    DIGGING("87ad920a66e38cc3426a5bff084667e8772116915e298098567c139f222e2c42");

    private final String texture;

    SnifferState(String texture) {
        this.texture = "http://textures.minecraft.net/texture/"+texture;
    }

    public String getTexture() {
        return texture;
    }

    public static SnifferState getByID(int id) {
        for (SnifferState v : values()) {
            if (v.ordinal() == id) return v;
        }
        return null;
    }

    public static SnifferState getByName(String name) {
        for (SnifferState wrapper : values()) {
            if (wrapper.name().equalsIgnoreCase(name)) return wrapper;
        }
        return IDLING;
    }

    public static SnifferState getPrevious(SnifferState current) {
        return (current == IDLING) ? DIGGING : values()[current.ordinal() - 1];
    }

    public static SnifferState getNext(SnifferState current) {
        return (current == DIGGING) ? IDLING : values()[current.ordinal() + 1];
    }

    public int getId () {
        return ordinal();
    }
}
