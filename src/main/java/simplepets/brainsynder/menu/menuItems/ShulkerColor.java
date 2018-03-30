package simplepets.brainsynder.menu.menuItems;

import org.apache.commons.lang.WordUtils;
import org.json.simple.JSONArray;
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
            List<String> lore = new ArrayList<>();
            for (Object s : (JSONArray) item.toJSON().get("lore")) {
                String str = String.valueOf(s);
                lore.add(str.replace("%prev_color%", "§" + prev.getChatChar())
                        .replace("%prev_name%", WordUtils.capitalize(prev.name().toLowerCase()))
                        .replace("%curr_color%", "§" + typeID.getChatChar())
                        .replace("%curr_name%", WordUtils.capitalize(typeID.name().toLowerCase()))
                        .replace("%next_color%", "§" + next.getChatChar())
                        .replace("%next_name%", WordUtils.capitalize(typeID.name().toLowerCase())));
            }

            item.withLore(lore);
        }
        return item;
    }

    @Override
    public List<ItemBuilder> getDefaultItems() {
        List<ItemBuilder> items = new ArrayList<>();
        for (DyeColorWrapper color : DyeColorWrapper.values()) {
            ItemBuilder item = new ItemBuilder(MaterialWrapper.fromName(color.name() + "_SHULKER_BOX").toMaterial());
            item.withName(" ");
            item.addLore("&6Previous: %prev_color%%prev_name%",
                    "&6Current: %curr_color%%curr_name%",
                    "&6Next: %next_color%%next_name%");
            items.add(item);
        }
        return items;
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
