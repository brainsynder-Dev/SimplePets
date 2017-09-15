package simplepets.brainsynder.links;

import net.milkbowl.vault.Vault;
import org.bukkit.entity.Player;

public interface IVaultLink extends IPluginLink<Vault> {
    double getBalance(Player player);

    void depositPlayer(Player player, double amount);

    void withdrawPlayer(Player player, double amount);
}
