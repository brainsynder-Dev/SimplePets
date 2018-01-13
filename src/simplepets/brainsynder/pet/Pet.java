package simplepets.brainsynder.pet;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import simple.brainsynder.sound.SoundMaker;
import simple.brainsynder.storage.IStorage;
import simple.brainsynder.storage.StorageList;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.entity.IEntityControllerPet;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.IHorseAbstract;
import simplepets.brainsynder.api.event.pet.PetHatEvent;
import simplepets.brainsynder.api.event.pet.PetPreSpawnEvent;
import simplepets.brainsynder.api.event.pet.PetVehicleEvent;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.files.PetTranslate;
import simplepets.brainsynder.menu.MenuItem;
import simplepets.brainsynder.player.PetOwner;
import simplepets.brainsynder.reflection.PetSpawner;
import simplepets.brainsynder.reflection.ReflectionUtil;
import simplepets.brainsynder.utils.LinkRetriever;
import simplepets.brainsynder.wrapper.EntityWrapper;

import java.util.List;
import java.util.UUID;

public class Pet implements IPet {
    private IEntityPet ent;
    private Player owner;
    private PetType type;
    private boolean isHidden = false;
    private boolean isHat = false;
    private IStorage<MenuItem> items;
    private boolean vehicle;

    public Pet(UUID player, PetType type) {
        this.type = type;
        if (player == null) {
            return;
        }
        this.owner = Bukkit.getPlayer(player);
        if (!type.hasPermission(owner)) {
            owner.sendMessage(PetCore.get().getMessages().getString("No-Pet-Permission"));
            return;
        }
        PetOwner petOwner = PetOwner.getPetOwner(this.owner);
        PetPreSpawnEvent event = new PetPreSpawnEvent(owner, null, type);
        Bukkit.getServer().getPluginManager().callEvent(event);
        PetCore.get().forceSpawn = true;
        Location spawnLoc = owner.getLocation();
        if (petOwner.hasPet()) {
            IPet oldPet = petOwner.getPet();
            spawnLoc = oldPet.getEntity().getEntity().getLocation().clone();
            petOwner.removePet();
        }
        boolean allow = LinkRetriever.canSpawnPet(spawnLoc);
        if (!allow) {
            SoundMaker.BLOCK_ANVIL_LAND.playSound(owner.getLocation(), 0.5F, 0.5F);
            owner.sendMessage(PetCore.get().getMessages().getString("No-Spawning", true));
            return;
        }
        if (PetCore.get().getConfiguration().getBoolean("Worlds.Enabled")) {
            String world = spawnLoc.getWorld().getName();
            if (!PetCore.get().getConfiguration().getStringList("Worlds.Allowed-Worlds").contains(world)) {
                SoundMaker.BLOCK_ANVIL_LAND.playSound(owner.getLocation(), 0.5F, 0.5F);
                owner.sendMessage(PetCore.get().getMessages().getString("No-Spawning", true));
                return;
            }
        }
        IEntityPet ent = PetSpawner.spawnPet(spawnLoc, this, type.getEntityClass());
        if (ent == null) {
            SoundMaker.BLOCK_ANVIL_LAND.playSound(owner.getLocation(), 0.5F, 0.5F);
            PetCore.get().debug(2, "Pet was unable to summon... (Entity is null, issue occurred in ISpawner class)");
            return;
        }
        PetCore.get().forceSpawn = false;
        ent.getEntity().setMetadata("pet", new FixedMetadataValue(PetCore.get(), "pet"));
        this.ent = ent;
        IStorage<MenuItem> items = new StorageList<>();
        if (type.getPetData() != null) {
            for (Class<? extends MenuItem> item : type.getPetData().getItemClasses()) {
                MenuItem item1 = getItem(item);
                if (item1.isSupported())
                    if (item1 != null)
                        items.add(item1);
            }
        }
        this.items = items;
        petOwner.setPet(this);

        List<String> commands = PetTranslate.getList(getPetType().getConfigName() + ".On-Summon");
        if (!commands.isEmpty()) {
            commands.forEach(command -> Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command
                    .replace("{player}", getOwner().getName())
                    .replace("{location}", getPet().getLocation().getX() + " " + getPet().getLocation().getY() + " " + getPet().getLocation().getZ())
                    .replace("{type}", getPetType().name())
            ));
        }
    }

    private MenuItem getItem(Class<? extends MenuItem> clazz) {
        try {
            IEntityPet entity = ent;
            if (ent instanceof IEntityControllerPet)
                entity = ((IEntityControllerPet) ent).getVisibleEntity();
            return ReflectionUtil.initiateClass(ReflectionUtil.fillConstructor(clazz, PetType.class, IEntityPet.class), type, entity);
        } catch (Exception e) {
            return null;
        }
    }

    public IStorage<MenuItem> getItems() {
        return items;
    }

    public void hatPet() {
        final Player p = owner;
        if (PetCore.get().getConfiguration().getBoolean("Pet.NeedsPermission")) {
            if (p.hasPermission("Pet.PetToHat")) {
                handleHat();
            }
        } else {
            handleHat();
        }
    }

    public void handleHat() {
        final Player player = owner;
        boolean delay = false;
        if (isVehicle()) {
            PetVehicleEvent event = new PetVehicleEvent(this, PetVehicleEvent.Type.DISMOUNT);
            Bukkit.getServer().getPluginManager().callEvent(event);
            if (event.isCancelled())
                return;
            if (getPet().getPassenger() != null) {
                getPet().eject();
            }
            vehicle = false;
            delay = true;
        }
        if (!isHat) {
            final PetHatEvent event = new PetHatEvent(this, PetHatEvent.Type.SET);
            Bukkit.getServer().getPluginManager().callEvent(event);
            if (!event.isCancelled()) {
                if (delay) {
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            if (!event.isCancelled()) {
                                setHat(true);
                                owner.setPassenger(ent.getEntity());
                            }
                        }
                    }.runTaskLater(PetCore.get(), 3);
                } else {
                    if (!event.isCancelled()) {
                        setHat(true);
                        owner.setPassenger(ent.getEntity());
                    }
                }
                PetCore.get().getUtilities().sendMountPacket(player, ent.getEntity());
            }
        } else {
            PetHatEvent event = new PetHatEvent(this, PetHatEvent.Type.REMOVE);
            Bukkit.getServer().getPluginManager().callEvent(event);
            if (!event.isCancelled()) {
                setHat(false);
                PetCore.get().getUtilities().removePassenger(player, ent.getEntity());
            }
        }
    }

    public void handleRide() {
        final Player p = owner;
        if (getPet().getPassenger() != null) {
            PetVehicleEvent event = new PetVehicleEvent(this, PetVehicleEvent.Type.DISMOUNT);
            Bukkit.getServer().getPluginManager().callEvent(event);
            if (event.isCancelled())
                return;
            getPet().eject();
            vehicle = false;
            return;
        } else if (isVehicle()) {
            vehicle = false;
        }

        if (isHat) {
            PetHatEvent event = new PetHatEvent(this, PetHatEvent.Type.REMOVE);
            Bukkit.getServer().getPluginManager().callEvent(event);
            if (!event.isCancelled()) {
                p.eject();
                setHat(false);
                PetCore.get().getUtilities().sendMountPacket(p, ent.getEntity());
            }
        }
        PetVehicleEvent event = new PetVehicleEvent(this, PetVehicleEvent.Type.MOUNT);
        Bukkit.getServer().getPluginManager().callEvent(event);
        if (event.isCancelled())
            return;
        vehicle = true;
        getPet().teleport(p);
        new BukkitRunnable() {
            @Override
            public void run() {
                ent.getEntity().setPassenger(p);
            }
        }.runTaskLater(PetCore.get(), 2L);
    }

    public void ridePet() {
        if (ent instanceof IHorseAbstract) {
            IHorseAbstract horse = (IHorseAbstract) ent;
            if (!horse.isSaddled()) {
                horse.setSaddled(true);
            }
        }
        final Player p = owner;
        if ((PetCore.get().getConfiguration().getBoolean("Needs-Permission"))) {
            if (p.hasPermission("Pet.PetToMount")) {
                handleRide();
            }
        } else {
            handleRide();
        }
    }

    public boolean isVehicle() {
        if (getPet().getPassenger() != null)
            return true;
        if (vehicle)
            return true;
        vehicle = false;
        return false;
    }

    public void setVehicle(boolean value) {
        if (type.canMount(owner)) {
            if (ent instanceof IHorseAbstract) {
                IHorseAbstract horse = (IHorseAbstract) ent;
                if (!horse.isSaddled()) horse.setSaddled(true);
            }

            if (getPet().getPassenger() != null) {
                PetVehicleEvent event = new PetVehicleEvent(this, PetVehicleEvent.Type.DISMOUNT);
                Bukkit.getServer().getPluginManager().callEvent(event);
                if (event.isCancelled()) return;

                getPet().eject();
                value = false;
            } else if (isVehicle()) {
                value = false;
            }

            if (value && (vehicle != value)) {
                if (isHat) {
                    PetHatEvent event = new PetHatEvent(this, PetHatEvent.Type.REMOVE);
                    Bukkit.getServer().getPluginManager().callEvent(event);
                    if (event.isCancelled()) return;

                    PetCore.get().getUtilities().removePassenger(owner, ent.getEntity());
                    setHat(false);
                }

                PetVehicleEvent event = new PetVehicleEvent(this, PetVehicleEvent.Type.MOUNT);
                Bukkit.getServer().getPluginManager().callEvent(event);
                if (event.isCancelled()) return;

                value = true;
                getPet().teleport(owner);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        ent.getEntity().setPassenger(owner);
                    }
                }.runTaskLater(PetCore.get(), 2L);
            }
        }

        vehicle = value;
    }

    public void setInvisible(boolean flag) {
        Player p = owner;
        if (flag && (!isHidden)) {
            PetCore.get().getUtilities().hidePet(p);
            this.isHidden = true;
        } else {
            PetCore.get().getUtilities().showPet(p);
            this.isHidden = false;
        }
    }

    public PetType getType() {
        return type;
    }

    public EntityWrapper getEntityType() {
        return type.getType();
    }

    public IEntityPet getEntity() {
        return ent;
    }

    public IEntityPet getVisableEntity() {
        if (ent instanceof IEntityControllerPet)
            return ((IEntityControllerPet) ent).getVisibleEntity();
        return ent;
    }

    public Player getOwner() {
        return owner;
    }

    @Override
    public void removePet(boolean var1) {
        if (isHat()) handleHat();

        if (ent instanceof IEntityControllerPet) {
            ((IEntityControllerPet) ent).remove();
            return;
        }
        getPet().remove();
    }

    @Override
    public void teleport(Location var1) {
        getPet().teleport(var1);
    }

    @Override
    public PetType getPetType() {
        return type;
    }

    public Entity getPet() {
        return ent.getEntity();
    }

    public boolean isHidden() {
        return isHidden;
    }

    public void setHidden(boolean isHidden) {
        this.isHidden = isHidden;
    }

    public boolean isHat() {
        return isHat;
    }

    public void setHat(boolean value) {
        int delay = 1;
        if (isVehicle()) {
            setVehicle(false);
            delay = 3;
        }

        if (value) {
            PetHatEvent event = new PetHatEvent(this, PetHatEvent.Type.SET);
            Bukkit.getServer().getPluginManager().callEvent(event);
            if (event.isCancelled()) return;

            new BukkitRunnable() {
                @Override
                public void run() {
                    PetCore.get().getUtilities().clearPathfinders(owner);
                    owner.setPassenger(ent.getEntity());
                    PetCore.get().getUtilities().sendMountPacket(owner, ent.getEntity());
                }
            }.runTaskLater(PetCore.get(), delay);
        } else {
            PetHatEvent event = new PetHatEvent(this, PetHatEvent.Type.REMOVE);
            Bukkit.getServer().getPluginManager().callEvent(event);
            if (event.isCancelled()) return;

            PetCore.get().getUtilities().handlePathfinders(owner, getPet(), type.getSpeed());
            PetCore.get().getUtilities().removePassenger(owner, ent.getEntity());

        }

        if (value) {
            PetCore.get().getUtilities().clearPathfinders(owner);
        } else {
            if (owner.getPassenger() == null) {
                value = false;
                PetCore.get().getUtilities().handlePathfinders(owner, getPet(), type.getSpeed());
            }
        }
        this.isHat = value;
    }
}
