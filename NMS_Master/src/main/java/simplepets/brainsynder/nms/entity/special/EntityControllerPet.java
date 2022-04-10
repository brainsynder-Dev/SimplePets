package simplepets.brainsynder.nms.entity.special;

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
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.misc.IEntityControllerPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.plugin.SimplePets;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.nms.VersionTranslator;
import simplepets.brainsynder.nms.entity.list.EntityArmorStandPet;
import simplepets.brainsynder.nms.entity.list.EntityZombiePet;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EntityControllerPet extends EntityZombiePet implements IEntityControllerPet {

    private final List<Entity> entities = new ArrayList<>();
    private final LivingEntity pet;
    private Entity displayEntity, displayRider = null;
    private final boolean moving = false;

    public EntityControllerPet(PetType type, PetUser user, Location location) {
        super(EntityType.ZOMBIE, type, user);
        switch (type) {
            case ARMOR_STAND:
                pet = EntityArmorStandPet.spawn(location, this);
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
        SimplePets.getPetConfigManager().getPetConfig(getPetType()).ifPresent(config -> {
            SoundMaker sound = config.getSound();
            if (sound != null) sound.playSound(getEntity());
        });
    }

    @Override
    public List<Entity> getEntities() {
        entities.add(getEntity());
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
        if (pet != null) if (isBaby()) setBaby((getPetType() == PetType.SHULKER));
        Player p = getPetUser().getPlayer();
        if ((this.displayEntity == null)
                || (this.displayEntity.isDead())
                || (!this.displayEntity.isValid())) {
            displayEntity = null;
            kill();
            return;
        }

        if (this.displayEntity != null) {
            if (this.displayEntity.isValid()) {
                net.minecraft.world.entity.Entity entity = VersionTranslator.getEntityHandle(displayEntity);
                if (!displayEntity.getPassengers().isEmpty()){
                    if (displayRider == null) displayRider = displayEntity.getPassengers().get(0);
                    entity = VersionTranslator.getEntityHandle(displayRider);
                }
                updateName(entity);
                if (!canIgnoreVanish()) {
                    if (VersionTranslator.getEntityHandle(p).isInvisible() != entity.isInvisible()) entity.setInvisible(!entity.isInvisible());
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
        ServerPlayer owner = VersionTranslator.getEntityHandle(getVisibleEntity().getPetUser().getPlayer());
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
        displayEntity = null;
        displayRider = null;
    }

    @Override
    public void reloadLocation() {
        if (displayEntity.getPassenger() != null) {
            net.minecraft.world.entity.Entity displayEntity = VersionTranslator.getEntityHandle(this.displayEntity);
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
                    VersionTranslator.<ServerPlayer>getEntityHandle(player).connection.send(packet);
                }
            });
            return;
        }


        net.minecraft.world.entity.Entity displayEntity = VersionTranslator.getEntityHandle(this.displayEntity);
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
                VersionTranslator.<ServerPlayer>getEntityHandle(player).connection.send(packet);
            }
        });
    }

    @Override
    public void addPassenger(Entity passenger) {
        VersionTranslator.getEntityHandle(passenger).passengers.add(0, this);
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
        if (displayEntity == null) {
            remove();
            return this;
        }
        Optional<Object> displayOption = SimplePets.getSpawnUtil().getHandle(displayEntity);
        if (displayOption.isPresent() && (displayOption.get() instanceof IEntityPet)) {
            return (IEntityPet) displayOption.get();
        }else{
            if (displayEntity.getPassenger() != null) {
                Optional<Object> displayOption1 = SimplePets.getSpawnUtil().getHandle(displayEntity.getPassenger());
                if (displayOption1.isPresent() && (displayOption1.get() instanceof IEntityPet)) {
                    return (IEntityPet) displayOption1.get();
                }
            }
        }
        return this;
    }
}