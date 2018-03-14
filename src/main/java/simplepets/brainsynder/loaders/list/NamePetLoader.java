package simplepets.brainsynder.loaders.list;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import simplepets.brainsynder.loaders.ItemLoader;

import java.io.File;

public class NamePetLoader extends ItemLoader {
    public NamePetLoader(File file) {
        super(file);
    }

    @Override
    public void loadDefaults() {
        defaults.put("Slot-Number", "5");
        defaults.put("MaterialName", "NAME_TAG");
        defaults.put("MaterialData", "0");
        defaults.put("MaterialAmount", "1");
        defaults.put("DisplayName", "&a&lName Pet");
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
