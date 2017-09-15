package simplepets.brainsynder.loaders.list;

import org.bukkit.Material;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import simplepets.brainsynder.loaders.ItemLoader;

import java.io.File;

public class RemoveLoader extends ItemLoader {
    public RemoveLoader(File file) {
        super(file);
    }

    @Override
    public void loadDefaults() {
        defaults.put("Slot-Number", "50");
        defaults.put("MaterialName", Material.BARRIER.toString());
        defaults.put("MaterialData", "0");
        defaults.put("MaterialAmount", "1");
        defaults.put("DisplayName", "&4Remove Pet");
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
