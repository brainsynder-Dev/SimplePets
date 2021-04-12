package simplepets.brainsynder.api.wrappers;

public enum ParrotVariant {
    RED("a4ba8d66fecb1992e94b8687d6ab4a5320ab7594ac194a2615ed4df818edbc3"),
    BLUE("aca580b051c63be29da545a9aa7ff7e136df77a81c67dc1ee9e6170c14fb310"),
    GREEN("ab9a36c5589f3a2e59c1caa9b3b88fada76732bdb4a7926388a8c088bbbcb"),
    CYAN("2b94f236c4a642eb2bcdc3589b9c3c4a0b5bd5df9cd5d68f37f8c83f8e3f1"),
    GRAY("3d6f4a21e0d62af824f8708ac63410f1a01bbb41d7f4a702d9469c6113222");

    private final String texture;

    ParrotVariant(String texture) {
        this.texture = "http://textures.minecraft.net/texture/"+texture;
    }

    public String getTexture() {
        return texture;
    }

    public static ParrotVariant getById(int id) {
        for (ParrotVariant wrapper : values()) {
            if (wrapper.ordinal() == id) {
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
        return current.ordinal() == 0 ? GRAY : values()[(current.ordinal() - 1)];
    }

    public static ParrotVariant getNext(ParrotVariant current) {
        return current.ordinal() == 4 ? RED : values()[(current.ordinal() + 1)];
    }
}
