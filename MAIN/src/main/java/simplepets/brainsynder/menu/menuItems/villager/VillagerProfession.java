package simplepets.brainsynder.menu.menuItems.villager;

import lib.brainsynder.ServerVersion;
import lib.brainsynder.item.ItemBuilder;
import org.apache.commons.lang.WordUtils;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.hostile.IEntityZombieVillagerPet;
import simplepets.brainsynder.api.entity.passive.IEntityVillagerPet;
import simplepets.brainsynder.menu.menuItems.base.MenuItemAbstract;
import simplepets.brainsynder.pet.PetType;
import simplepets.brainsynder.utils.ValueType;
import simplepets.brainsynder.wrapper.villager.VillagerData;
import simplepets.brainsynder.wrapper.villager.VillagerType;

import java.util.ArrayList;
import java.util.List;

@ValueType(def = "NONE", target = "https://github.com/brainsynder-Dev/SimplePets/blob/master/MAIN/src/main/java/simplepets/brainsynder/wrapper/villager/VillagerType.java")
public class VillagerProfession extends MenuItemAbstract {
    public VillagerProfession(PetType type, IEntityPet entityPet) {
        super(type, entityPet);
    }
    public VillagerProfession(PetType type) {
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

        return type.getDataItemByName(getTargetName(), data.getType().getId());
    }

    @Override
    public List<ItemBuilder> getDefaultItems() {
        List<ItemBuilder> items = new ArrayList<>();
        for (VillagerType type : VillagerType.values()) {
            String name = WordUtils.capitalizeFully(type.name().toLowerCase());
            items.add(type.getIcon().withName("&6&lProfession: &c"+name));
        }
        return items;
    }

    @Override
    public void onLeftClick() {
        if (entityPet instanceof IEntityVillagerPet) {
            IEntityVillagerPet var = (IEntityVillagerPet) entityPet;
            VillagerData data = var.getVillagerData();
            var.setVillagerData(data.withType(VillagerType.getNext(data.getType())));
        }

        if (entityPet instanceof IEntityZombieVillagerPet) {
            IEntityZombieVillagerPet var = (IEntityZombieVillagerPet) entityPet;
            VillagerData data = var.getVillagerData();
            var.setVillagerData(data.withType(VillagerType.getNext(data.getType())));
        }
    }

    @Override
    public void onRightClick() {
        if (entityPet instanceof IEntityVillagerPet) {
            IEntityVillagerPet var = (IEntityVillagerPet) entityPet;
            VillagerData data = var.getVillagerData();
            var.setVillagerData(data.withType(VillagerType.getPrevious(data.getType())));
        }

        if (entityPet instanceof IEntityZombieVillagerPet) {
            IEntityZombieVillagerPet var = (IEntityZombieVillagerPet) entityPet;
            VillagerData data = var.getVillagerData();
            var.setVillagerData(data.withType(VillagerType.getPrevious(data.getType())));
        }
    }

    @Override
    public boolean isSupported() {
        return ServerVersion.isEqualNew(ServerVersion.v1_14_R1);
    }

    @Override
    public String getTargetName() {
        return "type";
    }
}
