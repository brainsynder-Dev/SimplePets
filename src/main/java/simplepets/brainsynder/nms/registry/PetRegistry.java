package simplepets.brainsynder.nms.registry;

import org.bukkit.Bukkit;
import simple.brainsynder.utils.ServerVersion;
import simplepets.brainsynder.reflection.ReflectionUtil;
import simplepets.brainsynder.wrapper.EntityWrapper;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * @Deprecated Will be removed when MC version 1.13 is released
 */
@Deprecated
public class PetRegistry {
    public static String getVersion() {
        return Bukkit.getServer().getClass().getPackage().getName().substring(23);
    }

    private static void registerEntity(String name, int id, Class<?> customClass) {
        if (ServerVersion.getVersion().getIntVersion() >= 111) return;

        if (customClass != null) {
            try {
                Field[] e = Class.forName("net.minecraft.server." + ReflectionUtil.getVersion() + ".EntityTypes").getDeclaredFields();
                for (Field field : e) {
                    if (!field.isAccessible()) {
                        field.setAccessible(true);
                    }

                    switch (field.getName()) {
                        case "d":
                            ((Map) field.get(null)).put(customClass, name);
                            break;
                        case "f":
                            ((Map) field.get(null)).put(customClass, id);
                    }
                }
            } catch (Exception ignored) {}
        }
    }

    private static void unregisterEntity(Class<?> customClass) {
        if (ServerVersion.getVersion().getIntVersion() >= 111)
            return;

        if (customClass != null) {
            try {
                Field[] e = Class.forName("net.minecraft.server." + ReflectionUtil.getVersion() + ".EntityTypes").getDeclaredFields();
                for (Field field : e) {
                    if (!field.isAccessible()) {
                        field.setAccessible(true);
                    }

                    String var8 = field.getName();
                    switch (var8) {
                        case "Wololo":
                            ((Map) field.get(null)).remove(customClass);
                            break;
                        case "f":
                            ((Map) field.get(null)).remove(customClass);
                    }
                }
            } catch (Exception var10) {
                var10.printStackTrace();
            }
        }
    }

    public void registerAll() {
        for (PetRegistry.List entity : List.values()) {
            if (entity.supported)
                registerEntity(entity.name, entity.id, entity.customClass);
        }
    }

    public void unregisterAll() {
        for (PetRegistry.List entity : List.values()) {
            if (entity.supported)
                unregisterEntity(entity.customClass);
        }
    }

    public void a(Class paramClass, String paramString, int paramInt) {
        Map<String, Class> mapC = getMap(getField("c", ReflectionUtil.getNmsClass("EntityTypes")));
        Map<Class, String> mapD = getMap(getField("d", ReflectionUtil.getNmsClass("EntityTypes")));
        Map<Integer, Class> mapE = getMap(getField("e", ReflectionUtil.getNmsClass("EntityTypes")));
        Map<Class, Integer> mapF = getMap(getField("f", ReflectionUtil.getNmsClass("EntityTypes")));
        Map<String, Integer> mapG = getMap(getField("g", ReflectionUtil.getNmsClass("EntityTypes")));
        mapC.put(paramString, paramClass);
        mapD.put(paramClass, paramString);
        mapE.put(paramInt, paramClass);
        mapF.put(paramClass, paramInt);
        mapG.put(paramString, paramInt);
    }

    private <T> T getField(String fieldName, Class fieldClass) {
        try {
            Field tr = fieldClass.getDeclaredField(fieldName);
            tr.setAccessible(true);
            return (T) tr.get(null);
        } catch (Throwable var5) {
            return null;
        }
    }

    private Map getMap(Object obj) {
        return (Map) obj;
    }

    public enum List {
        BLAZE(18, EntityWrapper.BLAZE, "EntityBlazePet", "EntityBlaze"),
        BAT(18, EntityWrapper.BAT, "EntityBatPet", "EntityBat"),
        CHICKEN(18, EntityWrapper.CHICKEN, "EntityChickenPet", "EntityChicken"),
        COW(18, EntityWrapper.COW, "EntityCowPet", "EntityCow"),
        CREEPER(18, EntityWrapper.CREEPER, "EntityCreeperPet", "EntityCreeper"),
        ENDERMAN(18, EntityWrapper.ENDERMAN, "EntityEndermanPet", "EntityEnderman"),
        GIANT(18, EntityWrapper.GIANT, "EntityGiantPet", "EntityGiantZombie"),
        HORSE(18, EntityWrapper.HORSE, "EntityHorsePet", "EntityHorse"),
        IRON_GOLEM(18, EntityWrapper.IRON_GOLEM, "EntityIronGolemPet", "EntityIronGolem"),
        MOOSHROOM(18, EntityWrapper.MUSHROOM_COW, "EntityMooshroomPet", "EntityMushroomCow"),
        OCELOT(18, EntityWrapper.OCELOT, "EntityOcelotPet", "EntityOcelot"),
        PIG(18, EntityWrapper.PIG, "EntityPigPet", "EntityPig"),
        PIGMAN(18, EntityWrapper.PIG_ZOMBIE, "EntityPigmanPet", "EntityPigZombie"),
        RABBIT(18, EntityWrapper.RABBIT, "EntityRabbitPet", "EntityRabbit"),
        SHEEP(18, EntityWrapper.SHEEP, "EntitySheepPet", "EntitySheep"),
        SILVERFISH(18, EntityWrapper.SILVERFISH, "EntitySilverfishPet", "EntitySilverfish"),
        SKELETON(18, EntityWrapper.SKELETON, "EntitySkeletonPet", "EntitySkeleton"),
        SNOWMAN(18, EntityWrapper.SNOWMAN, "EntitySnowmanPet", "EntitySnowman"),
        SPIDER(18, EntityWrapper.SPIDER, "EntitySpiderPet", "EntitySpider"),
        VILLAGER(18, EntityWrapper.VILLAGER, "EntityVillagerPet", "EntityVillager"),
        WOLF(18, EntityWrapper.WOLF, "EntityWolfPet", "EntityWolf"),
        ZOMBIE(18, EntityWrapper.ZOMBIE, "EntityZombiePet", "EntityZombie"),
        SQUID(18, EntityWrapper.SQUID, "EntitySquidPet", "EntitySquid"),
        GHAST(18, EntityWrapper.GHAST, "EntityGhastPet", "EntityGhast"),
        ENDERMITE(18, EntityWrapper.ENDERMITE, "EntityEndermitePet", "EntityEndermite"),
        WITHER(18, EntityWrapper.WITHER, "EntityWitherPet", "EntityWither"),
        WITCH(18, EntityWrapper.WITCH, "EntityWitchPet", "EntityWitch"),
        POLAR_BEAR(110, EntityWrapper.POLAR_BEAR, "EntityPolarBearPet", "EntityPolarBear"),
        GUARDIAN(110, EntityWrapper.GUARDIAN, "EntityGuardianPet", "EntityGuardian"),
        SLIME(110, EntityWrapper.SLIME, "EntitySlimePet", "EntitySlime"),
        STRAY(111, EntityWrapper.STRAY, "EntityStrayPet", "EntitySkeletonStray"),
        VEX(111, EntityWrapper.VEX, "EntityVexPet", "EntityVex"),
        LLAMA(111, EntityWrapper.LLAMA, "EntityLlamaPet", "EntityLlama"),
        EVOKER(111, EntityWrapper.EVOKER, "EntityEvokerPet", "EntityEvoker"),
        VINDICATOR(111, EntityWrapper.VINDICATOR, "EntityVindicatorPet", "EntityVindicator"),
        HUSK(111, EntityWrapper.HUSK, "EntityHuskPet", "EntityZombieHusk"),
        MULE(111, EntityWrapper.MULE, "EntityMulePet", "EntityHorseMule"),
        SKELETON_HORSE(111, EntityWrapper.SKELETON_HORSE, "EntitySkeletonHorsePet", "EntityHorseSkeleton"),
        ZOMBIE_HORSE(111, EntityWrapper.ZOMBIE_HORSE, "EntityZombieHorsePet", "EntityHorseZombie"),
        MAGMA_CUBE(111, EntityWrapper.MAGMA_CUBE, "EntityMagmaCubePet", "EntityMagmaCube"),
        WITHER_SKELETON(111, EntityWrapper.WITHER_SKELETON, "EntityWitherSkeletonPet", "EntitySkeletonWither"),
        ELDER_GUARDIAN(111, EntityWrapper.ELDER_GUARDIAN, "EntityElderGuardianPet", "EntityGuardianElder"),
        CAVE_SPIDER(111, EntityWrapper.CAVE_SPIDER, "EntityCaveSpiderPet", "EntityCaveSpider"),
        SHULKER(111, EntityWrapper.SHULKER, "EntityShulkerPet", "EntityShulker");

        private String name;
        private String customClassName;
        private String nmsname;
        private int id;
        private Class<?> customClass;
        private boolean supported;

        List(int id, EntityWrapper entityWrapper, String petnmsName, String nmsName) {
            supported = (id <= ReflectionUtil.getVersionInt());
            if (supported) {
                customClassName = petnmsName;
                this.customClass = ReflectionUtil.getPetNMSClass(petnmsName);
                if (customClass != null) {
                    this.name = entityWrapper.getName();
                    this.id = entityWrapper.getTypeId();
                    nmsname = nmsName;
                }
            }
        }

        public boolean isSupported() {
            return supported;
        }

        public Class<?> getCustomClass() {
            return customClass;
        }

        public String getCustomClassName() {
            return customClassName;
        }

        public Class<?> getNMS() {
            return ReflectionUtil.getNmsClass(nmsname);
        }

        public String getName() {
            return name;
        }

        public int getId() {
            return id;
        }
    }
}
