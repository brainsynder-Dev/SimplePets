package simplepets.brainsynder.addon;

import lib.brainsynder.files.YamlFile;

import java.io.File;

public abstract class AddonConfig extends YamlFile {
    public AddonConfig(File folder, String fileName) {
        super(folder, fileName);
    }
}
