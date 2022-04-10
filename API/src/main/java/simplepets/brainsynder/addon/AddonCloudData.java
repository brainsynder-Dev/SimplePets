package simplepets.brainsynder.addon;

import com.google.common.collect.Lists;

import java.util.List;

public class AddonCloudData {
    private final String url;
    private final String name;
    private final String author;
    private String supportedVersion = "";
    private final double version;
    private List<String> description = Lists.newArrayList();

    public AddonCloudData(String url, String name, String author, double version) {
        this.url = url;
        this.name = name;
        this.author = author;
        this.version = version;
    }

    public AddonCloudData setDescription(List<String> description) {
        this.description = description;
        return this;
    }

    public AddonCloudData setSupportedVersion(String supportedVersion) {
        this.supportedVersion = supportedVersion;
        return this;
    }

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
