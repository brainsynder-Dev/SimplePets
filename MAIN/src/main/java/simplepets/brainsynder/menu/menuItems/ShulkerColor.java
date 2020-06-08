package simplepets.brainsynder.menu.menuItems;

import lib.brainsynder.ServerVersion;
import lib.brainsynder.item.ItemBuilder;
import lib.brainsynder.nms.DataConverter;
import org.apache.commons.lang.WordUtils;
import org.bukkit.inventory.meta.ItemMeta;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.misc.IColorable;
import simplepets.brainsynder.menu.menuItems.base.MenuItemAbstract;
import simplepets.brainsynder.pet.PetType;
import simplepets.brainsynder.utils.ValueType;
import simplepets.brainsynder.wrapper.DyeColorWrapper;

import java.util.ArrayList;
import java.util.List;

@ValueType(def = "WHITE", target = "https://github.com/brainsynder-Dev/SimplePets/blob/master/MAIN/src/main/java/simplepets/brainsynder/wrapper/DyeColorWrapper.java")
public class ShulkerColor extends MenuItemAbstract {
    public ShulkerColor(PetType type, IEntityPet entityPet) {
        super(type, entityPet);
    }
    public ShulkerColor(PetType type) {
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
            item = type.getDataItemByName("shulkercolor", typeID.getWoolData());
            DyeColorWrapper prev = DyeColorWrapper.getPrevious(typeID);
            DyeColorWrapper next = DyeColorWrapper.getNext(typeID);
            List<String> lore = new ArrayList<>();
            List<String> currentLore = item.getMetaValue(ItemMeta.class, ItemMeta::getLore);
            for (String str : currentLore) {
                lore.add(str.replace("%prev_color%", "§" + prev.getChatChar())
                        .replace("%prev_name%", WordUtils.capitalize(prev.name().toLowerCase()))
                        .replace("%curr_color%", "§" + typeID.getChatChar())
                        .replace("%curr_name%", WordUtils.capitalize(typeID.name().toLowerCase()))
                        .replace("%next_color%", "§" + next.getChatChar())
                        .replace("%next_name%", WordUtils.capitalize(next.name().toLowerCase())));
            }

            item.withLore(lore);
        }
        return item;
    }

    @Override
    public List<ItemBuilder> getDefaultItems() {
        List<ItemBuilder> items = new ArrayList<>();
        for (DyeColorWrapper color : DyeColorWrapper.values()) {
            String name = color.name();
            if (name.equalsIgnoreCase("SILVER")
                    && (ServerVersion.getVersion() == ServerVersion.v1_13_R1
                    || ServerVersion.getVersion() == ServerVersion.v1_13_R2)) {
                name = "LIGHT_GRAY";
            }
            ItemBuilder item = new ItemBuilder(DataConverter.getMaterial(name + "_SHULKER_BOX"));
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
