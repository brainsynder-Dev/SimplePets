package simplepets.brainsynder.files;

import com.google.common.base.Charsets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.bukkit.ChatColor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public abstract class JSONFile {
    private final Charset ENCODE = Charsets.UTF_8;
    protected HashMap<String, Object> defaults = new HashMap<>();
    private File file;
    private JSONObject json;
    private JSONParser parser = new JSONParser();

    /**
     * E.G: new File (JavaPlugin.getDataFolder(), "FILE.json")
     *
     * @param file The file where the data is kept
     */
    public JSONFile(File file) {
        this.file = file;
        reload();
    }

    /**
     * This is a method that loads the default values that you have configured.
     * To set the default values use this as an example:
     * <p>
     * super.defaults.put ("Enabled", false);
     * <p>
     * With this you can also do the JSON class functions like JSONArray or JSONObject
     */
    public void loadDefaults() {
    }

    /**
     * This right here loads the default values from the file.
     * This also makes sure the file is not empty, if it is
     * it will load some blank JSON data
     */
    public void reload() {
        try {
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            if (!file.exists()) {
                OutputStreamWriter pw = new OutputStreamWriter(new FileOutputStream(file), ENCODE);
                pw.write("{");
                pw.write("}");
                pw.flush();
                pw.close();
            }
            json = (JSONObject) parser.parse(new InputStreamReader(new FileInputStream(file), ENCODE));
            loadDefaults();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is to update/change the values of the data stored (In order to refresh the data you must use the reload method)
     *
     * @param tag   The configuration tag
     * @param value The value that the configuration tag holds
     */
    public void set(String tag, Object value) {
        json.put(tag, value);
    }

    /**
     * This saves the values of the file so that they are in the file (*Recommended* Should be called in the onEnable and onDisable).
     *
     * @return If true, then it has saved. If not it means an error occurred.
     */
    public boolean save() {
        try {
            JSONObject toSave = new JSONObject();

            for (Map.Entry<String, Object> stringObjectEntry : defaults.entrySet()) {
                Object o = stringObjectEntry.getValue();
                if (o instanceof String) {
                    toSave.put(stringObjectEntry.getKey(), getString(stringObjectEntry.getKey(), false));
                } else if (o instanceof Integer) {
                    toSave.put(stringObjectEntry.getKey(), getInteger(stringObjectEntry.getKey()));
                } else if (o instanceof Double) {
                    toSave.put(stringObjectEntry.getKey(), getDouble(stringObjectEntry.getKey()));
                } else if (o instanceof JSONArray) {
                    toSave.put(stringObjectEntry.getKey(), getArray(stringObjectEntry.getKey()));
                } else if (o instanceof JSONObject) {
                    toSave.put(stringObjectEntry.getKey(), getObject(stringObjectEntry.getKey()));
                }
            }
            TreeMap<String, Object> treeMap = new TreeMap<>();
            treeMap.putAll(toSave);
            Gson g = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
            String prettyJsonString = g.toJson(treeMap);
            OutputStreamWriter fw = new OutputStreamWriter(new FileOutputStream(file), ENCODE);
            try {
                fw.write(prettyJsonString.replace("\u0026", "&"));
            } finally {
                fw.flush();
                fw.close();
            }
            return true;
        } catch (Exception ignored) {
            return false;
        }
    }

    private String getRawData(String key) {
        return json.containsKey(key) ? json.get(key).toString()
                : (defaults.containsKey(key) ? defaults.get(key).toString() : key);
    }

    public String getString(String key, boolean color) {
        return color ?
                ChatColor.translateAlternateColorCodes('&', getRawData(key))
                :
                getRawData(key);
    }

    public boolean getBoolean(String key) {
        return Boolean.valueOf(getRawData(key));
    }

    public double getDouble(String key) {
        try {
            return Double.parseDouble(getRawData(key));
        } catch (Exception ignored) {
        }
        return 0.0;
    }

    public int getInteger(String key) {
        try {
            return Integer.parseInt(getRawData(key));
        } catch (Exception ignored) {
        }
        return 0;
    }

    public JSONObject getObject(String key) {
        return json.containsKey(key) ? (JSONObject) json.get(key)
                : (defaults.containsKey(key) ? (JSONObject) defaults.get(key) : new JSONObject());
    }

    public JSONArray getArray(String key) {
        return json.containsKey(key) ? (JSONArray) json.get(key)
                : (defaults.containsKey(key) ? (JSONArray) defaults.get(key) : new JSONArray());
    }

    public String getRawData(JSONObject obj, String key) {
        return obj.containsKey(key) ? obj.get(key).toString() : key;
    }

    public String getString(JSONObject obj, String key) {
        return getRawData(obj, key);
    }

    public String getColorString(JSONObject obj, String key) {
        return ChatColor.translateAlternateColorCodes('&', getRawData(obj, key));
    }

    public boolean getBoolean(JSONObject obj, String key) {
        return Boolean.parseBoolean(getRawData(obj, key));
    }

    public double getDouble(JSONObject obj, String key) {
        try {
            return Double.parseDouble(getRawData(obj, key));
        } catch (Exception ignored) {
        }
        return 0.0;
    }

    public int getInteger(JSONObject obj, String key) {
        try {
            return Integer.parseInt(getRawData(obj, key));
        } catch (Exception ignored) {
        }
        return 0;
    }

    public JSONObject getObject(JSONObject obj, String key) {
        return obj.containsKey(key) ? (JSONObject) obj.get(key)
                : new JSONObject();
    }

    public JSONArray getArray(JSONObject obj, String key) {
        return obj.containsKey(key) ? (JSONArray) obj.get(key)
                : new JSONArray();
    }
}