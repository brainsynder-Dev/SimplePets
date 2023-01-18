package simplepets.brainsynder.addon;

import com.google.common.collect.Lists;
import lib.brainsynder.utils.AdvString;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

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
        StringBuilder builder = new StringBuilder();
        for (String line : description) builder.append(line).append(" ");

        String combinedDescription = AdvString.replaceLast(" ", "", builder.toString());
        List<String> updatedDescription = new ArrayList<>();
        Collections.addAll(updatedDescription, splitString(combinedDescription, 6).split("\n"));

        this.description = updatedDescription;
        return this;
    }

    /**
     * It takes a string and splits it into lines of a given length
     *
     * @param text The text to be split.
     * @param wordsPerLine The number of words per line.
     * @return A string with new lines inserted.
     */
    private String splitString(String text, int wordsPerLine) {
        final StringBuilder newText = new StringBuilder();

        final StringTokenizer wordTokenizer = new StringTokenizer(text);
        long wordCount = 1;
        while (wordTokenizer.hasMoreTokens()) {
            newText.append(wordTokenizer.nextToken());
            if (wordTokenizer.hasMoreTokens()) {
                if (wordCount++ % wordsPerLine == 0) {
                    newText.append("\n");
                } else {
                    newText.append(" ");
                }
            }
        }
        return newText.toString();
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
