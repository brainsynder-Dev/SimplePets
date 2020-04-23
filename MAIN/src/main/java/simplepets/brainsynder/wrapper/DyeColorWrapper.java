package simplepets.brainsynder.wrapper;

import lib.brainsynder.item.ItemBuilder;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Material;

public enum DyeColorWrapper {
    WHITE(0, 15, 'f', "cbd29e73f5aec159486d88eaee909ae0227f32b5f1f8f41342eb030909dd65e"),
    ORANGE(1, 14, '6', "fea590b681589fb9b0e8664ee945b41eb3851faf66aaf48525fba169c34270"),
    MAGENTA(2, 13, '5', "9133fa52dd74d711e53747da963b8adecf92db946be113b56c38b3dc270eeb3"),
    LIGHT_BLUE(3, 12, '9', "2fb3db103285e92588984b871d8f5b19c89f4e295c99260808a60e43f8bd3d"),
    YELLOW(4, 11, 'e', "14c4141c1edf3f7e41236bd658c5bc7b5aa7abf7e2a852b647258818acd70d8"),
    LIME(5, 10, 'a', "50f4ef7d6c5d8da3618275223bdca147c9cfaac77d37e8ae81d757a593676ac2"),
    PINK(6, 9, 'd', "d57dbb6901759a1526b549e19b379f1aac8f38bc8b3fa8567ba3dcf887a72"),
    GRAY(7, 8, '8', "5509da9895bbe78c1dccd26ca65368d21ad47efa96e6b641b5893366453aef"),
    SILVER(8, 7, '7', "6243b12d521618e487bb3a434b5766ea5ee5864e5afe2ad995ae13039c4aa"),
    CYAN(9, 6, '3', "95b9a48467f0212aa68864e6342116f8f79a275454bf215f67f701a6f2c818"),
    PURPLE(10, 5, '5', "d2a0ad35c3bae8714f5da5b9e799c9ffd8663f1abd71343a2fa016d022b4bf"),
    BLUE(11, 4, '1', "a2cd272eeb38bf783a98a46fa1e2e8d462d852fbaaedef0dce2c1f717a2a"),
    BROWN(12, 3, '4', "9cb9916de8a2df9e12cfdb98dc5241d6d83f65532be47b89fe305bbd1d4ef1b"),
    GREEN(13, 2, '2', "dba7e07cf9d5e6aee9c74b95b155d924bfe2403ec3ba73a419c9cd1684ebb6"),
    RED(14, 1, 'c', "97c1f1ead4d531caa4a5b0d69edbce29af789a2550e5ddbd23775be05e2df2c4"),
    BLACK(15, 0, '0', "9ddebbb062f6a385a91ca05f18f5c0acbe33e2d06ee9e7416cef6ee43dfe2fb");

    private final String texture;
    private final byte woolData;
    private final byte dyeData;
    private final char chatChar;

    DyeColorWrapper(int woolData, int dyeData, char chatChar, String texture) {
        this.woolData = (byte) woolData;
        this.dyeData = (byte) dyeData;
        this.chatChar = chatChar;
        this.texture = "http://textures.minecraft.net/texture/"+texture;
    }

    public static DyeColorWrapper getByName (String name) {
        for (DyeColorWrapper wrapper : values()) {
            if (wrapper.name().equalsIgnoreCase(name)) return wrapper;
        }
        return WHITE;
    }

    public static DyeColorWrapper getPrevious(DyeColorWrapper current) {
        int original = current.ordinal();
        if (original == 0) {
            return BLACK;
        }
        return values()[(original - 1)];
    }

    public static DyeColorWrapper getNext(DyeColorWrapper current) {
        if (current.ordinal() == 15) {
            return WHITE;
        }
        return values()[(current.ordinal() + 1)];
    }

    public static DyeColorWrapper getByWoolData(byte data) {
        for (DyeColorWrapper wrapper : values()) {
            if (wrapper.woolData == data)
                return wrapper;
        }
        return null;
    }

    public static DyeColorWrapper getByDyeData(byte data) {
        for (DyeColorWrapper wrapper : values()) {
            if (wrapper.dyeData == data)
                return wrapper;
        }
        return null;
    }

    public ItemBuilder getIcon () {
        ItemBuilder builder = new ItemBuilder(Material.PLAYER_HEAD);
        builder.withName("&6Color: &e"+WordUtils.capitalizeFully(name().toLowerCase().replace("_", "")));
        builder.setTexture(texture);
        return builder;
    }

    public byte getWoolData() {return this.woolData;}

    public byte getDyeData() {return this.dyeData;}

    public char getChatChar() {return this.chatChar;}
}
