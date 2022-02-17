package simplepets.brainsynder.listeners;

import lib.brainsynder.utils.Colorize;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.spigotmc.event.entity.EntityDismountEvent;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.event.user.PetRenameEvent;
import simplepets.brainsynder.api.pet.CommandReason;
import simplepets.brainsynder.api.plugin.SimplePets;
import simplepets.brainsynder.api.plugin.config.ConfigOption;

public class PetEventListener implements Listener {

    @EventHandler
    public void onRename(PetRenameEvent event) {
        String name = event.getName();

        Player player = event.getUser().getPlayer();

        if (ConfigOption.INSTANCE.RENAME_TRIM.getValue()) name = name.trim();
        if (player.hasPermission("pet.name.bypass")) return;
        String rawPattern = ConfigOption.INSTANCE.RENAME_BLOCKED_PATTERN.getValue();
        if ((rawPattern != null) && (!rawPattern.isEmpty())) {
            if (event.getName().matches(rawPattern)) name = null;
        }
        name = Colorize.translateBungeeHex(name);

        if (!player.hasPermission("pet.name.color") || !ConfigOption.INSTANCE.RENAME_COLOR_ENABLED.getValue())
            name = ChatColor.stripColor(Colorize.removeHexColor(name));
        if (!player.hasPermission("pet.name.color.hex") || !ConfigOption.INSTANCE.RENAME_COLOR_HEX.getValue())
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
