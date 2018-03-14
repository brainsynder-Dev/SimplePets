package simplepets.brainsynder.menu.items.list;

import org.bukkit.Material;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import simplepets.brainsynder.menu.inventory.CustomInventory;
import simplepets.brainsynder.menu.items.Item;
import simplepets.brainsynder.player.PetOwner;

import java.io.File;

public class Ride extends Item {
    public Ride(File file) {
        super(file);
    }

    @Override
    public void loadDefaults() {
        defaults.put(MATERIAL, Material.DIAMOND_BARDING.toString());
        defaults.put(DATA, "0");
        defaults.put(AMOUNT, "1");
        defaults.put(DISPLAY_NAME, "&e&lToggle Pet Riding");
        defaults.put(LORE_ENABLED, "false");
        defaults.put(LORE, new JSONArray());
        JSONObject custom = new JSONObject();
        custom.put(ENABLED, "false");
        custom.put(SKULL_OWNER, "SimpleAPI");
        custom.put(TEXTURE_URL, "");
        defaults.put(CUSTOM_SKULL, custom);
    }

    @Override
    public void onClick(PetOwner owner, CustomInventory inventory) {
        if (owner.hasPet())
            owner.getPet().toggleRiding();
    }
}
