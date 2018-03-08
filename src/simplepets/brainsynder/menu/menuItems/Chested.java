package simplepets.brainsynder.menu.menuItems;

import org.bukkit.Material;
import simple.brainsynder.api.ItemMaker;
import simplepets.brainsynder.api.entity.IChestedAbstractPet;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.menu.menuItems.base.MenuItemAbstract;
import simplepets.brainsynder.pet.PetDefault;

public class Chested extends MenuItemAbstract {
    private ItemMaker item = new ItemMaker(Material.CHEST);

    public Chested(PetDefault type, IEntityPet entityPet) {
        super(type, entityPet);
    }

    @Override
    public ItemMaker getItem() {
        if (entityPet instanceof IChestedAbstractPet) {
            IChestedAbstractPet var = (IChestedAbstractPet) entityPet;
            item.setName("&6Chested: &e" + var.isChested());
        }
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
