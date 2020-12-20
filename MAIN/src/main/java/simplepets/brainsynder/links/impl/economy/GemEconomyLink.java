package simplepets.brainsynder.links.impl.economy;

import me.xanium.gemseconomy.GemsEconomy;
import me.xanium.gemseconomy.api.GemsEconomyAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import simplepets.brainsynder.links.EconomyLink;
import simplepets.brainsynder.links.impl.PluginLink;

public class GemEconomyLink extends PluginLink<GemsEconomy> implements EconomyLink<GemsEconomy> {
    private GemsEconomyAPI api = null;

    public GemEconomyLink() {
        super("GemsEconomy");
    }

    @Override
    public boolean onHook() {
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("GemsEconomy");
        if ((plugin == null) || (!plugin.isEnabled())) return false;
        api = new GemsEconomyAPI();
        return true;
    }

    @Override
    public void onUnhook() {
        api = null;
        super.onUnhook();
    }

    @Override
    public double getBalance(Player player) {
        if (!isHooked()) return 0;
        if (api == null) onHook();
        return api.getBalance(player.getUniqueId());
    }

    @Override
    public void depositPlayer(Player player, double amount) {
        if (!isHooked()) return;
        if (api == null) onHook();
        api.deposit(player.getUniqueId(), amount);
    }

    @Override
    public void withdrawPlayer(Player player, double amount) {
        if (!isHooked()) return;
        if (api == null) onHook();
        api.withdraw(player.getUniqueId(), amount);
    }
}
