package simplepets.brainsynder.storage.files;

import org.apache.commons.lang.WordUtils;
import org.bukkit.plugin.Plugin;
import simplepets.brainsynder.pet.PetType;
import simplepets.brainsynder.storage.files.base.FileMaker;

import java.util.ArrayList;
import java.util.Arrays;

public class PetTranslator extends FileMaker {
    public PetTranslator(Plugin plugin) {
        super(plugin, "PetTranslator.yml");
    }

    public void loadDefaults () {
        for (PetType type : PetType.values()) {
            String cfgName = type.getConfigName();
            String name = WordUtils.capitalizeFully(type.getType().toString().toLowerCase().replace("_", " "));
            setDefault(cfgName + ".Enabled", true);
            setDefault(cfgName + ".Hat", true);
            setDefault(cfgName + ".Mount", true);
            setDefault(cfgName + ".Fly", PetType.flyable.contains(type));
            setDefault(cfgName + ".DisableItemLore", false);
            setDefault(cfgName + ".ItemData.MaterialWrapper", type.getMaterial().toString());
            setDefault(cfgName + ".ItemData.Durability", (int) type.getData());
            setDefault(cfgName + ".WalkSpeed", 0.6000000238418579D);
            setDefault(cfgName + ".RideSpeed", 0.4000000238418579D);
            setDefault(cfgName + ".DefaultName", "&a&l%player%'s " + name + " Pet");
            setDefault(cfgName + ".DisplayName", "&f&l%name% Pet");
            setDefault(cfgName + ".NoColorName", name);
            try {
                if (type.getAmbientSound() != null) {
                    setDefault(cfgName + ".AmbientSound", type.getAmbientSound().getSound());
                } else {
                    setDefault(cfgName + ".AmbientSound", "off");
                }
            } catch (Exception e) {
                setDefault(cfgName + ".AmbientSound", "off");
            }
            setDefault(cfgName + ".Description", Arrays.asList("Click here to select", "The " + name + " pet."));
            setDefault(cfgName + ".On-Summon", new ArrayList<>());

            //TODO: These values are temp. disabled till a fix arises.
            //set(type.getConfigName() + ".Float-Down", true);
            //set(type.getConfigName() + ".Up-Speed", 0.5D);
            //set(type.getConfigName() + ".Float-Speed", 0.6000004238418579D);
        }
    }

    public String getString(PetType type, String tag) {
        return getString("Pets." + type.getConfigName() + '.' + tag);
    }

    public boolean isSet(PetType type, String tag) {
        return isSet("Pets." + type.getConfigName() + '.' + tag);
    }

    public Integer getInteger(PetType type, String tag) {
        return getInt("Pets." + type.getConfigName() + '.' + tag);
    }

    public double getDouble(PetType type, String tag) {
        return getDouble("Pets." + type.getConfigName() + '.' + tag);
    }

    public Boolean getBoolean(PetType type, String tag) {
        return getBoolean("Pets." + type.getConfigName() + '.' + tag);
    }
}
