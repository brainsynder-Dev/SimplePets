package simplepets.brainsynder.utils;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class InventoryStorage implements ConfigurationSerializable {
    @Getter
    private Inventory inventory;

    public InventoryStorage(Inventory inventory) {
        this.inventory = inventory;
    }

    public InventoryStorage(Map<String, Object> config) {
        if (config.isEmpty()) {
            this.inventory = null;
        } else {
            inventory = Bukkit.createInventory(null, (int) config.get("size"), (String) config.get("title"));
            Map<String, Object> items = ((MemorySection) config.get("items")).getValues(false);
            for (String slot : items.keySet()) {
                ItemStack item = (ItemStack) items.get(slot);
                inventory.setItem(Integer.parseInt(slot), item);
            }
        }
    }

    public InventoryStorage(InventoryHolder holder, Map<String, Object> config) {
        if (config.isEmpty()) {
            this.inventory = null;
        } else {
            inventory = Bukkit.createInventory(holder, (int) config.get("size"), (String) config.get("title"));
            Map<String, Object> items = ((MemorySection) config.get("items")).getValues(false);
            for (String slot : items.keySet()) {
                ItemStack item = (ItemStack) items.get(slot);
                inventory.setItem(Integer.parseInt(slot), item);
            }
        }
    }

    public static InventoryStorage deserialize(Map<String, Object> config) {
        return new InventoryStorage(config);
    }

    @Override
    public Map<String, Object> serialize() {
        Map map = new HashMap();
        if (inventory == null)
            return map;

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
}
