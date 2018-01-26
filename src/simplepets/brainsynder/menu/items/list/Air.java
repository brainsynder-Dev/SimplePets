package simplepets.brainsynder.menu.items.list;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import simplepets.brainsynder.menu.items.CustomItem;

import java.io.File;

public class Air extends CustomItem {
    public Air(File file) {
        super(file);
    }

    @Override
    public void loadDefaults() {
        defaults.put("_COMMENT_", "This not editable simply because you can not modify air :P");
    }

    @Override
    public ItemStack getItem() {
        return new ItemStack(Material.AIR);
    }
}
