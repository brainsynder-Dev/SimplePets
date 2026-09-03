package simplepets.brainsynder.utils;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.plugin.RegisteredListener;
import simplepets.brainsynder.PetCore;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class SpawnBlockDiagnostics {
    public static Optional<String> findCancellingPlugin(Event probeEvent) {
        if (!(probeEvent instanceof Cancellable cancellable)) return Optional.empty();
        cancellable.setCancelled(false);

        for (RegisteredListener listener : probeEvent.getHandlers().getRegisteredListeners()) {
            if (listener.getPriority() == EventPriority.MONITOR) continue;
            if (isOwnedByPets(listener)) continue;

            try {
                listener.callEvent(probeEvent);
            } catch (Throwable ignored) {
                continue;
            }

            if (cancellable.isCancelled()) return Optional.of(listener.getPlugin().getName());
        }

        return Optional.empty();
    }

    public static List<String> getListeningPlugins(Event event) {
        Set<String> pluginNames = new LinkedHashSet<>();

        for (RegisteredListener listener : event.getHandlers().getRegisteredListeners()) {
            if (isOwnedByPets(listener)) continue;
            pluginNames.add(listener.getPlugin().getName());
        }

        return new ArrayList<>(pluginNames);
    }

    private static boolean isOwnedByPets(RegisteredListener listener) {
        return listener.getPlugin().equals(PetCore.getInstance());
    }
}
