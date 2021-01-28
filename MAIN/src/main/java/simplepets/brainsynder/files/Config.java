package simplepets.brainsynder.files;

import lib.brainsynder.files.YamlFile;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.files.options.ConfigOption;

public class Config extends YamlFile {
    public Config(PetCore core) {
        super(core.getDataFolder(), "config.yml");
    }

    @Override
    public void loadDefaults() {
        for (ConfigOption option : ConfigOption.values()) addDefault(option);
    }
}
