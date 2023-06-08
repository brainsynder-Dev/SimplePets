package simplepets.brainsynder.api.pet;

import com.google.common.collect.Lists;
import lib.brainsynder.EnumVersion;
import lib.brainsynder.ServerVersion;
import lib.brainsynder.SupportedVersion;
import lib.brainsynder.item.ItemBuilder;
import lib.brainsynder.sounds.SoundMaker;
import lib.brainsynder.utils.Capitalise;
import lib.brainsynder.utils.Colorize;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.ambient.IEntityArmorStandPet;
import simplepets.brainsynder.api.entity.hostile.*;
import simplepets.brainsynder.api.entity.passive.*;
import simplepets.brainsynder.api.pet.annotations.InDevelopment;
import simplepets.brainsynder.api.pet.annotations.LargePet;
import simplepets.brainsynder.api.pet.annotations.PetCustomization;
import simplepets.brainsynder.api.pet.data.*;
import simplepets.brainsynder.api.pet.data.bee.BeeAngryData;
import simplepets.brainsynder.api.pet.data.bee.FlippedData;
import simplepets.brainsynder.api.pet.data.bee.NectarData;
import simplepets.brainsynder.api.pet.data.bee.StingerData;
import simplepets.brainsynder.api.pet.data.color.ColorData;
import simplepets.brainsynder.api.pet.data.color.RainbowData;
import simplepets.brainsynder.api.pet.data.color.ResetColorData;
import simplepets.brainsynder.api.pet.data.fox.FoxCrouchingData;
import simplepets.brainsynder.api.pet.data.fox.FoxInterestData;
import simplepets.brainsynder.api.pet.data.fox.FoxSittingData;
import simplepets.brainsynder.api.pet.data.fox.FoxTypeData;
import simplepets.brainsynder.api.pet.data.frog.CroakingData;
import simplepets.brainsynder.api.pet.data.frog.FrogVariantData;
import simplepets.brainsynder.api.pet.data.frog.TongueData;
import simplepets.brainsynder.api.pet.data.goat.LeftHornData;
import simplepets.brainsynder.api.pet.data.goat.RightHornData;
import simplepets.brainsynder.api.pet.data.horse.ChestData;
import simplepets.brainsynder.api.pet.data.horse.HorseArmorData;
import simplepets.brainsynder.api.pet.data.horse.HorseColorData;
import simplepets.brainsynder.api.pet.data.horse.HorseStyleData;
import simplepets.brainsynder.api.pet.data.panda.PandaSittingData;
import simplepets.brainsynder.api.pet.data.panda.PandaSleepData;
import simplepets.brainsynder.api.pet.data.panda.PandaSneezeData;
import simplepets.brainsynder.api.pet.data.panda.PandaTypeData;
import simplepets.brainsynder.api.pet.data.villager.VillagerBiomeData;
import simplepets.brainsynder.api.pet.data.villager.VillagerLevelData;
import simplepets.brainsynder.api.pet.data.villager.VillagerTypeData;
import simplepets.brainsynder.api.pet.data.warden.WardenAngerData;
import simplepets.brainsynder.api.pet.data.warden.WardenVibrationData;
import simplepets.brainsynder.api.plugin.SimplePets;
import simplepets.brainsynder.debug.DebugLevel;

import java.lang.annotation.Annotation;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public enum PetType {
    UNKNOWN(new ItemBuilder(Material.STONE)),

    @PetCustomization(ambient = SoundMaker.ENTITY_ALLAY_AMBIENT_WITHOUT_ITEM, weight = PetWeight.LIGHT)
    ALLAY(IEntityAllayPet.class, "40e1c7064af7dee68677efaa95f6e6e01430b006dd91638ea2a61849254488ec"),

    @InDevelopment
    @PetCustomization(ambient = SoundMaker.ENTITY_ARMORSTAND_FALL, weight = PetWeight.LIGHT)
    ARMOR_STAND(IEntityArmorStandPet.class, Material.ARMOR_STAND),

    @PetCustomization(ambient = SoundMaker.ENTITY_AXOLOTL_IDLE_WATER, weight = PetWeight.LIGHT)
    AXOLOTL(IEntityAxolotlPet.class, "5c138f401c67fc2e1e387d9c90a9691772ee486e8ddbf2ed375fc8348746f936",
            AgeData.class,
            AxolotlVariantData.class,
            AxolotlPlayDeadData.class),

    @PetCustomization(ambient = SoundMaker.ENTITY_BAT_AMBIENT)
    BAT(IEntityBatPet.class, "9e99deef919db66ac2bd28d6302756ccd57c7f8b12b9dca8f41c3e0a04ac1cc",
            BatHangData.class),

    @PetCustomization(ambient = SoundMaker.ENTITY_BEE_LOOP, weight = PetWeight.LIGHT)
    BEE(IEntityBeePet.class, "fa2cb74c13245d3ce9bacc8b1600af02fd7c91f501feaf97364e1f8b6f04f47f",
            AgeData.class, BeeAngryData.class, NectarData.class, StingerData.class, FlippedData.class),

    @PetCustomization(ambient = SoundMaker.ENTITY_BLAZE_AMBIENT, weight = PetWeight.SLIGHTLY_HEAVY)
    BLAZE(IEntityBlazePet.class, "b78ef2e4cf2c41a2d14bfde9caff10219f5b1bf5b35a49eb51c6467882cb5f0"),

    @InDevelopment // TODO: Remove this when finished with the mob
    @PetCustomization(ambient = SoundMaker.UI_BUTTON_CLICK, weight = PetWeight.SLIGHTLY_HEAVY)
    CAMEL(IEntityCamelPet.class, "92b31239520511ca7b6712ef0ecfb55b6c56b9347240f4cbf9925ce0bf0fa445",
            AgeData.class, SittingData.class),

    @PetCustomization(ambient = SoundMaker.ENTITY_CAT_AMBIENT, weight = PetWeight.LIGHT)
    CAT(IEntityCatPet.class, "6b253fc6b656988453a2d7138fca4d1f2752f47691f0c434e432183771cfe1",
            AgeData.class, TamedData.class, SittingData.class, SleepData.class, CatCollarData.class, CatTypeData.class, CatTiltData.class),

    @PetCustomization(ambient = SoundMaker.ENTITY_SPIDER_AMBIENT, weight = PetWeight.SLIGHTLY_HEAVY)
    CAVE_SPIDER(IEntityCaveSpiderPet.class, "5617f7dd5ed16f3bd186440517cd440a170015b1cc6fcb2e993c05de33f"),

    @PetCustomization(ambient = SoundMaker.ENTITY_CHICKEN_AMBIENT, weight = PetWeight.LIGHT)
    CHICKEN(IEntityChickenPet.class, "1638469a599ceef7207537603248a9ab11ff591fd378bea4735b346a7fae893",
            AgeData.class),

    @PetCustomization(ambient = SoundMaker.ENTITY_COD_AMBIENT)
    COD(IEntityCodPet.class, "7892d7dd6aadf35f86da27fb63da4edda211df96d2829f691462a4fb1cab0"),

    @PetCustomization(ambient = SoundMaker.ENTITY_COW_AMBIENT, weight = PetWeight.SLIGHTLY_HEAVY)
    COW(IEntityCowPet.class, "c5a9cd58d4c67bccc8fb1f5f756a2d381c9ffac2924b7f4cb71aa9fa13fb5c",
            AgeData.class),

    @PetCustomization(ambient = SoundMaker.ENTITY_CREEPER_HURT, weight = PetWeight.SLIGHTLY_HEAVY)
    CREEPER(IEntityCreeperPet.class, Material.CREEPER_HEAD,
            PoweredData.class),

    @PetCustomization(ambient = SoundMaker.ENTITY_DOLPHIN_AMBIENT, weight = PetWeight.SLIGHTLY_HEAVY)
    DOLPHIN(IEntityDolphinPet.class, "8e9688b950d880b55b7aa2cfcd76e5a0fa94aac6d16f78e833f7443ea29fed3"),

    @PetCustomization(ambient = SoundMaker.ENTITY_DONKEY_AMBIENT, weight = PetWeight.HEAVY)
    DONKEY(IEntityDonkeyPet.class, "399bb50d1a214c394917e25bb3f2e20698bf98ca703e4cc08b42462df309d6e6",
            AgeData.class, ChestData.class, EatingData.class, SaddleData.class),

    @PetCustomization(ambient = SoundMaker.ENTITY_DROWNED_AMBIENT, weight = PetWeight.SLIGHTLY_HEAVY)
    DROWNED(IEntityDrownedPet.class, "c3f7ccf61dbc3f9fe9a6333cde0c0e14399eb2eea71d34cf223b3ace22051",
            AgeData.class, ArmsData.class, ShakeData.class),

    @LargePet
    @PetCustomization(ambient = SoundMaker.ENTITY_ELDER_GUARDIAN_AMBIENT, weight = PetWeight.HEAVY)
    ELDER_GUARDIAN(IEntityElderGuardianPet.class, "1c797482a14bfcb877257cb2cff1b6e6a8b8413336ffb4c29a6139278b436b"),

    @PetCustomization(ambient = SoundMaker.ENTITY_ENDERMAN_AMBIENT, weight = PetWeight.SLIGHTLY_HEAVY)
    ENDERMAN(IEntityEndermanPet.class, "96c0b36d53fff69a49c7d6f3932f2b0fe948e032226d5e8045ec58408a36e951",
            EndermanScreamData.class),

    @PetCustomization(ambient = SoundMaker.ENTITY_ENDERMITE_AMBIENT, weight = PetWeight.LIGHT)
    ENDERMITE(IEntityEndermitePet.class, "5bc7b9d36fb92b6bf292be73d32c6c5b0ecc25b44323a541fae1f1e67e393a3e"),

    @PetCustomization(ambient = SoundMaker.ENTITY_EVOKER_AMBIENT, weight = PetWeight.SLIGHTLY_HEAVY)
    EVOKER(IEntityEvokerPet.class, "d954135dc82213978db478778ae1213591b93d228d36dd54f1ea1da48e7cba6",
            SpellData.class),

    @PetCustomization(ambient = SoundMaker.ENTITY_FOX_AMBIENT, weight = PetWeight.SLIGHTLY_HEAVY)
    FOX(IEntityFoxPet.class, "d8954a42e69e0881ae6d24d4281459c144a0d5a968aed35d6d3d73a3c65d26a",
            AgeData.class, SleepData.class, FoxInterestData.class, FoxCrouchingData.class, FoxTypeData.class, FoxSittingData.class),

    @PetCustomization(ambient = SoundMaker.ENTITY_FROG_AMBIENT, weight = PetWeight.LIGHT)
    FROG(IEntityFrogPet.class, "23ce6f9998ed2da757d1e6372f04efa20e57dfc17c3a06478657bbdf51c2f2a2",
            FrogVariantData.class, CroakingData.class, TongueData.class),

    @LargePet
    @PetCustomization(ambient = SoundMaker.ENTITY_GHAST_AMBIENT, weight = PetWeight.HEAVY)
    GHAST(IEntityGhastPet.class, "7a8b714d32d7f6cf8b37e221b758b9c599ff76667c7cd45bbc49c5ef19858646",
            GhastScreamData.class),

    @LargePet
    @PetCustomization(ambient = SoundMaker.ENTITY_ZOMBIE_AMBIENT, weight = PetWeight.YOUR_KILLING_ME)
    GIANT(IEntityGiantPet.class, Material.ZOMBIE_HEAD),

    @PetCustomization(ambient = SoundMaker.ENTITY_GLOW_SQUID_AMBIENT, weight = PetWeight.SLIGHTLY_HEAVY)
    GLOW_SQUID(IEntityGlowSquidPet.class, "3e94a1bb1cb00aaa153a74daf4b0eea20b8974522fe9901eb55aef478ebeff0d",
            GlowingData.class),

    @PetCustomization(ambient = SoundMaker.ENTITY_GOAT_AMBIENT, weight = PetWeight.SLIGHTLY_HEAVY)
    GOAT(IEntityGoatPet.class, "957607099d06b7a8b1327093cd0a488be7c9f50b6121b22151271b59170f3c21",
            AgeData.class, LeftHornData.class, RightHornData.class),

    @PetCustomization(ambient = SoundMaker.ENTITY_GUARDIAN_AMBIENT, weight = PetWeight.HEAVY)
    GUARDIAN(IEntityGuardianPet.class, "a0bf34a71e7715b6ba52d5dd1bae5cb85f773dc9b0d457b4bfc5f9dd3cc7c94"),

    @LargePet
    @PetCustomization(ambient = SoundMaker.ENTITY_HOGLIN_AMBIENT, weight = PetWeight.YOUR_KILLING_ME)
    HOGLIN(IEntityHoglinPet.class, "9bb9bc0f01dbd762a08d9e77c08069ed7c95364aa30ca1072208561b730e8d75",
            AgeData.class, ShakeData.class),

    @PetCustomization(ambient = SoundMaker.ENTITY_HORSE_AMBIENT, weight = PetWeight.HEAVY)
    HORSE(IEntityHorsePet.class, "628d1ab4be1e28b7b461fdea46381ac363a7e5c3591c9e5d2683fbe1ec9fcd3",
            AgeData.class, EatingData.class, HorseArmorData.class, HorseColorData.class, HorseStyleData.class, SaddleData.class),

    @PetCustomization(ambient = SoundMaker.ENTITY_HUSK_AMBIENT, weight = PetWeight.SLIGHTLY_HEAVY)
    HUSK(IEntityHuskPet.class, "d674c63c8db5f4ca628d69a3b1f8a36e29d8fd775e1a6bdb6cabb4be4db121",
            AgeData.class, ArmsData.class, ShakeData.class),

    @PetCustomization(ambient = SoundMaker.ENTITY_ILLUSIONER_AMBIENT, weight = PetWeight.SLIGHTLY_HEAVY)
    ILLUSIONER(IEntityIllusionerPet.class, "512512e7d016a2343a7bff1a4cd15357ab851579f1389bd4e3a24cbeb88b",
            SpellData.class),

    @PetCustomization(ambient = SoundMaker.ENTITY_IRON_GOLEM_STEP, weight = PetWeight.HEAVY)
    IRON_GOLEM(IEntityIronGolemPet.class, "89091d79ea0f59ef7ef94d7bba6e5f17f2f7d4572c44f90f76c4819a714"),

    @PetCustomization(ambient = SoundMaker.ENTITY_LLAMA_AMBIENT, weight = PetWeight.HEAVY)
    LLAMA(IEntityLlamaPet.class, "818cd457fbaf327fa39f10b5b36166fd018264036865164c02d9e5ff53f45",
            AgeData.class, ChestData.class, ResetColorData.class, LlamaSkinData.class, SaddleData.class),

    @PetCustomization(ambient = SoundMaker.ENTITY_MAGMA_CUBE_SQUISH, weight = PetWeight.SLIGHTLY_HEAVY)
    MAGMA_CUBE(IEntityMagmaCubePet.class, "38957d5023c937c4c41aa2412d43410bda23cf79a9f6ab36b76fef2d7c429",
            SizeData.class),

    @PetCustomization(ambient = SoundMaker.ENTITY_COW_AMBIENT, weight = PetWeight.SLIGHTLY_HEAVY)
    MOOSHROOM(IEntityMooshroomPet.class, "2b52841f2fd589e0bc84cbabf9e1c27cb70cac98f8d6b3dd065e55a4dcb70d77",
            AgeData.class, MooshroomColorData.class),

    @PetCustomization(ambient = SoundMaker.ENTITY_MULE_AMBIENT, weight = PetWeight.HEAVY)
    MULE(IEntityMulePet.class, "46dcda265e57e4f51b145aacbf5b59bdc6099ffd3cce0a661b2c0065d80930d8",
            AgeData.class, ChestData.class, EatingData.class, SaddleData.class),

    @PetCustomization(ambient = SoundMaker.ENTITY_OCELOT_AMBIENT, weight = PetWeight.LIGHT)
    OCELOT(IEntityOcelotPet.class, "5657cd5c2989ff97570fec4ddcdc6926a68a3393250c1be1f0b114a1db1",
            AgeData.class),

    @PetCustomization(ambient = SoundMaker.ENTITY_PANDA_AMBIENT, weight = PetWeight.HEAVY)
    PANDA(IEntityPandaPet.class, "dca096eea506301bea6d4b17ee1605625a6f5082c71f74a639cc940439f47166",
            AgeData.class, PandaTypeData.class, PandaSittingData.class, PandaSleepData.class, PandaSneezeData.class),

    @PetCustomization(ambient = SoundMaker.ENTITY_PARROT_AMBIENT, weight = PetWeight.LIGHT)
    PARROT(IEntityParrotPet.class, "a4ba8d66fecb1992e94b8687d6ab4a5320ab7594ac194a2615ed4df818edbc3",
            RainbowData.class, TamedData.class, ParrotColorData.class, SittingData.class),

    @PetCustomization(ambient = SoundMaker.ENTITY_PHANTOM_AMBIENT, weight = PetWeight.SLIGHTLY_HEAVY)
    PHANTOM(IEntityPhantomPet.class, "746830da5f83a3aaed838a99156ad781a789cfcf13e25beef7f54a86e4fa4",
            SizeData.class),

    @PetCustomization(ambient = SoundMaker.ENTITY_PIG_AMBIENT, weight = PetWeight.SLIGHTLY_HEAVY)
    PIG(IEntityPigPet.class, "621668ef7cb79dd9c22ce3d1f3f4cb6e2559893b6df4a469514e667c16aa4",
            AgeData.class, SaddleData.class),

    @PetCustomization(ambient = SoundMaker.ENTITY_PIGLIN_AMBIENT, weight = PetWeight.SLIGHTLY_HEAVY)
    PIGLIN(IEntityPiglinPet.class, "9f18107d275f1cb3a9f973e5928d5879fa40328ff3258054db6dd3e7c0ca6330",
            AgeData.class, ChargingData.class, DancingData.class, ShakeData.class),

    @PetCustomization(ambient = SoundMaker.ENTITY_PIGLIN_BRUTE_AMBIENT, weight = PetWeight.SLIGHTLY_HEAVY)
    PIGLIN_BRUTE(IEntityPiglinBrutePet.class, "3e300e9027349c4907497438bac29e3a4c87a848c50b34c21242727b57f4e1cf",
            ShakeData.class),

    @PetCustomization(ambient = SoundMaker.ENTITY_PILLAGER_AMBIENT, weight = PetWeight.SLIGHTLY_HEAVY)
    PILLAGER(IEntityPillagerPet.class, "4aee6bb37cbfc92b0d86db5ada4790c64ff4468d68b84942fde04405e8ef5333"),

    @PetCustomization(ambient = SoundMaker.ENTITY_POLAR_BEAR_AMBIENT, weight = PetWeight.HEAVY)
    POLARBEAR(IEntityPolarBearPet.class, "c4fe926922fbb406f343b34a10bb98992cee4410137d3f88099427b22de3ab90",
            AgeData.class, StandingData.class),

    @PetCustomization(ambient = SoundMaker.ENTITY_PUFFER_FISH_AMBIENT, weight = PetWeight.LIGHT)
    PUFFERFISH(IEntityPufferFishPet.class, "17152876bc3a96dd2a2299245edb3beef647c8a56ac8853a687c3e7b5d8bb",
            PufferSizeData.class),

    @PetCustomization(ambient = SoundMaker.ENTITY_RABBIT_AMBIENT, weight = PetWeight.LIGHT)
    RABBIT(IEntityRabbitPet.class, "ffecc6b5e6ea5ced74c46e7627be3f0826327fba26386c6cc7863372e9bc",
            AgeData.class, RabbitColorData.class),

    @LargePet
    @PetCustomization(ambient = SoundMaker.ENTITY_RAVAGER_AMBIENT, weight = PetWeight.YOUR_KILLING_ME)
    RAVAGER(IEntityRavagerPet.class, "cd20bf52ec390a0799299184fc678bf84cf732bb1bd78fd1c4b441858f0235a8",
            RavagerChompData.class),

    @PetCustomization(ambient = SoundMaker.ENTITY_SALMON_AMBIENT, weight = PetWeight.LIGHT)
    SALMON(IEntitySalmonPet.class, "8aeb21a25e46806ce8537fbd6668281cf176ceafe95af90e94a5fd84924878"),

    @PetCustomization(ambient = SoundMaker.ENTITY_SHEEP_AMBIENT, weight = PetWeight.SLIGHTLY_HEAVY)
    SHEEP(IEntitySheepPet.class, "f31f9ccc6b3e32ecf13b8a11ac29cd33d18c95fc73db8a66c5d657ccb8be70",
            AgeData.class, ColorData.class, RainbowData.class, ShearData.class),

    @PetCustomization(ambient = SoundMaker.ENTITY_SHULKER_AMBIENT, weight = PetWeight.SLIGHTLY_HEAVY)
    SHULKER(IEntityShulkerPet.class, "1433a4b73273a64c8ab2830b0fff777a61a488c92f60f83bfb3e421f428a44",
            ResetColorData.class, RainbowData.class, ShulkerCloseData.class),

    @PetCustomization(ambient = SoundMaker.ENTITY_SILVERFISH_AMBIENT, weight = PetWeight.LIGHT)
    SILVERFISH(IEntitySilverfishPet.class, "d06310a8952b265c6e6bed4348239ddea8e5482c8c68be6fff981ba8056bf2e"),

    @PetCustomization(ambient = SoundMaker.ENTITY_SKELETON_AMBIENT, weight = PetWeight.SLIGHTLY_HEAVY)
    SKELETON(IEntitySkeletonPet.class, Material.SKELETON_SKULL,
            ArmsData.class),

    @PetCustomization(ambient = SoundMaker.ENTITY_SKELETON_HORSE_AMBIENT, weight = PetWeight.HEAVY)
    SKELETON_HORSE(IEntitySkeletonHorsePet.class, "47effce35132c86ff72bcae77dfbb1d22587e94df3cbc2570ed17cf8973a",
            AgeData.class, EatingData.class, SaddleData.class),

    @PetCustomization(ambient = SoundMaker.ENTITY_SLIME_SQUISH, weight = PetWeight.SLIGHTLY_HEAVY)
    SLIME(IEntitySlimePet.class, "bb13133a8fb4ef00b71ef9bab639a66fbc7d5cffcc190c1df74bf2161dfd3ec7", SizeData.class),

    @PetCustomization(ambient = SoundMaker.ENTITY_SNOWMAN_AMBIENT, weight = PetWeight.SLIGHTLY_HEAVY)
    SNOWMAN(IEntitySnowmanPet.class, "9aed9fe4ed0893e325f4fbd32b093c1cc562cba27ff73359d356f1c288e441f9",
            PumpkinData.class),

    @PetCustomization(ambient = SoundMaker.ENTITY_SPIDER_AMBIENT, weight = PetWeight.SLIGHTLY_HEAVY)
    SPIDER(IEntitySpiderPet.class, "c87a96a8c23b83b32a73df051f6b84c2ef24d25ba4190dbe74f11138629b5aef"),

    @PetCustomization(ambient = SoundMaker.ENTITY_SQUID_AMBIENT, weight = PetWeight.SLIGHTLY_HEAVY)
    SQUID(IEntitySquidPet.class, "01433be242366af126da434b8735df1eb5b3cb2cede39145974e9c483607bac"),

    @PetCustomization(ambient = SoundMaker.ENTITY_STRAY_AMBIENT, weight = PetWeight.SLIGHTLY_HEAVY)
    STRAY(IEntityStrayPet.class, "2c5097916bc0565d30601c0eebfeb287277a34e867b4ea43c63819d53e89ede7",
            ArmsData.class),

    @PetCustomization(ambient = SoundMaker.ENTITY_STRIDER_AMBIENT, weight = PetWeight.HEAVY)
    STRIDER(IEntityStriderPet.class, "cb7ffdda656c68d88851a8e05b48cd2493773ffc4ab7d64e9302229fe3571059",
            AgeData.class, SaddleData.class),

    @PetCustomization(ambient = SoundMaker.ENTITY_TADPOLE_FLOP)
    TADPOLE(IEntityTadpolePet.class, "987035f5352334c2cba6ac4c65c2b9059739d6d0e839c1dd98d75d2e77957847"),

    @PetCustomization(ambient = SoundMaker.ENTITY_LLAMA_AMBIENT, weight = PetWeight.HEAVY)
    TRADER_LLAMA(IEntityTraderLlamaPet.class, "8424780b3c5c5351cf49fb5bf41fcb289491df6c430683c84d7846188db4f84d",
            AgeData.class, ChestData.class, ResetColorData.class, LlamaSkinData.class),

    @PetCustomization(ambient = SoundMaker.ENTITY_TROPICAL_FISH_AMBIENT, weight = PetWeight.LIGHT)
    TROPICAL_FISH(IEntityTropicalFishPet.class, "d6dd5e6addb56acbc694ea4ba5923b1b25688178feffa72290299e2505c97281",
            TropicalBodyColorData.class, TropicalPatternData.class, TropicalPatternColorData.class),

    @PetCustomization(ambient = SoundMaker.ENTITY_TURTLE_SHAMBLE, weight = PetWeight.HEAVY)
    TURTLE(IEntityTurtlePet.class, "0a4050e7aacc4539202658fdc339dd182d7e322f9fbcc4d5f99b5718a",
            AgeData.class),

    @PetCustomization(ambient = SoundMaker.ENTITY_VEX_AMBIENT, weight = PetWeight.LIGHT)
    VEX(IEntityVexPet.class, "c2ec5a516617ff1573cd2f9d5f3969f56d5575c4ff4efefabd2a18dc7ab98cd",
            PoweredData.class),

    @PetCustomization(ambient = SoundMaker.ENTITY_VILLAGER_AMBIENT, weight = PetWeight.SLIGHTLY_HEAVY)
    VILLAGER(IEntityVillagerPet.class, "41b830eb4082acec836bc835e40a11282bb51193315f91184337e8d3555583",
            AgeData.class, ShakeData.class, VillagerTypeData.class, VillagerBiomeData.class, VillagerLevelData.class),

    @PetCustomization(ambient = SoundMaker.ENTITY_VINDICATOR_AMBIENT, weight = PetWeight.SLIGHTLY_HEAVY)
    VINDICATOR(IEntityVindicatorPet.class, "6deaec344ab095b48cead7527f7dee61b063ff791f76a8fa76642c8676e2173",
            JohnnyData.class),

    @PetCustomization(ambient = SoundMaker.ENTITY_WANDERING_TRADER_AMBIENT, weight = PetWeight.SLIGHTLY_HEAVY)
    WANDERING_TRADER(IEntityWanderingTraderPet.class, "5f1379a82290d7abe1efaabbc70710ff2ec02dd34ade386bc00c930c461cf932"),

    @LargePet
    @PetCustomization(ambient = SoundMaker.ENTITY_WARDEN_AMBIENT, weight = PetWeight.HEAVY)
    WARDEN(IEntityWardenPet.class, "1dfd13ca08bf973bfef0293d770704a11ef5a9fe20d40671fb066724d3e18d8",
            WardenAngerData.class, WardenVibrationData.class),

    @PetCustomization(ambient = SoundMaker.ENTITY_WITCH_AMBIENT, weight = PetWeight.SLIGHTLY_HEAVY)
    WITCH(IEntityWitchPet.class, "20e13d18474fc94ed55aeb7069566e4687d773dac16f4c3f8722fc95bf9f2dfa",
            PotionData.class),

    @LargePet
    @PetCustomization(ambient = SoundMaker.ENTITY_WITHER_AMBIENT, weight = PetWeight.YOUR_KILLING_ME)
    WITHER(IEntityWitherPet.class, "cdf74e323ed41436965f5c57ddf2815d5332fe999e68fbb9d6cf5c8bd4139f",
            WitherShieldData.class, WitherSizeData.class),

    @PetCustomization(ambient = SoundMaker.ENTITY_WITHER_SKELETON_AMBIENT, weight = PetWeight.SLIGHTLY_HEAVY)
    WITHER_SKELETON(IEntityWitherSkeletonPet.class, Material.WITHER_SKELETON_SKULL,
            ArmsData.class),

    @PetCustomization(ambient = SoundMaker.ENTITY_WOLF_AMBIENT, weight = PetWeight.SLIGHTLY_HEAVY)
    WOLF(IEntityWolfPet.class, "24d7727f52354d24a64bd6602a0ce71a7b484d05963da83b470360faa9ceab5f",
            AgeData.class, TamedData.class, AngryData.class, ColorData.class, SittingData.class, WolfTiltData.class, ShakeData.class),

    @LargePet
    @PetCustomization(ambient = SoundMaker.ENTITY_ZOGLIN_AMBIENT, weight = PetWeight.YOUR_KILLING_ME)
    ZOGLIN(IEntityZoglinPet.class, "3c8c7c5d0556cd6629716e39188b21e7c0477479f242587bf19e0bc76b322551",
            AgeData.class),

    @PetCustomization(ambient = SoundMaker.ENTITY_ZOMBIE_AMBIENT, weight = PetWeight.SLIGHTLY_HEAVY)
    ZOMBIE(IEntityZombiePet.class, Material.ZOMBIE_HEAD,
            AgeData.class, ArmsData.class, ShakeData.class),

    @PetCustomization(ambient = SoundMaker.ENTITY_ZOMBIE_HORSE_AMBIENT, weight = PetWeight.HEAVY)
    ZOMBIE_HORSE(IEntityZombieHorsePet.class, "d22950f2d3efddb18de86f8f55ac518dce73f12a6e0f8636d551d8eb480ceec",
            AgeData.class, EatingData.class, SaddleData.class),

    @PetCustomization(ambient = SoundMaker.ENTITY_ZOMBIE_VILLAGER_AMBIENT, weight = PetWeight.SLIGHTLY_HEAVY)
    ZOMBIE_VILLAGER(IEntityZombieVillagerPet.class, "e5e08a8776c1764c3fe6a6ddd412dfcb87f41331dad479ac96c21df4bf3ac89c",
            AgeData.class, ArmsData.class, ShakeData.class, VillagerTypeData.class, VillagerBiomeData.class, VillagerLevelData.class),

    @PetCustomization(ambient = SoundMaker.ENTITY_ZOMBIFIED_PIGLIN_AMBIENT, weight = PetWeight.SLIGHTLY_HEAVY)
    ZOMBIFIED_PIGLIN(IEntityPigZombiePet.class, "7eabaecc5fae5a8a49c8863ff4831aaa284198f1a2398890c765e0a8de18da8c",
            AgeData.class, ArmsData.class);

    private final ItemBuilder builder;
    private final Class<? extends IEntityPet> entityClass;
    private final List<PetData> petData = Lists.newArrayList();

    PetType(ItemBuilder builder) {
        this(null, builder);
    }

    PetType(Class<? extends IEntityPet> entityClass, Material material) {
        this(entityClass, new ItemBuilder(material));
    }

    PetType(Class<? extends IEntityPet> entityClass, String textureID) {
        this(entityClass, new ItemBuilder(Material.PLAYER_HEAD).setTexture("http://textures.minecraft.net/texture/" + textureID));
    }

    @SafeVarargs
    PetType(Class<? extends IEntityPet> entityClass, Material material, Class<? extends PetData>... petData) {
        this(entityClass, new ItemBuilder(material), petData);
    }

    @SafeVarargs
    PetType(Class<? extends IEntityPet> entityClass, String textureID, Class<? extends PetData>... petData) {
        this(entityClass, new ItemBuilder(Material.PLAYER_HEAD).setTexture("http://textures.minecraft.net/texture/" + textureID), petData);
    }

    @SafeVarargs
    PetType(Class<? extends IEntityPet> entityClass, ItemBuilder builder, Class<? extends PetData>... petData) {
        this.entityClass = entityClass;
        LinkedList<Class<? extends PetData>> list = Lists.newLinkedList();
        list.addFirst(SilentData.class);
        list.addFirst(BurningData.class);
        list.addFirst(FrozenData.class);
        list.addFirst(Visible.class);
        list.addAll(Lists.newArrayList(petData));
        this.builder = builder.withName(Colorize.translateBungeeHex("&#c8f792" + Capitalise.capitalize(name().toLowerCase().replace("_", " "))));

        list.forEach(clazz -> {
            try {
                this.petData.add(clazz.newInstance());
            } catch (InstantiationException | IllegalAccessException e) {
                SimplePets.getDebugLogger().debug(DebugLevel.ERROR, "Failed to create an instance of " + clazz.getSimpleName());
                SimplePets.getDebugLogger().debug(DebugLevel.ERROR, "Error: " + e.getMessage());
            }
        });
    }

    public String getPermission () {
        return "pet.type."+name().toLowerCase().replace("_", "");
    }

    public String getPermission (String addition) {
        return getPermission()+"."+addition;
    }

    public List<PetData> getPetData() {
        return petData;
    }

    public ItemBuilder getBuilder() {
        return builder;
    }

    public Class<? extends IEntityPet> getEntityClass() {
        return entityClass;
    }

    public String getName() {
        return name().toLowerCase();
    }

    public EntityType getEntityType() {
        switch (this) {
            case POLARBEAR:
                return EntityType.POLAR_BEAR;
            case MOOSHROOM:
                return EntityType.MUSHROOM_COW;
            default:
                return EntityType.valueOf(name());
        }
    }

    public boolean isSupported() {
        try {
            if (entityClass != null) {
                if (entityClass.isAnnotationPresent(SupportedVersion.class)) {
                    SupportedVersion support = entityClass.getAnnotation(SupportedVersion.class);
                    if (support.maxVersion() == ServerVersion.UNKNOWN) {
                        return ServerVersion.isEqualNew(support.version());
                    }

                    return ServerVersion.isEqualOld(support.maxVersion()) && ServerVersion.isEqualNew(support.version());
                }
            }

            for (Annotation annotation : getClass().getField(this.name()).getAnnotations()) {
                if (annotation instanceof EnumVersion support) {
                    if (support.maxVersion() == ServerVersion.UNKNOWN) {
                        return ServerVersion.isEqualNew(support.version());
                    }

                    return ServerVersion.isEqualOld(support.maxVersion()) && ServerVersion.isEqualNew(support.version());
                }
            }
        } catch (NoSuchFieldException var7) {
            var7.printStackTrace();
        }

        return true;
    }


    public boolean isLargePet() {
        try {
            for (Annotation annotation : getClass().getField(this.name()).getAnnotations()) {
                if (annotation instanceof LargePet) return true;
            }
        } catch (NoSuchFieldException ignored) {
        }
        return false;
    }

    public boolean isInDevelopment() {
        try {
            for (Annotation annotation : getClass().getField(this.name()).getAnnotations()) {
                if (annotation instanceof InDevelopment) return true;
            }
        } catch (NoSuchFieldException ignored) {
        }
        return false;
    }

    public Optional<PetCustomization> getCustomization() {
        try {
            for (Annotation annotation : getClass().getField(this.name()).getAnnotations()) {
                if (annotation instanceof PetCustomization)
                    return Optional.of(((PetCustomization) annotation));
            }
        } catch (NoSuchFieldException ignored) {
        }
        return Optional.empty();
    }

    public static Optional<PetType> getPetType(String name) {
        try {
            return Optional.of(valueOf(PetType.class, name.toUpperCase().trim()));
        } catch (Exception ignored) {
        }
        return Optional.empty();
    }
}
