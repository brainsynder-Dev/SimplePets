package simplepets.brainsynder.nms.registry.v1_13_R1;

import net.minecraft.server.v1_13_R1.EntityMushroomCow;
import net.minecraft.server.v1_13_R1.EntityTypes;
import simplepets.brainsynder.nms.entities.v1_13_R1.impossamobs.EntityArmorStandPet;
import simplepets.brainsynder.nms.entities.v1_13_R1.impossamobs.EntityShulkerPet;
import simplepets.brainsynder.nms.entities.v1_13_R1.list.*;

public class Types {
    public static EntityTypes<EntityArmorStandPet> ARMOR_STAND = CustomTypes.a(1, "pet_stand", CustomTypes.a.a(EntityArmorStandPet.class, EntityArmorStandPet::new));
    public static EntityTypes<EntityBatPet> BAT = CustomTypes.a(3,"pet_bat", CustomTypes.a.a(EntityBatPet.class, EntityBatPet::new));
    public static EntityTypes<EntityBlazePet> BLAZE = CustomTypes.a(4,"pet_blaze", CustomTypes.a.a(EntityBlazePet.class, EntityBlazePet::new));
    public static EntityTypes<EntityCaveSpiderPet> CAVE_SPIDER = CustomTypes.a(6,"pet_cave_spider", CustomTypes.a.a(EntityCaveSpiderPet.class, EntityCaveSpiderPet::new));
    public static EntityTypes<EntityChickenPet> CHICKEN = CustomTypes.a(7,"pet_chicken", CustomTypes.a.a(EntityChickenPet.class, EntityChickenPet::new));
    public static EntityTypes<EntityCodPet> COD = CustomTypes.a(8,"pet_cod", CustomTypes.a.a(EntityCodPet.class, EntityCodPet::new));
    public static EntityTypes<EntityCowPet> COW = CustomTypes.a(9,"pet_cow", CustomTypes.a.a(EntityCowPet.class, EntityCowPet::new));
    public static EntityTypes<EntityCreeperPet> CREEPER = CustomTypes.a(10,"pet_creeper", CustomTypes.a.a(EntityCreeperPet.class, EntityCreeperPet::new));
    public static EntityTypes<EntityDolphinPet> DOLPHIN = CustomTypes.a(12,"pet_dolphin", CustomTypes.a.a(EntityDolphinPet.class, EntityDolphinPet::new));
    // TODO: DONKEY
    public static EntityTypes<EntityDrownedPet> DROWNED = CustomTypes.a(14,"pet_drowned", CustomTypes.a.a(EntityDrownedPet.class, EntityDrownedPet::new));
    public static EntityTypes<EntityElderGuardianPet> ELDER_GUARDIAN = CustomTypes.a(15,"pet_elder_guardian", CustomTypes.a.a(EntityElderGuardianPet.class, EntityElderGuardianPet::new));
    public static EntityTypes<EntityEndermanPet> ENDERMAN = CustomTypes.a(18,"pet_enderman", CustomTypes.a.a(EntityEndermanPet.class, EntityEndermanPet::new));
    public static EntityTypes<EntityEndermitePet> ENDERMITE = CustomTypes.a(19,"pet_endermite", CustomTypes.a.a(EntityEndermitePet.class, EntityEndermitePet::new));
    public static EntityTypes<EntityEvokerPet> EVOKER = CustomTypes.a(21,"pet_evoker", CustomTypes.a.a(EntityEvokerPet.class, EntityEvokerPet::new));
    public static EntityTypes<EntityGhastPet> GHAST = CustomTypes.a(26,"pet_ghast", CustomTypes.a.a(EntityGhastPet.class, EntityGhastPet::new));
    public static EntityTypes<EntityGiantPet> GIANT = CustomTypes.a(27,"pet_giant", CustomTypes.a.a(EntityGiantPet.class, EntityGiantPet::new));
    public static EntityTypes<EntityGuardianPet> GUARDIAN = CustomTypes.a(28,"pet_guardian", CustomTypes.a.a(EntityGuardianPet.class, EntityGuardianPet::new));
    public static EntityTypes<EntityHorsePet> HORSE = CustomTypes.a(29,"pet_horse", CustomTypes.a.a(EntityHorsePet.class, EntityHorsePet::new));
    public static EntityTypes<EntityHuskPet> HUSK = CustomTypes.a(30,"pet_husk", CustomTypes.a.a(EntityHuskPet.class, EntityHuskPet::new));
    public static EntityTypes<EntityIllusionerPet> ILLUSIONER = CustomTypes.a(31,"pet_illusioner", CustomTypes.a.a(EntityIllusionerPet.class, EntityIllusionerPet::new));
    public static EntityTypes<EntityIronGolemPet> IRON_GOLEM = CustomTypes.a(80,"pet_iron_golem", CustomTypes.a.a(EntityIronGolemPet.class, EntityIronGolemPet::new));
    public static EntityTypes<EntityLlamaPet> LLAMA = CustomTypes.a(36,"pet_llama", CustomTypes.a.a(EntityLlamaPet.class, EntityLlamaPet::new));
    public static EntityTypes<EntityMagmaCubePet> MAGMA_CUBE = CustomTypes.a(38,"pet_magma_cube", CustomTypes.a.a(EntityMagmaCubePet.class, EntityMagmaCubePet::new));
    public static EntityTypes<EntityMushroomCow> MOOSHROOM = CustomTypes.a(47,"pet_mooshroom", CustomTypes.a.a(EntityMushroomCow.class, EntityMushroomCow::new));
    public static EntityTypes<EntityMulePet> MULE = CustomTypes.a(46,"pet_mule", CustomTypes.a.a(EntityMulePet.class, EntityMulePet::new));
    public static EntityTypes<EntityOcelotPet> OCELOT = CustomTypes.a(48,"pet_ocelot", CustomTypes.a.a(EntityOcelotPet.class, EntityOcelotPet::new));
    public static EntityTypes<EntityParrotPet> PARROT = CustomTypes.a(50,"pet_parrot", CustomTypes.a.a(EntityParrotPet.class, EntityParrotPet::new));
    public static EntityTypes<EntityPigPet> PIG = CustomTypes.a(51,"pet_pig", CustomTypes.a.a(EntityPigPet.class, EntityPigPet::new));
    // TODO: PUFFERFISH
    public static EntityTypes<EntityPigmanPet> PIGMAN = CustomTypes.a(53,"pet_pigman", CustomTypes.a.a(EntityPigmanPet.class, EntityPigmanPet::new));
    public static EntityTypes<EntityPolarBearPet> POLAR_BEAR = CustomTypes.a(54,"pet_polar_bear", CustomTypes.a.a(EntityPolarBearPet.class, EntityPolarBearPet::new));
    public static EntityTypes<EntityRabbitPet> RABBIT = CustomTypes.a(56,"pet_rabbit", CustomTypes.a.a(EntityRabbitPet.class, EntityRabbitPet::new));
    public static EntityTypes<EntitySalmonPet> SALMON = CustomTypes.a(57,"pet_salmon", CustomTypes.a.a(EntitySalmonPet.class, EntitySalmonPet::new));
    public static EntityTypes<EntitySheepPet> SHEEP = CustomTypes.a(58,"pet_sheep", CustomTypes.a.a(EntitySheepPet.class, EntitySheepPet::new));
    public static EntityTypes<EntityShulkerPet> SHULKER = CustomTypes.a(59,"pet_shulker", CustomTypes.a.a(EntityShulkerPet.class, EntityShulkerPet::new));
    public static EntityTypes<EntitySilverfishPet> SILVERFISH = CustomTypes.a(61,"pet_silverfish", CustomTypes.a.a(EntitySilverfishPet.class, EntitySilverfishPet::new));
    public static EntityTypes<EntitySkeletonPet> SKELETON = CustomTypes.a(62,"pet_skeleton", CustomTypes.a.a(EntitySkeletonPet.class, EntitySkeletonPet::new));
    public static EntityTypes<EntitySkeletonHorsePet> SKELETON_HORSE = CustomTypes.a(63,"pet_skeleton_horse", CustomTypes.a.a(EntitySkeletonHorsePet.class, EntitySkeletonHorsePet::new));
    public static EntityTypes<EntitySlimePet> SLIME = CustomTypes.a(64,"pet_slime", CustomTypes.a.a(EntitySlimePet.class, EntitySlimePet::new));
    public static EntityTypes<EntitySnowmanPet> SNOWMAN = CustomTypes.a(66,"pet_snowman", CustomTypes.a.a(EntitySnowmanPet.class, EntitySnowmanPet::new));
    public static EntityTypes<EntitySpiderPet> SPIDER = CustomTypes.a(69,"pet_spider", CustomTypes.a.a(EntitySpiderPet.class, EntitySpiderPet::new));
    public static EntityTypes<EntitySquidPet> SQUID = CustomTypes.a(70,"pet_squid", CustomTypes.a.a(EntitySquidPet.class, EntitySquidPet::new));
    public static EntityTypes<EntityStrayPet> STRAY = CustomTypes.a(71,"pet_stray", CustomTypes.a.a(EntityStrayPet.class, EntityStrayPet::new));
    // TODO: TROPICAL_FISH
    public static EntityTypes<EntityTurtlePet> TURTLE = CustomTypes.a(73,"pet_turtle", CustomTypes.a.a(EntityTurtlePet.class, EntityTurtlePet::new));
    public static EntityTypes<EntityVexPet> VEX = CustomTypes.a(78,"pet_vex", CustomTypes.a.a(EntityVexPet.class, EntityVexPet::new));
    public static EntityTypes<EntityVillagerPet> VILLAGER = CustomTypes.a(79,"pet_villager", CustomTypes.a.a(EntityVillagerPet.class, EntityVillagerPet::new));
    public static EntityTypes<EntityVindicatorPet> VINDICATOR = CustomTypes.a(81,"pet_vindicator", CustomTypes.a.a(EntityVindicatorPet.class, EntityVindicatorPet::new));
    public static EntityTypes<EntityWitchPet> WITCH = CustomTypes.a(82,"pet_witch", CustomTypes.a.a(EntityWitchPet.class, EntityWitchPet::new));
    public static EntityTypes<EntityWitherPet> WITHER = CustomTypes.a(83,"pet_wither", CustomTypes.a.a(EntityWitherPet.class, EntityWitherPet::new));
    public static EntityTypes<EntityWitherSkeletonPet> WITHER_SKELETON = CustomTypes.a(84,"pet_wither_skeleton", CustomTypes.a.a(EntityWitherSkeletonPet.class, EntityWitherSkeletonPet::new));
    public static EntityTypes<EntityWolfPet> WOLF = CustomTypes.a(86,"pet_wolf", CustomTypes.a.a(EntityWolfPet.class, EntityWolfPet::new));
    public static EntityTypes<EntityZombiePet> ZOMBIE = CustomTypes.a(87,"pet_zombie", CustomTypes.a.a(EntityZombiePet.class, EntityZombiePet::new));
    public static EntityTypes<EntityZombieHorsePet> ZOMBIE_HORSE = CustomTypes.a(88,"pet_zombie_horse", CustomTypes.a.a(EntityZombieHorsePet.class, EntityZombieHorsePet::new));
    public static EntityTypes<EntityZombieVillagerPet> ZOMBIE_VILLAGER = CustomTypes.a(89,"pet_zombie_villager", CustomTypes.a.a(EntityZombieVillagerPet.class, EntityZombieVillagerPet::new));
    public static EntityTypes<EntityPhantomPet> PHANTOM = CustomTypes.a(90,"pet_phantom", CustomTypes.a.a(EntityPhantomPet.class, EntityPhantomPet::new));
}
