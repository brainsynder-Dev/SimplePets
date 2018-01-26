package simplepets.brainsynder.menu.items.list;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import simple.brainsynder.utils.ObjectPager;
import simplepets.brainsynder.menu.inventory.CustomInventory;
import simplepets.brainsynder.menu.inventory.InvLoaders;
import simplepets.brainsynder.menu.inventory.list.DataMenu;
import simplepets.brainsynder.menu.inventory.list.SelectionMenu;
import simplepets.brainsynder.menu.items.CustomItem;
import simplepets.brainsynder.player.PetOwner;
import simplepets.brainsynder.storage.PetTypeStorage;

import java.io.File;

public class PreviousPage extends CustomItem {
    public PreviousPage(File file) {
        super(file);
    }

    @Override
    public void loadDefaults() {
        defaults.put(MATERIAL, "ARROW");
        defaults.put(DATA, "0");
        defaults.put(AMOUNT, "1");
        defaults.put(DISPLAY_NAME, "&6&l<&m----");
        JSONArray lore = new JSONArray();
        lore.add("&7Click Here to go");
        lore.add("&7the previous page");
        defaults.put(LORE_ENABLED, "true");
        defaults.put(LORE, lore);
        JSONObject custom = new JSONObject();
        custom.put(ENABLED, "true");
        custom.put(SKULL_OWNER, "SimpleAPI");
        custom.put(TEXTURE_URL, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjI1OTliZDk4NjY1OWI4Y2UyYzQ5ODg1MjVjOTRlMTlkZGQzOWZhZDA4YTM4Mjg0YTE5N2YxYjcwNjc1YWNjIn19fQ==");
        defaults.put(CUSTOM_SKULL, custom);
    }

    @Override
    public void onClick(PetOwner owner, CustomInventory inventory) {
        if (inventory instanceof DataMenu) {
            InvLoaders.SELECTION.open(owner);
            return;
        }

        if (inventory instanceof SelectionMenu) {
            SelectionMenu menu = (SelectionMenu)inventory;
            ObjectPager<PetTypeStorage> pages = menu.getPages(owner);
            int current = menu.getCurrentPage(owner);

            if (pages == null) return;

            if (current > 1) menu.open(owner, (current-1));
        }
    }
}
