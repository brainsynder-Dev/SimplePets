package simplepets.brainsynder.api.pet.data.villager;

import simplepets.brainsynder.api.Namespace;
import simplepets.brainsynder.api.entity.passive.IEntityVillagerPet;
import simplepets.brainsynder.api.pet.PetData;
import simplepets.brainsynder.api.wrappers.villager.BiomeType;

@Namespace(namespace = "biome")
public class VillagerBiomeData extends PetData<IEntityVillagerPet> {
    public VillagerBiomeData() {
        for (BiomeType type : BiomeType.values()) {
            addDefaultItem(type.name(), type.getIcon()
                    .withName("&#c8c8c8{name}: &a"+type.name()));
        }
    }

    @Override
    public Object getDefaultValue() {
        return BiomeType.PLAINS;
    }

    @Override
    public void onLeftClick(IEntityVillagerPet entity) {
        entity.setBiome(BiomeType.getNext(entity.getBiome()));
    }

    @Override
    public void onRightClick(IEntityVillagerPet entity) {
        entity.setBiome(BiomeType.getPrevious(entity.getBiome()));
    }

    @Override
    public Object value(IEntityVillagerPet entity) {
        return entity.getBiome();
    }
}
