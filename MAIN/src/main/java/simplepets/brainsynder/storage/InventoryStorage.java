package simplepets.brainsynder.storage;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import simple.brainsynder.utils.Base64Wrapper;
import simplepets.brainsynder.PetCore;

import java.util.HashMap;
import java.util.Map;

public class InventoryStorage implements ConfigurationSerializable {
    private Inventory inventory;
    private String title = "UNKNOWN";

    public InventoryStorage(Inventory inventory, String title) {
        this.inventory = inventory;
        this.title = title;
    }

    public InventoryStorage(Map<String, Object> config) {
        if (config.isEmpty()) {
            this.inventory = null;
        } else {
            if (config.containsKey("title")) title = String.valueOf(config.get("title"));
            inventory = Bukkit.createInventory(null, PetCore.get().getConfiguration().getInt("PetItemStorage.Inventory-Size"), title);
            Map<String, Object> items = ((MemorySection) config.get("items")).getValues(false);
            if (!items.isEmpty()) {
                for (String slot : items.keySet()) {
                    if (Integer.parseInt(slot) >= (inventory.getSize()-1)) break;
                    ItemStack item = (ItemStack) items.get(slot);
                    inventory.setItem(Integer.parseInt(slot), item);
                }
            }
        }
    }

    public InventoryStorage(InventoryHolder holder, Map<String, Object> config) {
        if (config.isEmpty()) {
            this.inventory = null;
        } else {
            inventory = Bukkit.createInventory(holder, PetCore.get().getConfiguration().getInt("PetItemStorage.Inventory-Size"), String.valueOf(config.get("title")));
            if (config.containsKey("items")) {
                Map<String, Object> items = (Map<String, Object>) config.get("items");
                if (!items.isEmpty()) {
                    for (String slot : items.keySet()) {
                        if (Integer.parseInt(slot) >= (inventory.getSize()-1)) break;

                        ItemStack item = (ItemStack) items.get(slot);
                        inventory.setItem(Integer.parseInt(slot), item);
                    }
                }
            }
        }
    }

    public static InventoryStorage deserialize(Map<String, Object> config) {
        return new InventoryStorage(config);
    }

    public static InventoryStorage deserialize(InventoryHolder holder, Map<String, Object> config) {
        return new InventoryStorage(holder, config);
    }

    @Override
    public Map<String, Object> serialize() {
        Map map = new HashMap();
        if (inventory == null) return map;

        map.put("title", inventory.getTitle());
        map.put("size", inventory.getSize());
        Map<Integer, Object> items = new HashMap();
        int slot = 0;
        for (ItemStack item : inventory.getContents()) {
            items.put(slot, item);
            slot++;
        }
        map.put("items", items);
        return map;
    }

    public JSONObject toJSON() {
        JSONObject map = new JSONObject();
        if (inventory == null) return map;

        map.put("title", title);
        map.put("size", inventory.getSize());
        JSONArray items = new JSONArray();
        int slot = 0;
        for (ItemStack item : inventory.getContents()) {
            if ((item != null) && (item.getType() != Material.AIR)) {
                JSONObject json = new JSONObject();
                json.put("slot", String.valueOf(slot));
                json.put("stack", Base64Wrapper.encodeString(String.valueOf(PetCore.get().getUtilities().itemToString(item))));
                items.add(json);
            }
            slot++;
        }
        map.put("items", items);
        return map;
    }

    public static InventoryStorage fromJSON (InventoryHolder holder, JSONObject json) {
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> items = new HashMap();
        map.put("title", json.get("title"));
        map.put("size", json.getOrDefault("size", 27));
        if (json.containsKey("items")) {
            JSONArray array = (JSONArray) json.get("items");
            if (!array.isEmpty()) {
                for (Object obj : array) {
                    JSONObject inner = (JSONObject) obj;
                    items.put(String.valueOf(inner.get("slot")), PetCore.get().getUtilities().stringToItem(Base64Wrapper.decodeString(String.valueOf(inner.get("stack")))));
                }
            }
        }
        map.put("items", items);
        return deserialize(holder, map);
    }

    public Inventory getInventory() {return this.inventory;}
}
