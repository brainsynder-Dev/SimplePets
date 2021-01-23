package simplepets.brainsynder.files;

import lib.brainsynder.files.options.YamlOption;

import java.util.Arrays;
import java.util.List;

public enum ConfigOption implements YamlOption {
    NEEDS_PERMISSION("Needs-Permissions", false),
    HAT("PetToggles.All-Pets-Hat", true, "Allow-Pets-Being-Hats"),
    COLOR ("RenamePet.Allow-ColorCodes", true, "ColorCodes"),
    MAGIC ("RenamePet.Allow-&k", false, "Use&k"),
    CHAR_LIMIT_NUMBER ("RenamePet.CharacterLimit", 10),
    CHAR_LIMIT_ENABLED ("RenamePet.Limit-Number-Of-Characters", false);

    private final String path;
    private final Object defaultValue;
    private final List<String> oldPaths;

    ConfigOption(String path, Object defaultValue) {
        this(path, defaultValue, new String[0]);
    }

    ConfigOption(String path, Object defaultValue, String... oldPaths) {
        this.path = path;
        this.defaultValue = defaultValue;
        this.oldPaths = Arrays.asList(oldPaths);
    }

    public String getPath() {
        return path;
    }

    public Object getDefault() {
        return defaultValue;
    }

    public List<String> getOldPaths() {
        return oldPaths;
    }
}
