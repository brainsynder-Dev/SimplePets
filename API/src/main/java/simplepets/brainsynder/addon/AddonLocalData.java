package simplepets.brainsynder.addon;

import com.google.common.collect.Lists;
import lib.brainsynder.json.JsonArray;
import lib.brainsynder.json.JsonObject;
import lib.brainsynder.json.JsonValue;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * It's a class that holds all the data from the addon.json file
 * from addons currently installed on the server
 */
public class AddonLocalData {
    private final File file;
    private final String name;
    private final List<String> authors;
    private final int supportedBuild;
    private final double version;
    private final List<String> description = Lists.newArrayList();
    private final List<String> classChecks = Lists.newArrayList();
    private final List<SupportData> pluginSupport = Lists.newArrayList();

    public AddonLocalData(File file, JsonObject json) {
        this.file = file;
        this.name = json.getString("name", "UNKNOWN");
        this.authors = new ArrayList<>();
        if (json.names().contains("author")) {
            authors.add(json.getString("author", "UNKNOWN"));
        } else if (json.names().contains("authors")) {
            ((JsonArray) json.get("authors")).forEach(jsonValue -> authors.add(jsonValue.asString()));
        }
        this.version = json.getDouble("version", 0.0);
        this.supportedBuild = json.getInt("supported-build", -1);

        if (json.names().contains("description")) {
            JsonValue value = json.get("description");
            if (value instanceof JsonArray) {
                ((JsonArray) value).forEach(jsonValue -> description.add(jsonValue.asString()));
            } else {
                description.add(value.asString());
            }
        }

        if (json.names().contains("class-checks")) {
            JsonValue value = json.get("class-checks");
            if (value instanceof JsonArray) {
                ((JsonArray) value).forEach(jsonValue -> classChecks.add(jsonValue.asString()));
            } else {
                classChecks.add(value.asString());
            }
        }

        if (json.names().contains("plugin-support")) {
            JsonArray value = (JsonArray) json.get("plugin-support");
            value.forEach(jsonValue -> {
                JsonObject object = (JsonObject) jsonValue;
                pluginSupport.add(new SupportData(object.getString("name", "Unknown"), object.getString("url", "Unknown")));
            });
        }
    }

    /**
     * This function returns the file for the addons jar.
     *
     * @return The file object.
     */
    public File getFile() {
        return file;
    }

    /**
     * This function returns the version of the addon
     *
     * @return The version of the addon.
     */
    public double getVersion() {
        return version;
    }

    /**
     * This function returns a list of strings that represent the authors of the addon.
     *
     * @return A list of strings
     */
    public List<String> getAuthors() {
        return authors;
    }

    /**
     * > This function returns a list of strings that describe the addon
     *
     * @return A list of strings.
     */
    public List<String> getDescription() {
        return description;
    }

    /**
     * This function returns the name of the addon.
     *
     * @return The name of the addon.
     */
    public String getName() {
        return name;
    }

    /**
     * This function returns the build of SimplePets that it supports
     * Example: 5.0-BUILD-163  The build it would support is 163
     *
     * @return The supported build of the addon.
     */
    public int getSupportedBuild() {
        return supportedBuild;
    }

    /**
     * This function returns a list of strings that represent class paths that need to be checked.
     * This is mostly used if you are using a Plugins API that constantly changes class names/paths
     *
     * @return A list of strings
     */
    public List<String> getClassChecks() {
        return classChecks;
    }

    /**
     * This function returns a list of plugins that are supported
     *
     * @return A list of SupportData objects.
     */
    public List<SupportData> getPluginSupport() {
        return pluginSupport;
    }

    /**
     * SupportData is a class that has two fields, name and url, and two methods, name() and url().
     */
    public static final class SupportData {
        private final String name;
        private final String url;

        public SupportData(String name, String url) {
            this.name = name;
            this.url = url;
        }

        public String name() {
            return name;
        }

        public String url() {
            return url;
        }
    }
}
