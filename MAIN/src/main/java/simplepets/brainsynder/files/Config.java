package simplepets.brainsynder.files;

import lib.brainsynder.files.YamlFile;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.plugin.SimplePets;
import simplepets.brainsynder.api.plugin.config.ConfigOption;
import simplepets.brainsynder.debug.DebugLevel;

import java.util.function.BiConsumer;

public class Config extends YamlFile {
    public Config(PetCore core) {
        super(core.getDataFolder(), "config.yml");
    }

    @Override
    public void loadDefaults() {
        ConfigOption.INSTANCE.getOptions().forEach((key, entry) -> {
            // Load the default values just in case
            if (entry.getDescription() == null) {
                addDefault(key, entry.getDefaultValue());
            }else{
                addDefault(key, entry.getDefaultValue(), entry.getDescription()
                        .replace("{default}", String.valueOf(entry.getDefaultValue())) // Replace the {default} placeholder with what the default value is
                );
            }

            // Moves all the old keys to the new key
            if (!entry.getPastPaths().isEmpty()) {
                entry.getPastPaths().forEach(oldKey -> {
                    move(String.valueOf(oldKey), key);
                });
            }
        });

//        addSectionHeader("PetItemStorage", "The ability to have pets store items in another inventory is temporarily not implemented yet\nThis feature will be back as either an addon or re-implemented into the plugin");
//        addDefault("PetItemStorage.Enable", true, "Disabling this will remove players access to a GUI that stores items\nDefault: true");
//        addDefault("PetItemStorage.Inventory-Size", 27, "What size would you like the inventory to be?\nSizes: 9,18,27,36,45,54\nDefault: 27");


        updateSections();
    }

    public void initValues() {
        ConfigOption.INSTANCE.getOptions().forEach((key, entry) -> {
            // Fetch the configs value
            Object value = get(key);

            // Validate the value, make sure the types match to prevent booleans becoming numbers
            if (!value.getClass().getSimpleName().equals(entry.getDefaultValue().getClass().getSimpleName())) {
                SimplePets.getDebugLogger().debug(DebugLevel.CRITICAL, "Value of '"+key+"' can not be a '"+value.getClass().getSimpleName()+"' must be a '"+entry.getDefaultValue().getClass().getSimpleName()+"'" + ((entry.getExamples() != null) ? " Example(s): "+entry.getExamples() : ""));
                value = entry.getDefaultValue();
            }
            // Store the configured value into the Entry
            entry.setValue(value, false);
        });
    }


    public void setEnum(String key, Enum anEnum) {
        set(key, anEnum.name());
    }

    public <E extends Enum>E getEnum (String key, Class<E> type) {
        return getEnum(key, type, null);
    }
    public <E extends Enum>E getEnum (String key, Class<E> type, E fallback) {
        if (!contains(key)) return fallback;
        return (E) E.valueOf(type, getString(key));
    }

    private void updateSections () {
        remove("PetItemStorage.Enable");
        remove("PetItemStorage.Inventory-Size");
        remove("WorldGuard.BypassPermission");
        remove("PlotSquared.BypassPermission");
        remove("PlotSquared.On-Unclaimed-Plots.Move");
        remove("PlotSquared.On-Unclaimed-Plots.Spawn");
        remove("PlotSquared.On-Unclaimed-Plots.Riding");
        remove("PlotSquared.On-Roads.Move");
        remove("PlotSquared.On-Roads.Spawn");
        remove("PlotSquared.On-Roads.Riding");
        remove("PlotSquared.Block-If-Denied.Move");
        remove("PlotSquared.Block-If-Denied.Spawn");
        remove("PlotSquared.Block-If-Denied.Riding");
        remove("WorldBorder.Block-If-Denied.Move");
        remove("WorldBorder.Block-If-Denied.Spawn");
        remove("WorldBorder.Block-If-Denied.Riding");
        move("Needs-Pet-Permission-To-Open-GUI", "Permissions.Needs-Pet-Permission-for-GUI", logMove());
        move("Needs-Data-Permissions", "Permissions.Data-Permissions", logMove());
        move("Needs-Permission", "Permissions.Enabled", logMove());
        move("Remove-Item-If-No-Permission", "Permissions.Only-Show-Pets-Player-Can-Access", logMove());
        remove("WorldGuard.Spawning.Always-Allowed");
        remove("WorldGuard.Spawning.Blocked-Regions");
        remove("WorldGuard.Pet-Entering.Always-Allowed");
        remove("WorldGuard.Pet-Entering.Blocked-Regions");
        remove("WorldGuard.Pet-Riding.Always-Allowed");
        remove("WorldGuard.Pet-Riding.Blocked-Regions");
        remove("OldPetRegistering");
        remove("MySQL.Options.Auto_Reconnect"); // Not needed as the new system needs the connection to remain
        remove("PetToggles.Weight.Enabled");
        remove("PetToggles.Weight.Weight_Stacked");
        remove("PetToggles.Weight.Max_Weight");
        remove("Complete-Mobspawning-Deny-Bypass"); // This has not been fully used since 1.8

    }


    protected BiConsumer<String, String> logMove () {
        return (oldKey, newKey) -> {
            String name = getClass().getSimpleName().replace("File", "");
            SimplePets.getDebugLogger().debug("["+name+"] Moving '"+oldKey+"' to '"+newKey+"'");
        };
    }
}
