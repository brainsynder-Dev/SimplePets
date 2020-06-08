package simplepets.brainsynder.nms.v1_14_R1.entities.list;

import lib.brainsynder.nbt.StorageTagCompound;
import net.minecraft.server.v1_14_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.entity.IEntityControllerPet;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.event.pet.PetMoveEvent;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.pet.PetType;
import simplepets.brainsynder.pet.types.ShulkerPet;
import simplepets.brainsynder.reflection.FieldAccessor;
import simplepets.brainsynder.reflection.ReflectionUtil;

import java.util.ArrayList;
import java.util.List;

public class EntityControllerPet extends EntityZombiePet implements IEntityControllerPet {
    private final List<Entity> entities = new ArrayList<>();
    private Entity displayEntity, displayRider = null;
    private final boolean moving = false;
    
    public EntityControllerPet(EntityTypes<? extends EntityCreature> type, World world, IPet pet) {
        super(type, world, pet);
    }
    
    public EntityControllerPet(EntityTypes<? extends EntityCreature> type, World world) {
        super(type, world);
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
    public void repeatTask() {
        super.repeatTask();
        if (!this.isInvisible()) this.setInvisible(true);
        if (!isSilent()) this.setSilent(true);
        if (getPet() != null) if (isBaby()) setBaby((getPet().getPetType() instanceof ShulkerPet));
        Player p = getPet().getOwner();
        
        if (this.displayEntity != null) {
            if (this.displayEntity.isValid()) {
                net.minecraft.server.v1_14_R1.Entity entity = ((CraftEntity) displayEntity).getHandle();
                if (!displayEntity.getPassengers().isEmpty()) {
                    if (displayRider == null) displayRider = displayEntity.getPassengers().get(0);
                    entity = ((CraftEntity) displayRider).getHandle();
                }
                updateName(entity);
                reloadLocation();
                if (!canIgnoreVanish()) {
                    if (((CraftPlayer) p).getHandle().isInvisible() != entity.isInvisible())
                        entity.setInvisible(!entity.isInvisible());
                }
            }
        }

        PetType type = getVisibleEntity().getPet().getPetType();
        double current = getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).getValue();
        double rideSpeed = type.getRideSpeed();
        double walkSpeed = type.getSpeed();
        if (isOwnerRiding()) {
            if (current != rideSpeed)
                getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(rideSpeed);
        } else {
            if (current != walkSpeed)
                getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(walkSpeed);
        }
    }
    
    public void updateName(net.minecraft.server.v1_14_R1.Entity entity) {
        if (hasCustomName()) {
            setCustomNameVisible(false);
            entity.setCustomName(getCustomName());
        }
    }
    
    @Override
    public Entity getDisplayEntity() {
        return displayEntity;
    }
    
    @Override
    public Entity getDisplayRider() {
        return displayRider;
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
        for (Entity e : entities) {
            e.setMetadata("NO_DAMAGE", new FixedMetadataValue(PetCore.get(), "NO_DAMAGE"));
        }
    }
    
    @Override
    public void remove() {
        getBukkitEntity().remove();
        for (Entity ent : entities) ent.remove();
    }
    
    @Override
    public void reloadLocation() {
        if (displayEntity.getPassenger() != null) {
            net.minecraft.server.v1_14_R1.Entity displayEntity = ((CraftEntity) this.displayEntity).getHandle();
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
            
            displayEntity.setPositionRotation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
            loc.getWorld().getNearbyEntities(loc, 100, 100, 100).forEach(entity -> {
                if (entity instanceof Player) {
                    Player player = (Player) entity;
                    PacketPlayOutEntityTeleport packet = new PacketPlayOutEntityTeleport(displayEntity);
                    ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
                }
            });
            return;
        }
        
        
        net.minecraft.server.v1_14_R1.Entity displayEntity = ((CraftEntity) this.displayEntity).getHandle();
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
        
        displayEntity.setPositionRotation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
        loc.getWorld().getNearbyEntities(loc, 100, 100, 100).forEach(entity -> {
            if (entity instanceof Player) {
                Player player = (Player) entity;
                PacketPlayOutEntityTeleport packet = new PacketPlayOutEntityTeleport(displayEntity);
                ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
            }
        });
    }
    
    @Override
    public void addPassenger(Entity passenger) {
        
        ((CraftEntity) passenger).getHandle().passengers.add(0, this);
        if (passenger instanceof Player) {
            PetCore.get().getUtilities().sendMountPacket((Player) passenger, this.getBukkitEntity());
        }
    }
    
    @Override
    public IEntityPet getVisibleEntity() {
        Object handle = ReflectionUtil.getEntityHandle(displayEntity);
        if (handle instanceof IEntityPet) {
            return (IEntityPet) handle;
        } else {
            if (displayEntity.getPassenger() != null) {
                Object h = ReflectionUtil.getEntityHandle(displayEntity.getPassenger());
                if (h instanceof IEntityPet) {
                    return (IEntityPet) h;
                }
            }
        }
        return this;
    }

    private boolean isOwnerRiding() {
        if (pet == null) return false;
        if (pet.getOwner() == null) return false;
        if (((CraftEntity)getDisplayEntity()).getHandle().passengers.size() == 0)
            return false;
        EntityPlayer owner = ((CraftPlayer) getOwner()).getHandle();
        for (net.minecraft.server.v1_14_R1.Entity passenger : ((CraftEntity)getDisplayEntity()).getHandle().passengers) {
            if (passenger.getUniqueID().equals(owner.getUniqueID())) {
                return true;
            }
        }
        return false;
    }

    private boolean isOnGround(net.minecraft.server.v1_14_R1.Entity entity) {
        org.bukkit.block.Block block = entity.getBukkitEntity().getLocation().subtract(0, 0.5, 0).getBlock();
        return block.getType().isSolid();
    }

    public void move(Vec3D vec3D, FieldAccessor<Boolean> fieldAccessor) {
        float strafe = (float) vec3D.x;
        float vertical = (float) vec3D.y;
        float forward = (float) vec3D.z;
        reloadLocation();
            EntityPlayer owner = ((CraftPlayer) getOwner()).getHandle();
            if (fieldAccessor != null) {
                if (fieldAccessor.hasField(owner)) {
                    if (fieldAccessor.get(owner)) {
                        if (isOnGround(this)) {
                            setMot(getMot().x, 0.5, getMot().z);
                        } else {
                            if (pet.getPetType().canFly(pet.getOwner())) {
                                setMot(getMot().x, 0.3, getMot().z);
                            }
                        }
                    }
                }
            }
            this.yaw = owner.yaw;
            this.lastYaw = this.yaw;
            this.pitch = (float) (owner.pitch * 0.5);
            this.setYawPitch(this.yaw, this.pitch);
            this.aL = this.aF = this.yaw;
            this.K = 1.0F;
            strafe = (float) (owner.bb * 0.5);
            forward = owner.bd;
            if (forward <= 0.0) {
                forward *= 0.25;
            }

            strafe *= 0.75;
            this.o((float) getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).getValue());
            if (!world.isClientSide) {
                super.e(new Vec3D(strafe, vertical, forward));
            }

            if (pet == null) {
                if (displayEntity != null) {
                    displayEntity.remove();
                    this.remove();
                }
                return;
            }

            if (pet.getOwner() == null) {
                if (displayEntity != null) {
                    displayEntity.remove();
                    this.remove();
                }
                return;
            }
            try {
                PetMoveEvent event = new PetMoveEvent(this, PetMoveEvent.Cause.RIDE);
                Bukkit.getServer().getPluginManager().callEvent(event);
            }catch (Throwable ignored) {}

    }

    public boolean isMoving() {
        return this.moving;
    }
    
    
}