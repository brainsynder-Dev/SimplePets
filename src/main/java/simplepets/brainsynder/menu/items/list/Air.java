package simplepets.brainsynder.menu.items.list;

import org.bukkit.Material;
import simplepets.brainsynder.menu.items.Item;
import simplepets.brainsynder.utils.ItemBuilder;

import java.io.File;

public class Air extends Item {
    public Air(File file) {
        super(file);
    }

    @Override
    public void loadDefaults() {
        defaults.put("_COMMENT_", "This not editable simply because you can not modify air :P");
        super.loadDefaults();
    }

    @Override
    public ItemBuilder getItemBuilder() {
        return new ItemBuilder(Material.AIR);
    }

    @Override
    public ItemBuilder getDefaultItem() {
        return new ItemBuilder(Material.AIR);
    }
}
