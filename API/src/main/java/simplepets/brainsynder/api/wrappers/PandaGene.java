package simplepets.brainsynder.api.wrappers;

import lib.brainsynder.item.ItemBuilder;
import org.bukkit.Material;

public enum PandaGene {
    NORMAL ("dca096eea506301bea6d4b17ee1605625a6f5082c71f74a639cc940439f47166"),
    LAZY ("7818b681cace1c641919f53edadecb142330d089a826b56219138c33b7a5e0db"),
    WORRIED ("c8e921c57e54dd07337ffba629e72caf3850d836b76562b1bc3bc5949f2d41d"),
    PLAYFUL ("b6463e64ce29764db3cb46806cee606afc24bdf0ce14b6660c270a96c787426"),
    BROWN ("c5d0d45bf992b072cf5c513e06beefe8bdc809c8150f3d14b883796a7b74e406"),
    WEAK ("5c2d25e956337d82791fa0e6617a40086f02d6ebfbfd5a6459889cf206fca787"),
    AGGRESSIVE ("83fe1e782ae96a30336a03ef74681ce3a6905fcc673fa56c046aaee6aa28307d");

    private final String texture;
    PandaGene (String texture) {
        this.texture = "http://textures.minecraft.net/texture/"+texture;
    }

    public ItemBuilder getIcon () {
        ItemBuilder builder = new ItemBuilder(Material.PLAYER_HEAD);
        builder.setTexture(texture);
        return builder;
    }

    public static PandaGene byId(int id) {
        for (PandaGene gene : values()) if (gene.ordinal() == id) return gene;
        return NORMAL;
    }

    public static PandaGene byName(String name) {
        for (PandaGene gene : values()) if (gene.name().equalsIgnoreCase(name)) return gene;
        return NORMAL;
    }

    public static PandaGene getPrevious(PandaGene current) {
        if (current == NORMAL) return AGGRESSIVE;
        return values()[(current.ordinal() - 1)];
    }
    public static PandaGene getNext(PandaGene current) {
        if (current == AGGRESSIVE) return NORMAL;
        return values()[(current.ordinal() + 1)];
    }
}