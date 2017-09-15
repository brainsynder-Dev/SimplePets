package simplepets.brainsynder.loaders.list;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import simplepets.brainsynder.loaders.ItemLoader;

import java.io.File;

public class PreviousPageLoader extends ItemLoader {
    public PreviousPageLoader(File file) {
        super(file);
    }

    @Override
    public void loadDefaults() {
        defaults.put("Slot-Number", "46");
        defaults.put("MaterialName", "ARROW");
        defaults.put("MaterialData", "0");
        defaults.put("MaterialAmount", "1");
        defaults.put("DisplayName", "&6&l<&m----");
        JSONArray lore = new JSONArray();
        lore.add("&7Click Here to go");
        lore.add("&7the previous page");
        defaults.put("DisplayLoreEnabled", "true");
        defaults.put("DisplayLore", lore);
        defaults.put("FakeEnchanted", "false");
        JSONObject custom = new JSONObject();
        custom.put("Enabled", "true");
        custom.put("SkullOwner", "SimpleAPI");
        custom.put("TextureURL", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjI1OTliZDk4NjY1OWI4Y2UyYzQ5ODg1MjVjOTRlMTlkZGQzOWZhZDA4YTM4Mjg0YTE5N2YxYjcwNjc1YWNjIn19fQ==");
        defaults.put("CustomSkull", custom);
    }
}
