package simplepets.brainsynder.menu.settings;

import org.bukkit.configuration.ConfigurationSection;
import simplepets.brainsynder.storage.files.Config;

public class SettingsMenu {
    public static void checkCode (Config config) {
        StringBuilder builder = new StringBuilder();

        config.getKeys(false).forEach(key -> {
            if (config.isConfigurationSection(key)) {
                builder.append(key).append("\n");
                config.getSection(key).getKeys(false).forEach(s -> {
                    builder.append("    ").append(s).append("\n");
                });

            }else{
                builder.append(key).append("\n");
            }
        });
    }

    private static void handleSection (StringBuilder builder, String key, Config config, ConfigurationSection section, int count) {
        StringBuilder space = new StringBuilder();
        for (int i = 0; i < count; i++) {
            space.append("  ");
        }

        builder.append(key).append("\n");
        section.getKeys(false).forEach(s -> {
            //if (config.isConfigurationSection())
            builder.append(space).append(s).append("\n");
        });
    }
}
