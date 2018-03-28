package simplepets.brainsynder.menu.menuItems;

import org.bukkit.Material;
import simple.brainsynder.api.ItemMaker;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.IRainbow;
import simplepets.brainsynder.menu.menuItems.base.MenuItemAbstract;
import simplepets.brainsynder.pet.PetDefault;

public class Rainbow extends MenuItemAbstract {
    //This material is temp... I want to use the SKULL_ITEM with the texture below as the default
    //http://textures.minecraft.net/texture/cffc977cc7e10e564a09638a53bbc4c54c9c8dac7450ba3dfa3c9099d94f5
    private ItemMaker item = new ItemMaker(Material.REDSTONE_ORE);

    public Rainbow(PetDefault type, IEntityPet entityPet) {
        super(type, entityPet);
    }
    public Rainbow(PetDefault type) {
        super(type);
    }

    @Override
    public ItemMaker getItem() {
        if (entityPet instanceof IRainbow) {
            IRainbow var = (IRainbow) entityPet;
            item.setName("&6Rainbow: &e" + var.isRainbow());
        }
        return item;
    }

    @Override
    public void onLeftClick() {
        if (entityPet instanceof IRainbow) {
            IRainbow pet = (IRainbow) entityPet;
            pet.setRainbow(!pet.isRainbow());
        }
    }
}
