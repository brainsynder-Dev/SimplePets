package simplepets.brainsynder.menu.items;

import lib.brainsynder.json.JsonArray;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.inventory.CustomInventory;
import simplepets.brainsynder.api.inventory.Item;
import simplepets.brainsynder.api.user.PetUser;

import java.io.File;

public abstract class CustomItem extends Item {
    public static final String COMMANDS = "CommandsOnClick";
    protected static final String NAMESPACE = "namespace";

    public CustomItem(File file) {
        super(file);
    }

    public void runCommands (PetUser owner) {
        if (!hasKey(COMMANDS)) return;
        JsonArray array = (JsonArray) getValue(COMMANDS);


        owner.getPetEntities().forEach(entityPet -> {
            String loc = "";

            if (owner.hasPets()) {
                Location location = entityPet.getEntity().getLocation();
                loc = location.getBlockX() + " " + location.getBlockY() + " " + location.getBlockZ();
            }

            for (Object o : array) {
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), String.valueOf(o)
                        .replace("{location}", loc)
                        .replace("{name}", owner.getPlayer().getName())
                        .replace("{type}", entityPet.getPetType().getName())
                );
            }
        });
    }

    @Override
    public void onClick(PetUser user, CustomInventory inventory, IEntityPet pet) {
        runCommands(user);
    }
}
