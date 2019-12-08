package simplepets.brainsynder.menu.items.list;

import simple.brainsynder.api.ItemBuilder;
import simplepets.brainsynder.menu.items.Item;

import java.io.File;

public class RedPlaceholder extends Item {
    public RedPlaceholder(File file) {
        super(file);
    }

    @Override
    public ItemBuilder getDefaultItem() {
        return ItemBuilder.getColored(simple.brainsynder.utils.MatType.STAINED_GLASS_PANE, 14).withName(" ");
    }
}
