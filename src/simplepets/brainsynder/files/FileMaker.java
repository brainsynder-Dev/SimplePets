package simplepets.brainsynder.files;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import simple.brainsynder.storage.IStorage;
import simple.brainsynder.storage.StorageList;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class FileMaker {
    private File file;
    private FileConfiguration configuration;

    public FileMaker(Plugin plugin, String directory, String fileName) {
        try {
            File folder = new File(plugin.getDataFolder().toString() + File.separator + directory);
            if (!folder.exists()) folder.mkdirs();

            file = new File(folder, fileName);
            if (!file.exists()) file.createNewFile();

            new File(plugin.getDataFolder().toString() + File.separator + directory, fileName);
            this.configuration = YamlConfiguration.loadConfiguration(file);
        } catch (Throwable ignored) {
        }
    }

    public FileMaker(File folder, String fileName) {
        try {
            if (!folder.exists()) folder.mkdirs();

            file = new File(folder, fileName);
            if (!file.exists()) file.createNewFile();

            this.file = new File(folder, fileName);
            this.configuration = YamlConfiguration.loadConfiguration(file);
        } catch (Throwable ignored) {
        }
    }

    public FileMaker(Plugin plugin, String fileName) {
        file = new File(plugin.getDataFolder(), fileName);
        if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
        if (!file.exists()) try {
            file.createNewFile();
        } catch (IOException ignored) {
        }
        this.configuration = YamlConfiguration.loadConfiguration(file);
    }

    public String getString(String tag, boolean color) {
        this.configuration = YamlConfiguration.loadConfiguration(file);
        return this.configuration.get(tag) != null ? (color ? this.translate(this.configuration.getString(tag)) : this.configuration.getString(tag)) : tag;
    }

    public String getString(String tag) {
        this.configuration = YamlConfiguration.loadConfiguration(file);
        return this.configuration.get(tag) != null ? this.configuration.getString(tag) : tag;
    }

    public ItemStack getItemStack(String tag) {
        this.configuration = YamlConfiguration.loadConfiguration(file);
        return this.configuration.getItemStack(tag);
    }

    public boolean getBoolean(String tag) {
        this.configuration = YamlConfiguration.loadConfiguration(file);
        return this.configuration.get(tag) != null && this.configuration.getBoolean(tag);
    }

    public int getInt(String tag) {
        this.configuration = YamlConfiguration.loadConfiguration(file);
        return this.configuration.get(tag) != null ? this.configuration.getInt(tag) : 0;
    }

    public double getDouble(String tag) {
        this.configuration = YamlConfiguration.loadConfiguration(file);
        return this.configuration.get(tag) != null ? this.configuration.getDouble(tag) : 0.0D;
    }

    public Set<String> getKeys(boolean tag) {
        this.configuration = YamlConfiguration.loadConfiguration(file);
        return this.configuration.getKeys(tag);
    }

    public List<String> getStringList(String tag) {
        this.configuration = YamlConfiguration.loadConfiguration(file);
        return (List) (this.isSet(tag) ? this.configuration.getStringList(tag) : new ArrayList());
    }

    public IStorage<String> getStorageList(String tag) {
        return (IStorage<String>) new StorageList(this.getStringList(tag));
    }

    public boolean isSet(String tag) {
        this.configuration = YamlConfiguration.loadConfiguration(file);
        return this.configuration.get(tag) != null;
    }

    public ConfigurationSection getSection(String tag) {
        this.configuration = YamlConfiguration.loadConfiguration(file);
        return this.configuration.getConfigurationSection(tag);
    }

    private String translate(String msg) {
        return msg.replace("&", "§");
    }

    public void set(String tag, Object data) {
        configuration.set(tag, data);
        try {
            configuration.save(file);
            //this.simpleConfig.reloadConfig();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void set(String tag, Object data, String... comments) {
        set(tag, data);
    }

    public void setHeader(String... header) {
        this.configuration.options().header(Arrays.toString(header));
    }

    public Map<String, Object> getConfigSectionValue(Object o) {
        return this.getConfigSectionValue(o, false);
    }

    public Map<String, Object> getConfigSectionValue(Object o, boolean deep) {
        this.configuration = YamlConfiguration.loadConfiguration(file);
        Map<String, Object> map = new HashMap();
        if (o == null) {
            return map;
        } else {
            if (o instanceof ConfigurationSection) {
                map = ((ConfigurationSection) o).getValues(deep);
            } else if (o instanceof Map) {
                map = (Map) o;
            }

            return map;
        }
    }

    public void set(boolean checkNull, String tag, Object data) {
        if (checkNull) {
            if (!this.isSet(tag)) {
                this.set(tag, data);
            }
        } else {
            this.set(tag, data);
        }

    }

    public void set(boolean checkNull, String tag, Object data, String... comments) {
        if (checkNull) {
            if (!this.isSet(tag)) {
                this.set(tag, data, comments);
            }
        } else {
            this.set(tag, data, comments);
        }

    }
}