package simplepets.brainsynder.api.wrappers;

import org.bukkit.NamespacedKey;

import java.util.Locale;

public enum OxidationWrapper {
    UNAFFECTED("99e24e94dbe42e230d83293a77d61ff7101a8c68ab68bbc6a93f9630fb2fdb4"),
    EXPOSED("8a9e410c8b7fdbd4b9d8ea8075bc66a9c1ada9bc15873b4b1deadaa2812b847d"),
    WEATHERED("8262992d6d5a62a1dd3ede8e4c34f1c18050bab64271d926c1c0c6162b2cd74e"),
    OXIDIZED("cc2aab8a0fec3cc06a4d47480e024f88f2b07bc96ee09e5331e5f4db6b273885");

    private final NamespacedKey key;
    private final String texture;

    OxidationWrapper(String texture) {
        this.texture = "http://textures.minecraft.net/texture/" + texture;
        this.key = NamespacedKey.minecraft(name().toLowerCase(Locale.ROOT));
    }

    public String getTexture() {
        return texture;
    }

    public NamespacedKey getKey() {
        return key;
    }

    public static OxidationWrapper getByID(int id) {
        for (OxidationWrapper catType : values()) if (catType.ordinal() == id) return catType;
        return UNAFFECTED;
    }

    public static OxidationWrapper getByName(String name) {
        for (OxidationWrapper catType : values()) if (catType.name().equalsIgnoreCase(name)) return catType;
        return UNAFFECTED;
    }

    public static OxidationWrapper getPrevious(OxidationWrapper current) {
        if (current == UNAFFECTED) return OXIDIZED;
        return values()[(current.ordinal() - 1)];
    }

    public static OxidationWrapper getNext(OxidationWrapper current) {
        if (current == OXIDIZED) return UNAFFECTED;
        return values()[(current.ordinal() + 1)];
    }
}