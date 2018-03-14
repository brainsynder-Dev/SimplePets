package simplepets.brainsynder.event;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.event.Cancellable;

public abstract class CancellablePetEvent extends SimplePetEvent implements Cancellable {
    @Getter
    @Setter
    private boolean cancelled = false;

    public CancellablePetEvent(PetEventType type) {
        super(type);
    }

    public CancellablePetEvent(PetEventType type, boolean async) {
        super(type, async);
    }
}
