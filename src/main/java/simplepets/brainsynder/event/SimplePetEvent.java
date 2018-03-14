package simplepets.brainsynder.event;

import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class SimplePetEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    @Getter
    private PetEventType type = PetEventType.UNKNOWN;

    public SimplePetEvent(PetEventType type) {
        if (type == null) {
            this.type = PetEventType.UNKNOWN;
            return;
        }
        this.type = type;
    }

    public SimplePetEvent(PetEventType type, boolean async) {
        super(async);
        if (type == null) {
            this.type = PetEventType.UNKNOWN;
            return;
        }
        this.type = type;
    }

    public SimplePetEvent() {
        this(null);
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public enum PetEventType {
        SPAWN,
        REMOVE,
        PRE_SPAWN,
        NAME_CHANGE,
        INVENTORY_OPEN,
        INVENTORY_SELECT,
        VEHICLE,
        HAT,
        DATA_CHANGE,
        MOVE,
        UNKNOWN
    }
}
