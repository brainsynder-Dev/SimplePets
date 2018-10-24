package simplepets.brainsynder.api.event;

import org.bukkit.event.Cancellable;

public abstract class CancellablePetEvent extends SimplePetEvent implements Cancellable {
    private boolean cancelled = false;

    public CancellablePetEvent(PetEventType type) {
        super(type);
    }

    public CancellablePetEvent(PetEventType type, boolean async) {
        super(type, async);
    }

    public boolean isCancelled() {return this.cancelled;}

    public void setCancelled(boolean cancelled) {this.cancelled = cancelled; }
}
