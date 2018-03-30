package simplepets.brainsynder.menu.menuItems;

import org.bukkit.Material;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.ambient.IEntityArmorStandPet;
import simplepets.brainsynder.api.entity.passive.IEntityPolarBearPet;
import simplepets.brainsynder.menu.menuItems.base.MenuItemAbstract;
import simplepets.brainsynder.pet.PetDefault;
import simplepets.brainsynder.pet.types.ArmorStandDefault;
import simplepets.brainsynder.pet.types.PolarBearDefault;
import simplepets.brainsynder.utils.ItemBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Stand extends MenuItemAbstract {


    public Stand(PetDefault type, IEntityPet entityPet) {
        super(type, entityPet);
    }
    public Stand(PetDefault type) {
        super(type);
    }

    @Override
    public ItemBuilder getItem() {
        ItemBuilder item = type.getDataItemByName("stand", 0);
        if (entityPet instanceof IEntityArmorStandPet) {
            IEntityArmorStandPet var = (IEntityArmorStandPet) entityPet;
            item.withName(String.valueOf(item.toJSON().get("name")).replace("%value%", String.valueOf(var.isSmall())));
        } else if (entityPet instanceof IEntityPolarBearPet) {
            IEntityPolarBearPet var = (IEntityPolarBearPet) entityPet;
            item.withName(String.valueOf(item.toJSON().get("name")).replace("%value%", String.valueOf(var.isStanding())));
        }
        return item;
    }

    @Override
    public List<ItemBuilder> getDefaultItems() {
        ItemBuilder item = new ItemBuilder(Material.ARMOR_STAND);
        if (type instanceof ArmorStandDefault) {
            item.withName("&6Small: &e%value%");
        } else if (type instanceof PolarBearDefault) {
            item = new ItemBuilder(Material.IRON_LEGGINGS);
            item.withName("&6Standing: &e%value%");
        }
        return new ArrayList<>(Collections.singleton(item));
    }

    @Override
    public void onLeftClick() {
        if (entityPet instanceof IEntityArmorStandPet) {
            IEntityArmorStandPet pet = (IEntityArmorStandPet) entityPet;
            if (pet.isSmall()) {
                pet.setSmall(false);
            } else {
                pet.setSmall(true);
            }
        } else if (entityPet instanceof IEntityPolarBearPet) {
            IEntityPolarBearPet pet = (IEntityPolarBearPet) entityPet;
            if (pet.isStanding()) {
                pet.setStandingUp(false);
            } else {
                pet.setStandingUp(true);
            }
        }
    }
}
