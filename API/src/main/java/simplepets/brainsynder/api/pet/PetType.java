package simplepets.brainsynder.api.pet;

import lib.brainsynder.EnumVersion;
import lib.brainsynder.ServerVersion;
import lib.brainsynder.SupportedVersion;
import org.bukkit.entity.EntityType;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.ambient.IEntityArmorStandPet;
import simplepets.brainsynder.api.entity.hostile.*;
import simplepets.brainsynder.api.entity.passive.*;

import java.lang.annotation.Annotation;
import java.util.Optional;

public enum PetType {
    UNKNOWN,
    ARMOR_STAND (IEntityArmorStandPet.class),
    AXOLOTL (IEntityAxolotlPet.class), //TODO
    BAT (IEntityBatPet.class),
    BEE (IEntityBeePet.class),
    BLAZE (IEntityBlazePet.class),
    CAT (IEntityCatPet.class),
    CAVE_SPIDER (IEntityCaveSpiderPet.class),
    CHICKEN (IEntityChickenPet.class),
    COD (IEntityCodPet.class),
    COW (IEntityCowPet.class),
    CREEPER (IEntityCreeperPet.class),
    DOLPHIN (IEntityDolphinPet.class),
    DONKEY (IEntityDonkeyPet.class),
    DROWNED (IEntityDrownedPet.class),
    @LargePet ELDER_GUARDIAN (IEntityElderGuardianPet.class),
    ENDERMAN (IEntityEndermanPet.class),
    ENDERMITE (IEntityEndermitePet.class),
    EVOKER (IEntityEvokerPet.class),
    FOX (IEntityFoxPet.class),
    @LargePet GHAST (IEntityGhastPet.class),
    @LargePet GIANT (IEntityGiantPet.class),
    GLOW_SQUID (IEntityGlowSquidPet.class), // TODO
    GUARDIAN (IEntityGuardianPet.class),
    HOGLIN (IEntityHoglinPet.class),
    HORSE (IEntityHorsePet.class),
    HUSK (IEntityHuskPet.class),
    ILLUSIONER (IEntityIllusionerPet.class),
    IRON_GOLEM (IEntityIronGolemPet.class),
    LLAMA (IEntityLlamaPet.class),
    MAGMA_CUBE (IEntityMagmaCubePet.class),
    MOOSHROOM (IEntityMooshroomPet.class),
    MULE (IEntityMulePet.class),
    OCELOT (IEntityOcelotPet.class),
    PANDA (IEntityPandaPet.class),
    PARROT (IEntityParrotPet.class),
    PHANTOM (IEntityPhantomPet.class),
    PIG (IEntityPigPet.class),
    PIGLIN (IEntityPiglinPet.class),
    PIGLIN_BRUTE (IEntityPiglinBrutePet.class),
    PILLAGER (IEntityPillagerPet.class),
    POLARBEAR (IEntityPolarBearPet.class),
    PUFFERFISH(IEntityPufferFishPet.class),
    RABBIT (IEntityRabbitPet.class),
    @LargePet RAVAGER (IEntityRavagerPet.class),
    SALMON (IEntitySalmonPet.class),
    SHEEP (IEntitySheepPet.class),
    SHULKER (IEntityShulkerPet.class),
    SILVERFISH (IEntitySilverfishPet.class),
    SKELETON (IEntitySkeletonPet.class),
    SKELETON_HORSE (IEntitySkeletonHorsePet.class),
    SLIME (IEntitySlimePet.class),
    SNOWMAN (IEntitySnowmanPet.class),
    SPIDER (IEntitySpiderPet.class),
    SQUID (IEntitySquidPet.class),
    STRAY (IEntityStrayPet.class),
    STRIDER (IEntityStriderPet.class),
    TRADER_LLAMA (IEntityTraderLlamaPet.class),
    TROPICAL_FISH (IEntityTropicalFishPet.class),
    TURTLE (IEntityTurtlePet.class),
    VEX (IEntityVexPet.class),
    VILLAGER (IEntityVillagerPet.class),
    VINDICATOR (IEntityVindicatorPet.class),
    WANDERING_TRADER (IEntityWanderingTraderPet.class),
    WITCH (IEntityWitchPet.class),
    @LargePet WITHER (IEntityWitherPet.class),
    WITHER_SKELETON (IEntityWitherSkeletonPet.class),
    WOLF (IEntityWolfPet.class),
    ZOGLIN (IEntityZoglinPet.class),
    ZOMBIE (IEntityZombiePet.class),
    ZOMBIE_HORSE (IEntityZombieHorsePet.class),
    ZOMBIE_VILLAGER (IEntityZombieVillagerPet.class),
    ZOMBIFIED_PIGLIN (IEntityPigZombiePet.class);

    private Class<? extends IEntityPet> entityClass;
    PetType(){}
    PetType(Class<? extends IEntityPet> entityClass) {
        this.entityClass = entityClass;
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
