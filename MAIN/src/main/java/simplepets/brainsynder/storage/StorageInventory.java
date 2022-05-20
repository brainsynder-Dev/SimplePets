package simplepets.brainsynder.storage;

import lib.brainsynder.nbt.StorageTagCompound;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.inventory.Inventory;
import org.bukkit.util.StringUtil;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.plugin.config.ConfigOption;
import simplepets.brainsynder.menu.inventory.holders.StorageHolder;

public class StorageInventory {
    private StorageTagCompound rawCompound;
    private Inventory inventory;

    public static StorageInventory fromCompound (StorageTagCompound compound) {
        StorageInventory storageInventory = new StorageInventory();
        storageInventory.inventory = Bukkit.createInventory(
                new StorageHolder(),
                getInventorySize(compound.getEnum("type", PetType.class)),
                ""
        );
        return storageInventory;
    }

    public static int getInventorySize (PetType type) {
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
