package simplepets.brainsynder.menu.items.list;

import simple.brainsynder.api.ItemBuilder;
import simplepets.brainsynder.menu.items.Item;

import java.io.File;

public class Placeholder extends Item {
    public Placeholder(File file) {
        super(file);
    }

    @Override
    public ItemBuilder getDefaultItem() {
        return ItemBuilder.getColored(simple.brainsynder.utils.MatType.STAINED_GLASS_PANE, 8).withName(" ");
    }
}
