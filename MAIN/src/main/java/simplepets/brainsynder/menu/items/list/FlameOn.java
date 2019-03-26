package simplepets.brainsynder.menu.items.list;

import org.bukkit.Material;
import org.json.simple.JSONArray;
import simple.brainsynder.api.ItemBuilder;
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

        JSONArray array = new JSONArray();
        array.add("particle flame {location} 1.0 1.0 1.0 0.0 20");
        setDefault(COMMANDS, array);
        defaults.put(NAMESPACE, "flameon");
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
