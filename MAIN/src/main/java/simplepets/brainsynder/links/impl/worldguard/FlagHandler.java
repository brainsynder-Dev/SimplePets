package simplepets.brainsynder.links.impl.worldguard;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.association.RegionAssociable;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import simplepets.brainsynder.PetCore;

public class FlagHandler {

    public StateFlag ALLOW_PET_SPAWN = new StateFlag("allow-pet-spawn", true);
    public StateFlag ALLOW_PET_ENTER = new StateFlag("allow-pet-enter", true);
    public StateFlag ALLOW_PET_RIDING = new StateFlag("allow-pet-riding", true);

    public FlagHandler() {
        FlagRegistry registry = WorldGuard.getInstance().getFlagRegistry();
        registry.register(ALLOW_PET_ENTER);
        registry.register(ALLOW_PET_SPAWN);
        registry.register(ALLOW_PET_RIDING);
    }

    public boolean canPetEnter(Player player, Location loc) {
        return query(player, loc, ALLOW_PET_ENTER);
    }

    public boolean canPetSpawn(Player player, Location loc) {
        return query(player, loc, ALLOW_PET_SPAWN);
    }

    public boolean canRidePet(Player player, Location loc) {
        return query(player, loc, ALLOW_PET_RIDING);
    }

    private boolean query(Player player, Location loc, StateFlag flag) {
        if (player != null) {
            if (player.hasPermission(PetCore.get().getConfiguration().getString("WorldGuard.BypassPermission", false))) return true;
        }
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        // Get the region manager for the world we're inWorld
        RegionManager manager = container.get(BukkitAdapter.adapt(loc.getWorld()));
        // Uses the query cache
        RegionQuery query = container.createQuery();
        if (manager == null) return true;
        // Get all the applicable regions, as some will overlap
        ApplicableRegionSet regions = query.getApplicableRegions(BukkitAdapter.adapt(loc));

        return regions.testState(player != null ? WorldGuardPlugin.inst().wrapPlayer(player) : null, flag);
    }

}
