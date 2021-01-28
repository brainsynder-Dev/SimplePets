package simplepets.brainsynder.api.pet;

import lib.brainsynder.EnumVersion;
import lib.brainsynder.ServerVersion;
import lib.brainsynder.SupportedVersion;
import lib.brainsynder.item.ItemBuilder;
import lib.brainsynder.utils.Capitalise;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.ambient.IEntityArmorStandPet;
import simplepets.brainsynder.api.entity.hostile.*;
import simplepets.brainsynder.api.entity.passive.*;

import java.lang.annotation.Annotation;
import java.util.Optional;

public enum PetType {
    UNKNOWN(new ItemBuilder(Material.STONE)),
    ARMOR_STAND (IEntityArmorStandPet.class, Material.ARMOR_STAND),
    AXOLOTL (IEntityAxolotlPet.class, "5c138f401c67fc2e1e387d9c90a9691772ee486e8ddbf2ed375fc8348746f936"), //TODO: 1.17 Mob
    BAT (IEntityBatPet.class, "9e99deef919db66ac2bd28d6302756ccd57c7f8b12b9dca8f41c3e0a04ac1cc"),
    BEE (IEntityBeePet.class, "fa2cb74c13245d3ce9bacc8b1600af02fd7c91f501feaf97364e1f8b6f04f47f"),
    BLAZE (IEntityBlazePet.class, "b78ef2e4cf2c41a2d14bfde9caff10219f5b1bf5b35a49eb51c6467882cb5f0"),
    CAT (IEntityCatPet.class, "6b253fc6b656988453a2d7138fca4d1f2752f47691f0c434e432183771cfe1"),
    CAVE_SPIDER (IEntityCaveSpiderPet.class, "5617f7dd5ed16f3bd186440517cd440a170015b1cc6fcb2e993c05de33f"),
    CHICKEN (IEntityChickenPet.class, "1638469a599ceef7207537603248a9ab11ff591fd378bea4735b346a7fae893"),
    COD (IEntityCodPet.class, "7892d7dd6aadf35f86da27fb63da4edda211df96d2829f691462a4fb1cab0"),
    COW (IEntityCowPet.class, "c5a9cd58d4c67bccc8fb1f5f756a2d381c9ffac2924b7f4cb71aa9fa13fb5c"),
    CREEPER (IEntityCreeperPet.class, Material.CREEPER_HEAD),
    DOLPHIN (IEntityDolphinPet.class, "8e9688b950d880b55b7aa2cfcd76e5a0fa94aac6d16f78e833f7443ea29fed3"),
    DONKEY (IEntityDonkeyPet.class, "399bb50d1a214c394917e25bb3f2e20698bf98ca703e4cc08b42462df309d6e6"),
    DROWNED (IEntityDrownedPet.class, "c3f7ccf61dbc3f9fe9a6333cde0c0e14399eb2eea71d34cf223b3ace22051"),
    @LargePet ELDER_GUARDIAN (IEntityElderGuardianPet.class, "1c797482a14bfcb877257cb2cff1b6e6a8b8413336ffb4c29a6139278b436b"),
    ENDERMAN (IEntityEndermanPet.class, "96c0b36d53fff69a49c7d6f3932f2b0fe948e032226d5e8045ec58408a36e951"),
    ENDERMITE (IEntityEndermitePet.class, "5bc7b9d36fb92b6bf292be73d32c6c5b0ecc25b44323a541fae1f1e67e393a3e"),
    EVOKER (IEntityEvokerPet.class, "d954135dc82213978db478778ae1213591b93d228d36dd54f1ea1da48e7cba6"),
    FOX (IEntityFoxPet.class, "d8954a42e69e0881ae6d24d4281459c144a0d5a968aed35d6d3d73a3c65d26a"),
    @LargePet GHAST (IEntityGhastPet.class, "7a8b714d32d7f6cf8b37e221b758b9c599ff76667c7cd45bbc49c5ef19858646"),
    @LargePet GIANT (IEntityGiantPet.class, Material.ZOMBIE_HEAD),
    GLOW_SQUID (IEntityGlowSquidPet.class, "3e94a1bb1cb00aaa153a74daf4b0eea20b8974522fe9901eb55aef478ebeff0d"), // TODO: 1.17 Mob
    GOAT (IEntityGoatPet.class, "957607099d06b7a8b1327093cd0a488be7c9f50b6121b22151271b59170f3c21"), // TODO: 1.17 Mob
    GUARDIAN (IEntityGuardianPet.class, "a0bf34a71e7715b6ba52d5dd1bae5cb85f773dc9b0d457b4bfc5f9dd3cc7c94"),
    HOGLIN (IEntityHoglinPet.class, "9bb9bc0f01dbd762a08d9e77c08069ed7c95364aa30ca1072208561b730e8d75"),
    HORSE (IEntityHorsePet.class, "628d1ab4be1e28b7b461fdea46381ac363a7e5c3591c9e5d2683fbe1ec9fcd3"),
    HUSK (IEntityHuskPet.class, "d674c63c8db5f4ca628d69a3b1f8a36e29d8fd775e1a6bdb6cabb4be4db121"),
    ILLUSIONER (IEntityIllusionerPet.class, "512512e7d016a2343a7bff1a4cd15357ab851579f1389bd4e3a24cbeb88b"),
    IRON_GOLEM (IEntityIronGolemPet.class, "89091d79ea0f59ef7ef94d7bba6e5f17f2f7d4572c44f90f76c4819a714"),
    LLAMA (IEntityLlamaPet.class, "818cd457fbaf327fa39f10b5b36166fd018264036865164c02d9e5ff53f45"),
    MAGMA_CUBE (IEntityMagmaCubePet.class, "38957d5023c937c4c41aa2412d43410bda23cf79a9f6ab36b76fef2d7c429"),
    MOOSHROOM (IEntityMooshroomPet.class, "2b52841f2fd589e0bc84cbabf9e1c27cb70cac98f8d6b3dd065e55a4dcb70d77"),
    MULE (IEntityMulePet.class, "46dcda265e57e4f51b145aacbf5b59bdc6099ffd3cce0a661b2c0065d80930d8"),
    OCELOT (IEntityOcelotPet.class, "5657cd5c2989ff97570fec4ddcdc6926a68a3393250c1be1f0b114a1db1"),
    PANDA (IEntityPandaPet.class, "dca096eea506301bea6d4b17ee1605625a6f5082c71f74a639cc940439f47166"),
    PARROT (IEntityParrotPet.class, "a4ba8d66fecb1992e94b8687d6ab4a5320ab7594ac194a2615ed4df818edbc3"),
    PHANTOM (IEntityPhantomPet.class, "746830da5f83a3aaed838a99156ad781a789cfcf13e25beef7f54a86e4fa4"),
    PIG (IEntityPigPet.class, "621668ef7cb79dd9c22ce3d1f3f4cb6e2559893b6df4a469514e667c16aa4"),
    PIGLIN (IEntityPiglinPet.class, "9f18107d275f1cb3a9f973e5928d5879fa40328ff3258054db6dd3e7c0ca6330"),
    PIGLIN_BRUTE (IEntityPiglinBrutePet.class, "3e300e9027349c4907497438bac29e3a4c87a848c50b34c21242727b57f4e1cf"),
    PILLAGER (IEntityPillagerPet.class, "4aee6bb37cbfc92b0d86db5ada4790c64ff4468d68b84942fde04405e8ef5333"),
    POLARBEAR (IEntityPolarBearPet.class, "c4fe926922fbb406f343b34a10bb98992cee4410137d3f88099427b22de3ab90"),
    PUFFERFISH(IEntityPufferFishPet.class, "17152876bc3a96dd2a2299245edb3beef647c8a56ac8853a687c3e7b5d8bb"),
    RABBIT (IEntityRabbitPet.class, "ffecc6b5e6ea5ced74c46e7627be3f0826327fba26386c6cc7863372e9bc"),
    @LargePet RAVAGER (IEntityRavagerPet.class, "cd20bf52ec390a0799299184fc678bf84cf732bb1bd78fd1c4b441858f0235a8"),
    SALMON (IEntitySalmonPet.class, "8aeb21a25e46806ce8537fbd6668281cf176ceafe95af90e94a5fd84924878"),
    SHEEP (IEntitySheepPet.class, "f31f9ccc6b3e32ecf13b8a11ac29cd33d18c95fc73db8a66c5d657ccb8be70"),
    SHULKER (IEntityShulkerPet.class, "1433a4b73273a64c8ab2830b0fff777a61a488c92f60f83bfb3e421f428a44"),
    SILVERFISH (IEntitySilverfishPet.class, "d06310a8952b265c6e6bed4348239ddea8e5482c8c68be6fff981ba8056bf2e"),
    SKELETON (IEntitySkeletonPet.class, Material.SKELETON_SKULL),
    SKELETON_HORSE (IEntitySkeletonHorsePet.class, "47effce35132c86ff72bcae77dfbb1d22587e94df3cbc2570ed17cf8973a"),
    SLIME (IEntitySlimePet.class, "bb13133a8fb4ef00b71ef9bab639a66fbc7d5cffcc190c1df74bf2161dfd3ec7"),
    SNOWMAN (IEntitySnowmanPet.class, "9aed9fe4ed0893e325f4fbd32b093c1cc562cba27ff73359d356f1c288e441f9"),
    SPIDER (IEntitySpiderPet.class, "c87a96a8c23b83b32a73df051f6b84c2ef24d25ba4190dbe74f11138629b5aef"),
    SQUID (IEntitySquidPet.class, "01433be242366af126da434b8735df1eb5b3cb2cede39145974e9c483607bac"),
    STRAY (IEntityStrayPet.class, "2c5097916bc0565d30601c0eebfeb287277a34e867b4ea43c63819d53e89ede7"),
    STRIDER (IEntityStriderPet.class, "cb7ffdda656c68d88851a8e05b48cd2493773ffc4ab7d64e9302229fe3571059"),
    TRADER_LLAMA (IEntityTraderLlamaPet.class, "8424780b3c5c5351cf49fb5bf41fcb289491df6c430683c84d7846188db4f84d"),
    TROPICAL_FISH (IEntityTropicalFishPet.class, "d6dd5e6addb56acbc694ea4ba5923b1b25688178feffa72290299e2505c97281"),
    TURTLE (IEntityTurtlePet.class, "0a4050e7aacc4539202658fdc339dd182d7e322f9fbcc4d5f99b5718a"),
    VEX (IEntityVexPet.class, "c2ec5a516617ff1573cd2f9d5f3969f56d5575c4ff4efefabd2a18dc7ab98cd"),
    VILLAGER (IEntityVillagerPet.class, "41b830eb4082acec836bc835e40a11282bb51193315f91184337e8d3555583"),
    VINDICATOR (IEntityVindicatorPet.class, "6deaec344ab095b48cead7527f7dee61b063ff791f76a8fa76642c8676e2173"),
    WANDERING_TRADER (IEntityWanderingTraderPet.class, "5f1379a82290d7abe1efaabbc70710ff2ec02dd34ade386bc00c930c461cf932"),
    @LargePet WARDEN (IEntityWardenPet.class, "1dfd13ca08bf973bfef0293d770704a11ef5a9fe20d40671fb066724d3e18d8"), // TODO: 1.17 Mob
    WITCH (IEntityWitchPet.class, "20e13d18474fc94ed55aeb7069566e4687d773dac16f4c3f8722fc95bf9f2dfa"),
    @LargePet WITHER (IEntityWitherPet.class, "cdf74e323ed41436965f5c57ddf2815d5332fe999e68fbb9d6cf5c8bd4139f"),
    WITHER_SKELETON (IEntityWitherSkeletonPet.class, Material.WITHER_SKELETON_SKULL),
    WOLF (IEntityWolfPet.class, "24d7727f52354d24a64bd6602a0ce71a7b484d05963da83b470360faa9ceab5f"),
    ZOGLIN (IEntityZoglinPet.class, "3c8c7c5d0556cd6629716e39188b21e7c0477479f242587bf19e0bc76b322551"),
    ZOMBIE (IEntityZombiePet.class, Material.ZOMBIE_HEAD),
    ZOMBIE_HORSE (IEntityZombieHorsePet.class, "d22950f2d3efddb18de86f8f55ac518dce73f12a6e0f8636d551d8eb480ceec"),
    ZOMBIE_VILLAGER (IEntityZombieVillagerPet.class, "e5e08a8776c1764c3fe6a6ddd412dfcb87f41331dad479ac96c21df4bf3ac89c"),
    ZOMBIFIED_PIGLIN (IEntityPigZombiePet.class, "7eabaecc5fae5a8a49c8863ff4831aaa284198f1a2398890c765e0a8de18da8c");

    private final ItemBuilder builder;
    private final Class<? extends IEntityPet> entityClass;
    PetType(ItemBuilder builder){
        this(null, builder);
    }
    PetType(Class<? extends IEntityPet> entityClass, Material material) {
        this (entityClass, new ItemBuilder(material));
    }
    PetType(Class<? extends IEntityPet> entityClass, String textureID) {
        this (entityClass, new ItemBuilder(Material.PLAYER_HEAD).setTexture("http://textures.minecraft.net/texture/"+textureID));
    }
    PetType(Class<? extends IEntityPet> entityClass, ItemBuilder builder) {
        this.entityClass = entityClass;
        this.builder = builder.withName(ChatColor.GREEN+ Capitalise.capitalize(name().toLowerCase().replace("_", " ")));
    }

    public ItemBuilder getBuilder() {
        return builder;
    }

    public Class<? extends IEntityPet> getEntityClass() {
        return entityClass;
    }

    public String getName () {
        return name().toLowerCase();
    }

    public EntityType getEntityType () {
        return EntityType.valueOf(name());
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
                if (annotation instanceof EnumVersion) {
                    EnumVersion support = (EnumVersion)annotation;
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
        } catch (NoSuchFieldException ignored) {}
        return false;
    }

    public static Optional<PetType> getPetType (String name) {
        try {
            return Optional.of(valueOf(PetType.class, name.toUpperCase().trim()));
        }catch (Exception ignored) {}
        return Optional.empty();
    }
}
