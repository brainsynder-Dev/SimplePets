package simplepets.brainsynder.versions.v1_18_R1.entity.special;

import lib.brainsynder.nbt.StorageTagCompound;
import lib.brainsynder.sounds.SoundMaker;
import net.minecraft.network.protocol.game.ClientboundTeleportEntityPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_18_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_18_R1.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.misc.IEntityControllerPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.plugin.SimplePets;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.versions.v1_18_R1.entity.list.EntityArmorStandPet;
import simplepets.brainsynder.versions.v1_18_R1.entity.list.EntityZombiePet;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EntityControllerPet extends EntityZombiePet implements IEntityControllerPet {

    private final List<Entity> entities = new ArrayList<>();
    private final LivingEntity pet;
    private Entity displayEntity, displayRider = null;
    private final boolean moving = false;

    public EntityControllerPet(PetType type, PetUser user) {
        super(EntityType.ZOMBIE, type, user);
        switch (type) {
            case ARMOR_STAND:
                pet = EntityArmorStandPet.spawn(getEntity().getLocation(), this);
                setDisplayEntity(pet.getBukkitEntity());
                break;
            case SHULKER:
                throw new IllegalStateException("Not yet initialised!");
            default:
                throw new IllegalStateException("This pet does not use controller pets!");
        }
    }

    @Override
    public void playAmbientSound() {
        if (isPetSilent()) return;
        SimplePets.getPetConfigManager().getPetConfig(getVisibleEntity().getPetType()).ifPresent(config -> {
            SoundMaker sound = config.getSound();
            if (sound != null) sound.playSound(getEntity());
        });
    }

    @Override
    public List<Entity> getEntities() {
        return entities;
    }

    @Override
    public StorageTagCompound asCompound() {
        return super.asCompound();
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        super.applyCompound(object);
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.isInvisible()) this.setInvisible(true);
        if (!isSilent()) this.setSilent(true);
        if (pet != null) if (isBaby()) setBaby((getVisibleEntity().getPetType() == PetType.SHULKER));
        Player p = getVisibleEntity().getPetUser().getPlayer();

        if (this.displayEntity != null) {
            if (this.displayEntity.isValid()) {
                net.minecraft.world.entity.Entity entity = ((CraftEntity) displayEntity).getHandle();
                if (!displayEntity.getPassengers().isEmpty()){
                    if (displayRider == null) displayRider = displayEntity.getPassengers().get(0);
                    entity = ((CraftEntity) displayRider).getHandle();
                }
                updateName(entity);
                if (!canIgnoreVanish()) {
                    if (((CraftPlayer) p).getHandle().isInvisible() != entity.isInvisible()) entity.setInvisible(!entity.isInvisible());
                }
            }else{
                displayEntity = null;
                kill();
                return;
            }
        }

        double current = getAttribute(Attributes.MOVEMENT_SPEED).getValue();
        if (isOwnerRiding()) {
            if (current != rideSpeed)
                getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(rideSpeed);
        } else {
            if (current != walkSpeed)
                getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(walkSpeed);
        }
    }

    @Override
    public void move(MoverType enummovetype, Vec3 vec3d) {
        super.move(enummovetype, vec3d);
        if (displayEntity == null) return;
        reloadLocation();
    }

    public void updateName(net.minecraft.world.entity.Entity entity) {
        if (isCustomNameVisible()) setCustomNameVisible(false);
        if (!entity.isCustomNameVisible()) entity.setCustomNameVisible(true);
        if (hasCustomName() && (!getCustomName().equals(entity.getCustomName()))) entity.setCustomName(getCustomName());
    }

    @Override
    protected boolean isOwnerRiding() {
        if (displayEntity == null || getVisibleEntity() == null) return false;
        if (getVisibleEntity().getPetUser() == null) return false;
        if (displayEntity.getPassengers().size() == 0)
            return false;
        ServerPlayer owner = ((CraftPlayer) getVisibleEntity().getPetUser().getPlayer()).getHandle();
        for (Entity passenger : displayEntity.getPassengers()) {
            if (passenger.getUniqueId().equals(owner.getUUID())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Optional<Entity> getDisplayEntity() {
        return Optional.of(displayEntity);
    }

    @Override
    public Optional<Entity> getDisplayRider() {
        return Optional.of(displayRider);
    }

    @Override
    public void setDisplayEntity(Entity entity) {
        if (!entities.contains(entity))
            entities.add(entity);
        if (entity.getPassenger() != null) {
            displayRider = entity.getPassenger();
            if (!entities.contains(entity.getPassenger()))
                entities.add(entity.getPassenger());
        }

        displayEntity = entity;
    }

    @Override
    public void remove() {
        getBukkitEntity().remove();
        for (Entity ent : entities) ent.remove();
    }

    @Override
    public void reloadLocation() {
        if (displayEntity.getPassenger() != null) {
            net.minecraft.world.entity.Entity displayEntity = ((CraftEntity) this.displayEntity).getHandle();
            Location loc;
            if (this.displayRider != null) {
                if (this.displayRider.getType().equals(EntityType.SHULKER)) {
                    loc = getBukkitEntity().getLocation().clone().subtract(0, 0.735, 0);
                } else {
                    loc = getBukkitEntity().getLocation().clone();
                }
            } else {
                loc = getBukkitEntity().getLocation().clone();
            }

            displayEntity.moveTo(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
            loc.getWorld().getNearbyEntities(loc, 100, 100, 100).forEach(entity -> {
                if (entity instanceof Player) {
                    Player player = (Player) entity;
                    ClientboundTeleportEntityPacket packet = new ClientboundTeleportEntityPacket(displayEntity);
                    ((CraftPlayer) player).getHandle().connection.send(packet);
                }
            });
            return;
        }


        net.minecraft.world.entity.Entity displayEntity = ((CraftEntity) this.displayEntity).getHandle();
        Location loc;
        if (this.displayRider != null) {
            if (this.displayRider.getType().equals(EntityType.SHULKER)) {
                loc = getBukkitEntity().getLocation().clone().add(0, 0.75, 0);
            } else {
                loc = getBukkitEntity().getLocation().clone();
            }
        } else {
            loc = getBukkitEntity().getLocation().clone();
        }

        displayEntity.moveTo(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
        loc.getWorld().getNearbyEntities(loc, 100, 100, 100).forEach(entity -> {
            if (entity instanceof Player) {
                Player player = (Player) entity;
                ClientboundTeleportEntityPacket packet = new ClientboundTeleportEntityPacket(displayEntity);
                ((CraftPlayer) player).getHandle().connection.send(packet);
            }
        });
    }

    @Override
    public void addPassenger(Entity passenger) {

        ((CraftEntity) passenger).getHandle().passengers.add(0, this);
    }

    @Override
    public boolean isMoving() {
        return false;
    }

    @Override
    public void updateName() {

    }

    @Override
    public IEntityPet getVisibleEntity() {
        Object handle = SimplePets.getSpawnUtil().getHandle(displayEntity);
        if (handle instanceof IEntityPet) {
            return (IEntityPet) handle;
        } else {
            if (displayEntity.getPassenger() != null) {
                Object h = SimplePets.getSpawnUtil().getHandle(displayEntity.getPassenger());
                if (h instanceof IEntityPet) {
                    return (IEntityPet) h;
                }
            }
        }
        return this;
    }
}