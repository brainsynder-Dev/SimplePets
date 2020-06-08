package simplepets.brainsynder.menu.inventory;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.json.simple.JSONObject;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.menu.items.Item;
import simplepets.brainsynder.player.PetOwner;
import simplepets.brainsynder.storage.files.base.JSONFile;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class CustomInventory extends JSONFile {
    private int size = 0;
    private String title;
    private boolean enabled = true;
    protected Map<String, Integer> pageSave = new HashMap<>();
    private final Map<Integer, Item> slots = new HashMap<>();

    public CustomInventory(File file) {
        super(file);
    }

    public static File getLocation(PetCore core, Class<? extends CustomInventory> clazz) {
        File folder = new File(core.getDataFolder().toString() + "/Inventories/");
        return new File(folder, clazz.getSimpleName() + ".json");
    }

    public void reset (PetOwner owner) {
        Player player = Bukkit.getPlayer(owner.getUuid());
        pageSave.remove(player.getName());
    }

    public void load() {
        if (hasKey("enabled")) enabled = getBoolean("enabled");

        title = getString("title", true);
        size = getInteger("size");

        if (!getArray("slots").isEmpty()) {
            for (Object o : getArray("slots")) {
                JSONObject json = (JSONObject) o;
                try {
                    String key = String.valueOf(json.get("slot"));
                    int slot = Integer.parseInt(key);
                    Item item = PetCore.get().getItemLoaders().getLoader(String.valueOf(json.get("item")));
                    if (item == null) {
                        PetCore.get().debug(2, "Could not retrieve the item for: " + key);
                        continue;
                    }
                    slots.put((slot - 1), item);
                } catch (NumberFormatException ignored) {
                }
            }
        }
    }

    // When a player clicks a slot that is not a menu item (EG: Air or pre-determined slots (for armor on the ArmorStand))
    public void onClick(int slot, ItemStack item, Player player){}

    public int getCurrentPage(PetOwner owner) {
        Player player = Bukkit.getPlayer(owner.getUuid());
        return pageSave.getOrDefault(player.getName(), 1);
    }

    public Map<Integer, Item> getSlots() {
        return slots;
    }

    public int getSize() {
        return size;
    }

    public String getTitle() {
        return title;
    }

    public void open(PetOwner owner) {
        open(owner, 1);
    }

    public void open(PetOwner owner, int page) {}

    public boolean isEnabled() {
        return enabled;
    }
}
