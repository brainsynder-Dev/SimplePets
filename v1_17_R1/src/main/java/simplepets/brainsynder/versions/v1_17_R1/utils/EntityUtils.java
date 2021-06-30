package simplepets.brainsynder.versions.v1_17_R1.utils;

import lib.brainsynder.reflection.Reflection;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerType;
import simplepets.brainsynder.api.wrappers.villager.BiomeType;
import simplepets.brainsynder.versions.v1_17_R1.entity.FakeEntity;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

public class EntityUtils {
    private static final Map<BiomeType, VillagerType> stored = new HashMap<>();
    private static final Map<VillagerType, BiomeType> storedInverted = new HashMap<>();
    private static Field jumpingField;

    static {
        try{
            FakeEntity fakeEntity = new FakeEntity();
            fakeEntity.setJumping(true);
            for(Field field : LivingEntity.class.getDeclaredFields()){
                if(!Modifier.isProtected(field.getModifiers())){
                    continue;
                }
                if(boolean.class.isAssignableFrom(field.getType())){
                    field.setAccessible(true);
                    if(field.getBoolean(fakeEntity)){
                        jumpingField = field;
                        break;
                    }
                    field.setAccessible(false);
                }
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    public static Field getJumpingField() {
        return jumpingField;
    }

    public static VillagerType getTypeFromBiome(BiomeType type) {
        if (stored.isEmpty()) initStores();
        return stored.get(type);
    }

    public static BiomeType getBiomeFromType(VillagerType type) {
        if (storedInverted.isEmpty()) initStores();
        return storedInverted.get(type);
    }

    public static VillagerType findType(String... names) {
        for (String value : names) {
            Field field = Reflection.getField(VillagerType.class, value);
            if (field == null) continue;

            Object object = null;
            try {
                object = field.get(null);
            } catch (IllegalAccessException e1) {
                e1.printStackTrace();
            }
            if (object != null)
                return (VillagerType) object;
        }
        return null;
    }

    private static void initStores () {
        VillagerType DESERT = VillagerType.DESERT,
                JUNGLE = VillagerType.JUNGLE,
                PLAINS = VillagerType.PLAINS,
                SAVANNA = VillagerType.SAVANNA,
                SNOW = VillagerType.SNOW,
                SWAMP = VillagerType.SWAMP,
                TAIGA = VillagerType.TAIGA;

        if (stored.isEmpty()) {
            stored.put(BiomeType.DESERT, DESERT);
            stored.put(BiomeType.JUNGLE, JUNGLE);
            stored.put(BiomeType.PLAINS, PLAINS);
            stored.put(BiomeType.SAVANNA, SAVANNA);
            stored.put(BiomeType.SNOW, SNOW);
            stored.put(BiomeType.SWAMP, SWAMP);
            stored.put(BiomeType.TAIGA, TAIGA);
        }

        if (storedInverted.isEmpty()) {
            storedInverted.put(DESERT, BiomeType.DESERT);
            storedInverted.put(JUNGLE, BiomeType.JUNGLE);
            storedInverted.put(PLAINS, BiomeType.PLAINS);
            storedInverted.put(SAVANNA, BiomeType.SAVANNA);
            storedInverted.put(SNOW,BiomeType.SNOW);
            storedInverted.put(SWAMP,BiomeType.SWAMP);
            storedInverted.put(TAIGA,BiomeType.TAIGA);
        }
    }



    public static VillagerProfession getProfession(simplepets.brainsynder.api.wrappers.villager.VillagerType type) {

        VillagerProfession profession = VillagerProfession.NONE;
        switch (type) {
            case ARMORER:
                profession = VillagerProfession.ARMORER;
                break;
            case BUTCHER:
                profession = VillagerProfession.BUTCHER;
                break;
            case CARTOGRAPHER:
                profession = VillagerProfession.CARTOGRAPHER;
                break;
            case CLERIC:
                profession = VillagerProfession.CLERIC;
                break;
            case FARMER:
                profession = VillagerProfession.FARMER;
                break;
            case FISHERMAN:
                profession = VillagerProfession.FISHERMAN;
                break;
            case FLETCHER:
                profession = VillagerProfession.FLETCHER;
                break;
            case LEATHERWORKER:
                profession = VillagerProfession.LEATHERWORKER;
                break;
            case LIBRARIAN:
                profession = VillagerProfession.LIBRARIAN;
                break;
            case MASON:
                profession = VillagerProfession.MASON;
                break;
            case NITWIT:
                profession = VillagerProfession.NITWIT;
                break;
            case SHEPHERD:
                profession = VillagerProfession.SHEPHERD;
                break;
            case TOOLSMITH:
                profession = VillagerProfession.TOOLSMITH;
                break;
            case WEAPONSMITH:
                profession = VillagerProfession.WEAPONSMITH;
                break;
        }
        return profession;
    }
}
