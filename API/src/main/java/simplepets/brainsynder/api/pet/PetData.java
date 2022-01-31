package simplepets.brainsynder.api.pet;

import lib.brainsynder.item.ItemBuilder;
import lib.brainsynder.json.JsonObject;
import lib.brainsynder.json.JsonValue;
import simplepets.brainsynder.api.Namespace;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.plugin.SimplePets;
import simplepets.brainsynder.debug.DebugLevel;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Handles how the pets get modified
 *
 * @param <EntityParam> - Used if the class is only used for a certain entity
 */
public abstract class PetData<EntityParam extends IEntityPet> {
    private final Map<String, ItemBuilder> defaultItems;

    public PetData() {
        defaultItems = new HashMap<>();
    }

    /**
     * All PetData classes must have a Namespace Annotation to give it a name
     * This method just retrieves it
     */
    public Namespace getNamespace() {
        if (getClass().isAnnotationPresent(Namespace.class)) return getClass().getAnnotation(Namespace.class);
        throw new NullPointerException(getClass().getSimpleName() + " is missing @Namespace");
    }

    /**
     * This is only used when generating the pet data for the files
     *  Use {@link PetData#getDefault(IEntityPet)}
     */
    @Deprecated
    public abstract Object getDefaultValue();

    public Optional<Object> getDefault (PetType type) {
        Optional<IPetConfig> optional = SimplePets.getPetConfigManager().getPetConfig(type);
        Namespace namespace = getNamespace();
        if (optional.isPresent()) {
            IPetConfig config = optional.get();
            JsonObject json = config.getRawData(namespace.namespace());
            if (json.names().contains("default")) {
                JsonValue value = json.get("default");
                if (value.isBoolean()) return Optional.of(value.asBoolean());
                if (value.isNumber()) return Optional.of(value.asInt());
                if (value.isString()) return Optional.of(value.asString());
            }
        }
        return Optional.empty();
    }

    public Map<String, ItemBuilder> getDefaultItems() {
        return defaultItems;
    }

    public boolean isEnabled (EntityParam entity) {
        Optional<IPetConfig> optional = SimplePets.getPetConfigManager().getPetConfig(entity.getPetType());
        Namespace namespace = getNamespace();
        return optional.map(config -> config.getRawData(namespace.namespace()).getBoolean("enabled", true)).orElse(true);
    }

    /**
     * Fetches the item for the entity, it will also get the correct item for the value
     *
     * @param entity
     * @return
     */
    public Optional<ItemBuilder> getItem(EntityParam entity) {
        Optional<IPetConfig> optional = SimplePets.getPetConfigManager().getPetConfig(entity.getPetType());
        Namespace namespace = getNamespace();

        String value = String.valueOf(value(entity));
        // Could not find the pet config, using default item if available
        if (!optional.isPresent()) {
            if (defaultItems.containsKey(value)) return Optional.of(defaultItems.get(value));
            SimplePets.getDebugLogger().debug(DebugLevel.ERROR, getClass().getSimpleName()+" had no default item for '"+value+"'");
            return Optional.empty();
        }

        IPetConfig config = optional.get();
        if (defaultItems.containsKey(value)) return config.getDataItem(namespace.namespace(), value, defaultItems.get(value));
        return config.getDataItem(namespace.namespace(), value);
    }

    /**
     * Adds a default item for the value
     *
     * @param value - Value name (EG: true/false or 1/2 or BLACK/WHITE)
     * @param builder
     */
    public void addDefaultItem (String value, ItemBuilder builder) {
        defaultItems.put(value, builder);
    }

    /**
     * Called when ever the item is left clicked
     *
     * @param entity - The Entity that is being modified
     */
    public abstract void onLeftClick (EntityParam entity);

    /**
     * Called when ever the item is right clicked
     *
     * @param entity - The Entity that is being modified
     */
    public void onRightClick (EntityParam entity) {
        onLeftClick(entity);
    }

    /**
     * Will return the current value of the data
     *
     * @param entity - Entity that is being modified
     */
    public abstract Object value (EntityParam entity);

    /**
     * Checks if the PetData is able to be changed for the entity
     *
     * @param entity - Entity that is being modified
     * @return
     *      true - Entity can be modified
     *      false - Entity cant be modified
     */
    public boolean isModifiable (EntityParam entity) {
        return true;
    }
}
