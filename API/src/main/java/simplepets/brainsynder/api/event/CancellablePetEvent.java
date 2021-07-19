package simplepets.brainsynder.api.event;

import org.bukkit.event.Cancellable;

public abstract class CancellablePetEvent extends SimplePetEvent implements Cancellable {
    private boolean cancelled = false;
    private String reason = null;


    public boolean isCancelled() {
        return this.cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public void setCancelled(boolean cancelled, String reason) {
        this.cancelled = cancelled;
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }
}
