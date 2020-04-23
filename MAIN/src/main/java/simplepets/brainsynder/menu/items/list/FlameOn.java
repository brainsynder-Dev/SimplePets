package simplepets.brainsynder.menu.items.list;

import lib.brainsynder.item.ItemBuilder;
import lib.brainsynder.json.JsonArray;
import org.bukkit.Material;
import simplepets.brainsynder.menu.inventory.CustomInventory;
import simplepets.brainsynder.menu.items.CustomItem;
import simplepets.brainsynder.player.PetOwner;

import java.io.File;

public class FlameOn extends CustomItem {
    public FlameOn(File file) {
        super(file);
    }

    @Override
    public void loadDefaults() {
        setDefault("__COMMENT__", "This is an example of how the 'Custom Items' work");

        JsonArray array = new JsonArray();
        array.add("particle flame {location} 1.0 1.0 1.0 0.0 20");
        setDefault(COMMANDS, array);
        defaults.add(NAMESPACE, "flameon");
        super.loadDefaults();
    }

    @Override
    public ItemBuilder getDefaultItem() {
        return new ItemBuilder(Material.FLINT_AND_STEEL).withName("&c&lBurn Baby Burn");
    }

    @Override
    public void onClick(PetOwner owner, CustomInventory inventory) {
        super.onClick(owner, inventory);
        runCommands(owner);
    }
}
