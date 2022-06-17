package simplepets.brainsynder.api.wrappers;

import org.bukkit.NamespacedKey;

import java.util.Locale;

public enum FrogVariant {
    TEMPERATE ("23ce6f9998ed2da757d1e6372f04efa20e57dfc17c3a06478657bbdf51c2f2a2"),
    WARM ("f77314fa038ec31357845a93274b4dc884124686728ffe0ded9c35466aca0aab"),
    COLD ("ce62e8a048d040eb0533ba26a866cd9c2d0928c931c50b4482ac3a3261fab6f0");

    private final String texture;
    FrogVariant(String texture) {
        this.texture = "http://textures.minecraft.net/texture/"+texture;
    }

    public String getTexture() {
        return texture;
    }

    public static FrogVariant getByID(int id) {
        for (FrogVariant frogVariant : values()) if (frogVariant.ordinal() == id) return frogVariant;
        return TEMPERATE;
    }
    public static FrogVariant getByName(String name) {
        for (FrogVariant frogVariant : values()) if (frogVariant.name().equalsIgnoreCase(name)) return frogVariant;
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

    public NamespacedKey getKey () {
        return NamespacedKey.minecraft(this.name().toLowerCase(Locale.ROOT));
    }
}