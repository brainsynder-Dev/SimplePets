package simplepets.brainsynder.menu.items.list;

import simplepets.brainsynder.menu.items.Item;
import simplepets.brainsynder.utils.ItemBuilder;
import simplepets.brainsynder.utils.Utilities;

import java.io.File;

public class Placeholder extends Item {
    public Placeholder(File file) {
        super(file);
    }

    @Override
    public ItemBuilder getDefaultItem() {
        return new ItemBuilder(Utilities.toMaterial(Utilities.Type.STAINED_GLASS_PANE, 8)).withName(" ");
    }
}
