package simplepets.brainsynder.storage;

import lib.brainsynder.nbt.StorageTagCompound;
import lib.brainsynder.nbt.StorageTagList;
import lib.brainsynder.nbt.StorageTagTools;
import lib.brainsynder.utils.Colorize;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.StringUtil;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.plugin.config.ConfigOption;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.menu.inventory.holders.StorageHolder;

import java.util.HashMap;
import java.util.Map;

public class StorageInventory {
    private StorageTagCompound rawCompound;
    private Map<Integer, ItemStack> items;

    public static StorageInventory fromCompound (StorageTagCompound compound) {
        StorageInventory storageInventory = new StorageInventory();

        storageInventory.rawCompound = compound;
        storageInventory.items = new HashMap<>();

        StorageTagList itemList = (StorageTagList) compound.getTag("items");
        itemList.getList().forEach(base -> {
            StorageTagCompound comp = (StorageTagCompound) base;

            storageInventory.items.put(comp.getInteger("slot"), StorageTagTools.toItemStack(comp.getCompoundTag("item")));
        });
        return storageInventory;
    }

    public void openInventory (Player player, PetUser user, PetType type) {
        Inventory inventory = Bukkit.createInventory(new StorageHolder(), getInventorySize(type), getInventoryTitle(user, type));
        items.forEach(inventory::setItem);
        player.openInventory(inventory);
    }

    public StorageTagCompound toCompound () {
        StorageTagCompound compound = new StorageTagCompound();
        return compound;
    }

    private String getInventoryTitle (PetUser user, PetType type) {
        return Colorize.translateBungeeHex(ConfigOption.INSTANCE.PET_STORAGE_TITLE.getValue()
                .replace("{player}", user.getOwnerName())
                .replace("{type}", type.getName())
        );
    }

    private int getInventorySize (PetType type) {
        for (String line : ConfigOption.INSTANCE.PET_STORAGE_LIMIT.getValue()) {
            if (!StringUtil.startsWithIgnoreCase(line, type.getName())) continue;
            if (!line.contains("-")) {
                try {
                    throw new InvalidConfigurationException("'"+line+"' is missing a '-' in between the pet type and the number, Example: COW-9");
                } catch (InvalidConfigurationException e) {
                    e.printStackTrace();
                }
                break;
            }
            String[] args = line.split("-");
            if (args.length != 2) {
                try {
                    throw new InvalidConfigurationException("'"+line+"' can only have 1 '-' in between the pet type and the number, Example: COW-9");
                } catch (InvalidConfigurationException e) {
                    e.printStackTrace();
                }
                break;
            }

            try {
                return Integer.parseInt(args[1]);
            }catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        return ConfigOption.INSTANCE.PET_STORAGE_DEFAULT_SIZE.getValue();
    }

}
