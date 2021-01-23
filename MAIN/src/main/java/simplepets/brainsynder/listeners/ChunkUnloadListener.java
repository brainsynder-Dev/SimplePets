package simplepets.brainsynder.listeners;

import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.plugin.SimplePets;
import simplepets.brainsynder.utils.Keys;
import simplepets.brainsynder.utils.UUIDDataType;

import java.util.Optional;

public class ChunkUnloadListener implements Listener {

    @EventHandler
    public void onUnload (ChunkUnloadEvent event) {
        for (Entity entity : event.getChunk().getEntities()) {
            PersistentDataContainer container = entity.getPersistentDataContainer();
            if (container.has(Keys.ENTITY_OWNER, new UUIDDataType())) {
                Optional<PetType> petType = PetType.getPetType(container.get(Keys.ENTITY_TYPE, PersistentDataType.STRING));
                petType.ifPresent(type -> {
                    SimplePets.getUserManager().getPetUser(container.get(Keys.ENTITY_OWNER, new UUIDDataType())).ifPresent(user -> {
                        user.removePet(type);
                    });
                });
            }
        }
    }

}
