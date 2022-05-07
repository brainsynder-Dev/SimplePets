package simplepets.brainsynder.addon;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * It's a class that holds data about an addon that is not installed on the server
 * but instead is in our addon database
 */
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

    /**
     * This function sets the description of the addon
     *
     * @param description A list of strings that describe the addon.
     * @return The AddonCloudData object is being returned.
     */
    public AddonCloudData setDescription(List<String> description) {
        this.description = description;
        return this;
    }

    /**
     * It sets the supported version of the addon.
     *
     * @param supportedVersion The version of the addon that is supported by the cloud.
     * @return The AddonCloudData object.
     */
    public AddonCloudData setSupportedVersion(String supportedVersion) {
        this.supportedVersion = supportedVersion;
        return this;
    }

    /**
     * This function returns the download url
     *
     * @return The url of the website.
     */
    public String getUrl() {
        return url;
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
     * This function returns the version of the addon
     *
     * @return The version of the addon.
     */
    public double getVersion() {
        return version;
    }

    /**
     * This function returns a list of strings that describe the addon
     *
     * @return A list of strings.
     */
    public List<String> getDescription() {
        return description;
    }

    /**
     * This function returns the version that is supported by the addon
     *
     * @return The version that is supported by the addon.
     */
    public String getSupportedVersion() {
        return supportedVersion;
    }

    /**
     * This function returns the author of the addon.
     *
     * @return The author of the addon.
     */
    public String getAuthor() {
        return author;
    }
}
