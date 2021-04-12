package simplepets.brainsynder.addon;

import com.google.common.collect.Lists;

import java.util.List;

public class AddonData {
    private final String url = "UNKNOWN";
    private final String name = "UNKNOWN";
    private final String author = "UNKNOWN";
    private final String supportedVersion = "";
    private final double version = 0.0;
    private final List<String> description = Lists.newArrayList();

    public String getUrl() {
        return url;
    }

    public String getName() {
        return name;
    }

    public double getVersion() {
        return version;
    }

    public List<String> getDescription() {
        return description;
    }

    public String getSupportedVersion() {
        return supportedVersion;
    }

    public String getAuthor() {
        return author;
    }
}
