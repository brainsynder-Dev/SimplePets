package simplepets.brainsynder.menu.menuItems;

import org.bukkit.Material;
import simplepets.brainsynder.api.entity.IChestedAbstractPet;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.menu.menuItems.base.MenuItemAbstract;
import simplepets.brainsynder.pet.PetDefault;
import simplepets.brainsynder.utils.ItemBuilder;

public class Chested extends MenuItemAbstract {
    private ItemBuilder item = type.getDataItemByName("chested");

    public Chested(PetDefault type, IEntityPet entityPet) {
        super(type, entityPet);
    }
    public Chested(PetDefault type) {
        super(type);
    }

    @Override
    public ItemBuilder getItem() {
        if (item != null) {
            if (entityPet instanceof IChestedAbstractPet) {
                IChestedAbstractPet var = (IChestedAbstractPet) entityPet;
                item.withName(String.valueOf(item.toJSON().get("name"))
                        .replace("%value%", String.valueOf(var.isChested())));
            }
        }

        return item;
    }

    @Override
    public ItemBuilder getDefaultItem() {
        ItemBuilder item = new ItemBuilder(Material.CHEST);
        item.withName("&6Chested: &e%value%");
        return item;
    }

    @Override
    public void onLeftClick() {
        if (entityPet instanceof IChestedAbstractPet) {
            IChestedAbstractPet chested = (IChestedAbstractPet) entityPet;
            if (chested.isChested()) {
                chested.setChested(false);
            } else {
                chested.setChested(true);
            }
        }
    }
}
