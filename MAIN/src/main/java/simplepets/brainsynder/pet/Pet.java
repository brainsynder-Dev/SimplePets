package simplepets.brainsynder.pet;

import lib.brainsynder.ServerVersion;
import lib.brainsynder.sounds.SoundMaker;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.entity.IEntityControllerPet;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.misc.IHorseAbstract;
import simplepets.brainsynder.api.event.pet.PetHatEvent;
import simplepets.brainsynder.api.event.pet.PetPreSpawnEvent;
import simplepets.brainsynder.api.event.pet.PetVehicleEvent;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.menu.menuItems.base.MenuItem;
import simplepets.brainsynder.player.PetOwner;
import simplepets.brainsynder.reflection.PetSpawner;
import simplepets.brainsynder.reflection.ReflectionUtil;
import simplepets.brainsynder.utils.Utilities;
import simplepets.brainsynder.wrapper.EntityWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Pet implements IPet {
    private IEntityPet ent;
    private Player owner;
    private final PetType type;
    private boolean isHidden = false;
    private boolean isHat = false;
    private List<MenuItem> items;
    private boolean vehicle;
    private final PetCore instance;

    public Pet(UUID player, PetType type, PetCore core) {
        this.type = type;
        this.instance = core;
        if (player == null) {
            return;
        }
        this.owner = Bukkit.getPlayer(player);
        if (!type.hasPermission(owner)) {
            owner.sendMessage(core.getMessages().getString("No-Pet-Permission"));
            return;
        }
        PetOwner petOwner = PetOwner.getPetOwner(this.owner);
        PetPreSpawnEvent event = new PetPreSpawnEvent(owner, null, type);
        Bukkit.getServer().getPluginManager().callEvent(event);
        core.forceSpawn = true;
        Location spawnLoc = owner.getLocation();
        Location walkTo = null;
        if (petOwner.hasPet()) {
            IPet oldPet = petOwner.getPet();
            walkTo = oldPet.getEntity().getWalkToLocation();
            spawnLoc = oldPet.getEntity().getEntity().getLocation().clone();
            petOwner.removePet();
        }
        boolean allow = core.getLinkRetriever().canSpawnPet(spawnLoc);
        if (!allow) {
            SoundMaker.BLOCK_ANVIL_LAND.playSound(owner.getLocation(), 0.5F, 0.5F);
            owner.sendMessage(core.getMessages().getString("No-Spawning", true));
            return;
        }
        if (core.getConfiguration().getBoolean("Worlds.Enabled")) {
            String world = spawnLoc.getWorld().getName();
            if (!core.getConfiguration().getStringList("Worlds.Allowed-Worlds").contains(world)) {
                SoundMaker.BLOCK_ANVIL_LAND.playSound(owner.getLocation(), 0.5F, 0.5F);
                owner.sendMessage(core.getMessages().getString("No-Spawning", true));
                return;
            }
        }
        IEntityPet ent = PetSpawner.spawnPet(spawnLoc, this, type.getEntityClass());
        if (ent == null) {
            SoundMaker.BLOCK_ANVIL_LAND.playSound(owner.getLocation(), 0.5F, 0.5F);
            core.debug(2, "Pet was unable to summon... (Entity is null, issue occurred in ISpawner class)");
            return;
        }
        ent.setWalkToLocation(walkTo);
        core.forceSpawn = false;
        ent.getEntity().setMetadata("pet", new FixedMetadataValue(core, "pet"));
        this.ent = ent;
        List<MenuItem> items = new ArrayList<>();
        if (type.getPetData() != null) {
            for (Class<? extends MenuItem> item : type.getPetData().getItemClasses()) {
                MenuItem item1 = getItem(item);
                if (item1 != null)
                    if (item1.isSupported()) {
                        items.add(item1);
                    }else{
                        PetCore.get().debug(type.getConfigName()+" DataItem '"+item1.getName()+"' as it is not supported for this version ("+ ServerVersion.getVersion().name() +").");
                    }
            }
        }
        this.items = items;
        petOwner.setPet(this);

        List<String> commands = type.getCommands();
        if (!commands.isEmpty()) {
            commands.forEach(command -> Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command
                    .replace("{player}", getOwner().getName())
                    .replace("{location}", getPet().getLocation().getX() + " " + getPet().getLocation().getY() + " " + getPet().getLocation().getZ())
                    .replace("{type}", getPetType().getConfigName())
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

    public List<MenuItem> getItems() {
        return items;
    }

    public void toggleHat() {
        setHat(!isHat());
    }

    public void toggleRiding(boolean byEvent) {
        setVehicle(!isVehicle(), byEvent);
    }

    public boolean isVehicle() {
        if (getPet().getPassenger() != null)
            return true;
        if (vehicle)
            return true;
        vehicle = false;
        return false;
    }

    public void setVehicle(boolean value, boolean byEvent) {
        if (!byEvent) {
            if (type.canMount(owner)) {
                if (ent instanceof IHorseAbstract) {
                    IHorseAbstract horse = (IHorseAbstract) ent;
                    if (!horse.isSaddled()) horse.setSaddled(true);
                }

                if (getPet().getPassenger() != null) {
                    PetVehicleEvent event = new PetVehicleEvent(this, PetVehicleEvent.Type.DISMOUNT);
                    Bukkit.getServer().getPluginManager().callEvent(event);
                    if (event.isCancelled()) return;

                    if (ent instanceof IEntityControllerPet) {
                        ((IEntityControllerPet) ent).getDisplayEntity().eject();
                    } else {
                        ent.getEntity().eject();
                    }
                    value = false;
                } else if (isVehicle()) {
                    value = false;
                }

                if (value && (vehicle != value)) {
                    if (isHat) {
                        PetHatEvent event = new PetHatEvent(this, PetHatEvent.Type.REMOVE);
                        Bukkit.getServer().getPluginManager().callEvent(event);
                        if (event.isCancelled()) return;

                        instance.getUtilities().removePassenger(owner, ent.getEntity());
                        setHat(false);
                    }

                    PetVehicleEvent event = new PetVehicleEvent(this, PetVehicleEvent.Type.MOUNT);
                    Bukkit.getServer().getPluginManager().callEvent(event);
                    if (event.isCancelled()) return;

                    value = true;
                    if (owner.getLocation().getBlock() != null) {
                        List<Material> blocks = Utilities.getBlacklistedMaterials();
                        if (!blocks.contains(owner.getLocation().getBlock().getType()) && !blocks.contains(owner.getEyeLocation().getBlock().getType())) {
                            getPet().teleport(owner);
                        }
                    } else {
                        getPet().teleport(owner);
                    }
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            if (ent instanceof IEntityControllerPet) {
                                ((IEntityControllerPet) ent).getDisplayEntity().setPassenger(owner);
                            } else {
                                ent.getEntity().setPassenger(owner);
                            }

                        }
                    }.runTaskLater(PetCore.get(), 2L);
                }
            }
        }
        vehicle = value;
    }

    public void setInvisible(boolean flag) {
        Player p = owner;
        if (flag && (!isHidden)) {
            instance.getUtilities().hidePet(p);
            this.isHidden = true;
        } else {
            instance.getUtilities().showPet(p);
            this.isHidden = false;
        }
    }

    public PetType getType() {
        return type;
    }

    public EntityWrapper getEntityType() {
        return type.getEntityType();
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
        if (isHat()) setHat(false);

        if (ent instanceof IEntityControllerPet) {
            ((IEntityControllerPet) ent).remove();
            return;
        }
        getPet().remove();
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

    public void setHidden(boolean flag) {
        Player p = owner;
        if (flag && (!isHidden)) {
            instance.getUtilities().hidePet(p);
            this.isHidden = true;
        } else {
            instance.getUtilities().showPet(p);
            this.isHidden = false;
        }
    }

    public boolean isHat() {
        if ((owner.getPassenger() != null)
                && (ReflectionUtil.getEntityHandle(owner.getPassenger()) instanceof IEntityPet)) {
            if (!isHat) isHat = true;
            return true;
        }
        return isHat;
    }

    public void setHat(boolean value) {
        int delay = 1;
        if (isVehicle()) {
            setVehicle(false, false);
            delay = 3;
        }
        IEntityPet ent = this.ent;

        if (ent instanceof IEntityControllerPet) {
            ent = ((IEntityControllerPet)ent).getVisibleEntity();
        }

        if (type.canHat(owner) && value && (isHat () != value)) {
            PetHatEvent event = new PetHatEvent(this, PetHatEvent.Type.SET);
            Bukkit.getServer().getPluginManager().callEvent(event);
            if (event.isCancelled()) return;

            IEntityPet finalEnt = ent;
            new BukkitRunnable() {
                @Override
                public void run() {
                    instance.getUtilities().clearPathfinders(owner);
                    instance.getUtilities().setPassenger(owner, finalEnt.getEntity());
                }
            }.runTaskLater(PetCore.get(), delay);
        } else {
            if (isHat()) {
                value = false;
                PetHatEvent event = new PetHatEvent(this, PetHatEvent.Type.REMOVE);
                Bukkit.getServer().getPluginManager().callEvent(event);
                if (event.isCancelled()) return;

                instance.getUtilities().handlePathfinders(owner, getPet(), type.getSpeed());
                if (ent instanceof IEntityControllerPet) {
                    IEntityControllerPet controller = ((IEntityControllerPet)ent);
                    if (controller.getDisplayRider() != null) {
                        getOwner().eject();
                        instance.getUtilities().sendMountPacket(getOwner(), controller.getDisplayRider());
                        instance.getUtilities().resetRideCooldown(controller.getDisplayRider());
                        controller.getDisplayEntity().setPassenger(controller.getDisplayRider());
                    }else{
                        instance.getUtilities().removePassenger(owner, ent.getEntity());
                    }
                }else{
                    instance.getUtilities().removePassenger(owner, ent.getEntity());
                }
            }
        }

        this.isHat = value;
    }
}
