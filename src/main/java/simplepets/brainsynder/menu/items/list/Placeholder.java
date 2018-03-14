package simplepets.brainsynder.menu.items.list;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import simplepets.brainsynder.menu.items.Item;
import simplepets.brainsynder.wrapper.MaterialWrapper;

import java.io.File;

public class Placeholder extends Item {
    public Placeholder(File file) {
        super(file);
    }

    @Override
    public void loadDefaults() {
        defaults.put(MATERIAL, MaterialWrapper.STAINED_GLASS_PANE.name());
        defaults.put(DATA, "8");
        defaults.put(AMOUNT, "1");
        defaults.put(DISPLAY_NAME, " ");
        defaults.put(LORE_ENABLED, "false");
        defaults.put(LORE, new JSONArray());
        JSONObject custom = new JSONObject();
        custom.put(ENABLED, "false");
        custom.put(SKULL_OWNER, "SimpleAPI");
        custom.put(TEXTURE_URL, "");
        defaults.put(CUSTOM_SKULL, custom);
    }
}
