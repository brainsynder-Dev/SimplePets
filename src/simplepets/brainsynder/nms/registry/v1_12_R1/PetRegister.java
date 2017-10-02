package simplepets.brainsynder.nms.registry.v1_12_R1;

import net.minecraft.server.v1_12_R1.*;
import simplepets.brainsynder.nms.IEntityRegistry;
import simplepets.brainsynder.nms.entities.v1_12_R1.impossamobs.EntityShulkerPet;
import simplepets.brainsynder.nms.entities.v1_12_R1.list.*;
import simplepets.brainsynder.wrapper.EntityWrapper;

public enum PetRegister implements IEntityRegistry<Entity> {
    BLAZE(NMSUtils.Type.BLAZE, EntityWrapper.BLAZE, EntityBlazePet.class, EntityBlaze.class),
    BAT(NMSUtils.Type.BAT, EntityWrapper.BAT, EntityBatPet.class, EntityBat.class),
    CHICKEN(NMSUtils.Type.CHICKEN, EntityWrapper.CHICKEN, EntityChickenPet.class, EntityChicken.class),
    COW(NMSUtils.Type.COW, EntityWrapper.COW, EntityCowPet.class, EntityCow.class),
    CREEPER(NMSUtils.Type.CREEPER, EntityWrapper.CREEPER, EntityCreeperPet.class, EntityCreeper.class),
    ENDERMAN(NMSUtils.Type.ENDERMAN, EntityWrapper.ENDERMAN, EntityEndermanPet.class, EntityEnderman.class),
    GIANT(NMSUtils.Type.GIANT, EntityWrapper.GIANT, EntityGiantPet.class, EntityGiantZombie.class),
    HORSE(NMSUtils.Type.HORSE, EntityWrapper.HORSE, EntityHorsePet.class, EntityHorse.class),
    IRON_GOLEM(NMSUtils.Type.IRON_GOLEM, EntityWrapper.IRON_GOLEM, EntityIronGolemPet.class, EntityIronGolem.class),
    MOOSHROOM(NMSUtils.Type.MOOSHROOM, EntityWrapper.MUSHROOM_COW, EntityMooshroomPet.class, EntityMushroomCow.class),
    OCELOT(NMSUtils.Type.OCELOT, EntityWrapper.OCELOT, EntityOcelotPet.class, EntityOcelot.class),
    PIG(NMSUtils.Type.PIG, EntityWrapper.PIG, EntityPigPet.class, EntityPig.class),
    PIGMAN(NMSUtils.Type.ZOMBIE_PIGMAN, EntityWrapper.PIG_ZOMBIE, EntityPigmanPet.class, EntityPigZombie.class),
    RABBIT(NMSUtils.Type.RABBIT, EntityWrapper.RABBIT, EntityRabbitPet.class, EntityRabbit.class),
    SHEEP(NMSUtils.Type.SHEEP, EntityWrapper.SHEEP, EntitySheepPet.class, EntitySheep.class),
    SILVERFISH(NMSUtils.Type.SILVERFISH, EntityWrapper.SILVERFISH, EntitySilverfishPet.class, EntitySilverfish.class),
    SKELETON(NMSUtils.Type.SKELETON, EntityWrapper.SKELETON, EntitySkeletonPet.class, EntitySkeleton.class),
    SNOWMAN(NMSUtils.Type.SNOWMAN, EntityWrapper.SNOWMAN, EntitySnowmanPet.class, EntitySnowman.class),
    SPIDER(NMSUtils.Type.SPIDER, EntityWrapper.SPIDER, EntitySpiderPet.class, EntitySpider.class),
    VILLAGER(NMSUtils.Type.VILLAGER, EntityWrapper.VILLAGER, EntityVillagerPet.class, EntityVillager.class),
    WOLF(NMSUtils.Type.WOLF, EntityWrapper.WOLF, EntityWolfPet.class, EntityWolf.class),
    ZOMBIE(NMSUtils.Type.ZOMBIE, EntityWrapper.ZOMBIE, EntityZombiePet.class, EntityZombie.class),
    SQUID(NMSUtils.Type.SQUID, EntityWrapper.SQUID, EntitySquidPet.class, EntitySquid.class),
    GHAST(NMSUtils.Type.GHAST, EntityWrapper.GHAST, EntityGhastPet.class, EntityGhast.class),
    ENDERMITE(NMSUtils.Type.ENDERMITE, EntityWrapper.ENDERMITE, EntityEndermitePet.class, EntityEndermite.class),
    WITHER(NMSUtils.Type.WITHER, EntityWrapper.WITHER, EntityWitherPet.class, EntityWither.class),
    WITCH(NMSUtils.Type.WITCH, EntityWrapper.WITCH, EntityWitchPet.class, EntityWitch.class),
    POLAR_BEAR(NMSUtils.Type.POLARBEAR, EntityWrapper.POLAR_BEAR, EntityPolarBearPet.class, EntityPolarBear.class),
    GUARDIAN(NMSUtils.Type.GUARDIAN, EntityWrapper.GUARDIAN, EntityGuardianPet.class, EntityGuardian.class),
    SLIME(NMSUtils.Type.SLIME, EntityWrapper.SLIME, EntitySlimePet.class, EntitySlime.class),
    STRAY(NMSUtils.Type.STRAY, EntityWrapper.STRAY, EntityStrayPet.class, EntitySkeletonStray.class),
    VEX(NMSUtils.Type.VEX, EntityWrapper.VEX, EntityVexPet.class, EntityVex.class),
    LLAMA(NMSUtils.Type.LLAMA, EntityWrapper.LLAMA, EntityLlamaPet.class, EntityLlama.class),
    EVOKER(NMSUtils.Type.EVOKER, EntityWrapper.EVOKER, EntityEvokerPet.class, EntityEvoker.class),
    VINDICATOR(NMSUtils.Type.VINDICATOR, EntityWrapper.VINDICATOR, EntityVindicatorPet.class, EntityVindicator.class),
    HUSK(NMSUtils.Type.HUSK, EntityWrapper.HUSK, EntityHuskPet.class, EntityZombieHusk.class),
    MULE(NMSUtils.Type.MULE, EntityWrapper.MULE, EntityMulePet.class, EntityHorseMule.class),
    SKELETON_HORSE(NMSUtils.Type.SKELETON_HORSE, EntityWrapper.SKELETON_HORSE, EntitySkeletonHorsePet.class, EntityHorseSkeleton.class),
    ZOMBIE_HORSE(NMSUtils.Type.ZOMBIE_HORSE, EntityWrapper.ZOMBIE_HORSE, EntityZombieHorsePet.class, EntityHorseZombie.class),
    MAGMA_CUBE(NMSUtils.Type.MAGMACUBE, EntityWrapper.MAGMA_CUBE, EntityMagmaCubePet.class, EntityMagmaCube.class),
    WITHER_SKELETON(NMSUtils.Type.WITHER_SKELETON, EntityWrapper.WITHER_SKELETON, EntityWitherSkeletonPet.class, EntitySkeletonWither.class),
    ELDER_GUARDIAN(NMSUtils.Type.ELDER_GUARDIAN, EntityWrapper.ELDER_GUARDIAN, EntityElderGuardianPet.class, EntityGuardianElder.class),
    CAVE_SPIDER(NMSUtils.Type.CAVE_SPIDER, EntityWrapper.CAVE_SPIDER, EntityCaveSpiderPet.class, EntityCaveSpider.class),
    SHULKER(NMSUtils.Type.SHULKER, EntityWrapper.SHULKER, EntityShulkerPet.class, EntityShulker.class),
    PARROT(NMSUtils.Type.PARROT, EntityWrapper.PARROT, EntityParrotPet.class, EntityParrot.class),
    ILLUSIONER(NMSUtils.Type.ILLUSIONER, EntityWrapper.ILLUSIONER, EntityIllusionerPet.class, EntityIllagerIllusioner.class);


    private Class<? extends EntityLiving> nms;
    private Class<? extends EntityLiving> pet;
    private EntityWrapper wrapper;
    private NMSUtils.Type type;

    PetRegister(NMSUtils.Type type, EntityWrapper wrapper, Class<? extends EntityLiving> pet, Class<? extends EntityLiving> nms) {
        this.wrapper = wrapper;
        this.nms = nms;
        this.pet = pet;
        this.type = type;
    }

    @Override
    public void registerPet() {
        NMSUtils.registerEntity("pet_" + wrapper.getName(), type, pet, false);
    }

    @Override
    public void unRegisterPet() {
    }

    @Override
    public Class<? extends EntityLiving> getNMS() {
        return nms;
    }

    @Override
    public Class<? extends EntityLiving> getPet() {
        return pet;
    }

    @Override
    public EntityWrapper getEntityWrapper() {
        return wrapper;
    }
}
