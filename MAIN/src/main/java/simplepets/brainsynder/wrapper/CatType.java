package simplepets.brainsynder.wrapper;

import lib.brainsynder.item.ItemBuilder;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Material;

public enum CatType {
    TABBY ("16df72c34e7fdad4bea41d96678b72f29f606e2ca75e3590a278932714be98"),
    BLACK ("3a12188258601bcb7f76e3e2489555a26c0d76e6efec2fd966ca372b6dde00"),
    RED ("04098414ab31179e9e18bc3e7e8c9666c26ac39d5eb53a571cef3d5cfacc42"),
    SIAMESE ("d5b3f8ca4b3a555ccb3d194449808b4c9d783327197800d4d65974cc685af2ea"),
    SHORTHAIR ("5389e0d5d3e81f84b570e2978244b3a73e5a22bcdb6874b44ef5d0f66ca24eec"),
    CALICO ("340097271bb680fe981e859e8ba93fea28b813b1042bd277ea3329bec493eef3"),
    PERSIAN ("ff40c746260ef91c96b27159795e87191ae7ce3d5f767bf8c74faad9689af25d"),
    RAGDOLL ("dc7a45d25889e3fdf7797cb258e26d4e94f5bc13eef00795dafef2e83e0ab511"),
    WHITE ("1d1ff4e532bf92ea808e8075ce136c44854a03e74d0bbce5b93b48974aeda"),
    JELLIE ("a0db41376ca57df10fcb1539e86654eecfd36d3fe75e8176885e93185df280a5"),
    ALL_BLACK ("22c1e81ff03e82a3e71e0cd5fbec607e11361089aa47f290d46c8a2c07460d92");

    private final String texture;
    CatType (String texture) {
        this.texture = "http://textures.minecraft.net/texture/"+texture;
    }

    public ItemBuilder getIcon () {
        ItemBuilder builder = new ItemBuilder(Material.PLAYER_HEAD);
        builder.setTexture(texture);
        builder.withName("&6Type: &e"+ WordUtils.capitalizeFully(name().toLowerCase().replace("_", " ")));
        return builder;
    }
    public static CatType getByID(int id) {
        for (CatType v : values()) if (v.ordinal() == id) return v;
        return TABBY;
    }
    public static CatType getByName(String name) {
        for (CatType wrapper : values()) if (wrapper.name().equalsIgnoreCase(name)) return wrapper;
        return TABBY;
    }

    public static CatType getPrevious(CatType current) {
        if (current == TABBY) return ALL_BLACK;
        return values()[(current.ordinal() - 1)];
    }
    public static CatType getNext(CatType current) {
        if (current == ALL_BLACK) return TABBY;
        return values()[(current.ordinal() + 1)];
    }
}
