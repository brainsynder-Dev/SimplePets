package simplepets.brainsynder.wrapper;

import org.bukkit.entity.EntityType;

public enum EntityWrapper {
    UNKNOWN("null", -1),
    ELDER_GUARDIAN("elder_guardian", 4),
    WITHER_SKELETON("wither_skeleton", 5),
    STRAY("stray", 6),
    HUSK("husk", 23),
    DRAGON_FIREBALL("dragon_fireball", 26),
    ZOMBIE_VILLAGER("zombie_villager", 27),
    SKELETON_HORSE("skeleton_horse", 28),
    ZOMBIE_HORSE("zombie_horse", 29),
    ARMOR_STAND("armor_stand", 30),
    DONKEY("donkey", 31),
    MULE("mule", 32),
    EVOKER("evocation_illager", 34),
    VEX("vex", 35),
    VINDICATOR("vindication_illager", 36),
    ILLUSIONER("illusion_illager", 37),
    CREEPER("creeper", 50),
    SKELETON("skeleton", 51),
    SPIDER("spider", 52),
    GIANT("giant", 53),
    ZOMBIE("zombie", 54),
    SLIME("slime", 55),
    GHAST("ghast", 56),
    PIG_ZOMBIE("zombie_pigman", 57),
    ENDERMAN("enderman", 58),
    CAVE_SPIDER("cave_spider", 59),
    SILVERFISH("silverfish", 60),
    BLAZE("blaze", 61),
    MAGMA_CUBE("magma_cube", 62),
    ENDER_DRAGON("ender_dragon", 63),
    WITHER("wither", 64),
    BAT("bat", 65),
    WITCH("witch", 66),
    ENDERMITE("endermite", 67),
    GUARDIAN("guardian", 68),
    SHULKER("shulker", 69),
    PIG("pig", 90),
    SHEEP("sheep", 91),
    COW("cow", 92),
    CHICKEN("chicken", 93),
    SQUID("squid", 94),
    WOLF("wolf", 95),
    MUSHROOM_COW("mooshroom", 96),
    SNOWMAN("snowman", 97),
    OCELOT("ocelot", 98),
    IRON_GOLEM("villager_golem", 99),
    HORSE("horse", 100),
    RABBIT("rabbit", 101),
    POLAR_BEAR("polar_bear", 102),
    LLAMA("llama", 103),
    PARROT("parrot", 105),
    VILLAGER("villager", 120);

    private int typeId;
    private String name;

    EntityWrapper(String name, int typeId) {
        this.name = name;
        this.typeId = typeId;
    }

    public static EntityWrapper getByID(int id) {
        for (EntityWrapper entityWrapper : values()) {
            if (entityWrapper.typeId == id) {
                return entityWrapper;
            }
        }
        return null;
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
        } catch (Exception e) {
        }
        return EntityType.UNKNOWN;
    }

    public String getName() {
        return this.name;
    }

    public int getTypeId() {
        return this.typeId;
    }
}
