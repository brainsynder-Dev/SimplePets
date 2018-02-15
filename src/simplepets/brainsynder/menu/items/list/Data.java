package simplepets.brainsynder.menu.items.list;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import simplepets.brainsynder.menu.inventory.CustomInventory;
import simplepets.brainsynder.menu.inventory.InvLoaders;
import simplepets.brainsynder.menu.items.Item;
import simplepets.brainsynder.player.PetOwner;

import java.io.File;

public class Data extends Item {
    public Data(File file) {
        super(file);
    }

    @Override
    public void loadDefaults() {
        defaults.put(MATERIAL, "BONE");
        defaults.put(DATA, "0");
        defaults.put(AMOUNT, "1");
        defaults.put(DISPLAY_NAME, "&cPet Data");
        JSONArray lore = new JSONArray();
        lore.add("&7Click Here to open");
        lore.add("&7the Pet Data Menu");
        defaults.put(LORE_ENABLED, "true");
        defaults.put(LORE, lore);
        JSONObject custom = new JSONObject();
        custom.put(ENABLED, "true");
        custom.put(SKULL_OWNER, "SimpleAPI");
        custom.put(TEXTURE_URL, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODUxNGQyMjViMjYyZDg0N2M3ZTU1N2I0NzQzMjdkY2VmNzU4YzJjNTg4MmU0MWVlNmQ4YzVlOWNkM2JjOTE0In19fQ==");
        defaults.put(CUSTOM_SKULL, custom);
    }

    @Override
    public void onClick(PetOwner owner, CustomInventory inventory) {
        inventory.reset(owner);
        InvLoaders.PET_DATA.open(owner);
    }
}
