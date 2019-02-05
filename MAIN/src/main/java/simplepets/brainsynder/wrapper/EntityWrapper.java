package simplepets.brainsynder.wrapper;

import org.bukkit.entity.EntityType;

// This class is not used for registering mobs only for internal use
public enum EntityWrapper {
    UNKNOWN("null"),
    ELDER_GUARDIAN,
    WITHER_SKELETON,
    STRAY,
    HUSK,
    ZOMBIE_VILLAGER,
    SKELETON_HORSE,
    ZOMBIE_HORSE,
    ARMOR_STAND,
    DONKEY,
    MULE,
    EVOKER("evocation_illager"),
    VEX,
    VINDICATOR("vindication_illager"),
    ILLUSIONER("illusion_illager"),
    CREEPER,
    SKELETON,
    SPIDER,
    GIANT,
    ZOMBIE,
    SLIME,
    GHAST,
    PIG_ZOMBIE("zombie_pigman"),
    ENDERMAN,
    CAVE_SPIDER,
    SILVERFISH,
    BLAZE,
    MAGMA_CUBE,
    ENDER_DRAGON,
    WITHER,
    BAT,
    WITCH,
    ENDERMITE,
    GUARDIAN,
    SHULKER,
    PIG,
    SHEEP,
    COW,
    CHICKEN,
    SQUID,
    WOLF,
    MUSHROOM_COW("mooshroom"),
    SNOWMAN,
    OCELOT,
    IRON_GOLEM("villager_golem"),
    HORSE,
    RABBIT,
    POLAR_BEAR,
    LLAMA,
    PARROT,
    VILLAGER,

    // 1.13 Mobs
    COD,
    SALMON,
    TURTLE,
    PHANTOM,
    DROWNED,
    DOLPHIN,
    PUFFER_FISH,
    TROPICAL_FISH;

    private String name;

    EntityWrapper(String name) {
        this.name = name;
    }
    EntityWrapper() {
        this.name = name().toLowerCase();
    }

    public static EntityWrapper getByName(String name) {
        for (EntityWrapper entityWrapper : values()) {
            if (entityWrapper.name.equalsIgnoreCase(name)) {
                return entityWrapper;
            }
        }
        return null;
    }

    public EntityType toEntityType() {
        try {
            return EntityType.valueOf(name());
        } catch (Exception ignored) {
        }
        return EntityType.UNKNOWN;
    }

    public String getName() {
        return this.name;
    }
}
