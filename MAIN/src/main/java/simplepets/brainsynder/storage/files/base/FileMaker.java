package simplepets.brainsynder.storage.files.base;

import lib.brainsynder.files.YamlFile;
import org.bukkit.ChatColor;
import simplepets.brainsynder.PetCore;

import java.io.File;
import java.util.function.BiConsumer;

public class FileMaker extends YamlFile {
    private final PetCore core;

    public FileMaker(File folder, String fileName) {
        super(folder, fileName);
        core = PetCore.get();
    }

    public String getString(String tag, boolean color) {
        String value = super.getString(tag);
        if (value == null) return "";
        if (value.equals(tag)) return tag;

        if ((!tag.equals("prefix")) && contains("prefix")) {
            value = value.replace("{prefix}", getString("prefix"));
        }
        return (color ? translate(value) : value);
    }

    private String translate(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

    protected BiConsumer<String, String> logMove () {
        return (oldKey, newKey) -> {
            String name = getClass().getSimpleName().replace("File", "");
            core.debug("["+name+"] Moving '"+oldKey+"' to '"+newKey+"'");
        };
    }
}