package simplepets.brainsynder.menu.items;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.json.simple.JSONArray;
import simplepets.brainsynder.player.PetOwner;

import java.io.File;

public class CustomItem extends Item {
    protected static final String NAMESPACE = "namespace";

    public CustomItem(File file) {
        super(file);
    }

    public void runCommands (PetOwner owner) {
        if (!hasKey(COMMANDS)) return;
        JSONArray array = getArray(COMMANDS);

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
