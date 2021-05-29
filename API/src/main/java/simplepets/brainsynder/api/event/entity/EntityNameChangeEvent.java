package simplepets.brainsynder.api.event.entity;

import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.event.SimplePetEvent;

/**
 * This event gets called when the name of the entity gets changed.
 * It can be from the saved data, user changing the name, or the modify command
 */
public class EntityNameChangeEvent extends SimplePetEvent {
    private final IEntityPet entity;
    private String name;
    private String prefix = "";
    private String suffix = "";

    public EntityNameChangeEvent(IEntityPet entity, String name) {
        this.entity = entity;
        this.name = name;
    }

    public IEntityPet getEntity() {
        return entity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }
}
