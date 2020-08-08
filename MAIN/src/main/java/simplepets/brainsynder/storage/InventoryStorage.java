package simplepets.brainsynder.storage;

import lib.brainsynder.nbt.StorageTagCompound;
import lib.brainsynder.nbt.StorageTagList;
import lib.brainsynder.nbt.StorageTagString;
import lib.brainsynder.utils.Base64Wrapper;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import simplepets.brainsynder.PetCore;

import java.util.HashMap;
import java.util.Map;

public class InventoryStorage implements ConfigurationSerializable {
    private final Inventory inventory;
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
            if (config.containsKey("title")) title = String.valueOf(config.get("title"));
            inventory = Bukkit.createInventory(holder, PetCore.get().getConfiguration().getInt("PetItemStorage.Inventory-Size"), title);
            if (config.containsKey("items")) {
                Map<Integer, Object> items = (Map<Integer, Object>) config.get("items");
                if (!items.isEmpty()) {
                    for (Integer slot : items.keySet()) {
                        if (slot > (inventory.getSize())) break;
                        ItemStack item = (ItemStack) items.get(slot);
                        inventory.setItem(slot, item);
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

        map.put("title", title);
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

    public StorageTagCompound toCompound() {
        StorageTagCompound compound = new StorageTagCompound();
        if (inventory == null) return compound;

        compound.setString("title", title);
        compound.setInteger("size", inventory.getSize());
        StorageTagList items = new StorageTagList();
        int slot = 0;
        for (ItemStack item : inventory.getContents()) {
            if ((item != null) && (item.getType() != Material.AIR)) {
                StorageTagCompound json = new StorageTagCompound();
                json.setInteger("slot", slot);
                json.setItemStack("stack", item);
                items.appendTag(json);
            }
            slot++;
        }
        compound.setTag("items", items);
        return compound;
    }

    public static InventoryStorage fromCompound (InventoryHolder holder, StorageTagCompound compound) {
        Map<String, Object> map = new HashMap<>();
        Map<Integer, Object> items = new HashMap();
        map.put("title", compound.getString("title"));
        map.put("size", compound.getInteger("size", 27));
        if (compound.hasKey("items")) {
            StorageTagList array = (StorageTagList) compound.getTag("items");
            if (!array.getList().isEmpty()) {
                array.getList().forEach(storageBase -> {
                    StorageTagCompound item = (StorageTagCompound) storageBase;
                    ItemStack stack;

                    if (item.getTag("stack") instanceof StorageTagString) {
                        stack = PetCore.get().getUtilities().stringToItem(Base64Wrapper.decodeString(String.valueOf(item.getString("stack"))));
                    }else{
                        stack = item.getItemStack("stack");
                    }

                    items.put(item.getInteger("slot"), stack);
                });
            }
        }
        map.put("items", items);
        return deserialize(holder, map);
    }

    public Inventory getInventory() {return this.inventory;}
}
