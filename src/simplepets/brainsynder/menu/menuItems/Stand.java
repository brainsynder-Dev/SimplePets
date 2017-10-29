package simplepets.brainsynder.menu.menuItems;

import org.bukkit.Material;
import simple.brainsynder.api.ItemMaker;
import simplepets.brainsynder.menu.MenuItemAbstract;
import simplepets.brainsynder.nms.entities.type.IEntityArmorStandPet;
import simplepets.brainsynder.nms.entities.type.IEntityPolarBearPet;
import simplepets.brainsynder.nms.entities.type.main.IEntityPet;
import simplepets.brainsynder.pet.PetType;

public class Stand extends MenuItemAbstract {
    private ItemMaker item = new ItemMaker(Material.ARMOR_STAND);

    public Stand(PetType type, IEntityPet entityPet) {
        super(type, entityPet);
    }

    @Override
    public ItemMaker getItem() {
        if (entityPet instanceof IEntityArmorStandPet) {
            IEntityArmorStandPet var = (IEntityArmorStandPet) entityPet;
            item.setName("&6Small: &e" + var.isSmall());
        } else if (entityPet instanceof IEntityPolarBearPet) {
            IEntityPolarBearPet var = (IEntityPolarBearPet) entityPet;
            item = new ItemMaker(Material.IRON_LEGGINGS);
            item.setName("&6Standing: &e" + var.isStanding());
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
