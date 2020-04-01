package simplepets.brainsynder.nms.v1_13_R1.entities.list;

import lib.brainsynder.nbt.StorageTagCompound;
import net.minecraft.server.v1_13_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_13_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_13_R1.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.entity.IEntityControllerPet;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.event.pet.PetMoveEvent;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.pet.types.ShulkerDefault;
import simplepets.brainsynder.reflection.FieldAccessor;
import simplepets.brainsynder.reflection.ReflectionUtil;

import java.util.ArrayList;
import java.util.List;

public class EntityControllerPet extends EntityZombiePet implements IEntityControllerPet {
    private List<Entity> entities = new ArrayList<>();
    private Entity displayEntity, displayRider = null;
    private IPet pet;
    private boolean moving = false;
    private FieldAccessor<Boolean> fieldAccessor;

    public EntityControllerPet(EntityTypes<?> type, World world, IPet pet) {
        super(type, world, pet);
        this.pet = pet;
        fieldAccessor = FieldAccessor.getField(EntityLiving.class, "bg", Boolean.TYPE);
    }
    public EntityControllerPet(EntityTypes<?> type, World world) {
        super(type, world);
    }

  //  @Override
  //  public void a(float f, float f1, float f2) {
  //      System.out.println("Pong!");
  //      super.a(f, f1, f2);
  //      reloadLocation();
  //  }


    @Override
    public List<Entity> getEntities() {
        return entities;
    }

    private boolean isOwnerRiding() {
        if (pet == null) return false;
        if (pet.getOwner() == null) return false;
        if (((CraftEntity)getDisplayEntity()).getHandle().passengers.size() == 0)
            return false;
        EntityPlayer owner = ((CraftPlayer) getOwner()).getHandle();
        for (net.minecraft.server.v1_13_R1.Entity passenger : ((CraftEntity)getDisplayEntity()).getHandle().passengers) {
            if (passenger.getUniqueID().equals(owner.getUniqueID())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void a(float strafe, float vertical, float forward) {
        reloadLocation();
        if (passengers == null) {
            this.Q = (float) 0.5;
            this.aU = (float) 0.02;
            super.a(strafe, vertical, forward);
        } else {
            if (this.pet == null) {
                this.Q = (float) 0.5;
                this.aU = (float) 0.02;
                super.a(strafe, vertical, forward);
                return;
            }
            if (!isOwnerRiding()) {
                this.Q = (float) 0.5;
                this.aU = (float) 0.02;
                super.a(strafe, vertical, forward);
                return;
            }
            EntityPlayer owner = ((CraftPlayer) getOwner()).getHandle();
            if (fieldAccessor != null) {
                if (fieldAccessor.hasField(owner)) {
                    if (fieldAccessor.get(owner)) {
                        if (isOnGround(this)) {
                            this.motY = 0.5;
                        } else {
                            if (pet.getPetType().canFly(pet.getOwner())) {
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
            this.aR = this.aP = this.yaw;
            this.Q = (float) 1.0;
            strafe = (float) (owner.bh * 0.5);
            forward = owner.bj;
            if (forward <= 0.0) {
                forward *= 0.25;
            }

            strafe *= 0.75;
            this.o((float) getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).getValue());
            if (!world.isClientSide) {
                super.a(strafe, vertical, forward);
            }

            if (pet == null) {
                if (bukkitEntity != null)
                    bukkitEntity.remove();
                return;
            }

            if (pet.getOwner() == null) {
                if (bukkitEntity != null)
                    bukkitEntity.remove();
                return;
            }
            try {
                PetMoveEvent event = new PetMoveEvent(this, PetMoveEvent.Cause.RIDE);
                Bukkit.getServer().getPluginManager().callEvent(event);
            }catch (Throwable ignored) {}
        }
    }

    private boolean isOnGround(net.minecraft.server.v1_13_R1.Entity entity) {
        Location loc = entity.getBukkitEntity().getLocation();
        org.bukkit.block.Block block = loc.subtract(0, 0.5, 0).getBlock();
        return block.getType().isSolid();
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
                if (displayEntity.getPassenger() != null) {
                    this.displayEntity.getPassenger().setCustomName(getCustomName().getText());
                } else {
                    this.displayEntity.setCustomName(getCustomName().getText());
                }
                if (displayEntity.getPassenger() == null) {
                    reloadLocation();
                    net.minecraft.server.v1_13_R1.Entity displayEntity = ((CraftEntity) this.displayEntity).getHandle();
                    if (((CraftPlayer) getOwner()).getHandle().isInvisible() != displayEntity.isInvisible()) {
                        displayEntity.setInvisible(!displayEntity.isInvisible());
                    }
                    return;
                }
                if (this.displayEntity.getPassenger() != null) {
                    net.minecraft.server.v1_13_R1.Entity displayEntity = ((CraftEntity) this.displayEntity.getPassenger()).getHandle();
                    if (((CraftPlayer) p).getHandle().isInvisible() != displayEntity.isInvisible()) {
                        displayEntity.setInvisible(!displayEntity.isInvisible());
                    }
                }
            }
        }
    }

    public void updateName() {
        if (hasCustomName()) {
            setCustomNameVisible(false);
            if (displayEntity.getPassenger() != null) {
                this.displayEntity.getPassenger().setCustomName(getCustomName().getText());
            } else {
                this.displayEntity.setCustomName(getCustomName().getText());
            }
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
            net.minecraft.server.v1_13_R1.Entity displayEntity = ((CraftEntity) this.displayEntity).getHandle();
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


        net.minecraft.server.v1_13_R1.Entity displayEntity = ((CraftEntity) this.displayEntity).getHandle();
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

    public boolean isMoving() {return this.moving;}


}
