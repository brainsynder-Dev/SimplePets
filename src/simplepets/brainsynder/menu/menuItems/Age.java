package simplepets.brainsynder.menu.menuItems;

import org.bukkit.Material;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import simple.brainsynder.api.ItemMaker;
import simplepets.brainsynder.menu.MenuItemAbstract;
import simplepets.brainsynder.nms.entities.type.main.IAgeablePet;
import simplepets.brainsynder.nms.entities.type.main.IEntityPet;
import simplepets.brainsynder.pet.PetType;

public class Age extends MenuItemAbstract {
    private ItemMaker item = new ItemMaker(Material.WHEAT);

    public Age(PetType type, IEntityPet entityPet) {
        super(type, entityPet);
    }

    public static JSONObject toJSON() {
        JSONObject json = MenuItemAbstract.toJSON();
        json.putIfAbsent("Baby-Name", "&6Baby: &e{value}");
        json.putIfAbsent("Baby-Lore", new JSONArray());
        return json;
    }

    @Override
    public ItemMaker getItem() {
        if (entityPet instanceof IAgeablePet) {
            IAgeablePet var = (IAgeablePet) entityPet;
            item.setName("&6Baby: &e" + var.isBaby());
        }
        return item;
    }

    @Override
    public void onLeftClick() {
        if (entityPet instanceof IAgeablePet) {
            IAgeablePet age = (IAgeablePet) entityPet;
            if (age.isBaby()) {
                age.setBaby(false);
            } else {
                age.setBaby(true);
            }
        }
    }
}
