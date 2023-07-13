package simplepets.brainsynder.addon;

import com.google.common.collect.Lists;

import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

public class AddonCloudData {
    private final String id;
    private final String name;
    private final String author;
    private final String version;
    private final String downloadURL;
    private final String lastUpdated;
    private final int downloadCount;


    private final List<String> description = Lists.newArrayList();

    public AddonCloudData(String id, String name, String description, String author, String version, String downloadURL, String lastUpdated, int downloadCount) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.version = version;
        this.downloadURL = downloadURL;
        this.lastUpdated = lastUpdated;
        this.downloadCount = downloadCount;


        Collections.addAll(this.description, splitString(description, 6).split("\n"));
    }

    /**
     * It takes a string and splits it into lines of a given length
     *
     * @param text The text to be split.
     * @param wordsPerLine The number of words per line.
     * @return A string with new lines inserted.
     */
    private String splitString(String text, int wordsPerLine) {
        StringBuilder newText = new StringBuilder();

        StringTokenizer wordTokenizer = new StringTokenizer(text);
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

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAuthor() {
        return author;
    }

    public String getVersion() {
        return version;
    }

    public List<String> getDescription() {
        return description;
    }

    public String getDownloadURL() {
        return downloadURL;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public int getDownloadCount() {
        return downloadCount;
    }
}
