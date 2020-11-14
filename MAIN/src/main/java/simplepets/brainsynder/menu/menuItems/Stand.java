package simplepets.brainsynder.menu.menuItems;

import lib.brainsynder.item.ItemBuilder;
import org.bukkit.Material;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.ambient.IEntityArmorStandPet;
import simplepets.brainsynder.api.entity.passive.IEntityPolarBearPet;
import simplepets.brainsynder.menu.menuItems.base.MenuItemAbstract;
import simplepets.brainsynder.pet.PetType;
import simplepets.brainsynder.pet.types.ArmorStandPet;
import simplepets.brainsynder.pet.types.PolarBearPet;
import simplepets.brainsynder.utils.ValueType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ValueType(type = "boolean", def = "false")
public class Stand extends MenuItemAbstract {


    public Stand(PetType type, IEntityPet entityPet) {
        super(type, entityPet);
    }
    public Stand(PetType type) {
        super(type);
    }

    @Override
    public ItemBuilder getItem() {
        ItemBuilder item = type.getDataItemByName("stand", 0);
        if (entityPet instanceof IEntityArmorStandPet) {
            IEntityArmorStandPet var = (IEntityArmorStandPet) entityPet;
            item.withName(item.getName().replace("%value%", String.valueOf(var.isSmall())));
        } else if (entityPet instanceof IEntityPolarBearPet) {
            IEntityPolarBearPet var = (IEntityPolarBearPet) entityPet;
            item.withName(item.getName().replace("%value%", String.valueOf(var.isStanding())));
        }
        return item;
    }

    @Override
    public List<ItemBuilder> getDefaultItems() {
        ItemBuilder item = new ItemBuilder(Material.ARMOR_STAND);
        if (type instanceof ArmorStandPet) {
            item.withName("&6Small: &e%value%");
        } else if (type instanceof PolarBearPet) {
            item = new ItemBuilder(Material.IRON_LEGGINGS);
            item.withName("&6Standing: &e%value%");
        }
        return new ArrayList<>(Collections.singleton(item));
    }

    @Override
    public void onLeftClick() {
        if (entityPet instanceof IEntityArmorStandPet) {
            IEntityArmorStandPet pet = (IEntityArmorStandPet) entityPet;
            pet.setSmall(!pet.isSmall());
        } else if (entityPet instanceof IEntityPolarBearPet) {
            IEntityPolarBearPet pet = (IEntityPolarBearPet) entityPet;
            pet.setStandingUp(!pet.isStanding());
        }
    }
}
