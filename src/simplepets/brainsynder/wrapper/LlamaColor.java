package simplepets.brainsynder.wrapper;

import lombok.Getter;

public enum LlamaColor {
    CREAMY(0),
    WHITE(1),
    BROWN(2),
    GRAY(3);

    @Getter
    private int id;

    LlamaColor(int id) {
        this.id = id;
    }

    public static LlamaColor getByID(int id) {
        for (LlamaColor v : values()) {
            if (v.getId() == id) {
                return v;
            }
        }
        return null;
    }
}