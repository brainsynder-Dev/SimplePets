package simplepets.brainsynder.pet;

import lombok.Getter;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import simple.brainsynder.api.ItemMaker;
import simple.brainsynder.sound.SoundMaker;
import simple.brainsynder.utils.ServerVersion;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.files.PetTranslate;
import simplepets.brainsynder.nms.entities.type.*;
import simplepets.brainsynder.nms.entities.type.main.IEntityControllerPet;
import simplepets.brainsynder.nms.entities.type.main.IEntityPet;
import simplepets.brainsynder.utils.TypeStorage;
import simplepets.brainsynder.wrapper.EntityWrapper;
import simplepets.brainsynder.wrapper.MaterialWrapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static simple.brainsynder.utils.ServerVersion.*;
import static simplepets.brainsynder.wrapper.MaterialWrapper.*;

public enum PetType {
    BLAZE(SoundMaker.ENTITY_BLAZE_AMBIENT, v1_8_R3, BLAZE_ROD, (byte) 0, "Pet_Blaze", EntityWrapper.BLAZE, IEntityBlazePet.class, PetData.BLAZE),
    BAT(SoundMaker.ENTITY_BAT_AMBIENT, v1_8_R3, FEATHER, (byte) 0, "Pet_Bat", EntityWrapper.BAT, IEntityBatPet.class, PetData.BAT),
    CHICKEN(SoundMaker.ENTITY_CHICKEN_AMBIENT, v1_8_R3, COOKED_CHICKEN, (byte) 0, "Pet_Chicken", EntityWrapper.CHICKEN, IEntityChickenPet.class, PetData.AGE),
    COW(SoundMaker.ENTITY_COW_AMBIENT, v1_8_R3, COOKED_BEEF, (byte) 0, "Pet_Cow", EntityWrapper.COW, IEntityCowPet.class, PetData.AGE),
    CREEPER(SoundMaker.ENTITY_CREEPER_HURT, v1_8_R3, SULPHUR, (byte) 0, "Pet_Creeper", EntityWrapper.CREEPER, IEntityCreeperPet.class, PetData.POWERED),
    ENDERMAN(SoundMaker.ENTITY_ENDERMEN_AMBIENT, v1_8_R3, ENDER_PEARL, (byte) 0, "Pet_Enderman", EntityWrapper.ENDERMAN, IEntityEndermanPet.class, PetData.SCREAM),
    GIANT(SoundMaker.ENTITY_ZOMBIE_AMBIENT, v1_8_R3, EMERALD_BLOCK, (byte) 0, "Pet_Giant", EntityWrapper.GIANT, IEntityGiantPet.class),
    HORSE(SoundMaker.ENTITY_HORSE_AMBIENT, v1_8_R3, SADDLE, (byte) 0, "Pet_Horse", EntityWrapper.HORSE, IEntityHorsePet.class, PetData.HORSE),
    IRONGOLEM(SoundMaker.ENTITY_IRONGOLEM_STEP, v1_8_R3, IRON_BLOCK, (byte) 0, "Pet_IronGolem", EntityWrapper.IRON_GOLEM, IEntityIronGolemPet.class),
    MOOSHROOM(SoundMaker.ENTITY_MOOSHROOM_SHEAR, v1_8_R3, RED_MUSHROOM, (byte) 0, "Pet_Mooshroom", EntityWrapper.MUSHROOM_COW, IEntityMooshroomPet.class, PetData.AGE),
    OCELOT(SoundMaker.ENTITY_CAT_AMBIENT, v1_8_R3, COOKED_FISH, (byte) 0, "Pet_Ocelot", EntityWrapper.OCELOT, IEntityOcelotPet.class, PetData.OCELOT),
    PIG(SoundMaker.ENTITY_PIG_AMBIENT, v1_8_R3, PORK, (byte) 0, "Pet_Pig", EntityWrapper.PIG, IEntityPigPet.class, PetData.PIG),
    PIGMAN(SoundMaker.ENTITY_ZOMBIE_PIG_AMBIENT, v1_8_R3, GOLD_NUGGET, (byte) 0, "Pet_Pigman", EntityWrapper.PIG_ZOMBIE, IEntityPigmanPet.class, PetData.AGE),
    RABBIT(SoundMaker.ENTITY_RABBIT_AMBIENT, v1_8_R3, RABBIT_FOOT, (byte) 0, "Pet_Rabbit", EntityWrapper.RABBIT, IEntityRabbitPet.class, PetData.RABBIT),
    SHEEP(SoundMaker.ENTITY_SHEEP_AMBIENT, v1_8_R3, WOOL, (byte) 0, "Pet_Sheep", EntityWrapper.SHEEP, IEntitySheepPet.class, PetData.SHEEP),
    SILVERFISH(SoundMaker.ENTITY_SILVERFISH_AMBIENT, v1_8_R3, MONSTER_EGG, (byte) 0, "Pet_Silverfish", EntityWrapper.SILVERFISH, IEntitySilverfishPet.class),
    SKELETON(SoundMaker.ENTITY_SKELETON_AMBIENT, v1_8_R3, SKULL_ITEM, (byte) 0, "Pet_Skeleton", EntityWrapper.SKELETON, IEntitySkeletonPet.class),
    SNOWMAN(SoundMaker.ENTITY_SNOWMAN_AMBIENT, v1_8_R3, SNOW_BALL, (byte) 0, "Pet_SnowMan", EntityWrapper.SNOWMAN, IEntitySnowmanPet.class, PetData.PUMPKIN),
    SPIDER(SoundMaker.ENTITY_SPIDER_AMBIENT, v1_8_R3, SPIDER_EYE, (byte) 0, "Pet_Spider", EntityWrapper.SPIDER, IEntitySpiderPet.class),
    VILLAGER(SoundMaker.ENTITY_VILLAGER_AMBIENT, v1_8_R3, EMERALD, (byte) 0, "Pet_Villager", EntityWrapper.VILLAGER, IEntityVillagerPet.class, PetData.VILLAGER),
    WOLF(SoundMaker.ENTITY_WOLF_AMBIENT, v1_8_R3, BONE, (byte) 0, "Pet_Wolf", EntityWrapper.WOLF, IEntityWolfPet.class, PetData.WOLF),
    ZOMBIE(SoundMaker.ENTITY_ZOMBIE_AMBIENT, v1_8_R3, ROTTEN_FLESH, (byte) 0, "Pet_Zombie", EntityWrapper.ZOMBIE, IEntityZombiePet.class, PetData.AGE),
    SQUID(SoundMaker.ENTITY_SQUID_AMBIENT, v1_8_R3, INK_SACK, (byte) 0, "Pet_Squid", EntityWrapper.SQUID, IEntitySquidPet.class),
    GHAST(SoundMaker.ENTITY_GHAST_AMBIENT, v1_8_R3, GHAST_TEAR, (byte) 0, "Pet_Ghast", EntityWrapper.GHAST, IEntityGhastPet.class),
    ENDERMITE(SoundMaker.ENTITY_ENDERMITE_AMBIENT, v1_8_R3, EYE_OF_ENDER, (byte) 0, "Pet_Endermite", EntityWrapper.ENDERMITE, IEntityEndermitePet.class),
    WITHER(SoundMaker.ENTITY_WITHER_AMBIENT, v1_8_R3, NETHER_STAR, (byte) 0, "Pet_Wither", EntityWrapper.WITHER, IEntityWitherPet.class, PetData.WITHER),
    WITCH(SoundMaker.ENTITY_WITCH_AMBIENT, v1_8_R3, POTION, (byte) 0, "Pet_Witch", EntityWrapper.WITCH, IEntityWitchPet.class),
    POLAR_BEAR(SoundMaker.ENTITY_POLAR_BEAR_AMBIENT, v1_10_R1, RAW_FISH, (byte) 0, "Pet_PolarBear", EntityWrapper.POLAR_BEAR, IEntityPolarBearPet.class, PetData.POLAR_BEAR),
    GUARDIAN(SoundMaker.ENTITY_GUARDIAN_AMBIENT, v1_10_R1, PRISMARINE_CRYSTALS, (byte) 0, "Pet_Guardian", EntityWrapper.GUARDIAN, IEntityGuardianPet.class),
    SLIME(SoundMaker.ENTITY_SLIME_SQUISH, v1_10_R1, SLIME_BALL, (byte) 0, "Pet_Slime", EntityWrapper.SLIME, IEntitySlimePet.class, PetData.SLIME),
    STRAY(SoundMaker.ENTITY_SKELETON_AMBIENT, v1_11_R1, CHAINMAIL_CHESTPLATE, (byte) 0, "Pet_Stray", EntityWrapper.STRAY, IEntityStrayPet.class),
    VEX(SoundMaker.ENTITY_VEX_AMBIENT, v1_11_R1, CLAY_BALL, (byte) 0, "Pet_Vex", EntityWrapper.VEX, IEntityVexPet.class, PetData.POWERED),
    LLAMA(SoundMaker.ENTITY_LLAMA_AMBIENT, v1_11_R1, CARPET, (byte) 0, "Pet_Llama", EntityWrapper.LLAMA, IEntityLlamaPet.class, PetData.LLAMA),
    EVOKER(SoundMaker.ENTITY_EVOCATION_ILLAGER_AMBIENT, v1_11_R1, FIREWORK_CHARGE, (byte) 0, "Pet_Evoker", EntityWrapper.EVOKER, IEntityEvokerPet.class, PetData.WIZARD),
    VINDICATOR(SoundMaker.ENTITY_VINDICATION_ILLAGER_AMBIENT, v1_11_R1, BEDROCK, (byte) 0, "Pet_Vindicator", EntityWrapper.VINDICATOR, IEntityVindicatorPet.class, PetData.JOHNNY),
    HUSK(SoundMaker.ENTITY_ZOMBIE_AMBIENT, v1_11_R1, SKULL_ITEM, (byte) 2, "Pet_Husk", EntityWrapper.HUSK, IEntityHuskPet.class, PetData.AGE),
    MULE(SoundMaker.ENTITY_MULE_AMBIENT, v1_11_R1, SADDLE, (byte) 0, "Pet_Mule", EntityWrapper.MULE, IEntityMulePet.class, PetData.MULE),
    ZOMBIE_HORSE(SoundMaker.ENTITY_ZOMBIE_HORSE_AMBIENT, v1_11_R1, SADDLE, (byte) 0, "Pet_ZombieHorse", EntityWrapper.ZOMBIE_HORSE, IEntityZombieHorsePet.class, PetData.HORSE_OTHER),
    SKELETON_HORSE(SoundMaker.ENTITY_SKELETON_HORSE_AMBIENT, v1_11_R1, SADDLE, (byte) 0, "Pet_SkeletonHorse", EntityWrapper.SKELETON_HORSE, IEntitySkeletonHorsePet.class, PetData.HORSE_OTHER),
    MAGMA_CUBE(SoundMaker.ENTITY_MAGMACUBE_SQUISH, v1_11_R1, MAGMA, (byte) 0, "Pet_MagmaCube", EntityWrapper.MAGMA_CUBE, IEntityMagmaCubePet.class, PetData.SLIME),
    WITHER_SKELETON(SoundMaker.ENTITY_SKELETON_AMBIENT, v1_11_R1, SKULL_ITEM, (byte) 1, "Pet_WitherSkeleton", EntityWrapper.WITHER_SKELETON, IEntityWitherSkeletonPet.class),
    ELDER_GUARDIAN(SoundMaker.ENTITY_ELDER_GUARDIAN_AMBIENT, v1_11_R1, SEA_LANTERN, (byte) 0, "Pet_ElderGuardian", EntityWrapper.ELDER_GUARDIAN, IEntityElderGuardianPet.class),
    CAVE_SPIDER(SoundMaker.ENTITY_SPIDER_AMBIENT, v1_11_R1, FERMENTED_SPIDER_EYE, (byte) 0, "Pet_CaveSpider", EntityWrapper.CAVE_SPIDER, IEntityCaveSpiderPet.class),
    ARMOR_STAND(SoundMaker.ENTITY_ARMORSTAND_FALL, v1_11_R1, MaterialWrapper.ARMOR_STAND, (byte) 0, "Pet_ArmorStand", EntityWrapper.ARMOR_STAND, IEntityControllerPet.class, PetData.ARMOR_STAND),
    SHULKER(SoundMaker.ENTITY_SHULKER_AMBIENT, v1_11_R1, PURPLE_SHULKER_BOX, (byte) 0, "Pet_Shulker", EntityWrapper.SHULKER, IEntityControllerPet.class, PetData.SHULKER),
    PARROT(SoundMaker.ENTITY_PARROT_STEP, v1_12_R1, FEATHER, (byte) 0, "Pet_Parrot", EntityWrapper.PARROT, IEntityParrotPet.class, PetData.PARROT),
    ILLUSIONER(SoundMaker.ENTITY_ILLUSION_ILLAGER_AMBIENT, v1_12_R1, ENDER_PORTAL_FRAME, (byte) 0, "Pet_Illusioner", EntityWrapper.ILLUSIONER, IEntityIllusionerPet.class, PetData.WIZARD),
    ZOMBIE_VILLAGER(SoundMaker.ENTITY_ZOMBIE_VILLAGER_AMBIENT, v1_12_R1, ROTTEN_FLESH, (byte) 0, "Pet_ZombieVillager", EntityWrapper.ZOMBIE_VILLAGER, IEntityZombieVillagerPet.class, PetData.ZOMBIE_VILLAGER);

    public static final List<PetType> flyable = new ArrayList<>();
    private static Map<PetType, TypeStorage> storage = new HashMap<>();

    static {
        if (flyable.isEmpty()) {
            flyable.add(BAT);
            flyable.add(VEX);
            flyable.add(BLAZE);
            flyable.add(GHAST);
            flyable.add(PARROT);
        }
    }

    private MaterialWrapper mat;
    private byte data;
    private String perm;
    private String cfgName;
    private EntityWrapper type;
    private ServerVersion maxVer;
    private Class<? extends IEntityPet> mainEnt;
    private PetData pData = null;
    @Getter
    private SoundMaker ambientSound = null;

    PetType(SoundMaker defSound, ServerVersion maxVer
            , MaterialWrapper mat
            , byte data
            , String clazz
            , EntityWrapper type
            , Class<? extends IEntityPet> mainEnt, PetData pdata) {
        this(defSound, maxVer, mat, data, clazz, type, mainEnt);
        this.pData = pdata;
    }


    PetType(SoundMaker defSound, ServerVersion maxVer
            , MaterialWrapper mat
            , byte data
            , String clazz
            , EntityWrapper type
            , Class<? extends IEntityPet> mainEnt) {
        this.maxVer = maxVer;
        this.data = data;
        this.perm = clazz.replace("Pet_", "").toLowerCase();
        this.cfgName = clazz.replace("Pet_", "");
        this.mainEnt = mainEnt;
        this.mat = mat;
        if (isSupported()) {
            this.type = type;
            if (!PetTranslate.isSet(this, "AmbientSound")) {
                ambientSound = defSound;
            } else {
                if (PetTranslate.getString(this, "AmbientSound").equals("off")) {
                    ambientSound = null;
                } else {
                    ambientSound = SoundMaker.fromString(PetTranslate.getString(this, "AmbientSound"));
                }
            }
        } else {
            this.type = EntityWrapper.UNKNOWN;
        }
    }

    public static PetType getByName(String name) {
        for (PetType type : values()) {
            if (type.cfgName.equalsIgnoreCase(name)) {
                return type;
            }
        }
        return null;
    }

    public static PetType getByMaterial(Material mat) {
        for (PetType type : values()) {
            if (type.mat == MaterialWrapper.fromMaterial(mat)) {
                return type;
            }
        }
        return null;
    }

    public static PetType getByItem(ItemStack item) {
        for (PetType type : values()) {
            if (type.getItem().isSimilar(item)) {
                return type;
            }
        }
        return null;
    }

    public static void initiate() {
        PetCore.get().debug("Loading PetType Items/Defaults...");
        for (PetType type : values()) {
            if (!storage.containsKey(type)) {
                TypeStorage typeStorage = new TypeStorage();
                typeStorage.setNoColorName(PetTranslate.getString(type.cfgName + ".NoColorName"));
                typeStorage.setDisplayName(PetTranslate.getString(type.cfgName + ".DisplayName"));
                typeStorage.setDescription(PetTranslate.getList(type.cfgName + ".Description"));
                typeStorage.setDefaultName(PetTranslate.getString(type.cfgName + ".DefaultName"));
                try {
                    Object mat = PetTranslate.get(type.cfgName + ".ItemData.MaterialWrapper");
                    MaterialWrapper material = MaterialWrapper.fromIDName(mat);
                    if (material == MaterialWrapper.NOT_SUPPORTED) {
                        PetCore.get().debug("Not Supported MaterialWrapper, Resetting the " + type.cfgName + " MaterialWrapper to the Default MaterialWrapper...");
                        PetTranslate.setOver(type.cfgName + ".ItemData.MaterialWrapper", type.mat.toString());
                        material = type.mat;
                    }
                    if (material == MaterialWrapper.AIR) {
                        PetCore.get().debug("Not Supported MaterialWrapper, Resetting the " + type.cfgName + " MaterialWrapper to the Default MaterialWrapper...");
                        PetTranslate.setOver(type.cfgName + ".ItemData.MaterialWrapper", type.mat.toString());
                        material = type.mat;
                    }
                    int data = PetTranslate.getInteger(type.cfgName + ".ItemData.Durability");
                    ItemMaker maker = new ItemMaker(material.toMaterial(), (byte) data);
                    maker.setName(typeStorage.getDisplayName().replace("%name%", typeStorage.getNoColorName()));
                    if (!PetTranslate.getBoolean(type.cfgName + ".DisableItemLore")) {
                        if ((typeStorage.getDescription().size() != 0)
                                || (!typeStorage.getDescription().isEmpty())
                                || (typeStorage.getDescription() != null)) {
                            for (String s : typeStorage.getDescription())
                                maker.addLoreLine(s);
                        }
                    }
                    maker.setFlags(
                            ItemFlag.HIDE_ATTRIBUTES,
                            ItemFlag.HIDE_DESTROYS,
                            ItemFlag.HIDE_ENCHANTS,
                            ItemFlag.HIDE_PLACED_ON,
                            ItemFlag.HIDE_POTION_EFFECTS,
                            ItemFlag.HIDE_UNBREAKABLE
                    );
                    typeStorage.setItem(maker.create());
                } catch (Exception ignored) {
                    System.out.println("[SimplePets ERROR] Unable to load the item for the " + type.toString() + " Pet, attempting to get the default item...");
                    typeStorage.setItem(type.rawItem());
                }
                storage.put(type, typeStorage);
            }
        }
    }

    public PetData getPetData() {
        return pData;
    }

    public Class<? extends IEntityPet> getEntityClass() {
        return mainEnt;
    }

    public boolean isSupported() {
        return (maxVer.getIntVersion() <= ServerVersion.getVersion().getIntVersion());
    }

    public EntityWrapper getType() {
        return type;
    }

    public String getConfigName() {
        return this.cfgName;
    }

    public boolean hasPermission(Player player) {
        if (!PetCore.get().getConfiguration().getBoolean("Needs-Permission")) {
            return true;
        }
        return player.hasPermission("Pet.types.*") || player.hasPermission(getPermission());
    }

    public boolean isEnabled() {
        return PetTranslate.getBoolean(this.cfgName + ".Enabled");
    }

    public boolean canHat(Player player) {
        if (PetTranslate.getBoolean(this.cfgName + ".Hat")) {
            if (PetCore.hasPerm(player, "Pet.PetToHat")) return true;
            if (PetCore.hasPerm(player, getPermission() + ".hat")) return true;
        }
        return false;
    }

    public double getSpeed() {
        return PetTranslate.getDouble(this.cfgName + ".WalkSpeed");
    }

    public boolean canMount(Player player) {
        if (this == ARMOR_STAND)
            return false;
        if (this == SHULKER)
            return false;
        if (PetTranslate.getBoolean(this.cfgName + ".Mount")) {
            if (PetCore.hasPerm(player, "Pet.PetToMount")) return true;
            if (PetCore.hasPerm(player, getPermission() + ".mount")) return true;
        }
        return false;
    }

    public boolean canFly(Player player) {
        if (PetTranslate.getBoolean(this.cfgName + ".Fly")) {
            if (PetCore.hasPerm(player, "Pet.FlyAll")) return true;
            if (PetCore.hasPerm(player, getPermission() + ".fly")) return true;
        }
        return false;
    }

    public boolean canFlyDefault() {
        return flyable.contains(this);
    }

    public float getRideSpeed() {
        return (float) PetTranslate.getDouble(this.cfgName + ".RideSpeed");
    }

    public String getDisplayName() {
        if (storage.containsKey(this)) {
            TypeStorage store = storage.get(this);
            return store.getDisplayName().replace("%name%", getNoColorName());
        }
        String name = WordUtils.capitalizeFully(this.toString().toLowerCase().replace("_", " "));
        return name;
    }

    public String getDefaultName() {
        if (storage.containsKey(this)) {
            TypeStorage store = storage.get(this);
            return store.getDefaultName();
        }
        String name = WordUtils.capitalizeFully(this.toString().toLowerCase().replace("_", " "));
        return name;
    }

    public String getNoColorName() {
        if (storage.containsKey(this)) {
            TypeStorage store = storage.get(this);
            return store.getNoColorName();
        }
        String name = WordUtils.capitalizeFully(this.toString().toLowerCase().replace("_", " "));
        return name;
    }

    public List<String> getDescription() {
        if (storage.containsKey(this)) {
            TypeStorage store = storage.get(this);
            return store.getDescription();
        }
        return new ArrayList<>();
    }

    public String getPermission() {
        return "Pet." + this.perm;
    }

    public byte getData() {
        return data;
    }

    public Material getMaterial() {
        return mat.toMaterial();
    }

    public ItemStack getItem() {
        if (storage.containsKey(this)) {
            TypeStorage store = storage.get(this);
            return store.getItem();
        }
        return rawItem();
    }

    public Pet setPet(Player player) {
        return new Pet(player.getUniqueId(), this);
    }

    private ItemStack rawItem() {
        Object mat = PetTranslate.get(this.cfgName + ".ItemData.MaterialWrapper");
        MaterialWrapper material = MaterialWrapper.fromIDName(mat);
        if (material == MaterialWrapper.NOT_SUPPORTED) {
            PetCore.get().debug("Not Supported MaterialWrapper, Resetting the " + this.cfgName + " MaterialWrapper to the Default MaterialWrapper...");
            PetTranslate.setOver(this.cfgName + ".ItemData.MaterialWrapper", this.mat.toString());
            material = this.mat;
        }
        if (material == MaterialWrapper.AIR) {
            PetCore.get().debug("Not Supported MaterialWrapper, Resetting the " + this.cfgName + " MaterialWrapper to the Default MaterialWrapper...");
            PetTranslate.setOver(this.cfgName + ".ItemData.MaterialWrapper", this.mat.toString());
            material = this.mat;
        }
        if (material.getSupportedVersion().getIntVersion() > ServerVersion.getVersion().getIntVersion()) {
            material = BARRIER;
        }
        int data = PetTranslate.getInteger(this.cfgName + ".ItemData.Durability");
        ItemMaker maker = new ItemMaker(material.toMaterial(), (byte) data);
        maker.setName(WordUtils.capitalizeFully(this.toString().toLowerCase().replace("_", " ")));
        if (!PetTranslate.getBoolean(this.cfgName + ".DisableItemLore")) {
            if ((getDescription().size() != 0) || (!getDescription().isEmpty()) || (getDescription() != null)) {
                for (String s : getDescription())
                    maker.addLoreLine(s);
            }
        }
        maker.setFlags(
                ItemFlag.HIDE_ATTRIBUTES,
                ItemFlag.HIDE_DESTROYS,
                ItemFlag.HIDE_ENCHANTS,
                ItemFlag.HIDE_PLACED_ON,
                ItemFlag.HIDE_POTION_EFFECTS,
                ItemFlag.HIDE_UNBREAKABLE
        );
        return maker.create();
    }
}
