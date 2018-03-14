package simplepets.brainsynder.files;

import org.apache.commons.lang.WordUtils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.pet.PetType;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PetTranslate {
    public static String getString(String tag) {
        File f = new File(PetCore.get().getDataFolder(), "PetTranslator.yml");
        FileConfiguration con = YamlConfiguration.loadConfiguration(f);
        return translate(con.getString("Pets." + tag));
    }

    public static String getString(PetType type, String tag) {
        File f = new File(PetCore.get().getDataFolder(), "PetTranslator.yml");
        FileConfiguration con = YamlConfiguration.loadConfiguration(f);
        return translate(con.getString("Pets." + type.getConfigName() + '.' + tag));
    }

    public static boolean isSet(PetType type, String tag) {
        File f = new File(PetCore.get().getDataFolder(), "PetTranslator.yml");
        FileConfiguration con = YamlConfiguration.loadConfiguration(f);
        return con.get("Pets." + type.getConfigName() + '.' + tag) != null;
    }

    public static Integer getInteger(PetType type, String tag) {
        File f = new File(PetCore.get().getDataFolder(), "PetTranslator.yml");
        FileConfiguration con = YamlConfiguration.loadConfiguration(f);
        return con.getInt("Pets." + type.getConfigName() + '.' + tag);
    }

    public static long getDouble(PetType type, String tag) {
        File f = new File(PetCore.get().getDataFolder(), "PetTranslator.yml");
        FileConfiguration con = YamlConfiguration.loadConfiguration(f);
        return con.getLong("Pets." + type.getConfigName() + '.' + tag);
    }

    public static Boolean getBoolean(PetType type, String tag) {
        File f = new File(PetCore.get().getDataFolder(), "PetTranslator.yml");
        FileConfiguration con = YamlConfiguration.loadConfiguration(f);
        return con.getBoolean("Pets." + type.getConfigName() + '.' + tag);
    }

    public static Integer getInteger(String tag) {
        File f = new File(PetCore.get().getDataFolder(), "PetTranslator.yml");
        FileConfiguration con = YamlConfiguration.loadConfiguration(f);
        return con.getInt("Pets." + tag);
    }

    public static double getDouble(String tag) {
        File f = new File(PetCore.get().getDataFolder(), "PetTranslator.yml");
        FileConfiguration con = YamlConfiguration.loadConfiguration(f);
        return con.getDouble("Pets." + tag);
    }

    public static Boolean getBoolean(String tag) {
        File f = new File(PetCore.get().getDataFolder(), "PetTranslator.yml");
        FileConfiguration con = YamlConfiguration.loadConfiguration(f);
        return con.getBoolean("Pets." + tag);
    }

    public static void set(String tag, Object data) {
        File f = new File(PetCore.get().getDataFolder(), "PetTranslator.yml");
        FileConfiguration con = YamlConfiguration.loadConfiguration(f);
        if (con.get("Pets." + tag) == null) {
            con.set("Pets." + tag, data);
        }
        try {
            con.save(f);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setOver(String tag, Object data) {
        File f = new File(PetCore.get().getDataFolder(), "PetTranslator.yml");
        FileConfiguration con = YamlConfiguration.loadConfiguration(f);
        con.set("Pets." + tag, data);
        try {
            con.save(f);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Object get(String tag) {
        File f = new File(PetCore.get().getDataFolder(), "PetTranslator.yml");
        FileConfiguration con = YamlConfiguration.loadConfiguration(f);
        return con.get("Pets." + tag);
    }

    public static ConfigurationSection getSection(String tag) {
        File f = new File(PetCore.get().getDataFolder(), "PetTranslator.yml");
        FileConfiguration con = YamlConfiguration.loadConfiguration(f);
        return con.getConfigurationSection("Pets." + tag);
    }

    public static void setHeader(String text) {
        File f = new File(PetCore.get().getDataFolder(), "PetTranslator.yml");
        FileConfiguration con = YamlConfiguration.loadConfiguration(f);
        if (!f.exists()) {
            con.options().header(text);
        }
        try {
            con.save(f);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadDefaults() {
        for (PetType type : PetType.values()) {
            String name = WordUtils.capitalizeFully(type.getType().toString().toLowerCase().replace("_", " "));
            set(type.getConfigName() + ".Enabled", true);
            set(type.getConfigName() + ".Hat", true);
            set(type.getConfigName() + ".Mount", true);
            set(type.getConfigName() + ".Fly", PetType.flyable.contains(type));
            set(type.getConfigName() + ".DisableItemLore", false);
            set(type.getConfigName() + ".ItemData.MaterialWrapper", type.getMaterial().toString());
            set(type.getConfigName() + ".ItemData.Durability", (int) type.getData());
            set(type.getConfigName() + ".WalkSpeed", 0.6000000238418579D);
            set(type.getConfigName() + ".RideSpeed", 0.4000000238418579D);
            set(type.getConfigName() + ".DefaultName", "&a&l%player%'s " + name + " Pet");
            set(type.getConfigName() + ".DisplayName", "&f&l%name% Pet");
            set(type.getConfigName() + ".NoColorName", name);
            try {
                if (type.getAmbientSound() != null) {
                    set(type.getConfigName() + ".AmbientSound", type.getAmbientSound().getSound());
                } else {
                    set(type.getConfigName() + ".AmbientSound", "off");
                }
            } catch (Exception e) {
                set(type.getConfigName() + ".AmbientSound", "off");
            }
            set(type.getConfigName() + ".Description", Arrays.asList("Click here to select", "The " + name + " pet."));
            set(type.getConfigName() + ".On-Summon", new ArrayList<>());

            //TODO: These values are temp. disabled till a fix arises.
            //set(type.getConfigName() + ".Float-Down", true);
            //set(type.getConfigName() + ".Up-Speed", 0.5D);
            //set(type.getConfigName() + ".Float-Speed", 0.6000004238418579D);
        }
    }

    public static <T> T getPetData(PetType type, String key) {
        File f = new File(PetCore.get().getDataFolder(), "PetTranslator.yml");
        FileConfiguration con = YamlConfiguration.loadConfiguration(f);
        return (T) con.get("Pets." + type.getConfigName() + '.' + key);
    }

    private static String translate(String msg) {
        return msg.replace("&", "§");
    }

    public static List<String> getList(String tag) {
        File f = new File(PetCore.get().getDataFolder(), "PetTranslator.yml");
        FileConfiguration con = YamlConfiguration.loadConfiguration(f);
        return con.getStringList("Pets." + tag);
    }
}
