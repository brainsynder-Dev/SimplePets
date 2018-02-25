package simplepets.brainsynder.pet;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
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
import simplepets.brainsynder.menu.menuItems.base.MenuItem;
import simplepets.brainsynder.player.PetOwner;
import simplepets.brainsynder.reflection.PetSpawner;
import simplepets.brainsynder.reflection.ReflectionUtil;
import simplepets.brainsynder.wrapper.EntityWrapper;

import java.util.ArrayList;
import java.util.Arrays;
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
    private PetCore instance;

    public Pet(UUID player, PetType type, PetCore instance) {
        PetCore core = PetCore.get();
        this.type = type;
        this.instance = instance;
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
        if (petOwner.hasPet()) {
            IPet oldPet = petOwner.getPet();
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
        core.forceSpawn = false;
        ent.getEntity().setMetadata("pet", new FixedMetadataValue(core, "pet"));
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

        List<String> commands = core.getTranslator().getStringList(getPetType().getConfigName() + ".On-Summon");
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

    public void toggleHat() {
        setHat(!isHat());
    }

    public void toggleRiding() {
        setVehicle(!isVehicle());
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

                    instance.getUtilities().removePassenger(owner, ent.getEntity());
                    setHat(false);
                }

                PetVehicleEvent event = new PetVehicleEvent(this, PetVehicleEvent.Type.MOUNT);
                Bukkit.getServer().getPluginManager().callEvent(event);
                if (event.isCancelled()) return;

                value = true;
                if (owner.getLocation().getBlock() != null) {
                    List<Material> blocks = new ArrayList<>(Arrays.asList(Material.STAINED_GLASS_PANE,
                            Material.THIN_GLASS,
                            Material.IRON_FENCE,
                            Material.IRON_DOOR,
                            Material.WOODEN_DOOR,
                            Material.ACACIA_DOOR,
                            Material.BIRCH_DOOR,
                            Material.DARK_OAK_DOOR,
                            Material.JUNGLE_DOOR,
                            Material.SPRUCE_DOOR,
                            Material.WOOD_DOOR,
                            Material.ACACIA_FENCE_GATE,
                            Material.BIRCH_FENCE_GATE,
                            Material.DARK_OAK_FENCE_GATE,
                            Material.FENCE_GATE,
                            Material.JUNGLE_FENCE_GATE,
                            Material.SPRUCE_FENCE_GATE));
                    if (!blocks.contains(owner.getLocation().getBlock().getType()) && !blocks.contains(owner.getEyeLocation().getBlock().getType())) {
                        getPet().teleport(owner);
                    }
                } else {
                    getPet().teleport(owner);
                }
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
            setVehicle(false);
            delay = 3;
        }


        if (type.canHat(owner) && value && (isHat () != value)) {
            PetHatEvent event = new PetHatEvent(this, PetHatEvent.Type.SET);
            Bukkit.getServer().getPluginManager().callEvent(event);
            if (event.isCancelled()) return;

            new BukkitRunnable() {
                @Override
                public void run() {
                    instance.getUtilities().clearPathfinders(owner);
                    instance.getUtilities().setPassenger(owner, ent.getEntity());
                }
            }.runTaskLater(PetCore.get(), delay);
        } else {
            if (isHat()) {
                value = false;
                PetHatEvent event = new PetHatEvent(this, PetHatEvent.Type.REMOVE);
                Bukkit.getServer().getPluginManager().callEvent(event);
                if (event.isCancelled()) return;

                instance.getUtilities().handlePathfinders(owner, getPet(), type.getSpeed());
                instance.getUtilities().removePassenger(owner, ent.getEntity());
            }
        }

        this.isHat = value;
    }
}
