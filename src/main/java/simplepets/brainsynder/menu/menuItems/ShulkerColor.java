package simplepets.brainsynder.menu.menuItems;

import org.apache.commons.lang.WordUtils;
import simplepets.brainsynder.api.entity.IColorable;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.menu.menuItems.base.MenuItemAbstract;
import simplepets.brainsynder.pet.PetDefault;
import simplepets.brainsynder.utils.ItemBuilder;
import simplepets.brainsynder.wrapper.DyeColorWrapper;
import simplepets.brainsynder.wrapper.MaterialWrapper;

import java.util.ArrayList;
import java.util.List;

public class ShulkerColor extends MenuItemAbstract {
    public ShulkerColor(PetDefault type, IEntityPet entityPet) {
        super(type, entityPet);
    }
    public ShulkerColor(PetDefault type) {
        super(type);
    }

    @Override
    public ItemBuilder getItem() {
        ItemBuilder item = null;
        if (getEntityPet() instanceof IColorable) {
            IColorable var = (IColorable) getEntityPet();
            DyeColorWrapper typeID = DyeColorWrapper.PURPLE;
            if (var.getColor() != null)
                typeID = var.getColor();
            item = new ItemBuilder(MaterialWrapper.fromName(typeID.name() + "_SHULKER_BOX").toMaterial());
            item.withName(" ");
            DyeColorWrapper prev = DyeColorWrapper.getPrevious(typeID);
            DyeColorWrapper next = DyeColorWrapper.getNext(typeID);
            item.addLore("&6Previous: §" + prev.getChatChar() + WordUtils.capitalize(prev.toString().toLowerCase()),
                    "&6Current: §" + typeID.getChatChar() + WordUtils.capitalize(typeID.toString().toLowerCase()),
                    "&6Next: §" + next.getChatChar() + WordUtils.capitalize(next.toString().toLowerCase()));
        }
        return item;
    }

    @Override
    public List<ItemBuilder> getDefaultItems() {
        ItemBuilder item = null;
        if (getEntityPet() instanceof IColorable) {
            IColorable var = (IColorable) getEntityPet();
            DyeColorWrapper typeID = DyeColorWrapper.PURPLE;
            if (var.getColor() != null)
                typeID = var.getColor();
            item = new ItemBuilder(MaterialWrapper.fromName(typeID.name() + "_SHULKER_BOX").toMaterial());
            item.withName(" ");
            DyeColorWrapper prev = DyeColorWrapper.getPrevious(typeID);
            DyeColorWrapper next = DyeColorWrapper.getNext(typeID);
            item.addLore("&6Previous: §" + prev.getChatChar() + WordUtils.capitalize(prev.toString().toLowerCase()),
                    "&6Current: §" + typeID.getChatChar() + WordUtils.capitalize(typeID.toString().toLowerCase()),
                    "&6Next: §" + next.getChatChar() + WordUtils.capitalize(next.toString().toLowerCase()));
        }
        return new ArrayList<>(); // TODO
    }

    @Override
    public void onLeftClick() {
        if (entityPet instanceof IColorable) {
            IColorable var = (IColorable) entityPet;
            DyeColorWrapper wrapper = DyeColorWrapper.PURPLE;
            if (var.getColor() != null)
                wrapper = var.getColor();
            var.setColor(DyeColorWrapper.getNext(wrapper));
        }
    }

    @Override
    public void onRightClick() {
        if (entityPet instanceof IColorable) {
            IColorable var = (IColorable) entityPet;
            DyeColorWrapper wrapper = DyeColorWrapper.PURPLE;
            if (var.getColor() != null)
                wrapper = var.getColor();
            var.setColor(DyeColorWrapper.getPrevious(wrapper));
        }
    }
}
