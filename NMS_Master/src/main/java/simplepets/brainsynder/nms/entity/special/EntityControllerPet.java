package simplepets.brainsynder.nms.entity.special;

import lib.brainsynder.nbt.StorageTagCompound;
import lib.brainsynder.sounds.SoundMaker;
import net.minecraft.network.protocol.game.ClientboundTeleportEntityPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
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
import simplepets.brainsynder.nms.entity.list.EntityShulkerPet;
import simplepets.brainsynder.nms.entity.list.EntityZombiePet;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class EntityControllerPet extends EntityZombiePet implements IEntityControllerPet {

    private final LinkedList<Entity> ENTITIES = new LinkedList<>();
    private final LivingEntity PET;
    private Entity displayEntity, displayRider = null;

    public EntityControllerPet(PetType type, PetUser user, Location location) {
        super(EntityType.ZOMBIE, type, user);
        setDisplayName(false);

        ENTITIES.addLast(getEntity());
        switch (type) {
            case ARMOR_STAND -> {
                PET = EntityArmorStandPet.spawn(location, this);
                ENTITIES.addLast(VersionTranslator.getBukkitEntity(PET));
                displayEntity = VersionTranslator.getBukkitEntity(PET);
            }
            case SHULKER -> {
                EntityGhostStand ghostStand = EntityGhostStand.spawn(location, this);
                ghostStand.setSmall(true);
                ghostStand.setNoGravity(true);
                Entity ghost = VersionTranslator.getBukkitEntity(ghostStand);
                ENTITIES.addLast(ghost);

                PET = EntityShulkerPet.spawn(location, this, ghostStand);
                PET.collides = false;
                Entity shulker = VersionTranslator.getBukkitEntity(PET);
                ghost.addPassenger(shulker);
                ENTITIES.addLast(shulker);

                displayRider = shulker;
                displayEntity = ghost;
            }
            default -> throw new IllegalStateException("This pet does not use controller pets!");
        }
        collides = false;
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
            kill();
            return;
        }

        if (displayRider != null) {
            if (this.displayRider.isValid()) {
                net.minecraft.world.entity.Entity entity = VersionTranslator.getEntityHandle(displayRider);
                updateName(entity);
                if (!canIgnoreVanish()) {
                    if (VersionTranslator.getEntityHandle(p).isInvisible() != entity.isInvisible()) entity.setInvisible(!entity.isInvisible());
                }
            }else{
                displayEntity = null;
                kill();
                return;
            }
        }else if (this.displayEntity != null) {
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
    public InteractionResult interactAt(net.minecraft.world.entity.player.Player entityhuman, Vec3 vec3d, InteractionHand enumhand) {
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
    }

    @Override
    public void remove() {
        VersionTranslator.getBukkitEntity(this).remove();
        for (Entity ent : ENTITIES) ent.remove();
        displayEntity = null;
        displayRider = null;
    }

    @Override
    public void reloadLocation() {
        if (displayEntity.getPassenger() != null) {
            net.minecraft.world.entity.Entity displayEntity = VersionTranslator.getEntityHandle(this.displayEntity);
            Location loc;
            if (this.displayRider != null) {
                if (getPetType() == PetType.SHULKER) {
                    loc = VersionTranslator.getBukkitEntity(this).getLocation().clone().subtract(0, 0.735, 0);
                } else {
                    loc = VersionTranslator.getBukkitEntity(this).getLocation().clone();
                }
            } else {
                loc = VersionTranslator.getBukkitEntity(this).getLocation().clone();
            }

            displayEntity.moveTo(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
            loc.getWorld().getNearbyEntities(loc, 100, 100, 100).forEach(entity -> {
                if (entity instanceof Player player) {
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
                loc = VersionTranslator.getBukkitEntity(this).getLocation().clone().add(0, 0.75, 0);
            } else {
                loc = VersionTranslator.getBukkitEntity(this).getLocation().clone();
            }
        } else {
            loc = VersionTranslator.getBukkitEntity(this).getLocation().clone();
        }

        displayEntity.moveTo(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
        loc.getWorld().getNearbyEntities(loc, 100, 100, 100).forEach(entity -> {
            if (entity instanceof Player player) {
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
        if (displayRider != null) {
            Optional<Object> displayOption1 = SimplePets.getSpawnUtil().getHandle(displayRider);
            if (displayOption1.isPresent() && (displayOption1.get() instanceof IEntityPet)) {
                return (IEntityPet) displayOption1.get();
            }
        }
        return this;
    }
}