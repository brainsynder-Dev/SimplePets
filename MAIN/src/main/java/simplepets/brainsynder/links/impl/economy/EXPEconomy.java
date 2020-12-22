package simplepets.brainsynder.links.impl.economy;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import simplepets.brainsynder.links.EconomyLink;

public class EXPEconomy implements EconomyLink {
    @Override
    public double getBalance(Player player) {
        return player.getTotalExperience();
    }

    @Override
    public void depositPlayer(Player player, double amount) {
        player.setTotalExperience((int) (player.getTotalExperience() + amount));
    }

    @Override
    public void withdrawPlayer(Player player, double amount) {
        player.setTotalExperience((int) (player.getTotalExperience() - amount));
    }

    @Override
    public Plugin getDependency() {
        return null;
    }

    @Override
    public boolean isHooked() {
        return true;
    }

    @Override
    public String getDependencyName() {
        return "Experience";
    }
}
