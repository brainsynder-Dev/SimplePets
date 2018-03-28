package simplepets.brainsynder.menu.menuItems;

import org.bukkit.Material;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.passive.IEntitySnowmanPet;
import simplepets.brainsynder.menu.menuItems.base.MenuItemAbstract;
import simplepets.brainsynder.pet.PetDefault;
import simplepets.brainsynder.utils.ItemBuilder;

public class Pumpkin extends MenuItemAbstract {
    private ItemBuilder item = type.getDataItemByName("pumpkin");

    public Pumpkin(PetDefault type, IEntityPet entityPet) {
        super(type, entityPet);
    }
    public Pumpkin(PetDefault type) {
        super(type);
    }

    @Override
    public int getVersion() {
        return 19;
    }

    @Override
    public ItemBuilder getItem() {
        if (entityPet instanceof IEntitySnowmanPet) {
            IEntitySnowmanPet var = (IEntitySnowmanPet) entityPet;
            item.withName(String.valueOf(item.toJSON().get("name")).replace("%value%", String.valueOf(var.hasPumpkin())));
        }
        return item;
    }

    @Override
    public ItemBuilder getDefaultItem() {
        ItemBuilder item = new ItemBuilder(Material.PUMPKIN);
        item.withName("&6Pumpkin: &e%value%");
        return item;
    }

    @Override
    public void onLeftClick() {
        if (entityPet instanceof IEntitySnowmanPet) {
            IEntitySnowmanPet var = (IEntitySnowmanPet) entityPet;
            if (var.hasPumpkin()) {
                var.setHasPumpkin(false);
            } else {
                var.setHasPumpkin(true);
            }
        }
    }
}
