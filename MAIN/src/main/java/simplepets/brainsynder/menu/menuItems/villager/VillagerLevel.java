package simplepets.brainsynder.menu.menuItems.villager;

import org.bukkit.Material;
import simple.brainsynder.api.ItemBuilder;
import simple.brainsynder.utils.ServerVersion;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.misc.IProfession;
import simplepets.brainsynder.menu.menuItems.base.MenuItemAbstract;
import simplepets.brainsynder.pet.PetDefault;
import simplepets.brainsynder.utils.ValueType;
import simplepets.brainsynder.wrapper.villager.VillagerData;

import java.util.ArrayList;
import java.util.List;

@ValueType(type = "Integer", def = "1", target = "1,2,3,4")
public class VillagerLevel extends MenuItemAbstract {
    public VillagerLevel(PetDefault type, IEntityPet entityPet) {
        super(type, entityPet);
    }
    public VillagerLevel(PetDefault type) {
        super(type);
    }

    @Override
    public ItemBuilder getItem() {
        VillagerData data = VillagerData.getDefault();
        if (entityPet instanceof IProfession) {
            IProfession var = (IProfession) entityPet;
            data = var.getVillagerData();
        }

        return type.getDataItemByName(getTargetName(), (data.getLevel()-1));
    }

    @Override
    public List<ItemBuilder> getDefaultItems() {
        List<ItemBuilder> items = new ArrayList<>();
        items.add(new ItemBuilder(Material.IRON_INGOT).withName("&6&lLevel: &e1"));
        items.add(new ItemBuilder(Material.GOLD_INGOT).withName("&6&lLevel: &e2"));
        items.add(new ItemBuilder(Material.DIAMOND).withName("&6&lLevel: &e3"));
        items.add(new ItemBuilder(Material.EMERALD).withName("&6&lLevel: &e4"));
        return items;
    }

    @Override
    public void onLeftClick() {
        if (entityPet instanceof IProfession) {
            IProfession var = (IProfession) entityPet;
            VillagerData data = var.getVillagerData();
            int level = data.getLevel();

            if (level == 4) {
                level = 1;
            }else{
                level++;
            }
            var.setVillagerData(data.withLevel(level));
        }
    }

    @Override
    public void onRightClick() {
        if (entityPet instanceof IProfession) {
            IProfession var = (IProfession) entityPet;
            VillagerData data = var.getVillagerData();
            int level = data.getLevel();

            if (level == 1) {
                level = 4;
            }else{
                level--;
            }
            var.setVillagerData(data.withLevel(level));
        }
    }

    @Override
    public boolean isSupported() {
        return ServerVersion.isEqualNew(ServerVersion.v1_14_R1);
    }

    @Override
    public String getTargetName() {
        return "level";
    }
}
