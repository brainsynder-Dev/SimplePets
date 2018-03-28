package simplepets.brainsynder.menu.menuItems;

import org.bukkit.Material;
import simple.brainsynder.api.ItemMaker;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.ambient.IEntityArmorStandPet;
import simplepets.brainsynder.api.entity.passive.IEntityPolarBearPet;
import simplepets.brainsynder.menu.menuItems.base.MenuItemAbstract;
import simplepets.brainsynder.pet.PetDefault;
import simplepets.brainsynder.utils.ItemBuilder;

public class Stand extends MenuItemAbstract {
    private ItemBuilder item = type.getDataItemByName("stand");

    public Stand(PetDefault type, IEntityPet entityPet) {
        super(type, entityPet);
    }
    public Stand(PetDefault type) {
        super(type);
    }

    @Override
    public ItemBuilder getItem() {
        if (entityPet instanceof IEntityArmorStandPet) {
            IEntityArmorStandPet var = (IEntityArmorStandPet) entityPet;
            item.withName("&6Small: &e" + var.isSmall());
        } else if (entityPet instanceof IEntityPolarBearPet) {
            IEntityPolarBearPet var = (IEntityPolarBearPet) entityPet;
            item = new ItemBuilder(Material.IRON_LEGGINGS);
            item.withName("&6Standing: &e" + var.isStanding());
        }
        return item;
    }

    @Override
    public ItemBuilder getDefaultItem() {
        ItemBuilder item = new ItemBuilder(Material.ARMOR_STAND);
        if (entityPet instanceof IEntityArmorStandPet) {
            IEntityArmorStandPet var = (IEntityArmorStandPet) entityPet;
            item.withName("&6Small: &e" + var.isSmall());
        } else if (entityPet instanceof IEntityPolarBearPet) {
            IEntityPolarBearPet var = (IEntityPolarBearPet) entityPet;
            item = new ItemBuilder(Material.IRON_LEGGINGS);
            item.withName("&6Standing: &e" + var.isStanding());
        }
        return item;
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
