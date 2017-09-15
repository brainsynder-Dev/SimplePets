package simplepets.brainsynder.menu.menuItems;

import simple.brainsynder.api.ItemMaker;
import simple.brainsynder.api.SkullMaker;
import simplepets.brainsynder.menu.MenuItemAbstract;
import simplepets.brainsynder.nms.entities.type.IEntityArmorStandPet;
import simplepets.brainsynder.nms.entities.type.main.IEntityPet;
import simplepets.brainsynder.pet.PetType;

public class Clone extends MenuItemAbstract {
    private SkullMaker item = new SkullMaker().setOwner("Steve").setSkullOwner("Steve");

    public Clone(PetType type, IEntityPet entityPet) {
        super(type, entityPet);
    }

    @Override
    public ItemMaker getItem() {
        if (entityPet instanceof IEntityArmorStandPet) {
            IEntityArmorStandPet var = (IEntityArmorStandPet) entityPet;
            item.setName("&6IsClone: &e" + var.isOwner());
        }
        return item;
    }

    @Override
    public void onLeftClick() {
        if (entityPet instanceof IEntityArmorStandPet) {
            IEntityArmorStandPet bat = (IEntityArmorStandPet) entityPet;
            if (bat.isOwner()) {
                bat.setOwner(false);
            } else {
                bat.setOwner(true);
            }
        }
    }
}
