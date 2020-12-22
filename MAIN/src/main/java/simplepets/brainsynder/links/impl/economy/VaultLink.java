package simplepets.brainsynder.links.impl.economy;

import net.milkbowl.vault.Vault;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import simplepets.brainsynder.links.EconomyLink;
import simplepets.brainsynder.links.impl.PluginLink;

public class VaultLink extends PluginLink<Vault> implements EconomyLink<Vault> {
    private Economy econ = null;

    public VaultLink() {
        super("Vault");
    }

    @Override
    public boolean onHook() {
        RegisteredServiceProvider economyProvider = Bukkit.getServer().getServicesManager().getRegistration(Economy.class);
        if (economyProvider != null) {
            this.econ = (Economy) economyProvider.getProvider();
        }
        return true;

    }

    @Override
    public void onUnhook() {
        econ = null;
        super.onUnhook();
    }

    @Override
    public double getBalance(Player player) {
        if (!isHooked()) return 0.0;
        if (econ == null) onHook();
        return econ.getBalance(player);
    }

    @Override
    public void depositPlayer(Player player, double amount) {
        if (!isHooked()) return;
        if (econ == null) onHook();
        econ.depositPlayer(player, amount);
    }

    @Override
    public void withdrawPlayer(Player player, double amount) {
        if (!isHooked()) return;
        if (econ == null) onHook();
        econ.withdrawPlayer(player, amount);
    }
}
