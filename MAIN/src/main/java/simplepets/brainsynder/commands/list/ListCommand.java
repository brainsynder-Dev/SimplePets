package simplepets.brainsynder.commands.list;

import lib.brainsynder.commands.annotations.ICommand;
import lib.brainsynder.nms.Tellraw;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.CommandSender;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.pet.IPetConfig;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.plugin.SimplePets;
import simplepets.brainsynder.commands.PetSubCommand;

import java.util.Optional;

@ICommand(
        name = "list",
        description = "Lists all the different pet types"
)
public class ListCommand extends PetSubCommand {
    public ListCommand(PetCore plugin) {
        super(plugin);
    }

    @Override
    public void run(CommandSender sender) {
        Tellraw raw = Tellraw.getInstance("Pet List: ").color("#d1c9c9");
        for (PetType type : PetType.values()) {
            if (type == PetType.UNKNOWN) continue;
            Optional<IPetConfig> config = SimplePets.getPetConfigManager().getPetConfig(type);
            String tooltip = "";
            ChatColor color = ChatColor.GREEN; // lime
            if (config.isPresent() && (!config.get().isEnabled())) {
                color = ChatColor.RED; // red
                tooltip = color + "DISABLED";
            }else if (!type.isSupported()) {
                color = ChatColor.GOLD; // tan/light orange
                tooltip = color + "NOT SUPPORTED";
            }else if (!SimplePets.getSpawnUtil().isRegistered(type)) {
                color = ChatColor.YELLOW; // yellow
                tooltip = color + "NOT REGISTERED";
            }
            raw.then(type.getName()).color(color);
            if (!tooltip.isEmpty()) raw.tooltip(tooltip);

            raw.then(", ").color("#d1c9c9");
        }
        raw.removeLastPart();
        raw.send(sender);
    }
}
