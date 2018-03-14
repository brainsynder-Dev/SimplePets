package simplepets.brainsynder.menu.items.list;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
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
        defaults.put("__COMMENT__", "This is an example of how the 'Custom Items' work");
        defaults.put(NAMESPACE, "flameon");
        defaults.put(MATERIAL, "FLINT_AND_STEEL");
        defaults.put(DATA, "0");
        defaults.put(AMOUNT, "1");
        defaults.put(DISPLAY_NAME, "&c&lBurn Baby Burn");
        defaults.put(LORE_ENABLED, "false");
        defaults.put(LORE, new JSONArray());

        JSONArray array = new JSONArray();
        array.add("particle flame {location} 1.0 1.0 1.0 0.0 20");
        defaults.put(COMMANDS, array);

        JSONObject custom = new JSONObject();
        custom.put(ENABLED, "false");
        custom.put(SKULL_OWNER, "SimpleAPI");
        custom.put(TEXTURE_URL, "");
        defaults.put(CUSTOM_SKULL, custom);
    }

    @Override
    public void onClick(PetOwner owner, CustomInventory inventory) {
        super.onClick(owner, inventory);
        runCommands(owner);
    }
}
