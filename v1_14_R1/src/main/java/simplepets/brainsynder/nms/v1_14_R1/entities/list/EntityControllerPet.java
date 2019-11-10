package simplepets.brainsynder.nms.v1_14_R1.entities.list;

import net.minecraft.server.v1_14_R1.*;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.*;
import org.bukkit.metadata.FixedMetadataValue;
import simple.brainsynder.nbt.StorageTagCompound;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.entity.IEntityControllerPet;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.pet.types.ShulkerDefault;
import simplepets.brainsynder.reflection.ReflectionUtil;

import java.util.ArrayList;
import java.util.List;

public class EntityControllerPet extends EntityZombiePet implements IEntityControllerPet {
    private List<Entity> entities = new ArrayList<>();
    private Entity displayEntity, displayRider = null;
    private boolean moving = false;
    
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
        if (getPet() != null) if (isBaby()) setBaby((getPet().getPetType() instanceof ShulkerDefault));
        Player p = getPet().getOwner();
        
        if (this.displayEntity != null) {
            if (this.displayEntity.isValid()) {
                net.minecraft.server.v1_14_R1.Entity entity = ((CraftEntity) displayEntity).getHandle();
                if (!displayEntity.getPassengers().isEmpty())
                    entity = ((CraftEntity) displayRider).getHandle();
                updateName(entity);
                reloadLocation();
                if (((CraftPlayer) p).getHandle().isInvisible() != entity.isInvisible())
                    entity.setInvisible(!entity.isInvisible());
            }
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
    
    public boolean isMoving() {
        return this.moving;
    }
    
    
}