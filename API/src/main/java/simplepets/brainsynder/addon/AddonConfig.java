package simplepets.brainsynder.addon;

import lib.brainsynder.files.YamlFile;
import lib.brainsynder.files.options.YamlOption;

import java.io.File;

public class AddonConfig extends YamlFile {
    public AddonConfig(File folder, String fileName) {
        super(folder, fileName);
    }

    @Override
    public void loadDefaults() {

    }

    @Override
    public void addDefault(String key, Object value) {
        if (!contains(key)) set(key, value);
    }

    @Override
    public void addDefault(YamlOption option) {
        addDefault(option.getPath(), option.getDefault());
    }

    @Override
    public void addDefault(YamlOption option, String comment) {
        addComment(option.getPath(), comment);
        addDefault(option);
    }

    @Override
    public void addDefault(String key, Object value, String comment) {
        addComment(key, comment);
        addDefault(key, value);
    }
}
