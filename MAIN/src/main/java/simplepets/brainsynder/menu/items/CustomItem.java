package simplepets.brainsynder.menu.items;

import lib.brainsynder.json.JsonArray;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import simplepets.brainsynder.player.PetOwner;

import java.io.File;

public abstract class CustomItem extends Item {
    protected static final String NAMESPACE = "namespace";

    public CustomItem(File file) {
        super(file);
    }

    public void runCommands (PetOwner owner) {
        if (!hasKey(COMMANDS)) return;
        JsonArray array = (JsonArray) getValue(COMMANDS);

        String loc = "";

        if (owner.hasPet()) {
            Location location = owner.getPet().getEntity().getEntity().getLocation();
            loc = location.getBlockX() + " " + location.getBlockY() + " " + location.getBlockZ();
        }

        for (Object o : array) {
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), String.valueOf(o)
                    .replace("{location}", loc)
                    .replace("{name}", owner.getPlayer().getName())
                    .replace("{type}", owner.getPet().getPetType().getConfigName())
            );
        }
    }
}
