package simplepets.brainsynder.api.pet;

import com.google.common.collect.Lists;
import org.bsdevelopment.pluginutils.inventory.ItemBuilder;

import org.bsdevelopment.pluginutils.text.Colorize;
import org.bsdevelopment.pluginutils.text.WordUtils;
import org.bsdevelopment.pluginutils.version.VersionCompatibility;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.ambient.IEntityArmorStandPet;
import simplepets.brainsynder.api.entity.hostile.*;
import simplepets.brainsynder.api.entity.passive.*;
import simplepets.brainsynder.api.pet.annotations.InDevelopment;
import simplepets.brainsynder.api.pet.annotations.LargePet;
import simplepets.brainsynder.api.pet.annotations.PetCustomization;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public enum PetType {
    UNKNOWN(ItemBuilder.of(Material.STONE)),

    @PetCustomization(ambient = "ENTITY_ALLAY_AMBIENT_WITHOUT_ITEM", weight = PetWeight.LIGHT)
    ALLAY(IEntityAllayPet.class, "40e1c7064af7dee68677efaa95f6e6e01430b006dd91638ea2a61849254488ec", PetDataRegistry.Allay.DANCING),

    @PetCustomization(ambient = "ENTITY_ARMADILLO_AMBIENT", weight = PetWeight.HEAVY)
    ARMADILLO(IEntityArmadilloPet.class, "9164ed0e0ef69b0ce7815e4300b4413a4828fcb0092918543545a418a48e0c3c", PetDataRegistry.BABY, PetDataRegistry.Armadillo.PHASE),

    @InDevelopment
    @PetCustomization(ambient = "ENTITY_ARMORSTAND_FALL", weight = PetWeight.LIGHT)
    ARMOR_STAND(IEntityArmorStandPet.class, Material.ARMOR_STAND),

    @PetCustomization(ambient = "ENTITY_AXOLOTL_IDLE_WATER", weight = PetWeight.LIGHT)
    AXOLOTL(IEntityAxolotlPet.class, "5c138f401c67fc2e1e387d9c90a9691772ee486e8ddbf2ed375fc8348746f936",
        PetDataRegistry.BABY, PetDataRegistry.Axolotl.VARIANT, PetDataRegistry.Axolotl.PLAY_DEAD),

    @PetCustomization(ambient = "ENTITY_BAT_AMBIENT")
    BAT(IEntityBatPet.class, "9e99deef919db66ac2bd28d6302756ccd57c7f8b12b9dca8f41c3e0a04ac1cc", PetDataRegistry.Bat.HANG),

    @PetCustomization(ambient = "ENTITY_BEE_LOOP", weight = PetWeight.LIGHT)
    BEE(IEntityBeePet.class, "fa2cb74c13245d3ce9bacc8b1600af02fd7c91f501feaf97364e1f8b6f04f47f",
        PetDataRegistry.BABY, PetDataRegistry.Bee.ANGRY, PetDataRegistry.Bee.NECTAR, PetDataRegistry.Bee.STINGER, PetDataRegistry.Bee.FLIPPED),

    @PetCustomization(ambient = "ENTITY_BLAZE_AMBIENT", weight = PetWeight.SLIGHTLY_HEAVY)
    BLAZE(IEntityBlazePet.class, "b20657e24b56e1b2f8fc219da1de788c0c24f36388b1a409d0cd2d8dba44aa3b"),

    @PetCustomization(ambient = "ENTITY_BREEZE_IDLE_GROUND", weight = PetWeight.LIGHT)
    BREEZE(IEntityBreezePet.class, "a275728af7e6a29c88125b675a39d88ae9919bb61fdc200337fed6ab0c49d65c"),

    @PetCustomization(ambient = "ENTITY_BOGGED_AMBIENT", weight = PetWeight.SLIGHTLY_HEAVY)
    BOGGED(IEntityBoggedPet.class, "a3b9003ba2d05562c75119b8a62185c67130e9282f7acbac4bc2824c21eb95d9", PetDataRegistry.SHEAR),

    @PetCustomization(ambient = "ENTITY_CAMEL_AMBIENT", weight = PetWeight.SLIGHTLY_HEAVY)
    CAMEL(IEntityCamelPet.class, "92b31239520511ca7b6712ef0ecfb55b6c56b9347240f4cbf9925ce0bf0fa445", PetDataRegistry.BABY, PetDataRegistry.SITTING),

    @PetCustomization(ambient = "ENTITY_CAMEL_HUSK_AMBIENT", weight = PetWeight.SLIGHTLY_HEAVY)
    CAMEL_HUSK(IEntityCamelHuskPet.class, "3bd7a92a6f67b7500d16c4e12f28058ec2859311556ba03be2d1f581170f2db6", PetDataRegistry.BABY, PetDataRegistry.SITTING),

    @PetCustomization(ambient = "ENTITY_CAT_AMBIENT", weight = PetWeight.LIGHT)
    CAT(IEntityCatPet.class, "6b253fc6b656988453a2d7138fca4d1f2752f47691f0c434e432183771cfe1",
        PetDataRegistry.BABY, PetDataRegistry.TAMED, PetDataRegistry.SITTING, PetDataRegistry.SLEEP, PetDataRegistry.COLOR, PetDataRegistry.Cat.TYPE, PetDataRegistry.Cat.LOOK_UP),

    @PetCustomization(ambient = "ENTITY_SPIDER_AMBIENT", weight = PetWeight.SLIGHTLY_HEAVY)
    CAVE_SPIDER(IEntityCaveSpiderPet.class, "5617f7dd5ed16f3bd186440517cd440a170015b1cc6fcb2e993c05de33f"),

    @PetCustomization(ambient = "ENTITY_CHICKEN_AMBIENT", weight = PetWeight.LIGHT)
    CHICKEN(IEntityChickenPet.class, "1638469a599ceef7207537603248a9ab11ff591fd378bea4735b346a7fae893", PetDataRegistry.BABY, PetDataRegistry.Chicken.VARIANT),

    @PetCustomization(ambient = "ENTITY_COD_AMBIENT")
    COD(IEntityCodPet.class, "7892d7dd6aadf35f86da27fb63da4edda211df96d2829f691462a4fb1cab0"),

    @PetCustomization(ambient = "ENTITY_COW_AMBIENT", weight = PetWeight.SLIGHTLY_HEAVY)
    COW(IEntityCowPet.class, "c5a9cd58d4c67bccc8fb1f5f756a2d381c9ffac2924b7f4cb71aa9fa13fb5c", PetDataRegistry.BABY, PetDataRegistry.Cow.VARIANT),

    @PetCustomization(ambient = "ENTITY_CREEPER_HURT", weight = PetWeight.SLIGHTLY_HEAVY)
    CREEPER(IEntityCreeperPet.class, Material.CREEPER_HEAD, PetDataRegistry.POWERED),

    @PetCustomization(ambient = "ENTITY_CREAKING_AMBIENT", weight = PetWeight.SLIGHTLY_HEAVY)
    CREAKING(IEntityCreakingPet.class, "77b5be72769ccff1a6cb77c5848e01d7e5704a3d349c0737ff93cb54d02380ac"),

    @PetCustomization(ambient = "ENTITY_COPPER_GOLEM_STEP", weight = PetWeight.SLIGHTLY_HEAVY)
    COPPER_GOLEM(IEntityCopperGolemPet.class, "99e24e94dbe42e230d83293a77d61ff7101a8c68ab68bbc6a93f9630fb2fdb4", PetDataRegistry.CopperGolem.OXIDATION),

    @PetCustomization(ambient = "ENTITY_DOLPHIN_AMBIENT", weight = PetWeight.SLIGHTLY_HEAVY)
    DOLPHIN(IEntityDolphinPet.class, "8e9688b950d880b55b7aa2cfcd76e5a0fa94aac6d16f78e833f7443ea29fed3",
            PetDataRegistry.BABY),

    @PetCustomization(ambient = "ENTITY_DONKEY_AMBIENT", weight = PetWeight.HEAVY)
    DONKEY(IEntityDonkeyPet.class, "399bb50d1a214c394917e25bb3f2e20698bf98ca703e4cc08b42462df309d6e6",
        PetDataRegistry.BABY, PetDataRegistry.Horse.CHEST, PetDataRegistry.Horse.EATING, PetDataRegistry.SADDLE),

    @PetCustomization(ambient = "ENTITY_DROWNED_AMBIENT", weight = PetWeight.SLIGHTLY_HEAVY)
    DROWNED(IEntityDrownedPet.class, "c3f7ccf61dbc3f9fe9a6333cde0c0e14399eb2eea71d34cf223b3ace22051",
        PetDataRegistry.BABY, PetDataRegistry.ARMS, PetDataRegistry.SHAKE),

    @LargePet
    @PetCustomization(ambient = "ENTITY_ELDER_GUARDIAN_AMBIENT", weight = PetWeight.HEAVY)
    ELDER_GUARDIAN(IEntityElderGuardianPet.class, "1c797482a14bfcb877257cb2cff1b6e6a8b8413336ffb4c29a6139278b436b"),

    @PetCustomization(ambient = "ENTITY_ENDERMAN_AMBIENT", weight = PetWeight.SLIGHTLY_HEAVY)
    ENDERMAN(IEntityEndermanPet.class, "96c0b36d53fff69a49c7d6f3932f2b0fe948e032226d5e8045ec58408a36e951", PetDataRegistry.Enderman.SCREAM),

    @PetCustomization(ambient = "ENTITY_ENDERMITE_AMBIENT", weight = PetWeight.LIGHT)
    ENDERMITE(IEntityEndermitePet.class, "5bc7b9d36fb92b6bf292be73d32c6c5b0ecc25b44323a541fae1f1e67e393a3e"),

    @PetCustomization(ambient = "ENTITY_EVOKER_AMBIENT", weight = PetWeight.SLIGHTLY_HEAVY)
    EVOKER(IEntityEvokerPet.class, "d954135dc82213978db478778ae1213591b93d228d36dd54f1ea1da48e7cba6", PetDataRegistry.SPELL),

    @PetCustomization(ambient = "ENTITY_FOX_AMBIENT", weight = PetWeight.SLIGHTLY_HEAVY)
    FOX(IEntityFoxPet.class, "d8954a42e69e0881ae6d24d4281459c144a0d5a968aed35d6d3d73a3c65d26a",
        PetDataRegistry.BABY, PetDataRegistry.SLEEP, PetDataRegistry.Fox.INTEREST, PetDataRegistry.Fox.CROUCHING, PetDataRegistry.Fox.TYPE, PetDataRegistry.Fox.SITTING),

    @PetCustomization(ambient = "ENTITY_FROG_AMBIENT", weight = PetWeight.LIGHT)
    FROG(IEntityFrogPet.class, "23ce6f9998ed2da757d1e6372f04efa20e57dfc17c3a06478657bbdf51c2f2a2",
        PetDataRegistry.Frog.VARIANT, PetDataRegistry.Frog.CROAKING, PetDataRegistry.Frog.TONGUE),

    @LargePet
    @PetCustomization(ambient = "ENTITY_GHAST_AMBIENT", weight = PetWeight.HEAVY)
    GHAST(IEntityGhastPet.class, "7a8b714d32d7f6cf8b37e221b758b9c599ff76667c7cd45bbc49c5ef19858646", PetDataRegistry.Ghast.SCREAM),

    @LargePet
    @PetCustomization(ambient = "ENTITY_ZOMBIE_AMBIENT", weight = PetWeight.YOUR_KILLING_ME)
    GIANT(IEntityGiantPet.class, Material.ZOMBIE_HEAD),

    @PetCustomization(ambient = "ENTITY_GLOW_SQUID_AMBIENT", weight = PetWeight.SLIGHTLY_HEAVY)
    GLOW_SQUID(IEntityGlowSquidPet.class, "3e94a1bb1cb00aaa153a74daf4b0eea20b8974522fe9901eb55aef478ebeff0d", PetDataRegistry.BABY, PetDataRegistry.GlowSquid.GLOWING),

    @PetCustomization(ambient = "ENTITY_GOAT_AMBIENT", weight = PetWeight.SLIGHTLY_HEAVY)
    GOAT(IEntityGoatPet.class, "957607099d06b7a8b1327093cd0a488be7c9f50b6121b22151271b59170f3c21",
        PetDataRegistry.BABY, PetDataRegistry.Goat.LEFT_HORN, PetDataRegistry.Goat.RIGHT_HORN),

    @PetCustomization(ambient = "ENTITY_GUARDIAN_AMBIENT", weight = PetWeight.HEAVY)
    GUARDIAN(IEntityGuardianPet.class, "a0bf34a71e7715b6ba52d5dd1bae5cb85f773dc9b0d457b4bfc5f9dd3cc7c94"),

    @LargePet
    @PetCustomization(ambient = "ENTITY_HAPPY_GHAST_AMBIENT", weight = PetWeight.HEAVY)
    HAPPY_GHAST(IEntityHappyGhastPet.class, "a1a36cb93d01675c4622dd5c8d872110911ec12c372e89afa8ba03862867f6fb", PetDataRegistry.BABY, PetDataRegistry.RESET_COLOR),

    @LargePet
    @PetCustomization(ambient = "ENTITY_HOGLIN_AMBIENT", weight = PetWeight.YOUR_KILLING_ME)
    HOGLIN(IEntityHoglinPet.class, "9bb9bc0f01dbd762a08d9e77c08069ed7c95364aa30ca1072208561b730e8d75", PetDataRegistry.BABY, PetDataRegistry.SHAKE),

    @PetCustomization(ambient = "ENTITY_HORSE_AMBIENT", weight = PetWeight.HEAVY)
    HORSE(IEntityHorsePet.class, "628d1ab4be1e28b7b461fdea46381ac363a7e5c3591c9e5d2683fbe1ec9fcd3",
        PetDataRegistry.BABY, PetDataRegistry.Horse.EATING, PetDataRegistry.Horse.ARMOR, PetDataRegistry.Horse.COLOR, PetDataRegistry.Horse.STYLE, PetDataRegistry.SADDLE),

    @PetCustomization(ambient = "ENTITY_HUSK_AMBIENT", weight = PetWeight.SLIGHTLY_HEAVY)
    HUSK(IEntityHuskPet.class, "d674c63c8db5f4ca628d69a3b1f8a36e29d8fd775e1a6bdb6cabb4be4db121",
        PetDataRegistry.BABY, PetDataRegistry.ARMS, PetDataRegistry.SHAKE),

    @PetCustomization(ambient = "ENTITY_ILLUSIONER_AMBIENT", weight = PetWeight.SLIGHTLY_HEAVY)
    ILLUSIONER(IEntityIllusionerPet.class, "512512e7d016a2343a7bff1a4cd15357ab851579f1389bd4e3a24cbeb88b", PetDataRegistry.SPELL),

    @PetCustomization(ambient = "ENTITY_IRON_GOLEM_STEP", weight = PetWeight.HEAVY)
    IRON_GOLEM(IEntityIronGolemPet.class, "89091d79ea0f59ef7ef94d7bba6e5f17f2f7d4572c44f90f76c4819a714"),

    @PetCustomization(ambient = "ENTITY_LLAMA_AMBIENT", weight = PetWeight.HEAVY)
    LLAMA(IEntityLlamaPet.class, "818cd457fbaf327fa39f10b5b36166fd018264036865164c02d9e5ff53f45",
        PetDataRegistry.BABY, PetDataRegistry.Horse.CHEST, PetDataRegistry.RESET_COLOR, PetDataRegistry.Llama.SKIN, PetDataRegistry.SADDLE),

    @PetCustomization(ambient = "ENTITY_MAGMA_CUBE_SQUISH", weight = PetWeight.SLIGHTLY_HEAVY)
    MAGMA_CUBE(IEntityMagmaCubePet.class, "38957d5023c937c4c41aa2412d43410bda23cf79a9f6ab36b76fef2d7c429", PetDataRegistry.SIZE),

    @PetCustomization(ambient = "ENTITY_COW_AMBIENT", weight = PetWeight.SLIGHTLY_HEAVY)
    MOOSHROOM(IEntityMooshroomPet.class, "2b52841f2fd589e0bc84cbabf9e1c27cb70cac98f8d6b3dd065e55a4dcb70d77", PetDataRegistry.BABY, PetDataRegistry.Mooshroom.COLOR),

    @PetCustomization(ambient = "ENTITY_MULE_AMBIENT", weight = PetWeight.HEAVY)
    MULE(IEntityMulePet.class, "46dcda265e57e4f51b145aacbf5b59bdc6099ffd3cce0a661b2c0065d80930d8",
        PetDataRegistry.BABY, PetDataRegistry.Horse.CHEST, PetDataRegistry.Horse.EATING, PetDataRegistry.SADDLE),

    @PetCustomization(ambient = "ENTITY_NAUTILUS_AMBIENT", weight = PetWeight.LIGHT)
    NAUTILUS(IEntityNautilusPet.class, "3bb340dd3302615348de5162fe1670b9c5c9c616cd92d2de9d8398cb33e842ae",
            PetDataRegistry.BABY, PetDataRegistry.SADDLE, PetDataRegistry.Nautilus.ARMOR),

    @PetCustomization(ambient = "ENTITY_OCELOT_AMBIENT", weight = PetWeight.LIGHT)
    OCELOT(IEntityOcelotPet.class, "5657cd5c2989ff97570fec4ddcdc6926a68a3393250c1be1f0b114a1db1", PetDataRegistry.BABY),

    @PetCustomization(ambient = "ENTITY_PANDA_AMBIENT", weight = PetWeight.HEAVY)
    PANDA(IEntityPandaPet.class, "dca096eea506301bea6d4b17ee1605625a6f5082c71f74a639cc940439f47166",
        PetDataRegistry.BABY, PetDataRegistry.Panda.GENE, PetDataRegistry.Panda.EATING, PetDataRegistry.SLEEP, PetDataRegistry.Panda.SNEEZE),

    @PetCustomization(ambient = "ENTITY_PARCHED_AMBIENT", weight = PetWeight.SLIGHTLY_HEAVY)
    PARCHED(IEntityParchedPet.class, "24aeceff5f26dd8413c5c03547c234ac03108d187af0b9cd834a8ce12598591c"),

    @PetCustomization(ambient = "ENTITY_PARROT_AMBIENT", weight = PetWeight.LIGHT)
    PARROT(IEntityParrotPet.class, "a4ba8d66fecb1992e94b8687d6ab4a5320ab7594ac194a2615ed4df818edbc3",
        PetDataRegistry.RAINBOW, PetDataRegistry.TAMED, PetDataRegistry.Parrot.VARIANT, PetDataRegistry.SITTING),

    @PetCustomization(ambient = "ENTITY_PHANTOM_AMBIENT", weight = PetWeight.SLIGHTLY_HEAVY)
    PHANTOM(IEntityPhantomPet.class, "746830da5f83a3aaed838a99156ad781a789cfcf13e25beef7f54a86e4fa4", PetDataRegistry.SIZE),

    @PetCustomization(ambient = "ENTITY_PIG_AMBIENT", weight = PetWeight.SLIGHTLY_HEAVY)
    PIG(IEntityPigPet.class, "621668ef7cb79dd9c22ce3d1f3f4cb6e2559893b6df4a469514e667c16aa4",
        PetDataRegistry.BABY, PetDataRegistry.SADDLE, PetDataRegistry.Pig.VARIANT),

    @PetCustomization(ambient = "ENTITY_PIGLIN_AMBIENT", weight = PetWeight.SLIGHTLY_HEAVY)
    PIGLIN(IEntityPiglinPet.class, "9f18107d275f1cb3a9f973e5928d5879fa40328ff3258054db6dd3e7c0ca6330",
        PetDataRegistry.BABY, PetDataRegistry.Piglin.CHARGING, PetDataRegistry.Piglin.DANCING, PetDataRegistry.SHAKE),

    @PetCustomization(ambient = "ENTITY_PIGLIN_BRUTE_AMBIENT", weight = PetWeight.SLIGHTLY_HEAVY)
    PIGLIN_BRUTE(IEntityPiglinBrutePet.class, "3e300e9027349c4907497438bac29e3a4c87a848c50b34c21242727b57f4e1cf", PetDataRegistry.SHAKE),

    @PetCustomization(ambient = "ENTITY_PILLAGER_AMBIENT", weight = PetWeight.SLIGHTLY_HEAVY)
    PILLAGER(IEntityPillagerPet.class, "4aee6bb37cbfc92b0d86db5ada4790c64ff4468d68b84942fde04405e8ef5333"),

    @PetCustomization(ambient = "ENTITY_POLAR_BEAR_AMBIENT", weight = PetWeight.HEAVY)
    POLARBEAR(IEntityPolarBearPet.class, "c4fe926922fbb406f343b34a10bb98992cee4410137d3f88099427b22de3ab90", PetDataRegistry.BABY, PetDataRegistry.PolarBear.STANDING),

    @PetCustomization(ambient = "ENTITY_PUFFER_FISH_AMBIENT", weight = PetWeight.LIGHT)
    PUFFERFISH(IEntityPufferFishPet.class, "17152876bc3a96dd2a2299245edb3beef647c8a56ac8853a687c3e7b5d8bb", PetDataRegistry.Pufferfish.SIZE),

    @PetCustomization(ambient = "ENTITY_RABBIT_AMBIENT", weight = PetWeight.LIGHT)
    RABBIT(IEntityRabbitPet.class, "ffecc6b5e6ea5ced74c46e7627be3f0826327fba26386c6cc7863372e9bc", PetDataRegistry.BABY, PetDataRegistry.Rabbit.VARIANT),

    @LargePet
    @PetCustomization(ambient = "ENTITY_RAVAGER_AMBIENT", weight = PetWeight.YOUR_KILLING_ME)
    RAVAGER(IEntityRavagerPet.class, "cd20bf52ec390a0799299184fc678bf84cf732bb1bd78fd1c4b441858f0235a8", PetDataRegistry.Ravager.CHOMP),

    @PetCustomization(ambient = "ENTITY_SALMON_AMBIENT", weight = PetWeight.LIGHT)
    SALMON(IEntitySalmonPet.class, "8aeb21a25e46806ce8537fbd6668281cf176ceafe95af90e94a5fd84924878"),

    @PetCustomization(ambient = "ENTITY_SHEEP_AMBIENT", weight = PetWeight.SLIGHTLY_HEAVY)
    SHEEP(IEntitySheepPet.class, "f31f9ccc6b3e32ecf13b8a11ac29cd33d18c95fc73db8a66c5d657ccb8be70",
        PetDataRegistry.BABY, PetDataRegistry.COLOR, PetDataRegistry.RAINBOW, PetDataRegistry.SHEAR),

    @InDevelopment
    @PetCustomization(ambient = "ENTITY_SHULKER_AMBIENT", weight = PetWeight.SLIGHTLY_HEAVY)
    SHULKER(IEntityShulkerPet.class, "1433a4b73273a64c8ab2830b0fff777a61a488c92f60f83bfb3e421f428a44",
        PetDataRegistry.RESET_COLOR, PetDataRegistry.RAINBOW, PetDataRegistry.Shulker.CLOSE),

    @PetCustomization(ambient = "ENTITY_SILVERFISH_AMBIENT", weight = PetWeight.LIGHT)
    SILVERFISH(IEntitySilverfishPet.class, "d06310a8952b265c6e6bed4348239ddea8e5482c8c68be6fff981ba8056bf2e"),

    @PetCustomization(ambient = "ENTITY_SKELETON_AMBIENT", weight = PetWeight.SLIGHTLY_HEAVY)
    SKELETON(IEntitySkeletonPet.class, Material.SKELETON_SKULL),

    @PetCustomization(ambient = "ENTITY_SKELETON_HORSE_AMBIENT", weight = PetWeight.HEAVY)
    SKELETON_HORSE(IEntitySkeletonHorsePet.class, "47effce35132c86ff72bcae77dfbb1d22587e94df3cbc2570ed17cf8973a",
        PetDataRegistry.BABY, PetDataRegistry.Horse.EATING, PetDataRegistry.SADDLE),

    @PetCustomization(ambient = "ENTITY_SLIME_SQUISH", weight = PetWeight.SLIGHTLY_HEAVY)
    SLIME(IEntitySlimePet.class, "bb13133a8fb4ef00b71ef9bab639a66fbc7d5cffcc190c1df74bf2161dfd3ec7", PetDataRegistry.SIZE),

    @LargePet
    @PetCustomization(ambient = "ENTITY_SNIFFER_IDLE", weight = PetWeight.HEAVY)
    SNIFFER(IEntitySnifferPet.class, "87ad920a66e38cc3426a5bff084667e8772116915e298098567c139f222e2c42", PetDataRegistry.BABY, PetDataRegistry.Sniffer.STATE),

    @PetCustomization(ambient = "ENTITY_SNOWMAN_AMBIENT", weight = PetWeight.SLIGHTLY_HEAVY)
    SNOWMAN(IEntitySnowmanPet.class, "9aed9fe4ed0893e325f4fbd32b093c1cc562cba27ff73359d356f1c288e441f9", PetDataRegistry.Snowman.PUMPKIN),

    @PetCustomization(ambient = "ENTITY_SPIDER_AMBIENT", weight = PetWeight.SLIGHTLY_HEAVY)
    SPIDER(IEntitySpiderPet.class, "c87a96a8c23b83b32a73df051f6b84c2ef24d25ba4190dbe74f11138629b5aef"),

    @PetCustomization(ambient = "ENTITY_SQUID_AMBIENT", weight = PetWeight.SLIGHTLY_HEAVY)
    SQUID(IEntitySquidPet.class, "01433be242366af126da434b8735df1eb5b3cb2cede39145974e9c483607bac", PetDataRegistry.BABY),

    @PetCustomization(ambient = "ENTITY_STRAY_AMBIENT", weight = PetWeight.SLIGHTLY_HEAVY)
    STRAY(IEntityStrayPet.class, "2c5097916bc0565d30601c0eebfeb287277a34e867b4ea43c63819d53e89ede7"),

    @PetCustomization(ambient = "ENTITY_STRIDER_AMBIENT", weight = PetWeight.HEAVY)
    STRIDER(IEntityStriderPet.class, "cb7ffdda656c68d88851a8e05b48cd2493773ffc4ab7d64e9302229fe3571059", PetDataRegistry.BABY, PetDataRegistry.SADDLE),

    @PetCustomization(ambient = "ENTITY_SULFUR_CUBE_SQUISH", weight = PetWeight.SLIGHTLY_HEAVY)
    SULFUR_CUBE(IEntitySulfurCubePet.class, "f0d9056ec6db388af12304ef96ffdc8228dcf368ab255323258b716f990b4ab", PetDataRegistry.SIZE),

    @PetCustomization(ambient = "ENTITY_TADPOLE_FLOP")
    TADPOLE(IEntityTadpolePet.class, "987035f5352334c2cba6ac4c65c2b9059739d6d0e839c1dd98d75d2e77957847"),

    @PetCustomization(ambient = "ENTITY_LLAMA_AMBIENT", weight = PetWeight.HEAVY)
    TRADER_LLAMA(IEntityTraderLlamaPet.class, "8424780b3c5c5351cf49fb5bf41fcb289491df6c430683c84d7846188db4f84d",
        PetDataRegistry.BABY, PetDataRegistry.Horse.CHEST, PetDataRegistry.RESET_COLOR, PetDataRegistry.Llama.SKIN),

    @PetCustomization(ambient = "ENTITY_TROPICAL_FISH_AMBIENT", weight = PetWeight.LIGHT)
    TROPICAL_FISH(IEntityTropicalFishPet.class, "d6dd5e6addb56acbc694ea4ba5923b1b25688178feffa72290299e2505c97281",
        PetDataRegistry.TropicalFish.BODY_COLOR, PetDataRegistry.TropicalFish.PATTERN, PetDataRegistry.TropicalFish.PATTERN_COLOR),

    @PetCustomization(ambient = "ENTITY_TURTLE_SHAMBLE", weight = PetWeight.HEAVY)
    TURTLE(IEntityTurtlePet.class, "0a4050e7aacc4539202658fdc339dd182d7e322f9fbcc4d5f99b5718a", PetDataRegistry.BABY),

    @PetCustomization(ambient = "ENTITY_VEX_AMBIENT", weight = PetWeight.LIGHT)
    VEX(IEntityVexPet.class, "c2ec5a516617ff1573cd2f9d5f3969f56d5575c4ff4efefabd2a18dc7ab98cd",
        PetDataRegistry.POWERED),

    @PetCustomization(ambient = "ENTITY_VILLAGER_AMBIENT", weight = PetWeight.SLIGHTLY_HEAVY)
    VILLAGER(IEntityVillagerPet.class, "41b830eb4082acec836bc835e40a11282bb51193315f91184337e8d3555583",
        PetDataRegistry.BABY, PetDataRegistry.SHAKE, PetDataRegistry.Villager.PROFESSION, PetDataRegistry.Villager.BIOME, PetDataRegistry.Villager.LEVEL),

    @PetCustomization(ambient = "ENTITY_VINDICATOR_AMBIENT", weight = PetWeight.SLIGHTLY_HEAVY)
    VINDICATOR(IEntityVindicatorPet.class, "6deaec344ab095b48cead7527f7dee61b063ff791f76a8fa76642c8676e2173", PetDataRegistry.Vindicator.JOHNNY),

    @PetCustomization(ambient = "ENTITY_WANDERING_TRADER_AMBIENT", weight = PetWeight.SLIGHTLY_HEAVY)
    WANDERING_TRADER(IEntityWanderingTraderPet.class, "5f1379a82290d7abe1efaabbc70710ff2ec02dd34ade386bc00c930c461cf932"),

    @LargePet
    @PetCustomization(ambient = "ENTITY_WARDEN_AMBIENT", weight = PetWeight.HEAVY)
    WARDEN(IEntityWardenPet.class, "1dfd13ca08bf973bfef0293d770704a11ef5a9fe20d40671fb066724d3e18d8", PetDataRegistry.Warden.ANGER, PetDataRegistry.Warden.VIBRATION),

    @PetCustomization(ambient = "ENTITY_WITCH_AMBIENT", weight = PetWeight.SLIGHTLY_HEAVY)
    WITCH(IEntityWitchPet.class, "20e13d18474fc94ed55aeb7069566e4687d773dac16f4c3f8722fc95bf9f2dfa", PetDataRegistry.Witch.POTION),

    @LargePet
    @PetCustomization(ambient = "ENTITY_WITHER_AMBIENT", weight = PetWeight.YOUR_KILLING_ME)
    WITHER(IEntityWitherPet.class, "cdf74e323ed41436965f5c57ddf2815d5332fe999e68fbb9d6cf5c8bd4139f", PetDataRegistry.Wither.SHIELD, PetDataRegistry.Wither.SMALL),

    @PetCustomization(ambient = "ENTITY_WITHER_SKELETON_AMBIENT", weight = PetWeight.SLIGHTLY_HEAVY)
    WITHER_SKELETON(IEntityWitherSkeletonPet.class, Material.WITHER_SKELETON_SKULL),

    @PetCustomization(ambient = "ENTITY_WOLF_AMBIENT", weight = PetWeight.SLIGHTLY_HEAVY)
    WOLF(IEntityWolfPet.class, "24d7727f52354d24a64bd6602a0ce71a7b484d05963da83b470360faa9ceab5f",
        PetDataRegistry.BABY, PetDataRegistry.TAMED, PetDataRegistry.Wolf.ANGRY, PetDataRegistry.COLOR, PetDataRegistry.SITTING,
        PetDataRegistry.Wolf.TILT, PetDataRegistry.SHAKE, PetDataRegistry.Wolf.VARIANT),

    @LargePet
    @PetCustomization(ambient = "ENTITY_ZOGLIN_AMBIENT", weight = PetWeight.YOUR_KILLING_ME)
    ZOGLIN(IEntityZoglinPet.class, "3c8c7c5d0556cd6629716e39188b21e7c0477479f242587bf19e0bc76b322551", PetDataRegistry.BABY),

    @PetCustomization(ambient = "ENTITY_ZOMBIE_AMBIENT", weight = PetWeight.SLIGHTLY_HEAVY)
    ZOMBIE(IEntityZombiePet.class, Material.ZOMBIE_HEAD,
        PetDataRegistry.BABY, PetDataRegistry.ARMS, PetDataRegistry.SHAKE),

    @PetCustomization(ambient = "ENTITY_ZOMBIE_HORSE_AMBIENT", weight = PetWeight.HEAVY)
    ZOMBIE_HORSE(IEntityZombieHorsePet.class, "d22950f2d3efddb18de86f8f55ac518dce73f12a6e0f8636d551d8eb480ceec",
        PetDataRegistry.BABY, PetDataRegistry.Horse.EATING, PetDataRegistry.SADDLE),

    @PetCustomization(ambient = "ENTITY_ZOMBIE_NAUTILUS_AMBIENT", weight = PetWeight.LIGHT)
    ZOMBIE_NAUTILUS(IEntityZombieNautilusPet.class, "fd9a933376da44c3391307cb9f4cf03f16f3a54f495fd5a11bad8a373f9d5720",
            PetDataRegistry.BABY, PetDataRegistry.SADDLE, PetDataRegistry.Nautilus.ARMOR, PetDataRegistry.ZombieNautilus.VARIANT),

    @PetCustomization(ambient = "ENTITY_ZOMBIE_VILLAGER_AMBIENT", weight = PetWeight.SLIGHTLY_HEAVY)
    ZOMBIE_VILLAGER(IEntityZombieVillagerPet.class, "e5e08a8776c1764c3fe6a6ddd412dfcb87f41331dad479ac96c21df4bf3ac89c",
        PetDataRegistry.BABY, PetDataRegistry.ARMS, PetDataRegistry.SHAKE, PetDataRegistry.Villager.PROFESSION, PetDataRegistry.Villager.BIOME, PetDataRegistry.Villager.LEVEL),

    @PetCustomization(ambient = "ENTITY_ZOMBIFIED_PIGLIN_AMBIENT", weight = PetWeight.SLIGHTLY_HEAVY)
    ZOMBIFIED_PIGLIN(IEntityPigZombiePet.class, "7eabaecc5fae5a8a49c8863ff4831aaa284198f1a2398890c765e0a8de18da8c", PetDataRegistry.BABY, PetDataRegistry.ARMS);

    private final ItemBuilder builder;
    private final Class<? extends IEntityPet> entityClass;
    private final List<PetData> petData = Lists.newArrayList();

    PetType(ItemBuilder builder) {
        this(null, builder);
    }

    PetType(Class<? extends IEntityPet> entityClass, Material material) {
        this(entityClass, ItemBuilder.of(material));
    }

    PetType(Class<? extends IEntityPet> entityClass, String textureID) {
        this(entityClass, ItemBuilder.playerSkull("http://textures.minecraft.net/texture/" + textureID));
    }

    @SafeVarargs
    PetType(Class<? extends IEntityPet> entityClass, Material material, PetData<?>... petData) {
        this(entityClass, ItemBuilder.of(material), petData);
    }

    @SafeVarargs
    PetType(Class<? extends IEntityPet> entityClass, String textureID, PetData<?>... petData) {
        this(entityClass, ItemBuilder.playerSkull("http://textures.minecraft.net/texture/" + textureID), petData);
    }

    @SafeVarargs
    PetType(Class<? extends IEntityPet> entityClass, ItemBuilder builder, PetData<?>... petData) {
        this.entityClass = entityClass;
        LinkedList<PetData<?>> list = Lists.newLinkedList();
        list.addFirst(PetDataRegistry.SILENT);
        if (entityClass != IEntityWardenPet.class) list.addFirst(PetDataRegistry.BURNING);
        list.addFirst(PetDataRegistry.FROZEN);
        list.addFirst(PetDataRegistry.VISIBLE);
        if ((!IEntityArmorStandPet.class.isInstance(entityClass)) && (!IEntityShulkerPet.class.isInstance(entityClass)))
            list.addFirst(PetDataRegistry.HALF_SCALE);
        Arrays.asList(petData).forEach(data -> {
            if (data.isVersionSupported()) list.add(data);
        });
        this.builder = builder.withName(Colorize.translateBungeeHex("&#c8f792" + WordUtils.capitalize(name().toLowerCase().replace("_", " "))));

        this.petData.addAll(list);
    }

    public String getPermission() {
        return "pet.type." + name().toLowerCase().replace("_", "");
    }

    public String getPermission(String addition) {
        return getPermission() + "." + addition;
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
            case SNOWMAN:
                return EntityType.SNOW_GOLEM;
            default:
                return EntityType.valueOf(name());
        }
    }

    public boolean isSupported() {
        if (entityClass != null) {
            return VersionCompatibility.isCompatible(entityClass);
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
