package simplepets.brainsynder.loaders.list;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import simplepets.brainsynder.loaders.ItemLoader;
import simplepets.brainsynder.wrapper.MaterialWrapper;

import java.io.File;

public class PlaceholderLoader extends ItemLoader {
    public PlaceholderLoader(File file) {
        super(file);
    }

    @Override
    public void loadDefaults() {
        defaults.put("MaterialName", MaterialWrapper.STAINED_GLASS_PANE.name());
        defaults.put("MaterialData", "8");
        defaults.put("MaterialAmount", "1");
        defaults.put("DisplayName", " ");
        JSONArray lore = new JSONArray();
        defaults.put("DisplayLoreEnabled", "false");
        defaults.put("DisplayLore", lore);
        defaults.put("FakeEnchanted", "false");
        JSONObject custom = new JSONObject();
        custom.put("Enabled", "false");
        custom.put("SkullOwner", "SimpleAPI");
        custom.put("TextureURL", "");
        defaults.put("CustomSkull", custom);
    }
}
