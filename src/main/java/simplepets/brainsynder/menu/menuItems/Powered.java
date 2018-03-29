package simplepets.brainsynder.menu.menuItems;

import org.bukkit.Material;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.hostile.IEntityCreeperPet;
import simplepets.brainsynder.api.entity.hostile.IEntityVexPet;
import simplepets.brainsynder.menu.menuItems.base.MenuItemAbstract;
import simplepets.brainsynder.pet.PetDefault;
import simplepets.brainsynder.utils.ItemBuilder;

public class Powered extends MenuItemAbstract {


    public Powered(PetDefault type, IEntityPet entityPet) {
        super(type, entityPet);
    }
    public Powered(PetDefault type) {
        super(type);
    }

    @Override
    public ItemBuilder getItem() {
        ItemBuilder item = type.getDataItemByName("powered", 0);
        if (entityPet instanceof IEntityCreeperPet) {
            IEntityCreeperPet var = (IEntityCreeperPet) entityPet;
            item.withName(String.valueOf(item.toJSON().get("name")).replace("%value%", String.valueOf(var.isPowered())));
        }
        if (entityPet instanceof IEntityVexPet) {
            IEntityVexPet var = (IEntityVexPet) entityPet;
            item.withName(String.valueOf(item.toJSON().get("name")).replace("%value%", String.valueOf(var.isPowered())));
        }
        return item;
    }

    @Override
    public ItemBuilder getDefaultItem() {
        ItemBuilder item = new ItemBuilder(Material.SULPHUR);
        item.withName("&6Powered: &e%value%");
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
