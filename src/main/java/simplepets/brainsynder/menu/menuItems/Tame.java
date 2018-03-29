package simplepets.brainsynder.menu.menuItems;

import org.bukkit.Material;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.ITameable;
import simplepets.brainsynder.api.entity.passive.IEntityWolfPet;
import simplepets.brainsynder.menu.menuItems.base.MenuItemAbstract;
import simplepets.brainsynder.pet.PetDefault;
import simplepets.brainsynder.utils.ItemBuilder;

public class Tame extends MenuItemAbstract {


    public Tame(PetDefault type, IEntityPet entityPet) {
        super(type, entityPet);
    }
    public Tame(PetDefault type) {
        super(type);
    }

    @Override
    public ItemBuilder getItem() {
        int index = 3;
        if (entityPet instanceof IEntityWolfPet) {
            index = 4;
        }
        ItemBuilder item = type.getDataItemByName("tame", index);
        if (entityPet instanceof ITameable) {
            ITameable var = (ITameable) entityPet;
            item.withName(String.valueOf(item.toJSON().get("name")).replace("%value%", String.valueOf(var.isTamed())));
        }
        return item;
    }

    @Override
    public ItemBuilder getDefaultItem() {
        ItemBuilder item = new ItemBuilder(Material.BONE);
        item.withName("&6Tamed: &e%value%");
        return null;
    }

    @Override
    public void onLeftClick() {
        if (entityPet instanceof ITameable) {
            ITameable pet = (ITameable) entityPet;
            if (pet.isTamed()) {
                pet.setTamed(false);
            } else {
                pet.setTamed(true);
            }
        }
    }
}
