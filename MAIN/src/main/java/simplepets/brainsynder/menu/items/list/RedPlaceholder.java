package simplepets.brainsynder.menu.items.list;

import lib.brainsynder.item.ItemBuilder;
import simplepets.brainsynder.menu.items.Item;

import java.io.File;

public class RedPlaceholder extends Item {
    public RedPlaceholder(File file) {
        super(file);
    }

    @Override
    public ItemBuilder getDefaultItem() {
        return lib.brainsynder.nms.DataConverter.getColoredMaterial(lib.brainsynder.nms.DataConverter.MaterialType.STAINED_GLASS_PANE, 14).withName(" ");
    }
}
