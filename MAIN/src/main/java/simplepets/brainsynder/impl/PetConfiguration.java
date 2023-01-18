package simplepets.brainsynder.impl;

import com.google.common.collect.Lists;
import lib.brainsynder.apache.WordUtils;
import lib.brainsynder.files.JsonFile;
import lib.brainsynder.item.ItemBuilder;
import lib.brainsynder.json.JsonArray;
import lib.brainsynder.json.JsonObject;
import lib.brainsynder.nbt.StorageTagTools;
import lib.brainsynder.sounds.SoundMaker;
import lib.brainsynder.utils.Capitalise;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.pet.*;
import simplepets.brainsynder.api.pet.annotations.DisableDefault;
import simplepets.brainsynder.api.plugin.SimplePets;
import simplepets.brainsynder.api.plugin.config.ConfigOption;
import simplepets.brainsynder.debug.DebugLevel;
import simplepets.brainsynder.utils.Keys;
import simplepets.brainsynder.utils.Utilities;

import java.io.File;
import java.util.HashMap;
import java.util.List;
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

    public void reset () {
        configMap.clear();

        for (PetType type : PetType.values()) {
            if (!type.isSupported()) continue;
            if (!SimplePets.getSpawnUtil().isRegistered(type)) continue;
            configMap.put(type, new PetConfig(type));
        }
    }

    public void reset (PetType type) {
        configMap.remove(type);

        if (!type.isSupported()) return;
        if (!SimplePets.getSpawnUtil().isRegistered(type)) return;
        configMap.put(type, new PetConfig(type));
    }

    @Override
    public Optional<IPetConfig> getPetConfig(PetType type) {
        if (configMap.containsKey(type)) return Optional.of(configMap.get(type));
        return Optional.empty();
    }

    public class PetConfig implements IPetConfig {
        private final PetType type;
        private final JsonFile JSON;
        private final Map<String, JsonObject> additional;
        private final Map<CommandReason, List<String>> commandMap;

        private PetConfig(PetType type) {
            this.type = type;
            additional = new HashMap<>();
            commandMap = new HashMap<>();

            JSON = new JsonFile(new File(new File(plugin.getDataFolder() +File.separator+"Pets"), type.getName()+".json"), true){
                @Override
                public void loadDefaults() {
                    setDefault("enabled", true);
                    setDefault("hat", true);
                    setDefault("mount", true);
                    type.getCustomization().ifPresent(customization -> {
                        setDefault("ambient-sound", customization.ambient().name());
                    });

                    JsonObject reasons = new JsonObject();
                    for (CommandReason reason : CommandReason.values()) reasons.add(reason.name(), new JsonArray());
                    setDefault("commands", reasons);

                    setDefault("ride_speed", ConfigOption.INSTANCE.PET_TOGGLES_RIDE_SPEED.getValue());
                    setDefault("walk_speed", ConfigOption.INSTANCE.PET_TOGGLES_WALK_SPEED.getValue());
                    setDefault("fly_speed", ConfigOption.INSTANCE.PET_TOGGLES_FLY_SPEED.getValue());
                    setDefault("fly", canFlyDefault(type));
                    setDefault("float_down", false);

                    setDefault("display_name", "&a&l%player%'s " + Capitalise.capitalize(type.getName().replace("_", " ")) + " Pet");
                    setDefault("item", StorageTagTools.toJsonObject(type.getBuilder().toCompound()));


                    JsonObject dataObject = new JsonObject();
                    type.getPetData().forEach(petData -> {
                        JsonObject data = new JsonObject();
                        data.set("enabled", (!petData.getClass().isAnnotationPresent(DisableDefault.class)));

                        Object defaultValue = petData.getDefaultValue();
                        if (defaultValue instanceof Integer) {
                            data.set("default", (Integer) petData.getDefaultValue());
                        }else if (defaultValue instanceof Boolean) {
                            data.set("default", (Boolean) petData.getDefaultValue());
                        }else{
                            data.set("default", String.valueOf(petData.getDefaultValue()));
                        }
                        JsonObject values = new JsonObject();
                        petData.getDefaultItems().forEach((value, item) -> {
                            String name = petData.getNamespace().namespace();
                            name = name.replace("_", " ");
                            name = WordUtils.capitalize(name);
                            ItemBuilder builder = ((ItemBuilder)item);
                            String raw = builder.getName();
                            raw = raw.replace("{name}", name);
                            builder.withName(raw);
                            values.add(String.valueOf(value), StorageTagTools.toJsonObject(builder.toCompound()));
                        });
                        data.set("values", values);

                        dataObject.add(petData.getNamespace().namespace(), data);
                    });
                    setDefault("data", dataObject);
                }
            };

            if (JSON.hasKey("commands")) {
                JsonObject commands = (JsonObject) JSON.getValue("commands");
                commands.names().forEach(s -> {
                    CommandReason.getReason(s).ifPresent(reason -> {
                        List<String> list = commandMap.getOrDefault(reason, Lists.newArrayList());
                        JsonArray array = (JsonArray) commands.get(s);
                        array.forEach(jsonValue -> list.add(jsonValue.asString()));
                        commandMap.put(reason, list);
                    });
                });
            }

            // Makes sure all the pet data is added to the file.
            type.getPetData().forEach(this::checkPetData);
        }

        public JsonFile getJSON() {
            return JSON;
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
        public boolean canHat(Player player) {
            if (ConfigOption.INSTANCE.PET_TOGGLES_HAT.getValue()) return true;
            if (JSON.getBoolean("hat", true)) return Utilities.hasPermission(player, type.getPermission("hat"));
            return false;
        }

        @Override
        public boolean canMount(Player player) {
            if (ConfigOption.INSTANCE.PET_TOGGLES_MOUNTABLE.getValue()) return true;
            if (JSON.getBoolean("mount", true)) return Utilities.hasPermission(player, type.getPermission("mount"));
            return false;
        }

        @Override
        public boolean canFly(Player player) {
            if (ConfigOption.INSTANCE.PET_TOGGLES_FLYABLE.getValue()) return true;
            if (JSON.getBoolean("fly", true)) return Utilities.hasPermission(player, type.getPermission("fly"));
            return false;
        }

        @Override
        public boolean isEnabled() {
            return JSON.getBoolean("enabled");
        }

        @Override
        public boolean canFloat() {
            return JSON.getBoolean("float_down", false);
        }

        @Override
        public double getRideSpeed() {
            return JSON.getDouble("ride_speed", ConfigOption.INSTANCE.PET_TOGGLES_RIDE_SPEED.getValue());
        }

        @Override
        public double getWalkSpeed() {
            return JSON.getDouble("walk_speed", ConfigOption.INSTANCE.PET_TOGGLES_WALK_SPEED.getValue());
        }

        @Override
        public double getFlySpeed() {
            return JSON.getDouble("fly_speed", ConfigOption.INSTANCE.PET_TOGGLES_FLY_SPEED.getValue());
        }

        @Override
        public Optional<EntityType> getEntityType() {
            return Optional.empty();
        }

        @Override
        public SoundMaker getSound() {
            if (!JSON.containsKey("ambient-sound")) return null;
            String sound = JSON.getString("ambient-sound", null);
            if (sound == null) return null;
            return SoundMaker.fromString(sound);
        }

        @Override
        public ItemBuilder getBuilder() {
            try {
                return ItemBuilder.fromCompound(StorageTagTools.fromJsonObject((JsonObject) JSON.getValue("item"))).handleMeta(ItemMeta.class, itemMeta -> {
                    itemMeta.getPersistentDataContainer().set(Keys.GUI_ITEM, PersistentDataType.INTEGER, 1);
                    itemMeta.getPersistentDataContainer().set(Keys.PET_TYPE_ITEM, PersistentDataType.STRING, type.getName());
                    return itemMeta;
                });
            }catch (Exception e) {
                SimplePets.getDebugLogger().debug(DebugLevel.ERROR, "Failed to get default item for '"+type.getName()+"'");
                SimplePets.getDebugLogger().debug(DebugLevel.ERROR, "Error: "+e.getMessage());
                return type.getBuilder();
            }
        }

        @Override
        public Optional<ItemBuilder> getDataItem(String namespace, Object value) {
            if (JSON.containsKey("data")) {
                JsonObject dataObject = (JsonObject) JSON.getValue("data");
                if (dataObject.names().contains(namespace)) {
                    JsonObject data = (JsonObject) dataObject.get(namespace);
                    JsonObject values = (JsonObject) data.get("values");

                    if (values.names().isEmpty()) { // re-adds the data since it is missing
                        for (PetData petData : type.getPetData()) {
                            if (!petData.getNamespace().namespace().equals(namespace)) continue;
                            petData.getDefaultItems().forEach((o, o2) -> {
                                values.add(String.valueOf(o), StorageTagTools.toJsonObject(((ItemBuilder)o2).toCompound()));
                            });
                        }
                        data.set("values", values);

                        dataObject.add(namespace, data);
                        JSON.set("data", dataObject);
                        JSON.save(true);
                    }
                    if (values.names().contains(String.valueOf(value))) {
                        return Optional.of(ItemBuilder.fromCompound(StorageTagTools.fromJsonObject((JsonObject) values.get(String.valueOf(value)))));
                    }
                }
            }

            return Optional.empty();
        }

        @Override
        public Optional<ItemBuilder> getDataItem(String namespace, Object value, ItemBuilder fallback) {
            if (JSON.containsKey("data")) {
                JsonObject dataObject = (JsonObject) JSON.getValue("data");
                if (dataObject.names().contains(namespace)) {
                    JsonObject data = (JsonObject) dataObject.get(namespace);
                    JsonObject values = (JsonObject) data.get("values");

                    if (values.names().isEmpty()) { // re-adds the data since it is missing
                        for (PetData petData : type.getPetData()) {
                            if (!petData.getNamespace().namespace().equals(namespace)) continue;
                            petData.getDefaultItems().forEach((o, o2) -> {
                                values.add(String.valueOf(o), StorageTagTools.toJsonObject(((ItemBuilder)o2).toCompound()));
                            });
                        }

                        dataObject.add(namespace, data);
                        JSON.set("data", dataObject);
                        JSON.save(true);
                    }

                    if (values.names().contains(String.valueOf(value))) {
                        return Optional.of(ItemBuilder.fromCompound(StorageTagTools.fromJsonObject((JsonObject) values.get(String.valueOf(value)))));
                    }else{
                        // Does not contain value
                        values.add(String.valueOf(value), StorageTagTools.toJsonObject(fallback.toCompound()));
                        dataObject.add(namespace, data);
                        JSON.set("data", dataObject);
                        JSON.save(true);
                        return Optional.of(fallback);
                    }
                }
            }
            return Optional.empty();
        }

        @Override
        public JsonObject getRawData (String namespace) {
            JsonObject data = new JsonObject();
            if (JSON.containsKey("data")) {
                JsonObject dataObject = (JsonObject) JSON.getValue("data");
                if (dataObject.names().contains(namespace)) data = (JsonObject) dataObject.get(namespace);
            }
            return data;
        }

        @Override
        public Map<CommandReason, List<String>> getCommands() {
            return commandMap;
        }

        private boolean checkPetData(PetData petData) {
            String namespace = petData.getNamespace().namespace();
            if (JSON.containsKey("data")) {

                JsonObject dataObject = (JsonObject) JSON.getValue("data");
                JsonObject values;
                if (!dataObject.names().contains(namespace)) {
                    SimplePets.getDebugLogger().debug(DebugLevel.DEBUG, type.getName()+" | Missing namespace: "+namespace);
                    JsonObject data = new JsonObject();
                    values = new JsonObject();
                    petData.getDefaultItems().forEach((value, item) -> {
                        values.add(String.valueOf(value), StorageTagTools.toJsonObject(((ItemBuilder)item).toCompound()));
                    });
                    data.set("values", values);
                    data.set("enabled", true);
                    dataObject.add(namespace, data);
                    JSON.set("data", dataObject);
                    JSON.save(true);
                    return false;
                }

                if (dataObject.names().contains(namespace)) {

                    JsonObject data = (JsonObject) dataObject.get(namespace);
                    values = data.names().contains("values") ? (JsonObject) data.get("values") : new JsonObject();
                    boolean update = false;
                    for (Object object : petData.getDefaultItems().entrySet()) {
                        Map.Entry<String, ItemBuilder> entry = (Map.Entry<String, ItemBuilder>) object;
                        if (!values.names().contains(entry.getKey())) {
                            update = true;
                            SimplePets.getDebugLogger().debug(DebugLevel.DEBUG, type.getName()+" | "+namespace+" | Missing namespace: "+entry.getKey());
                            values.add(entry.getKey(), StorageTagTools.toJsonObject(entry.getValue().toCompound()));
                        }
                    }

                    if (update) {
                        data.set("values", values);
                        if (!data.names().contains("enabled")) data.set("enabled", true);
                        dataObject.add(namespace, data);
                        JSON.set("data", dataObject);
                        JSON.save(true);
                        return false;
                    }
                }
                return true;
            }

            SimplePets.getDebugLogger().debug(DebugLevel.DEBUG, type.getName()+" | Missing 'data' section");

            JsonObject dataObject = new JsonObject();
            JsonObject values = new JsonObject();
            JsonObject data = new JsonObject();
            petData.getDefaultItems().forEach((value, item) -> {
                values.add(String.valueOf(value), StorageTagTools.toJsonObject(((ItemBuilder)item).toCompound()));
            });
            if (!data.names().contains("enabled")) data.set("enabled", true);
            data.set("values", values);
            dataObject.add(namespace, data);
            JSON.set("data", dataObject);
            JSON.save(true);
            return false;
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
