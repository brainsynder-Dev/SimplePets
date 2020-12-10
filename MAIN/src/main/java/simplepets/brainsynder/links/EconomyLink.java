package simplepets.brainsynder.links;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public interface EconomyLink<T extends Plugin> extends IPluginLink<T> {
    double getBalance(Player player);

    void depositPlayer(Player player, double amount);

    void withdrawPlayer(Player player, double amount);
}
