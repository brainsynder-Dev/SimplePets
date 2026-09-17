package simplepets.brainsynder.nms.entity.special;

import net.minecraft.network.protocol.game.ClientboundTeleportEntityPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.phys.Vec3;
import org.bsdevelopment.nbt.StorageTagCompound;
import org.bsdevelopment.pluginutils.sound.SafeSound;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.misc.IEntityControllerPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.plugin.SimplePets;
import simplepets.brainsynder.api.plugin.config.ConfigOption;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.nms.EntitySelector;
import simplepets.brainsynder.nms.entity.list.EntityArmorStandPet;
import simplepets.brainsynder.nms.entity.list.EntityShulkerPet;
import simplepets.brainsynder.nms.entity.list.EntityZombiePet;
import simplepets.brainsynder.nms.helper.VersionHelper;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class EntityControllerPet extends EntityZombiePet implements IEntityControllerPet {

    private final LinkedList<Entity> ENTITIES = new LinkedList<>();
    private final LivingEntity PET;
    private Entity displayEntity, displayRider = null;

    public EntityControllerPet(PetType type, PetUser user, Location location) {
        super(EntitySelector.ZOMBIE, type, user);
        setDisplayName(false);

        ENTITIES.addLast(getEntity());
        switch (type) {
            case ARMOR_STAND -> {
                PET = EntityArmorStandPet.spawn(location, this);
                ENTITIES.addLast(PET.getBukkitEntity());
                displayEntity = PET.getBukkitEntity();
            }
            case SHULKER -> {
                EntityGhostStand ghostStand = EntityGhostStand.spawn(location, this);
                ghostStand.setSmall(true);
                ghostStand.setNoGravity(true);
                Entity ghost = ghostStand.getBukkitEntity();
                ENTITIES.addLast(ghost);

                PET = EntityShulkerPet.spawn(location, this, ghostStand);
                PET.collides = false;
                Entity shulker = PET.getBukkitEntity();
                ghost.addPassenger(shulker);
                ENTITIES.addLast(shulker);

                displayRider = shulker;
                displayEntity = ghost;
            }
            default -> throw new IllegalStateException("This pet does not use controller pets!");
        }
        ENTITIES.forEach(entity -> entity.setInvulnerable(true));
        collides = ConfigOption.PET_TOGGLES_MOB_PUSHER.get();
    }

    @Override
    public void playAmbientSound() {
        if (isPetSilent()) return;
        SimplePets.getPetConfigManager().getPetConfig(getPetType()).ifPresent(config -> {
            SafeSound sound = config.getSound();
            if (sound != null) sound.playAt(getEntity().getLocation(), 1f, 1f);
        });
    }

    @Override
    public List<Entity> getEntities() {
        return ENTITIES;
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
        if (isCustomNameVisible()) setCustomNameVisible(false);

        if (!this.isInvisible()) this.setInvisible(true);
        if (!isSilent()) this.setSilent(true);
        if ((!isBaby()) && (getPetType() == PetType.SHULKER)) setBaby((getPetType() == PetType.SHULKER));
        Player p = getPetUser().getPlayer();
        if ((this.displayEntity == null)
                || (this.displayEntity.isDead())
                || (!this.displayEntity.isValid())) {
            displayEntity = null;
            VersionHelper.killEntity(this, (ServerLevel) level());
            return;
        }

        if (displayRider != null) {
            if (this.displayRider.isValid()) {
                net.minecraft.world.entity.Entity entity = VersionHelper.getEntityHandle(displayRider);
                updateName(entity);
                if (!canIgnoreVanish()) {
                    if (VersionHelper.getEntityHandle(p).isInvisible() != entity.isInvisible()) entity.setInvisible(!entity.isInvisible());
                }
            }else{
                displayEntity = null;
                VersionHelper.killEntity(this, (ServerLevel) level());
                return;
            }
        }else if (this.displayEntity != null) {
            if (this.displayEntity.isValid()) {
                net.minecraft.world.entity.Entity entity = VersionHelper.getEntityHandle(displayEntity);
                if (!displayEntity.getPassengers().isEmpty()){
                    if (displayRider == null) displayRider = displayEntity.getPassengers().get(0);
                    entity = VersionHelper.getEntityHandle(displayRider);
                }
                updateName(entity);
                if (!canIgnoreVanish()) {
                    // if (VersionHelper.getEntityHandle(p).isInvisible() != entity.isInvisible()) entity.setInvisible(!entity.isInvisible());
                }
            }else{
                displayEntity = null;
                VersionHelper.killEntity(this, (ServerLevel) level());
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

    // Method was removed in version 26.1
    public InteractionResult interactAt(net.minecraft.world.entity.player.Player entityhuman, Vec3 vec3d, InteractionHand enumhand) {
        return InteractionResult.FAIL;
    }
    // Replacement for interactAt
    public InteractionResult interact(net.minecraft.world.entity.player.Player entityhuman, InteractionHand hand, final Vec3 location) {
        return InteractionResult.FAIL;
    }

    @Override
    public void move(MoverType enummovetype, Vec3 vec3d) {
        super.move(enummovetype, vec3d);
        if (displayEntity == null) return;
        reloadLocation();
    }

    public void updateName(net.minecraft.world.entity.Entity entity) {
        if (!entity.isCustomNameVisible()) entity.setCustomNameVisible(true);
        if (hasCustomName() && (!getCustomName().equals(entity.getCustomName()))) entity.setCustomName(getCustomName());
    }

    @Override
    protected boolean isOwnerRiding() {
        if (displayEntity == null || getVisibleEntity() == null) return false;
        if (getVisibleEntity().getPetUser() == null) return false;
        if (displayEntity.getPassengers().size() == 0)
            return false;
        ServerPlayer owner = VersionHelper.getEntityHandle(getVisibleEntity().getPetUser().getPlayer());
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
    }

    @Override
    public void remove() {
        this.getBukkitEntity().remove();
        for (Entity ent : ENTITIES) ent.remove();
        displayEntity = null;
        displayRider = null;
    }

    @Override
    public void reloadLocation() {
        if (displayEntity.getPassenger() != null) {
            net.minecraft.world.entity.Entity displayEntity = VersionHelper.getEntityHandle(this.displayEntity);
            Location loc;
            if (this.displayRider != null) {
                if (getPetType() == PetType.SHULKER) {
                    loc = this.getBukkitEntity().getLocation().clone().subtract(0, 0.735, 0);
                } else {
                    loc = this.getBukkitEntity().getLocation().clone();
                }
            } else {
                loc = this.getBukkitEntity().getLocation().clone();
            }

            VersionHelper.moveTo(displayEntity, loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
            loc.getWorld().getNearbyEntities(loc, 100, 100, 100).forEach(entity -> {
                if (entity instanceof Player player) {
                    ClientboundTeleportEntityPacket packet = VersionHelper.getTeleportPacket(displayEntity);
                    VersionHelper.<ServerPlayer>getEntityHandle(player).connection.send(packet);
                }
            });
            return;
        }


        net.minecraft.world.entity.Entity displayEntity = VersionHelper.getEntityHandle(this.displayEntity);
        Location loc;
        if (this.displayRider != null) {
            if (this.displayRider.getType().equals(EntitySelector.SHULKER)) {
                loc = this.getBukkitEntity().getLocation().clone().add(0, 0.75, 0);
            } else {
                loc = this.getBukkitEntity().getLocation().clone();
            }
        } else {
            loc = this.getBukkitEntity().getLocation().clone();
        }

        VersionHelper.moveTo(displayEntity, loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
        loc.getWorld().getNearbyEntities(loc, 100, 100, 100).forEach(entity -> {
            if (entity instanceof Player player) {
                ClientboundTeleportEntityPacket packet = VersionHelper.getTeleportPacket(displayEntity);
                VersionHelper.<ServerPlayer>getEntityHandle(player).connection.send(packet);
            }
        });
    }

    @Override
    public void addPassenger(Entity passenger) {
        VersionHelper.getEntityHandle(passenger).passengers.add(0, this);
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
        if (displayRider != null) {
            Optional<Object> displayOption1 = SimplePets.getSpawnUtil().getHandle(displayRider);
            if (displayOption1.isPresent() && (displayOption1.get() instanceof IEntityPet)) {
                return (IEntityPet) displayOption1.get();
            }
        }
        return this;
    }
}