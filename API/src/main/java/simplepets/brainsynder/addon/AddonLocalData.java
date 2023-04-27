package simplepets.brainsynder.addon;

import com.google.common.collect.Lists;
import lib.brainsynder.json.JsonArray;
import lib.brainsynder.json.JsonObject;
import lib.brainsynder.json.JsonValue;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AddonLocalData {
    private final File file;
    private final String name;
    private final List<String> authors;
    private final int supportedBuild;
    private final String version;
    private final List<String> description = Lists.newArrayList();
    private final List<String> classChecks = Lists.newArrayList();
    private final List<SupportData> pluginSupport = Lists.newArrayList();

    public AddonLocalData(File file, JsonObject json) {
        this.file = file;
        this.name = json.getString("name", "UNKNOWN");
        this.authors = new ArrayList<>();
        if (json.names().contains("author")) {
            authors.add(json.getString("author", "UNKNOWN"));
        }else if (json.names().contains("authors")) {
            ((JsonArray)json.get("authors")).forEach(jsonValue -> authors.add(jsonValue.asString()));
        }
        this.version = String.valueOf(json.getDouble("version", 0.0));
        this.supportedBuild = json.getInt("supported-build", -1);

        if (json.names().contains("description")) {
            JsonValue value = json.get("description");
            if (value instanceof JsonArray) {
                ((JsonArray)value).forEach(jsonValue -> description.add(jsonValue.asString()));
            }else{
                description.add(value.asString());
            }
        }

        if (json.names().contains("class-checks")) {
            JsonValue value = json.get("class-checks");
            if (value instanceof JsonArray) {
                ((JsonArray)value).forEach(jsonValue -> classChecks.add(jsonValue.asString()));
            }else{
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

    public File getFile() {
        return file;
    }

    public String getVersion() {
        return version;
    }

    public List<String> getAuthors() {
        return authors;
    }

    public List<String> getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public int getSupportedBuild() {
        return supportedBuild;
    }

    public List<String> getClassChecks() {
        return classChecks;
    }

    public List<SupportData> getPluginSupport() {
        return pluginSupport;
    }

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
