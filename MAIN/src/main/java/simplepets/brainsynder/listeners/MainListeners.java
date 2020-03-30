package simplepets.brainsynder.listeners;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.scheduler.BukkitRunnable;
import org.spigotmc.event.entity.EntityDismountEvent;
import org.spigotmc.event.entity.EntityMountEvent;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.misc.IImpossaPet;
import simplepets.brainsynder.player.PetOwner;
import simplepets.brainsynder.reflection.ReflectionUtil;
import simplepets.brainsynder.utils.Utilities;

import java.util.List;

public class MainListeners implements Listener {
    @EventHandler
    public void onhurt(EntityDamageEvent e) {
        Object handle = ReflectionUtil.getEntityHandle(e.getEntity());
        if (handle instanceof IImpossaPet) {
            e.setCancelled(true);
            return;
        }

        // Should fix issue with fall damage while riding pet...
        if ((e.getCause() != EntityDamageEvent.DamageCause.SUFFOCATION)
                && (e.getCause() != EntityDamageEvent.DamageCause.FALL)) return;
        if (e.getEntity() instanceof Player) {
            Player player = (Player) e.getEntity();
            PetOwner owner = PetOwner.getPetOwner(player);
            if (owner == null) return;
            if (!owner.hasPet()) return;
            if (!owner.getPet().isVehicle()) return;
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onhurt(EntityDamageByEntityEvent e) {
        Object handle = ReflectionUtil.getEntityHandle(e.getEntity());
        if (handle instanceof IImpossaPet) e.setCancelled(true);
    }

    @EventHandler
    public void onhurt(EntityDamageByBlockEvent e) {
        Object handle = ReflectionUtil.getEntityHandle(e.getEntity());
        if (handle instanceof IImpossaPet) e.setCancelled(true);
    }

    @EventHandler
    public void onManipulate(PlayerArmorStandManipulateEvent e) {
        Object handle = ReflectionUtil.getEntityHandle(e.getRightClicked());
        if (handle instanceof IEntityPet) {
            e.setCancelled(true);
            IEntityPet entityPet = (IEntityPet) handle;
            if (entityPet.getOwner().getName().equals(e.getPlayer().getName())) {
                PetCore.get().getInvLoaders().PET_DATA.open(PetOwner.getPetOwner(entityPet.getOwner()));
            }
        }
    }

    @EventHandler
    public void onInteract(EntityMountEvent e) {
        Object handle = ReflectionUtil.getEntityHandle(e.getMount());
        if (handle instanceof IEntityPet) {
            IEntityPet entityPet = (IEntityPet) handle;
            e.setCancelled(!entityPet.getPet().isVehicle());
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onInteract(PlayerInteractAtEntityEvent e) {
        if (!(e.getRightClicked() instanceof Player)) {
            Object handle = ReflectionUtil.getEntityHandle(e.getRightClicked());
            if (handle instanceof IEntityPet) {
                e.setCancelled(true);
                IEntityPet entityPet = (IEntityPet) handle;

                if (entityPet.getOwner().getName().equals(e.getPlayer().getName())) {
                    PetCore.get().getInvLoaders().PET_DATA.open(PetOwner.getPetOwner(entityPet.getOwner()));
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onDeath(PlayerDeathEvent e) {
        Player p = e.getEntity();
        if (p != null) {
            PetOwner owner = PetOwner.getPetOwner(p);
            if (owner != null) {
                if (owner.hasPet()) {
                    if (owner.getPet().getVisableEntity() == null) return;
                    if (!owner.hasPetToRespawn()) {
                        owner.setPetToRespawn(owner.getPet().getVisableEntity().asCompound());
                    }
                    owner.removePet();
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onRespawn(PlayerRespawnEvent e) {
        final Player p = e.getPlayer();
        if (p == null) {
            final PetOwner owner = PetOwner.getPetOwner(p);
            if (owner != null) {
                if (owner.hasPetToRespawn()) {
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            owner.respawnPet();
                        }
                    }.runTaskLater(PetCore.get(), 40);
                }
            }
        }
    }

    @EventHandler
    public void onTeleport(final PlayerTeleportEvent e) {
        if (e.getCause() == PlayerTeleportEvent.TeleportCause.UNKNOWN) return;
        Player p = e.getPlayer();
        PetOwner owner = PetOwner.getPetOwner(p);
        if (owner == null) return;
        owner.respawnPetFully(40);
    }

    @EventHandler
    public void onExit(EntityDismountEvent e) {
        Object handle = ReflectionUtil.getEntityHandle(e.getDismounted());
        if (handle instanceof IEntityPet) {
            IEntityPet pet = (IEntityPet) handle;
            if (pet.getOwner().getName().equals(e.getEntity().getName())) {
                pet.getPet().setVehicle(false, true);
                Location loc = pet.getEntity().getLocation();
                if (e.getEntity() instanceof Player) {
                    List<Material> blocks = Utilities.getBlacklistedMaterials();
                    if (blocks.contains(e.getEntity().getLocation().getBlock().getType())) {
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                e.getEntity().teleport(pet.getEntity().getLocation());
                            }
                        }.runTaskLater(PetCore.get(), 3);
                    } else {
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                if (!loc.getWorld().getName().equals(e.getEntity().getLocation().getWorld().getName())) {
                                    PetOwner.getPetOwner(pet.getOwner()).respawnPetFully(40);
                                    return;
                                }
                                if (loc.distanceSquared(e.getEntity().getLocation()) >= 5) {
                                    pet.getEntities().forEach(entity -> entity.teleport(e.getEntity().getLocation()));
                                }
                            }
                        }.runTaskLater(PetCore.get(), 10);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent e) {
        final Player p = e.getPlayer();

        final PetOwner owner = PetOwner.getPetOwner(p);
        if (owner != null) {
            if (owner.hasPet()) {
                if (PetCore.get().getConfiguration().getBoolean("RemovePetsOnWorldChange")) {
                    owner.removePet();
                    return;
                }

                owner.respawnPetFully(40);
            }
        }
    }
}
