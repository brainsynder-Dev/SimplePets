package simplepets.brainsynder.impl;

import lib.brainsynder.files.JsonFile;
import lib.brainsynder.item.ItemBuilder;
import lib.brainsynder.json.JsonObject;
import lib.brainsynder.nbt.StorageTagTools;
import lib.brainsynder.sounds.SoundMaker;
import lib.brainsynder.utils.Capitalise;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.pet.IPetConfig;
import simplepets.brainsynder.api.pet.PetConfigManager;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.plugin.SimplePets;
import simplepets.brainsynder.utils.Debug;
import simplepets.brainsynder.utils.DebugLevel;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public class PetConfiguration implements PetConfigManager {
    private final PetCore plugin;
    private final Map<PetType, IPetConfig> configMap;

    public PetConfiguration(PetCore plugin) {
        this.plugin = plugin;
        configMap = new HashMap<>();

        for (PetType type : PetType.values()) {
            if (!type.isSupported()) continue;
            if (!SimplePets.getSpawnUtil().isRegistered(type)) continue;
            configMap.put(type, new PetConfig(type));
        }
    }

    @Override
    public Optional<IPetConfig> getPetConfig(PetType type) {
        if (configMap.containsKey(type)) return Optional.of(configMap.get(type));
        return Optional.empty();
    }

    private class PetConfig implements IPetConfig {
        private final PetType type;
        private final JsonFile JSON;
        private final Map<String, JsonObject> additional;

        private PetConfig(PetType type) {
            this.type = type;
            additional = new HashMap<>();

            JSON = new JsonFile(new File(new File(plugin.getDataFolder().toString()+File.separator+"Pets"), type.getName()+".json"), true){
                @Override
                public void loadDefaults() {
                    setDefault("enabled", true);
                    setDefault("hat", true);
                    setDefault("mount", true);
                    type.getCustomization().ifPresent(customization -> {
                        setDefault("ambient-sound", customization.ambient().name());
                    });
                    setDefault("fly", canFlyDefault(type));
                    setDefault("float_down", false);

                    setDefault("display_name", "&a&l%player%'s " + Capitalise.capitalize(type.getName().replace("_", " ")) + " Pet");
                    setDefault("item", StorageTagTools.toJsonObject(type.getBuilder().toCompound()));

                    JsonObject dataObject = new JsonObject();
                    type.getPetData().forEach(petData -> {
                        JsonObject data = new JsonObject();
                        petData.getDefaultItems().keySet().forEach(value -> data.add(String.valueOf(value), StorageTagTools.toJsonObject(((ItemBuilder)petData.getDefaultItems().get(value)).toCompound())));
                        dataObject.add(petData.getNamespace().namespace(), data);
                    });
                    if(!dataObject.isEmpty()) setDefault("data", dataObject);
                }
            };
        }

        @Override
        public void handleAdditionalStorage(String pluginKey, Function<JsonObject, JsonObject> json) {
            additional.put(pluginKey, json.apply(additional.getOrDefault(pluginKey, new JsonObject())));
        }

        @Override
        public String getDisplayName() {
            return JSON.getString("display_name");
        }

        @Override
        public boolean hasPermission(Player player) {
            if (!plugin.getConfiguration().getBoolean("Needs-Permission")) return true;
            return player.hasPermission(getPermission());
        }

        @Override
        public boolean canHat(Player player) {
            if (!plugin.getConfiguration().getBoolean("PetToggles.All-Pets-Hat")) return false;
            if (JSON.getBoolean("hat")) {
                int permHat = plugin.hasPerm(player, getPermission() + ".hat", true);
                if (permHat == -1) {
                    int perm1 = plugin.hasPerm(player, "Pet.PetToHat", true);
                    if (perm1 > -1) return perm1 == 1;
                    if (plugin.hasPerm(player, getPermission() + ".*")) return true;
                }
                return permHat == 1;
            }
            return false;
        }

        @Override
        public boolean canMount(Player player) {
            return false;
        }

        @Override
        public boolean canFly(Player player) {
            return false;
        }

        @Override
        public boolean isEnabled() {
            return JSON.getBoolean("enabled");
        }

        @Override
        public boolean canFloat() {
            return false;
        }

        @Override
        public double getRideSpeed() {
            return 0;
        }

        @Override
        public double getWalkSpeed() {
            return 0;
        }

        @Override
        public Optional<EntityType> getEntityType() {
            return Optional.empty();
        }

        @Override
        public SoundMaker getSound() {
            if (!JSON.hasKey("ambient-sound")) return null;
            String sound = JSON.getString("ambient-sound", null);
            if (sound == null) return null;
            return SoundMaker.fromString(sound);
        }

        @Override
        public ItemBuilder getBuilder() {
            try {
                return ItemBuilder.fromCompound(StorageTagTools.fromJsonObject((JsonObject) JSON.getValue("item")));
            }catch (Exception e) {
                Debug.debug(DebugLevel.ERROR, "Failed to get default item for '"+type.getName()+"'");
                Debug.debug(DebugLevel.ERROR, "Error: "+e.getMessage());
                return type.getBuilder();
            }
        }

        @Override
        public Optional<ItemBuilder> getDataItem(String namespace, Object value) {
            if (JSON.hasKey("data")) {
                JsonObject dataObject = (JsonObject) JSON.getValue("data");
                if (dataObject.names().contains(namespace)) {
                    JsonObject data = (JsonObject) dataObject.get(namespace);
                    if (data.names().contains(String.valueOf(value))) {
                        return Optional.of(ItemBuilder.fromCompound(StorageTagTools.fromJsonObject((JsonObject) data.get(String.valueOf(value)))));
                    }
                }
            }

            return Optional.empty();
        }

        public String getPermission() {
            return "pet.type."+type.getName().replace("_", "");
        }

        private boolean canFlyDefault (PetType type) {
            return (type == PetType.BAT)
                    || (type == PetType.BEE)
                    || (type == PetType.BLAZE)
                    || (type == PetType.PHANTOM)
                    || (type == PetType.PARROT)
                    || (type == PetType.GHAST)
                    || (type == PetType.VEX)
                    || (type == PetType.WITHER);
        }
    }
}
