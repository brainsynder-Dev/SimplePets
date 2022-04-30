package simplepets.brainsynder.api.inventory;

import lib.brainsynder.files.JsonFile;
import lib.brainsynder.json.JsonArray;
import lib.brainsynder.json.JsonObject;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import simplepets.brainsynder.api.inventory.handler.InventoryType;
import simplepets.brainsynder.api.plugin.SimplePets;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.debug.DebugLevel;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public abstract class CustomInventory extends JsonFile {
    private int size = 0;
    private String title;
    private boolean enabled = true;
    protected Map<String, Integer> pageSave = new HashMap<>();
    private final Map<Integer, Item> slots = new HashMap<>();

    public CustomInventory(File file) {
        super(file);
    }

    public void reset (PetUser owner) {
        if (owner == null) return;
        Player player = owner.getPlayer();
        if (player == null) player = Bukkit.getPlayer(owner.getOwnerUUID());
        if (player == null) return;

        pageSave.remove(player.getName());
    }

    @Override
    public void loadDefaults() {
        if (hasKey("enabled")) enabled = getBoolean("enabled");

        title = getString("title");
        size = getInteger("size");

        JsonArray slots = (JsonArray) getValue("slots");
        slots.forEach(jsonValue -> {
            JsonObject json = (JsonObject) jsonValue;
            try {
                int slot = json.getInt("slot", -1);
                String namespace = json.getString("item", "-unknown-");
                if (slot == 0) {
                    SimplePets.getDebugLogger().debug(DebugLevel.ERROR, "Slot can not be 0 for item '"+namespace+"'");
                    return;
                }
                if (slot == -1) {
                    SimplePets.getDebugLogger().debug(DebugLevel.ERROR, "Invalid slot for item '"+namespace+"'");
                    return;
                }

                if (namespace.equalsIgnoreCase("-unknown-")) {
                    SimplePets.getDebugLogger().debug(DebugLevel.ERROR, "Invalid item for slot '"+slot+"'");
                    return;
                }

                SimplePets.getItemHandler().getItem(namespace).ifPresent(item -> {
                    CustomInventory.this.slots.put((slot - 1), item);
                });
            } catch (NumberFormatException ignored) {
            }
        });
    }

    /**
     * What happens when the player clicks an item in the Inventory
     *
     * @param slot - Slot clicked
     * @param item - Item being clicked
     * @param player - Player clicking the item
     */
    public abstract void onClick(int slot, ItemStack item, Player player);

    /**
     * Gets the current page for the user
     *
     * @param user
     * @return
     *      Default: 1
     */
    public int getCurrentPage(PetUser user) {
        return pageSave.getOrDefault(user.getPlayer().getName(), 1);
    }

    public Map<Integer, Item> getSlots() {
        if ((slots == null) || (slots.isEmpty())) {
            JsonArray slots = (JsonArray) getValue("slots");
            slots.forEach(jsonValue -> {
                JsonObject json = (JsonObject) jsonValue;
                try {
                    int slot = json.getInt("slot", -1);
                    String namespace = json.getString("item", "-unknown-");
                    if (slot == 0) {
                        SimplePets.getDebugLogger().debug(DebugLevel.ERROR, "Slot can not be 0 for item '"+namespace+"'");
                        return;
                    }
                    if (slot == -1) {
                        SimplePets.getDebugLogger().debug(DebugLevel.ERROR, "Invalid slot for item '"+namespace+"'");
                        return;
                    }

                    if (namespace.equalsIgnoreCase("-unknown-")) {
                        SimplePets.getDebugLogger().debug(DebugLevel.ERROR, "Invalid item for slot '"+slot+"'");
                        return;
                    }

                    SimplePets.getItemHandler().getItem(namespace).ifPresent(item -> {
                        CustomInventory.this.slots.put((slot - 1), item);
                    });
                } catch (NumberFormatException ignored) {
                }
            });
        }

        return slots;
    }

    public abstract InventoryType getInventoryType ();

    /**
     * Size of the inventory
     */
    public int getSize() {
        return getInteger("size", 9);
    }

    /**
     * Title of the inventory
     */
    public String getTitle() {
        return getString("title", "UNKNOWN");
    }

    /**
     * Opens the Inventory
     *
     * @param user - User to have the gui opened for
     */
    public void open(PetUser user) {
        open(user, 1);
    }

    /**
     * Opens the Inventory
     *
     * @param user - User to have the gui opened for
     * @param page
     *      - The page to open the GUI to
     *      Default: 1
     */
    public void open(PetUser user, int page) {}

    /**
     * Updates the currently opened GUI with updated items
     *
     * @param user
     */
    public void update (PetUser user) {}

    /**
     * Is the inventory enabled?
     */
    public boolean isEnabled() {
        return getBoolean("enabled", true);
    }
}
