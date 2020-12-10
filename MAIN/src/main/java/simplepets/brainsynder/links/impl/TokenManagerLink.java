package simplepets.brainsynder.links.impl;

import me.realized.tokenmanager.TokenManagerPlugin;
import me.realized.tokenmanager.api.TokenManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import simplepets.brainsynder.links.EconomyLink;

import java.util.OptionalLong;

public class TokenManagerLink extends PluginLink<TokenManagerPlugin> implements EconomyLink<TokenManagerPlugin> {
    private TokenManager manager = null;

    public TokenManagerLink() {
        super("TokenManager");
    }

    @Override
    public boolean onHook() {
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("TokenManager");
        if ((plugin == null) || (!plugin.isEnabled())) return false;
        manager = (TokenManager) Bukkit.getServer().getPluginManager().getPlugin("TokenManager");
        return true;
    }

    @Override
    public void onUnhook() {
        manager = null;
        super.onUnhook();
    }

    @Override
    public double getBalance(Player player) {
        if (!isHooked()) return 0;
        if (manager == null) onHook();
        OptionalLong optional = manager.getTokens(player);
        if (optional.isPresent()) return optional.getAsLong();
        return 0;
    }

    @Override
    public void depositPlayer(Player player, double amount) {
        if (!isHooked()) return;
        if (manager == null) onHook();
        manager.addTokens(player, (long) amount);
    }

    @Override
    public void withdrawPlayer(Player player, double amount) {
        if (!isHooked()) return;
        if (manager == null) onHook();
        manager.removeTokens(player, (long) amount);
    }
}
