package simplepets.brainsynder.api.wrappers;

import net.md_5.bungee.api.ChatColor;

public enum ColorWrapper {
    NONE(-1, -1, ChatColor.WHITE, "366a5c98928fa5d4b5d5b8efb490155b4dda3956bcaa9371177814532cfc"),
    WHITE(0, 15, ChatColor.WHITE, "366a5c98928fa5d4b5d5b8efb490155b4dda3956bcaa9371177814532cfc"),
    ORANGE(1, 14, ChatColor.of("#fca903"), "e79add3e5936a382a8f7fdc37fd6fa96653d5104ebcadb0d4f7e9d4a6efc454"),
    MAGENTA(2, 13, ChatColor.of("#ff00ff"), "3ef0c5773df560cc3fc73b54b5f08cd69856415ab569a37d6d44f2f423df20"),
    LIGHT_BLUE(3, 12, ChatColor.of("#94ccf7"), "33f75cc2b7f3b2418242e454187156c51b058e43425489a80a5568542b83c94"),
    YELLOW(4, 11, ChatColor.YELLOW, "c641682f43606c5c9ad26bc7ea8a30ee47547c9dfd3c6cda49e1c1a2816cf0ba"),
    LIME(5, 10, ChatColor.GREEN, "d27ca46f6a9bb89a24fcaf4cc0acf5e8285a66db7521378ed2909ae449697f"),
    PINK(6, 9, ChatColor.of("#fccfcc"), "ca5b93ac4ade53a4ff8cdb82eaaef006fe7291f649c3243ed6fc38fc9b5c68"),
    GRAY(7, 8, ChatColor.DARK_GRAY, "608f323462fb434e928bd6728638c944ee3d812e162b9c6ba070fcac9bf9"),
    LIGHT_GRAY(8, 7, ChatColor.GRAY, "38e2957699bc98a4b5d634ab71867eeb186b934bdb65d2c4b9dcc2b613cf5"),
    CYAN(9, 6, ChatColor.of("#00ffff"), "e0fb1f13ecb7fbb2fa49cd03d357fa7e278502b78730602aaa10655e4d9490e1"),
    PURPLE(10, 5, ChatColor.of("#a917e8"), "a32ae2cb8d2ae615141d2c65892f364fcaddf73fdec99be1aa6874863eeb5c"),
    BLUE(11, 4, ChatColor.BLUE, "f8157b4dc5efc217352894471c116d39a034fc397c24539a9d0eeb2a465ca"),
    BROWN(12, 3, ChatColor.of("#c98a63"), "e54e54e47b962b83fea779176e956612c12390b7d0eaa51a621c76255cddf3ab"),
    GREEN(13, 2, ChatColor.of("#1f8f09"), "78d58a7651fedae4c03efebc226c03fd791eb74a132babb974e8d838ac6882"),
    RED(14, 1, ChatColor.RED, "d2932b66decaeff6ebdc7c5be6b2467aa6f14b746388a06a2e1e1a8463e9e122"),
    BLACK(15, 0, ChatColor.BLACK, "967a2f218a6e6e38f2b545f6c17733f4ef9bbb288e75402949c052189ee");

    private final int woolData;
    private final int dyeData;
    private final ChatColor color;
    private final String texture;

    ColorWrapper(int woolData, int dyeData, ChatColor chatChar, String texture) {
        this.woolData = woolData;
        this.dyeData = dyeData;
        this.color = chatChar;
        this.texture = "http://textures.minecraft.net/texture/"+texture;
    }

    public static ColorWrapper getByName(String name) {
        for (ColorWrapper wrapper : values()) {
            if (wrapper.name().equalsIgnoreCase(name)) return wrapper;
        }

        return NONE;
    }

    public static ColorWrapper getPrevious(ColorWrapper current) {
        return (current == NONE) ? BLACK : values()[current.ordinal() - 1];
    }

    public static ColorWrapper getNext(ColorWrapper current) {
        return (current == BLACK) ? NONE : values()[current.ordinal() + 1];
    }

    public static ColorWrapper getByWoolData(byte data) {
        for (ColorWrapper wrapper : values()) {
            if (wrapper.woolData == data) return wrapper;
        }

        return NONE;
    }

    public static ColorWrapper getByDyeData(byte data) {
        for (ColorWrapper wrapper : values()) {
            if (wrapper.dyeData == data) return wrapper;
        }

        return NONE;
    }

    public int getWoolData() {
        return this.woolData;
    }

    public int getDyeData() {
        return this.dyeData;
    }

    public ChatColor getChatColor() {
        return this.color;
    }

    public String getTexture() {
        return texture;
    }
}