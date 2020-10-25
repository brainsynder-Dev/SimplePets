package simplepets.brainsynder.links.impl;

import org.bukkit.Location;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.links.IWorldGuardLink;
import simplepets.brainsynder.links.impl.worldguard.FlagHandler;
import simplepets.brainsynder.links.impl.worldguard.WGInterface;
import simplepets.brainsynder.links.impl.worldguard.old.WGOld;
import simplepets.brainsynder.player.PetOwner;

public class WorldGuardLink extends PluginLink implements IWorldGuardLink {
    private WGInterface wgInterface = null;
    private FlagHandler handler = null;

    public WorldGuardLink() {
        super("WorldGuard");
    }

    @Override
    public boolean onHook() {
        try {
            handler = new FlagHandler();
            return true;
        }catch (Exception e2) {
            e2.printStackTrace();
        }
        if (wgInterface == null) return false;
        if (!wgInterface.initiate()) {
            PetCore.get().debug("An Error occurred when initiating WGInterface");
            return false;
        }
        return true;
    }

    @Override
    public void onUnhook() {
        if (wgInterface != null) wgInterface.cleanup();
        wgInterface = null;
        super.onUnhook();
    }

    @Override
    public boolean allowPetEntry(PetOwner owner, Location at) {
        return handler == null || handler.canPetEnter(owner == null ? null : owner.getPlayer(), at);
    }

    @Override
    public boolean allowPetSpawn(PetOwner owner, Location at) {
        return handler == null || handler.canPetSpawn(owner == null ? null : owner.getPlayer(), at);
    }

    @Override
    public boolean allowPetRiding(PetOwner owner, Location at) {
        return handler == null || handler.canRidePet(owner == null ? null : owner.getPlayer(), at);
    }
}
