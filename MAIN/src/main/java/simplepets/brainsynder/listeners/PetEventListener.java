package simplepets.brainsynder.listeners;

import lib.brainsynder.utils.Colorize;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.spigotmc.event.entity.EntityDismountEvent;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.event.user.PetRenameEvent;
import simplepets.brainsynder.api.pet.CommandReason;
import simplepets.brainsynder.api.plugin.SimplePets;
import simplepets.brainsynder.files.Config;

public class PetEventListener implements Listener {

    @EventHandler
    public void onRename(PetRenameEvent event) {
        Config config = PetCore.getInstance().getConfiguration();
        String name = event.getName();

        Player player = event.getUser().getPlayer();

        if (config.getBoolean(Config.RENAME_TRIM, false)) name = name.trim();
        if (player.hasPermission("pet.name.bypass")) return;
        String rawPattern = config.getString("RenamePet.Blocked-RegexPattern", "");
        if ((rawPattern != null) && (!rawPattern.isEmpty())) {
            if (event.getName().matches(rawPattern)) name = null;
        }
        name = Colorize.translateBungeeHex(name);

        if (!player.hasPermission("pet.name.color") || !config.getBoolean(Config.COLOR, true))
            name = ChatColor.stripColor(Colorize.removeHexColor(name));
        if (!player.hasPermission("pet.name.color.hex") || !config.getBoolean(Config.HEX, true))
            name = Colorize.removeHexColor(name);

        event.setName(name);
    }

    @EventHandler
    public void onDismount (EntityDismountEvent event) {
        if ((event.getEntity() instanceof Player) && SimplePets.isPetEntity(event.getDismounted())) {
            SimplePets.getUserManager().getPetUser((Player) event.getEntity()).ifPresent(user -> {
                SimplePets.getSpawnUtil().getHandle(event.getDismounted()).ifPresent(o -> {
                    IEntityPet pet = (IEntityPet) o;
                    SimplePets.getPetUtilities().runPetCommands(CommandReason.RIDE_DISMOUNT, user, pet.getPetType());
                });
            });
        }
    }
}
