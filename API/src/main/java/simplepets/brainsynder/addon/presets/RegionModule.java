package simplepets.brainsynder.addon.presets;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import simplepets.brainsynder.addon.AddonConfig;
import simplepets.brainsynder.addon.AddonPermissions;
import simplepets.brainsynder.addon.PermissionData;
import simplepets.brainsynder.addon.PetModule;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.misc.IEntityControllerPet;
import simplepets.brainsynder.api.event.entity.PetEntitySpawnEvent;
import simplepets.brainsynder.api.event.entity.PetMountEvent;
import simplepets.brainsynder.api.event.entity.PetMoveEvent;
import simplepets.brainsynder.api.event.entity.movment.PetRideEvent;
import simplepets.brainsynder.api.user.PetUser;

public abstract class RegionModule extends PetModule {
    private boolean spawningEnabled = true;
    private String spawningReason;

    private boolean movingEnabled = true;
    private boolean movingRemove = true;

    private boolean mountingEnabled = true;
    private boolean mountingRemove = true;

    private boolean ridingEnabled = true;
    private boolean ridingRemove = true;
    private boolean ridingDismount = true;

    private PermissionData bypassPermission;

    public abstract boolean isSpawningAllowed (PetUser user, Location location);
    public abstract boolean isMovingAllowed (PetUser user, Location location);
    public abstract boolean isRidingAllowed (PetUser user, Location location);
    public abstract boolean isMountingAllowed (PetUser user, Location location);

    @Override
    public void init() {
        AddonPermissions.register(this, bypassPermission);
    }

    @Override
    public void loadDefaults(AddonConfig config) {
        config.addDefault("bypass-permission", "pet."+getNamespace().namespace().toLowerCase()+".bypass",
                "What should the bypass permission be set to?");

        config.addDefault("checks.spawning.enabled", true, "Should the addon check when a pet is spawned?");
        config.addDefault("checks.spawning.reason", "&cYour pet is not able to be spawned in this area",
                "This message is only visible when you hover over the 'pet failed to spawn' message\n" +
                        "You can make this blank or 'null' if you want no message");

        config.addDefault("checks.mounting.enabled", true, "Should the addon check when the player mounts the pet?");
        config.addDefault("checks.mounting.remove-pet", true, "Should the pet be removed or should it just be canceled?");

        config.addDefault("checks.moving.enabled", true, "Should the addon check when a pet moves?");
        config.addDefault("checks.moving.remove-pet", true, "Should the pet be removed or should it just be canceled?");

        config.addDefault("checks.riding.enabled", true, "Should the addon check when the pets owner is riding it?");
        config.addDefault("checks.riding.remove-pet", true, "Should the pet be removed or should it just be canceled?");
        config.addDefault("checks.riding.dismount", true, "Should the player be dismounted (if remove-pet is set to true this is ignored)?");

        spawningEnabled = config.getBoolean("checks.spawning.enabled", true);
        spawningReason = config.getString("checks.spawning.reason", null);

        mountingEnabled = config.getBoolean("checks.mounting.enabled", true);
        mountingRemove = config.getBoolean("checks.mounting.remove-pet", true);

        movingEnabled = config.getBoolean("checks.moving.enabled", true);
        movingRemove = config.getBoolean("checks.moving.remove-pet", true);

        ridingEnabled = config.getBoolean("checks.riding.enabled", true);
        ridingRemove = config.getBoolean("checks.riding.remove-pet", true);
        ridingDismount = config.getBoolean("checks.riding.dismount", true);

        bypassPermission = new PermissionData(config.getString("bypass-permission", "pet."+getNamespace().namespace().toLowerCase()+".bypass"))
                .setDescription("When the player has this permission they will be able to bypass the checks");
    }

    @EventHandler
    public void onSpawn(PetEntitySpawnEvent event) {
        if (!spawningEnabled) return; // Spawn checking is disabled
        Player player = event.getUser().getPlayer();
        if (player.hasPermission(bypassPermission.getPermission())) return; // Player has bypass SKIPPING

        // If the player/pet is not allowed in region cancel event
        boolean allowed = isSpawningAllowed(event.getUser(), event.getEntity().getEntity().getLocation());

        if ((spawningReason == null) || (spawningReason.equalsIgnoreCase("null")) || spawningReason.isEmpty()) {
            event.setCancelled(!allowed);
        }else{
            event.setCancelled(!allowed, spawningReason);
        }
    }

    @EventHandler
    public void onMove (PetMoveEvent event) {
        if (!movingEnabled) return; // Move checking is disabled
        Player player = event.getEntity().getPetUser().getPlayer();
        if (player == null) return;
        if (player.hasPermission(bypassPermission.getPermission())) return; // Player has bypass SKIPPING

        // Pet is allowed in region...
        if (isMovingAllowed(event.getEntity().getPetUser(), event.getTargetLocation())) return;
        event.setCancelled(true);
        if (movingRemove) event.getEntity().getPetUser().removePet(event.getEntity().getPetType());
    }

    @EventHandler
    public void onRide (PetRideEvent event) {
        if (!ridingEnabled) return; // Ride checking is disabled
        IEntityPet entityPet = event.getEntity();
        PetUser user = entityPet.getPetUser();
        Player player = user.getPlayer();
        if (player.hasPermission(bypassPermission.getPermission())) return; // Player has bypass SKIPPING

        // Pet is allowed in region...
        if (isRidingAllowed(user, event.getTargetLocation())) return;
        if (ridingRemove) {
            user.removePet(entityPet.getPetType());
            return;
        }else if (ridingDismount){
            if (entityPet.getEntity().getPassenger() != null) {
                if (entityPet instanceof IEntityControllerPet) {
                    ((IEntityControllerPet) entityPet).getDisplayEntity().ifPresent(Entity::eject);
                } else {
                    entityPet.getEntity().eject();
                }
            }
            return;
        }
        event.setCancelled(true);
    }

    @EventHandler
    public void onMount (PetMountEvent event) {
        if (!mountingEnabled) return; // Ride checking is disabled
        PetUser user = event.getEntity().getPetUser();
        Player player = user.getPlayer();
        if (player.hasPermission(bypassPermission.getPermission())) return; // Player has bypass SKIPPING

        // Pet is allowed in region...
        if (isMountingAllowed(user, event.getEntity().getEntity().getLocation())) return;
        event.setCancelled(true);
        if (mountingRemove) user.removePet(event.getEntity().getPetType());
    }

}
