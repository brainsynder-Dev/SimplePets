package simplepets.brainsynder.api.wrappers.horse;

public enum HorseColorType {
    WHITE("9f4bdd59d4f8f1d5782e0fee4bd64aed100627f188a91489ba37eeadededd827"),
    CREAMY("a6dae0ade0e0dafb6dbc7786ce4241242b6b6df527a0f7af0a42184c93fd646b"),
    CHESTNUT("9717d71025f7a62c90a333c51663ffeb385a9a0d92af68083c5b045c0524b23f"),
    BROWN("25e397def0af06feef22421860088186639732aa0a5eb5756e0aa6b03fd092c8"),
    BLACK("3efb0b9857d7c8d295f6df97b605f40b9d07ebe128a6783d1fa3e1bc6e44117"),
    GRAY("8f0d955889b0378d4933c956398567e770103ae9eff0f702d0d53d52e7f6a83b"),
    DARKBROWN("156b7bc1a4836eb428ea8925eceb5e01dfbd30c7deff6c9482689823203cfd2f");

    private final String texture;

    HorseColorType(String texture) {
        this.texture = "http://textures.minecraft.net/texture/"+texture;
    }

    public String getTexture() {
        return texture;
    }

    public static HorseColorType getByName(String name) {
        for (HorseColorType wrapper : values()) {
            if (wrapper.name().equalsIgnoreCase(name)) return wrapper;
        }
        return WHITE;
    }

    public static HorseColorType getByID(int id) {
        for (HorseColorType v : values()) {
            if (v.ordinal() == id) return v;
        }
        return null;
    }

    public static HorseColorType getPrevious(HorseColorType current) {
        return (current == WHITE) ? BLACK : values()[current.ordinal() - 1];
    }

    public static HorseColorType getNext(HorseColorType current) {
        return (current == DARKBROWN) ? WHITE : values()[current.ordinal() + 1];
    }
}