package simplepets.brainsynder.links.impl.economy;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import simplepets.brainsynder.links.EconomyLink;

public class EXPLevelEconomy implements EconomyLink {
    @Override
    public double getBalance(Player player) {
        return player.getLevel();
    }

    @Override
    public void depositPlayer(Player player, double amount) {
        player.setLevel((int) (player.getLevel() + amount));
    }

    @Override
    public void withdrawPlayer(Player player, double amount) {
        player.setLevel((int) (player.getLevel() - amount));
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
        return "Level";
    }
}
