package simplepets.brainsynder.listeners;

import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkUnloadEvent;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.plugin.SimplePets;

public class ChunkUnloadListener implements Listener {

    @EventHandler
    public void onUnload (ChunkUnloadEvent event) {
        for (Entity entity : event.getChunk().getEntities()) {
            if (!SimplePets.isPetEntity(entity)) continue;
            SimplePets.getSpawnUtil().getHandle(entity).ifPresent(o -> {
                IEntityPet pet = (IEntityPet) o;
                pet.getPetUser().removePet(pet.getPetType());
            });
        }
    }

}
