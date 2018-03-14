package simplepets.brainsynder.menu.menuItems;

import org.bukkit.Material;
import simple.brainsynder.api.ItemMaker;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.hostile.IEntityCreeperPet;
import simplepets.brainsynder.api.entity.hostile.IEntityVexPet;
import simplepets.brainsynder.menu.menuItems.base.MenuItemAbstract;
import simplepets.brainsynder.pet.PetDefault;

public class Powered extends MenuItemAbstract {
    private ItemMaker item = new ItemMaker(Material.SULPHUR);

    public Powered(PetDefault type, IEntityPet entityPet) {
        super(type, entityPet);
    }
    public Powered(PetDefault type) {
        super(type);
    }

    @Override
    public ItemMaker getItem() {
        if (entityPet instanceof IEntityCreeperPet) {
            IEntityCreeperPet var = (IEntityCreeperPet) entityPet;
            item.setName("&6Powered: &e" + var.isPowered());
        }
        if (entityPet instanceof IEntityVexPet) {
            IEntityVexPet var = (IEntityVexPet) entityPet;
            item.setName("&6Powered: &e" + var.isPowered());
        }
        return item;
    }

    @Override
    public void onLeftClick() {
        if (entityPet instanceof IEntityVexPet) {
            IEntityVexPet bat = (IEntityVexPet) entityPet;
            if (bat.isPowered()) {
                bat.setPowered(false);
            } else {
                bat.setPowered(true);
            }
        }
        if (entityPet instanceof IEntityCreeperPet) {
            IEntityCreeperPet bat = (IEntityCreeperPet) entityPet;
            if (bat.isPowered()) {
                bat.setPowered(false);
            } else {
                bat.setPowered(true);
            }
        }
    }
}
