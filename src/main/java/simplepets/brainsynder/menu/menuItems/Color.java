package simplepets.brainsynder.menu.menuItems;

import org.apache.commons.lang.WordUtils;
import org.bukkit.Material;
import org.json.simple.JSONArray;
import simple.brainsynder.api.ItemMaker;
import simplepets.brainsynder.api.entity.IColorable;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.passive.IEntitySheepPet;
import simplepets.brainsynder.menu.menuItems.base.MenuItemAbstract;
import simplepets.brainsynder.pet.PetDefault;
import simplepets.brainsynder.utils.ItemBuilder;
import simplepets.brainsynder.wrapper.DyeColorWrapper;

import java.util.List;

public class Color extends MenuItemAbstract {
    public Color(PetDefault type, IEntityPet entityPet) {
        super(type, entityPet);
    }
    public Color(PetDefault type) {
        super(type);
    }

    @Override
    public ItemBuilder getItem() {
        int index = 2;
        if (entityPet instanceof IEntitySheepPet) {
            index = 1;
        }
        ItemBuilder item = type.getDataItemByName("color", index);
        if (getEntityPet() instanceof IColorable) {
            IColorable var = (IColorable) getEntityPet();
            DyeColorWrapper typeID = DyeColorWrapper.WHITE;
            if (var.getColor() != null)
                typeID = var.getColor();
            item = new ItemBuilder(Material.valueOf(String.valueOf(item.toJSON().get("material"))), typeID.getWoolData());
            item.withName(String.valueOf(item.toJSON().get("name")));
            DyeColorWrapper prev = DyeColorWrapper.getPrevious(typeID);
            DyeColorWrapper next = DyeColorWrapper.getNext(typeID);
            List<String> lore = (JSONArray) item.toJSON().get("lore");
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
    public ItemBuilder getDefaultItem() {
        ItemBuilder item = new ItemBuilder(Material.WOOL, (byte) 0);
        if (getEntityPet() instanceof IColorable) {
            IColorable var = (IColorable) getEntityPet();
            DyeColorWrapper typeID = DyeColorWrapper.WHITE;
            if (var.getColor() != null)
                typeID = var.getColor();
            item = new ItemBuilder(Material.valueOf(String.valueOf(item.toJSON().get("material"))), typeID.getWoolData());
            item.withName(" ");
            item.addLore("&6Previous: %prev_color%%prev_name%",
                    "&6Current: %curr_color%%curr_name%",
                    "&6Next: %next_color%%next_name%");
        }
        return item;
    }

    @Override
    public void onLeftClick() {
        if (entityPet instanceof IColorable) {
            IColorable var = (IColorable) entityPet;
            DyeColorWrapper wrapper = DyeColorWrapper.WHITE;
            if (var.getColor() != null)
                wrapper = var.getColor();
            var.setColor(DyeColorWrapper.getNext(wrapper));
        }
    }

    @Override
    public void onRightClick() {
        if (entityPet instanceof IColorable) {
            IColorable var = (IColorable) entityPet;
            DyeColorWrapper wrapper = DyeColorWrapper.WHITE;
            if (var.getColor() != null)
                wrapper = var.getColor();
            var.setColor(DyeColorWrapper.getPrevious(wrapper));
        }
    }
}
