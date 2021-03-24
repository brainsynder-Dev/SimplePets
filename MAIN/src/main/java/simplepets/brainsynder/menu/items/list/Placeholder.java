package simplepets.brainsynder.menu.items.list;

import lib.brainsynder.item.ItemBuilder;
import lib.brainsynder.utils.DyeColorWrapper;
import simplepets.brainsynder.menu.items.Item;

import java.io.File;

public class Placeholder extends Item {
    public Placeholder(File file) {
        super(file);
    }

    @Override
    public ItemBuilder getDefaultItem() {
        return lib.brainsynder.nms.DataConverter.getColoredMaterial(lib.brainsynder.nms.DataConverter.MaterialType.STAINED_GLASS_PANE, DyeColorWrapper.LIGHT_GRAY).withName(" ");
    }
}
