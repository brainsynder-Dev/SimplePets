package simplepets.brainsynder.api.entity.misc;


import simplepets.brainsynder.api.wrappers.villager.BiomeType;
import simplepets.brainsynder.api.wrappers.villager.VillagerData;
import simplepets.brainsynder.api.wrappers.villager.VillagerLevel;
import simplepets.brainsynder.api.wrappers.villager.VillagerType;

public interface IProfession {
    /**
     * This gets used for 1.14+
     */
    default VillagerData getVillagerData () {
        return VillagerData.getDefault();
    }
    default VillagerType getVillagerType() {
        return getVillagerData().getType();
    }
    default BiomeType getBiome() {
        return getVillagerData().getBiome();
    }
    default VillagerLevel getMasteryLevel() {
        return getVillagerData().getLevel();
    }

    /**
     * This gets used for 1.14+
     */
    default void setVillagerData (VillagerData data){}
    default void setVillagerType (VillagerType type){
        setVillagerData(getVillagerData().withType(type));
    }
    default void setBiome (BiomeType biome){
        setVillagerData(getVillagerData().withBiome(biome));
    }
    default void setMasteryLevel(VillagerLevel level){
        setVillagerData(getVillagerData().withLevel(level));
    }
}
