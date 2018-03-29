package simplepets.brainsynder.menu.menuItems;

import org.bukkit.Material;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.hostile.IEntityBlazePet;
import simplepets.brainsynder.menu.menuItems.base.MenuItemAbstract;
import simplepets.brainsynder.pet.PetDefault;
import simplepets.brainsynder.utils.ItemBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Burning extends MenuItemAbstract {


    public Burning(PetDefault type, IEntityPet entityPet) {
        super(type, entityPet);
    }
    public Burning(PetDefault type) {
        super(type);
    }

    @Override
    public ItemBuilder getItem() {
        ItemBuilder item = type.getDataItemByName("burning", 0);
        if (entityPet instanceof IEntityBlazePet) {
            IEntityBlazePet var = (IEntityBlazePet) entityPet;
            item.withName(String.valueOf(item.toJSON().get("name"))
                    .replace("%value%", String.valueOf(var.isBurning())));
        }
        return item;
    }

    @Override
    public List<ItemBuilder> getDefaultItems() {
        ItemBuilder item = new ItemBuilder(Material.BLAZE_ROD);
        if (item != null) {
            item.withName("&6Burning: &e%value%");
        }
        return new ArrayList<>(Collections.singleton(item));
    }

    @Override
    public void onLeftClick() {
        if (entityPet instanceof IEntityBlazePet) {
            IEntityBlazePet pet = (IEntityBlazePet) entityPet;
            if (pet.isBurning()) {
                pet.setBurning(false);
            } else {
                pet.setBurning(true);
            }
        }
    }
}
