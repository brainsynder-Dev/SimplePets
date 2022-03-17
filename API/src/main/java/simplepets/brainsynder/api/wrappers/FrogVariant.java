package simplepets.brainsynder.api.wrappers;

public enum FrogVariant {
    TEMPERATE ("1b20e0c01e2a241fb6fbf45045b9c9dbfecf745c62a8fda6eb6522fc2d53e2cf"),
    WARM ("1b20e0c01e2a241fb6fbf45045b9c9dbfecf745c62a8fda6eb6522fc2d53e2cf"),
    COLD ("1b20e0c01e2a241fb6fbf45045b9c9dbfecf745c62a8fda6eb6522fc2d53e2cf");

    private final String texture;
    FrogVariant(String texture) {
        this.texture = "http://textures.minecraft.net/texture/"+texture;
    }

    public String getTexture() {
        return texture;
    }

    public static FrogVariant getByID(int id) {
        for (FrogVariant catType : values()) if (catType.ordinal() == id) return catType;
        return TEMPERATE;
    }
    public static FrogVariant getByName(String name) {
        for (FrogVariant catType : values()) if (catType.name().equalsIgnoreCase(name)) return catType;
        return TEMPERATE;
    }

    public static FrogVariant getPrevious(FrogVariant current) {
        if (current == TEMPERATE) return COLD;
        return values()[(current.ordinal() - 1)];
    }
    public static FrogVariant getNext(FrogVariant current) {
        if (current == COLD) return TEMPERATE;
        return values()[(current.ordinal() + 1)];
    }
}