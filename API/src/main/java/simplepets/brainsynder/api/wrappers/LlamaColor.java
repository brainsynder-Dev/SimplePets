package simplepets.brainsynder.api.wrappers;

public enum LlamaColor {
    CREAMY("2a5f10e6e6232f182fe966f501f1c3799d45ae19031a1e4941b5dee0feff059b"),
    WHITE("83d9b5915912ffc2b85761d6adcb428a812f9b83ff634e331162ce46c99e9"),
    BROWN("818cd457fbaf327fa39f10b5b36166fd018264036865164c02d9e5ff53f45"),
    GRAY("cf24e56fd9ffd7133da6d1f3e2f455952b1da462686f753c597ee82299a");

    private final String texture;

    LlamaColor(String texture) {
        this.texture = "http://textures.minecraft.net/texture/"+texture;
    }

    public static LlamaColor getByID(int id) {
        for (LlamaColor v : values()) {
            if (v.ordinal() == id) {
                return v;
            }
        }
        return null;
    }

    public static LlamaColor getByName(String name) {
        for (LlamaColor wrapper : values()) {
            if (wrapper.name().equalsIgnoreCase(name)) return wrapper;
        }
        return CREAMY;
    }



    public static LlamaColor getPrevious(LlamaColor current) {
        return (current == CREAMY) ? GRAY : values()[current.ordinal() - 1];
    }

    public static LlamaColor getNext(LlamaColor current) {
        return (current == GRAY) ? CREAMY : values()[current.ordinal() + 1];
    }

    public String getTexture() {
        return texture;
    }
}