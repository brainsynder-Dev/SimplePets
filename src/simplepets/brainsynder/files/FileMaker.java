package simplepets.brainsynder.files;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import simple.brainsynder.exceptions.SimpleAPIException;
import simple.brainsynder.files.other.SimpleConfig;
import simple.brainsynder.files.other.SimpleConfigManager;
import simple.brainsynder.storage.IStorage;
import simple.brainsynder.storage.StorageList;
import simple.brainsynder.utils.Valid;

import java.io.File;
import java.util.*;

public class FileMaker {
    private String _fileName;
    private SimpleConfig simpleConfig;
    private File file;
    private FileConfiguration configuration;

    public FileMaker(Plugin plugin, String directory, String fileName) {
        try {
            File file = new File(directory, fileName);
            if (!file.exists()) {
                file.mkdirs();
                file.createNewFile();
            }

            SimpleConfigManager manager = new SimpleConfigManager((JavaPlugin)plugin);
            this.simpleConfig = manager.getNewConfig(directory + '/' + fileName);
            new File(plugin.getDataFolder().toString() + File.separator + directory, fileName);
            this.configuration = this.simpleConfig.getConfig();
        } catch (Throwable ignored) {}
    }

    public FileMaker(File directory, String fileName) {
        throw new SimpleAPIException("Plugins that use SimpleAPI on here, do not yet support the new FileMaker code.");
    }

    public FileMaker(Plugin plugin, String fileName) {
        SimpleConfigManager manager = new SimpleConfigManager((JavaPlugin)plugin);
        this.simpleConfig = manager.getNewConfig(fileName);
        this.file = new File(plugin.getDataFolder(), fileName);
        this.configuration = this.simpleConfig.getConfig();
    }

    public String getString(String tag, boolean color) {
        return this.configuration.get(tag) != null ? (color ? this.translate(this.configuration.getString(tag)) : this.configuration.getString(tag)) : tag;
    }

    public String getString(String tag) {
        return this.configuration.get(tag) != null ? this.configuration.getString(tag) : tag;
    }

    public ItemStack getItemStack(String tag) {
        return this.configuration.getItemStack(tag);
    }

    public boolean getBoolean(String tag) {
        return this.configuration.get(tag) != null && this.configuration.getBoolean(tag);
    }

    public int getInt(String tag) {
        return this.configuration.get(tag) != null ? this.configuration.getInt(tag) : 0;
    }

    public double getDouble(String tag) {
        return this.configuration.get(tag) != null ? this.configuration.getDouble(tag) : 0.0D;
    }

    public Set<String> getKeys(boolean tag) {
        return this.configuration.getKeys(tag);
    }

    public List<String> getStringList(String tag) {
        return (List)(this.isSet(tag) ? this.configuration.getStringList(tag) : new ArrayList());
    }

    public IStorage<String> getStorageList(String tag) {
        IStorage<String> iList = new StorageList(this.getStringList(tag));
        return iList;
    }

    public boolean isSet(String tag) {
        return this.configuration.get(tag) != null;
    }

    public ConfigurationSection getSection(String tag) {
        return this.configuration.getConfigurationSection(tag);
    }

    public String translate(String msg) {
        return msg.replace("&", "§");
    }

    public void set(String tag, Object data) {
        this.simpleConfig.set(tag, data);
        this.simpleConfig.saveConfig();
        this.simpleConfig.reloadConfig();
    }

    public void set(String tag, Object data, String... comments) {
        Valid.notNull(comments);
        Valid.isTrue(!Arrays.asList(comments).isEmpty());
        this.simpleConfig.set(tag, data, comments);
        this.simpleConfig.saveConfig();
        this.simpleConfig.reloadConfig();
    }

    public void setHeader(String... header) {
        this.simpleConfig.setHeader(header);
    }

    public Map<String, Object> getConfigSectionValue(Object o) {
        return this.getConfigSectionValue(o, false);
    }

    public Map<String, Object> getConfigSectionValue(Object o, boolean deep) {
        Map<String, Object> map = new HashMap();
        if (o == null) {
            return map;
        } else {
            if (o instanceof ConfigurationSection) {
                map = ((ConfigurationSection)o).getValues(deep);
            } else if (o instanceof Map) {
                map = (Map)o;
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