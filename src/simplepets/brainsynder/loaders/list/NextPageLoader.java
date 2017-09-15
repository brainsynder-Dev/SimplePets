package simplepets.brainsynder.loaders.list;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import simplepets.brainsynder.loaders.ItemLoader;

import java.io.File;

public class NextPageLoader extends ItemLoader {
    public NextPageLoader(File file) {
        super(file);
    }

    @Override
    public void loadDefaults() {
        defaults.put("Slot-Number", "54");
        defaults.put("MaterialName", "ARROW");
        defaults.put("MaterialData", "0");
        defaults.put("MaterialAmount", "1");
        defaults.put("DisplayName", "&6&l&m----&6&l>");
        JSONArray lore = new JSONArray();
        lore.add("&7Click Here to go");
        lore.add("&7the next page");
        defaults.put("DisplayLoreEnabled", "true");
        defaults.put("DisplayLore", lore);
        defaults.put("FakeEnchanted", "false");
        JSONObject custom = new JSONObject();
        custom.put("Enabled", "true");
        custom.put("SkullOwner", "SimpleAPI");
        custom.put("TextureURL", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzJmOTEwYzQ3ZGEwNDJlNGFhMjhhZjZjYzgxY2Y0OGFjNmNhZjM3ZGFiMzVmODhkYjk5M2FjY2I5ZGZlNTE2In19fQ==");
        defaults.put("CustomSkull", custom);
    }
}
