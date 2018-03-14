package simplepets.brainsynder.links.impl;

import net.milkbowl.vault.Vault;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import simplepets.brainsynder.links.IVaultLink;

public class VaultLink extends PluginLink<Vault> implements IVaultLink {
    private Economy econ = null;

    public VaultLink() {
        super("Vault");
    }

    @Override
    public void onHook() {
        RegisteredServiceProvider economyProvider = Bukkit.getServer().getServicesManager().getRegistration(Economy.class);
        if (economyProvider != null) {
            this.econ = (Economy) economyProvider.getProvider();
        }
    }

    @Override
    public void onUnhook() {
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
