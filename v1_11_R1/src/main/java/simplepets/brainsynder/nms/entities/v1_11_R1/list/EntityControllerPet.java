package simplepets.brainsynder.nms.entities.v1_11_R1.list;

import net.minecraft.server.v1_11_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import simple.brainsynder.nbt.StorageTagCompound;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.entity.IEntityControllerPet;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.event.pet.PetMoveEvent;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.pet.types.ShulkerDefault;
import simplepets.brainsynder.reflection.ReflectionUtil;

import java.util.ArrayList;
import java.util.List;

public class EntityControllerPet extends EntityZombiePet implements IEntityControllerPet {
    private List<Entity> entities = new ArrayList<>();
    private Entity displayEntity;
    private Location previus;
    private boolean moving = false;

    public EntityControllerPet(World world, IPet pet) {
        super(world, pet);
    }

    public EntityControllerPet(World world) {
        super(world);
    }

    protected boolean isOwnerRiding() {
        if (((CraftEntity)getDisplayEntity()).getHandle().passengers.size() == 0)
            return false;
        EntityPlayer owner = ((CraftPlayer) getOwner()).getHandle();
        for (net.minecraft.server.v1_11_R1.Entity passenger : ((CraftEntity)getDisplayEntity()).getHandle().passengers) {
            if (passenger.getUniqueID().equals(owner.getUniqueID())) {

                return true;
            }
        }
        return false;
    }

    @Override
    public void g(float f, float f2) {
        reloadLocation();
        if (this.passengers == null) {
            this.P = (float) 0.5;
            this.aR = (float) 0.02;
            super.g(f, f2);
        } else {
            if (this.getPet() == null) {
                this.P = (float) 0.5;
                this.aR = (float) 0.02;
                super.g(f, f2);
                return;
            }
            if (!isOwnerRiding()) {
                this.P = (float) 0.5;
                this.aR = (float) 0.02;
                super.g(f, f2);
                return;
            }
            EntityPlayer owner = ((CraftPlayer) getOwner()).getHandle();
            if (fieldAccessor != null) {
                if (fieldAccessor.hasField(owner)) {
                    if (fieldAccessor.get(owner)) {
                        if (isOnGround(this)) {
                            this.motY = 0.5;
                        } else {
                            if (getPet().getPetType().canFly(getPet().getOwner())) {
                                this.motY = 0.3;
                            }
                        }
                    }
                }
            }
            this.yaw = owner.yaw;
            this.lastYaw = this.yaw;
            this.pitch = (float) (owner.pitch * 0.5);
            this.setYawPitch(this.yaw, this.pitch);
            this.aP = this.aN = this.yaw;
            this.P = (float) 1.0;
            f = (float) (owner.be * 0.5);
            f2 = owner.bf;
            if (f2 <= 0.0) {
                f2 *= 0.25;
            }

            f *= 0.75;
            this.l((float) getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).getValue());
            if (!world.isClientSide) {
                super.g(f, f2);
                this.aF = this.aG;
                double d0 = this.locX - this.lastX;
                double d1 = this.locZ - this.lastZ;
                float f5 = MathHelper.sqrt(d0 * d0 + d1 * d1) * 4.0F;
                if (f5 > 1.0F) {
                    f5 = 1.0F;
                }

                this.aG += (f5 - this.aG) * 0.4F;
                this.aH += this.aG;
            }

            if (getPet() == null) {
                if (bukkitEntity != null)
                    bukkitEntity.remove();
                return;
            }

            if (getPet().getOwner() == null) {
                if (bukkitEntity != null)
                    bukkitEntity.remove();
                return;
            }
            try {
                PetMoveEvent event = new PetMoveEvent(this, PetMoveEvent.Cause.RIDE);
                Bukkit.getServer().getPluginManager().callEvent(event);
            } catch (Throwable ignored) {
            }
        }
    }

    @Override
    public StorageTagCompound asCompound() {
        return getVisibleEntity().asCompound();
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        getVisibleEntity().applyCompound(object);
    }

    @Override
    public void repeatTask() {
        super.repeatTask();
        if (!this.isInvisible())
            this.setInvisible(true);
        if (getPet() != null) if (isBaby()) setBaby((getPet().getPetType() instanceof ShulkerDefault));
        Player p = getPet().getOwner();
        if (hasCustomName()) {
            setCustomNameVisible(false);
            if (displayEntity.getPassenger() != null) {
                this.displayEntity.getPassenger().setCustomName(getCustomName());
                this.displayEntity.getPassenger().setCustomNameVisible(true);
            } else {
                this.displayEntity.setCustomName(getCustomName());
                this.displayEntity.setCustomNameVisible(true);
            }
        }
        if (this.displayEntity != null) {
            if (this.displayEntity.isValid()) {
                if (displayEntity.getPassenger() == null) {
                    reloadLocation();
                    net.minecraft.server.v1_11_R1.Entity displayEntity = ((CraftEntity) this.displayEntity).getHandle();
                    if (((CraftPlayer) getOwner()).getHandle().isInvisible() != displayEntity.isInvisible()) {
                        displayEntity.setInvisible(!displayEntity.isInvisible());
                    }
                    return;
                }
                if (this.displayEntity.getPassenger() != null) {
                    net.minecraft.server.v1_11_R1.Entity displayEntity = ((CraftEntity) this.displayEntity.getPassenger()).getHandle();
                    if (((CraftPlayer) p).getHandle().isInvisible() != displayEntity.isInvisible()) {
                        displayEntity.setInvisible(!displayEntity.isInvisible());
                    }
                }
            }
        }
    }

    @Override
    public Entity getDisplayEntity() {
        return displayEntity;
    }

    @Override
    public List<Entity> getEntities() {
        return entities;
    }

    @Override
    public void setDisplayEntity(Entity entity) {
        if (!entities.contains(entity))
            entities.add(entity);
        if (entity.getPassenger() != null)
            if (!entities.contains(entity.getPassenger()))
                entities.add(entity.getPassenger());

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
            net.minecraft.server.v1_11_R1.Entity displayEntity = ((CraftEntity) this.displayEntity).getHandle();
            Location loc;
            if (this.getDisplayRider() != null) {
                if (this.getDisplayRider().getType().equals(EntityType.SHULKER)) {
                    loc = getBukkitEntity().getLocation().clone().subtract(0, 0.735, 0);
                } else {
                    loc = getBukkitEntity().getLocation().clone();
                }
            } else {
                loc = getBukkitEntity().getLocation().clone();
            }
            displayEntity.setPositionRotation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
            if (!loc.getWorld().getPlayers().isEmpty()) {
                for (Player player : loc.getWorld().getPlayers()) {
                    PacketPlayOutEntityTeleport packet = new PacketPlayOutEntityTeleport(displayEntity);
                    ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
                }
            }
            return;
        }
        net.minecraft.server.v1_11_R1.Entity displayEntity = ((CraftEntity) this.displayEntity).getHandle();
        Location loc;
        if (this.getDisplayRider() != null) {
            if (this.getDisplayRider().getType().equals(EntityType.SHULKER)) {
                loc = getBukkitEntity().getLocation().clone().add(0, 0.75, 0);
            } else {
                loc = getBukkitEntity().getLocation().clone();
            }
        } else {
            loc = getBukkitEntity().getLocation().clone();
        }
        displayEntity.setPositionRotation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
        if (!loc.getWorld().getPlayers().isEmpty()) {
            for (Player player : loc.getWorld().getPlayers()) {
                PacketPlayOutEntityTeleport packet = new PacketPlayOutEntityTeleport(displayEntity);
                ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
            }
        }
    }

    @Override
    public void addPassenger(Entity passenger) {
        this.passengers.add(((CraftEntity) passenger).getHandle());
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

    public boolean isMoving() {return this.moving;}
}
