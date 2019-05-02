package simplepets.brainsynder.menu.menuItems.villager;

import org.apache.commons.lang.WordUtils;
import simple.brainsynder.api.ItemBuilder;
import simple.brainsynder.utils.ServerVersion;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.hostile.IEntityZombieVillagerPet;
import simplepets.brainsynder.api.entity.passive.IEntityVillagerPet;
import simplepets.brainsynder.menu.menuItems.base.MenuItemAbstract;
import simplepets.brainsynder.pet.PetDefault;
import simplepets.brainsynder.wrapper.villager.BiomeType;
import simplepets.brainsynder.wrapper.villager.VillagerData;

import java.util.ArrayList;
import java.util.List;

public class VillagerBiome extends MenuItemAbstract {
    public VillagerBiome(PetDefault type, IEntityPet entityPet) {
        super(type, entityPet);
    }
    public VillagerBiome(PetDefault type) {
        super(type);
    }

    @Override
    public ItemBuilder getItem() {
        VillagerData data = VillagerData.getDefault();
        if (entityPet instanceof IEntityVillagerPet) {
            IEntityVillagerPet var = (IEntityVillagerPet) entityPet;
            data = var.getVillagerData();
        }
        if (entityPet instanceof IEntityZombieVillagerPet) {
            IEntityZombieVillagerPet var = (IEntityZombieVillagerPet) entityPet;
            data = var.getVillagerData();
        }

        return type.getDataItemByName("biome", data.getBiome().ordinal());
    }

    @Override
    public List<ItemBuilder> getDefaultItems() {
        List<ItemBuilder> items = new ArrayList<>();
        for (BiomeType type : BiomeType.values()) {
            String name = WordUtils.capitalizeFully(type.name().toLowerCase());
            items.add(type.getIcon().withName("&6&lBiome: &c"+name));
        }
        return items;
    }

    @Override
    public void onLeftClick() {
        if (entityPet instanceof IEntityVillagerPet) {
            IEntityVillagerPet var = (IEntityVillagerPet) entityPet;
            VillagerData data = var.getVillagerData();
            var.setVillagerData(data.withBiome(BiomeType.getNext(data.getBiome())));
        }

        if (entityPet instanceof IEntityZombieVillagerPet) {
            IEntityZombieVillagerPet var = (IEntityZombieVillagerPet) entityPet;
            VillagerData data = var.getVillagerData();
            var.setVillagerData(data.withBiome(BiomeType.getNext(data.getBiome())));
        }
    }

    @Override
    public void onRightClick() {
        if (entityPet instanceof IEntityVillagerPet) {
            IEntityVillagerPet var = (IEntityVillagerPet) entityPet;
            VillagerData data = var.getVillagerData();
            var.setVillagerData(data.withBiome(BiomeType.getPrevious(data.getBiome())));
        }

        if (entityPet instanceof IEntityZombieVillagerPet) {
            IEntityZombieVillagerPet var = (IEntityZombieVillagerPet) entityPet;
            VillagerData data = var.getVillagerData();
            var.setVillagerData(data.withBiome(BiomeType.getPrevious(data.getBiome())));
        }
    }

    @Override
    public boolean isSupported() {
        return ServerVersion.isEqualNew(ServerVersion.v1_14_R1);
    }

    @Override
    public String getTargetName() {
        return "biome";
    }
}
