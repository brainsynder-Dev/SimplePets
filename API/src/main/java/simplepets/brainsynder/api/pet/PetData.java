package simplepets.brainsynder.api.pet;

import lib.brainsynder.item.ItemBuilder;
import simplepets.brainsynder.api.Namespace;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.plugin.SimplePets;

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

    public Map<String, ItemBuilder> getDefaultItems() {
        return defaultItems;
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
            System.out.println("[SimplePets API] "+getClass().getSimpleName()+" had no default item for '"+value+"'");
            return Optional.empty();
        }

        IPetConfig config = optional.get();
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
}
