package simplepets.brainsynder.events;

import org.bukkit.entity.Entity;
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
import simplepets.brainsynder.menu.PetDataMenu;
import simplepets.brainsynder.nms.entities.type.IEntityParrotPet;
import simplepets.brainsynder.nms.entities.type.IEntityShulkerPet;
import simplepets.brainsynder.nms.entities.type.main.IEntityPet;
import simplepets.brainsynder.nms.entities.type.main.IImpossaPet;
import simplepets.brainsynder.pet.IPet;
import simplepets.brainsynder.player.PetOwner;
import simplepets.brainsynder.reflection.ReflectionUtil;
import simplepets.brainsynder.utils.PetRespawner;

import java.util.UUID;

public class OnHurtPet extends EventCore implements Listener {
    @EventHandler
    public void onhurt(EntityDamageEvent e) {
        if (e.getEntity().hasMetadata("NO_DAMAGE")) {
            e.setCancelled(true);
            return;
        }
        if (!(e.getEntity() instanceof Player)) {
            Object handle = ReflectionUtil.getEntityHandle(e.getEntity());
            if (handle instanceof IImpossaPet) {
                e.setCancelled(true);
            }
        } else {
            Player p = (Player) e.getEntity();
            PetOwner owner = PetOwner.getPetOwner(p);
            if (owner.hasPet()) {
                if (p.isInsideVehicle()) {
                    if (p.getVehicle() == owner.getPet().getEntity().getEntity()) {
                        if (e.getCause() == EntityDamageEvent.DamageCause.SUFFOCATION) {
                            e.setCancelled(true);
                        }
                        if (e.getCause() == EntityDamageEvent.DamageCause.FALL) {
                            e.setCancelled(true);
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onhurt(EntityDamageByEntityEvent e) {
        if (e.getEntity().hasMetadata("NO_DAMAGE")) {
            e.setCancelled(true);
            return;
        }
        if (!(e.getEntity() instanceof Player)) {
            Object handle = ReflectionUtil.getEntityHandle(e.getEntity());
            if (handle instanceof IEntityPet) {
                e.setCancelled(true);
            }
        } else {
            Player p = (Player) e.getEntity();
            if (!(e.getDamager() instanceof Player)) {
                Entity ent = e.getDamager();
                Object handle = ReflectionUtil.getEntityHandle(ent);
                if (handle instanceof IEntityPet) {
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onhurt(EntityDamageByBlockEvent e) {
        if (e.getEntity().hasMetadata("NO_DAMAGE")) {
            e.setCancelled(true);
            return;
        }
        if (!(e.getEntity() instanceof Player)) {
            Object handle = ReflectionUtil.getEntityHandle(e.getEntity());
            if (handle instanceof IImpossaPet) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEntityEvent e) {
        if (!(e.getRightClicked() instanceof Player)) {
            Object handle = ReflectionUtil.getEntityHandle(e.getRightClicked());
            if (handle instanceof IEntityPet) {
                e.setCancelled(true);
                IEntityPet entityPet = (IEntityPet) handle;
                if (entityPet instanceof IEntityShulkerPet)
                    return;
                if (entityPet instanceof IEntityParrotPet)
                    return;
                if (entityPet.getOwner().getName().equals(e.getPlayer().getName())) {
                    PetDataMenu data = new PetDataMenu(entityPet.getPet());
                    data.showTo(entityPet.getOwner());
                }
            }
        }
    }

    @EventHandler
    public void onManipulate(PlayerArmorStandManipulateEvent e) {
        Object handle = ReflectionUtil.getEntityHandle(e.getRightClicked());
        if (handle instanceof IEntityPet) {
            e.setCancelled(true);
            IEntityPet entityPet = (IEntityPet) handle;
            if (entityPet.getOwner().getName().equals(e.getPlayer().getName())) {
                PetDataMenu data = new PetDataMenu(entityPet.getPet());
                data.showTo(entityPet.getOwner());
            }
        }
    }

    @EventHandler
    public void onInteract(EntityMountEvent e) {
        Object handle = ReflectionUtil.getEntityHandle(e.getMount());
        if (handle instanceof IEntityPet) {
            IEntityPet entityPet = (IEntityPet) handle;
            if (!entityPet.getPet().isVehicle())
                e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onDeath(PlayerDeathEvent e) {
        Player p = e.getEntity();
        if (p == null) return;
        PetOwner owner = PetOwner.getPetOwner(p);
        if (owner.hasPet()) {
            if (owner.getPet().getVisableEntity() == null) return;
            if (!petTypeMap.containsKey(p.getUniqueId())) {
                petTypeMap.put(p.getUniqueId(), new PetRespawner(owner.getPet().getVisableEntity()));
            }
            owner.removePet();
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onRespawn(PlayerRespawnEvent e) {
        final Player p = e.getPlayer();
        if (p == null) return;
        final PetOwner petOwner = PetOwner.getPetOwner(p);
        if (petTypeMap.containsKey(p.getUniqueId())) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (petTypeMap.containsKey(p.getUniqueId())) {
                        if (!p.isOnline()) {
                            petTypeMap.remove(p.getUniqueId());
                            return;
                        }
                        PetRespawner respawner = petTypeMap.getKey(p.getUniqueId());
                        respawner.getPetType().setPet(p);
                        petOwner.getPet().getVisableEntity().applyCompound(respawner.getEntityData());
                        petTypeMap.remove(p.getUniqueId());
                    }
                }
            }.runTaskLater(PetCore.get(), 40);
        }
    }

    @EventHandler
    public void onTeleport(final PlayerTeleportEvent e) {
        final Player p = e.getPlayer();
        final PetOwner petOwner = PetOwner.getPetOwner(p);
        if (petOwner.hasPet()) {
            if (e.getCause() != PlayerTeleportEvent.TeleportCause.UNKNOWN) {
                if (p.getPassenger() != null) {
                    e.setCancelled(true);
                    return;
                }
                UUID uuid = p.getUniqueId();
                IPet pet = petOwner.getPet();
                if (pet.getVisableEntity() == null) return;
                if (!petTypeMap.containsKey(uuid)) {
                    petTypeMap.put(uuid, new PetRespawner(pet.getVisableEntity()));
                    petOwner.removePet();
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            if (petTypeMap.containsKey(p.getUniqueId())) {
                                if (!p.isOnline()) {
                                    petTypeMap.remove(uuid);
                                    return;
                                }
                                PetRespawner respawner = petTypeMap.getKey(p.getUniqueId());
                                respawner.getPetType().setPet(p);
                                IPet pet = petOwner.getPet();
                                pet.getVisableEntity().applyCompound(respawner.getEntityData());
                                petTypeMap.remove(uuid);
                            }
                        }
                    }.runTaskLater(PetCore.get(), 40);
                }
            }
        }
    }

    @EventHandler
    public void onExit(EntityDismountEvent e) {
        Object handle = ReflectionUtil.getEntityHandle(e.getDismounted());
        if (handle instanceof IEntityPet) {
            IEntityPet pet = (IEntityPet) handle;
            if (pet.getOwner().getName().equals(e.getEntity().getName())) {
                pet.getPet().setVehicle(false);
            }
        }
    }

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent e) {
        final Player p = e.getPlayer();
        final PetOwner petOwner = PetOwner.getPetOwner(p);
        if (petOwner.hasPet()) {
            if (PetCore.get().getConfiguration().getBoolean("RemovePetsOnWorldChange")) {
                petOwner.removePet();
            } else {
                final UUID uuid = p.getUniqueId();
                final IPet pet = petOwner.getPet();
                if (petOwner.hasPet()) {
                    if (pet.getVisableEntity() == null) return;
                    if (!petTypeMap.containsKey(uuid)) {
                        petTypeMap.put(uuid, new PetRespawner(pet.getVisableEntity()));
                        petOwner.removePet();
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                if (petTypeMap.containsKey(p.getUniqueId())) {
                                    if (!p.isOnline()) {
                                        petTypeMap.remove(uuid);
                                        return;
                                    }
                                    PetRespawner respawner = petTypeMap.getKey(p.getUniqueId());
                                    respawner.getPetType().setPet(p);
                                    pet.getVisableEntity().applyCompound(respawner.getEntityData());
                                    petTypeMap.remove(uuid);
                                }
                            }
                        }.runTaskLater(PetCore.get(), 40);
                    }
                }
            }
        }
    }
}
