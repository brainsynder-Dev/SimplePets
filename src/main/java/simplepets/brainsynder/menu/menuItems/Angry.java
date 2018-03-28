package simplepets.brainsynder.menu.menuItems;

import org.bukkit.Material;
import org.json.simple.JSONObject;
import simple.brainsynder.api.ItemMaker;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.passive.IEntityWolfPet;
import simplepets.brainsynder.menu.menuItems.base.MenuItemAbstract;
import simplepets.brainsynder.pet.PetDefault;
import simplepets.brainsynder.utils.ItemBuilder;

public class Angry extends MenuItemAbstract {
    private ItemBuilder item = type.getDataItemByName("angry");

    public Angry(PetDefault type, IEntityPet entityPet) {
        super(type, entityPet);
    }
    public Angry(PetDefault type) {
        super(type);
    }

    @Override
    public ItemBuilder getItem() {
        if (item != null) {
            if (entityPet instanceof IEntityWolfPet) {
                IEntityWolfPet var = (IEntityWolfPet) entityPet;
                item.withName(String.valueOf(item.toJSON().get("name"))
                        .replace("%value%", String.valueOf(var.isAngry())));

            }
        }
        return item;
    }

    @Override
    public ItemBuilder getDefaultItem() {
        ItemBuilder item = new ItemBuilder(Material.WOOL, (byte) 14);
        item.withName("&6Angry: &e%value%");
        return item;
    }

    @Override
    public void onLeftClick() {
        if (entityPet instanceof IEntityWolfPet) {
            IEntityWolfPet pet = (IEntityWolfPet) entityPet;
            if (pet.isAngry()) {
                pet.setAngry(false);
            } else {
                pet.setAngry(true);
            }
        }
    }
}
