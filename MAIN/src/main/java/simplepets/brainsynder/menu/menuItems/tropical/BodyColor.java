package simplepets.brainsynder.menu.menuItems.tropical;

import lib.brainsynder.item.ItemBuilder;
import org.apache.commons.lang.WordUtils;
import org.bukkit.inventory.meta.ItemMeta;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.passive.IEntityTropicalFishPet;
import simplepets.brainsynder.menu.menuItems.base.MenuItemAbstract;
import simplepets.brainsynder.pet.PetDefault;
import simplepets.brainsynder.utils.ValueType;
import simplepets.brainsynder.wrapper.DyeColorWrapper;

import java.util.ArrayList;
import java.util.List;

@ValueType(def = "WHITE", target = "https://github.com/brainsynder-Dev/SimplePets/blob/master/MAIN/src/main/java/simplepets/brainsynder/wrapper/DyeColorWrapper.java")
public class BodyColor extends MenuItemAbstract {
    public BodyColor(PetDefault type, IEntityPet entityPet) {
        super(type, entityPet);
    }
    public BodyColor(PetDefault type) {
        super(type);
    }

    @Override
    public ItemBuilder getItem() {
        ItemBuilder item = type.getDataItemByName("bodycolor", 0);

        if (getEntityPet() instanceof IEntityTropicalFishPet) {
            IEntityTropicalFishPet var = (IEntityTropicalFishPet) getEntityPet();
            DyeColorWrapper wrapper = DyeColorWrapper.WHITE;
            if (var.getBodyColor() != null) wrapper = var.getBodyColor();
            item = type.getDataItemByName("bodycolor", wrapper.getWoolData());

            DyeColorWrapper prev = DyeColorWrapper.getPrevious(wrapper);
            DyeColorWrapper next = DyeColorWrapper.getNext(wrapper);
            List<String> lore = new ArrayList<>();

            List<String> currentLore = item.getMetaValue(ItemMeta.class, ItemMeta::getLore);
            for (String str : currentLore) {
                lore.add(str.replace("%prev_color%", "§" + prev.getChatChar())
                .replace("%prev_name%", WordUtils.capitalize(prev.name().toLowerCase().replace("_", " ")))
                .replace("%curr_color%", "§" + wrapper.getChatChar())
                .replace("%curr_name%", WordUtils.capitalize(wrapper.name().toLowerCase().replace("_", " ")))
                .replace("%next_color%", "§" + next.getChatChar())
                .replace("%next_name%", WordUtils.capitalize(next.name().toLowerCase().replace("_", " "))));
            }

            item.withLore(lore);
        }
        return item;
    }

    @Override
    public List<ItemBuilder> getDefaultItems() {
        List<ItemBuilder> items = new ArrayList<>();
        for (DyeColorWrapper color : DyeColorWrapper.values()) {
            ItemBuilder item = lib.brainsynder.nms.DataConverter.getColoredMaterial(lib.brainsynder.nms.DataConverter.MaterialType.WOOL, color.getWoolData());
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
        if (entityPet instanceof IEntityTropicalFishPet) {
            IEntityTropicalFishPet var = (IEntityTropicalFishPet) entityPet;
            DyeColorWrapper wrapper = DyeColorWrapper.WHITE;
            if (var.getBodyColor() != null) wrapper = var.getBodyColor();
            var.setBodyColor(DyeColorWrapper.getNext(wrapper));
        }
    }

    @Override
    public void onRightClick() {
        if (entityPet instanceof IEntityTropicalFishPet) {
            IEntityTropicalFishPet var = (IEntityTropicalFishPet) entityPet;
            DyeColorWrapper wrapper = DyeColorWrapper.WHITE;
            if (var.getBodyColor() != null) wrapper = var.getBodyColor();
            var.setBodyColor(DyeColorWrapper.getPrevious(wrapper));
        }
    }
}
