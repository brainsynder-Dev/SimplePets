package simplepets.brainsynder.menu.items.list;

import org.bukkit.Material;
import simplepets.brainsynder.menu.items.Item;
import simplepets.brainsynder.utils.ItemBuilder;

import java.io.File;

public class Placeholder extends Item {
    public Placeholder(File file) {
        super(file);
    }

    @Override
    public ItemBuilder getDefaultItem() {
        return new ItemBuilder(Material.STAINED_GLASS_PANE).withData(8).withName(" ");
    }
}
