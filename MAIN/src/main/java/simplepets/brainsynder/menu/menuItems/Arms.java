package simplepets.brainsynder.menu.menuItems;

import org.bukkit.Material;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.hostile.IEntityZombiePet;
import simplepets.brainsynder.api.entity.misc.ISkeletonAbstract;
import simplepets.brainsynder.menu.menuItems.base.MenuItemAbstract;
import simplepets.brainsynder.pet.PetDefault;
import simplepets.brainsynder.utils.ItemBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Arms extends MenuItemAbstract {
    public Arms(PetDefault type, IEntityPet entityPet) {
        super(type, entityPet);
    }
    public Arms(PetDefault type) {
        super(type);
    }

    @Override
    public ItemBuilder getItem() {
        ItemBuilder item = type.getDataItemByName("arms", 0);
        if (entityPet instanceof IEntityZombiePet) {
            IEntityZombiePet var = (IEntityZombiePet) entityPet;
            item.withName(item.getName().replace("%value%", String.valueOf(var.isArmsRaised())));
        }else if (entityPet instanceof ISkeletonAbstract) {
            ISkeletonAbstract var = (ISkeletonAbstract) entityPet;
            item.withName(item.getName().replace("%value%", String.valueOf(var.isArmsRaised())));
        }
        return item;
    }

    @Override
    public List<ItemBuilder> getDefaultItems() {
        ItemBuilder item = new ItemBuilder(Material.STICK);
        item.withName("&6Arms Raised: &e%value%");
        return new ArrayList<>(Collections.singleton(item));
    }

    @Override
    public void onLeftClick() {
        if (entityPet instanceof IEntityZombiePet) {
            IEntityZombiePet pet = (IEntityZombiePet) entityPet;
            pet.setArmsRaised(!pet.isArmsRaised());
        }
        if (entityPet instanceof ISkeletonAbstract) {
            ISkeletonAbstract pet = (ISkeletonAbstract) entityPet;
            pet.setArmsRaised(!pet.isArmsRaised());
        }
    }
}
