package simplepets.brainsynder.nms.v1_11_R1.entities;

import net.minecraft.server.v1_11_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftCreature;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import simple.brainsynder.nbt.StorageTagCompound;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.entity.IEntityControllerPet;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.misc.IFlyablePet;
import simplepets.brainsynder.api.entity.passive.IEntityHorsePet;
import simplepets.brainsynder.api.event.pet.PetMoveEvent;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.v1_11_R1.entities.list.EntityControllerPet;
import simplepets.brainsynder.reflection.FieldAccessor;
import simplepets.brainsynder.wrapper.EntityWrapper;

public abstract class EntityPet extends EntityCreature implements IAnimal,
        IEntityPet {
    protected FieldAccessor<Boolean> fieldAccessor;
    private IPet pet;
    private Location walkTo = null;
    private double walkSpeed, rideSpeed;
    private boolean floatDown;

    public EntityPet(World world, IPet pet) {
        super(world);
        this.pet = pet;
        this.collides = false;
        this.setSize(0.5F, 0.5F);
        this.noclip = false;
        fieldAccessor = FieldAccessor.getField(EntityLiving.class, "bd", Boolean.TYPE);
        this.getAttributeInstance(GenericAttributes.maxHealth).setValue(20.0D);
        walkSpeed = pet.getPetType().getSpeed();
        rideSpeed = pet.getPetType().getRideSpeed();
        floatDown = pet.getPetType().canFloat();
    }

    public EntityPet(World world) {
        super(world);
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = new StorageTagCompound();
        object.setString("PetType", pet.getPetType().getConfigName());
        return object;
    }

    @Override
    public Location getWalkToLocation() {
        return walkTo;
    }

    @Override
    public void setWalkToLocation(Location location) {
        walkTo = location;
    }

    @Override
    public EntityWrapper getPetEntityType() {
        return pet.getPetType().getEntityType();
    }

    @Override
    public void applyCompound(StorageTagCompound object) {}

    public void setPassenger(int pos, org.bukkit.entity.Entity entity, org.bukkit.entity.Entity passenger) {
        try {
            ((CraftEntity) entity).getHandle().passengers.add(pos, ((CraftEntity) passenger).getHandle());
            PacketPlayOutMount packet = new PacketPlayOutMount(((CraftEntity) entity).getHandle());
            if (entity instanceof Player) {
                ((CraftPlayer) entity).getHandle().playerConnection.sendPacket(packet);
            }
        } catch (Exception e) {
            PetCore.get().debug(2, "Could not run method IEntityPet#setPassenger");
        }
    }

    public void removePassenger(org.bukkit.entity.Entity entity) {
        try {
            ((CraftEntity) entity).getHandle().passengers.clear();
            PacketPlayOutMount packet = new PacketPlayOutMount(((CraftEntity) entity).getHandle());
            if (entity instanceof Player) {
                ((CraftPlayer) entity).getHandle().playerConnection.sendPacket(packet);
            }
        } catch (Exception e) {
            PetCore.get().debug(2, "Could not run method IEntityPet#removePassenger");
        }
    }

    protected void registerDatawatchers() {
    }

    @Override
    public void f(double x, double y, double z) {

    }

    @Override
    protected void i() {
        super.i();
        registerDatawatchers();
    }

    public CraftCreature getBukkitEntity() {
        return (CraftCreature) super.getBukkitEntity();
    }

    @Override
    public Player getOwner() {
        return pet.getOwner();
    }

    @Override
    public IPet getPet() {
        return pet;
    }

    @Override
    public org.bukkit.entity.Entity getEntity() {
        return super.getBukkitEntity();
    }

    protected boolean isOnGround(Entity entity) {
        org.bukkit.block.Block block = entity.getBukkitEntity().getLocation().subtract(0, 0.5, 0).getBlock();
        return block.getType().isSolid();
    }

    @Override
    public boolean isCollidable() {
        return false;
    }

    public void repeatTask() {
        if (pet == null) return;
        if (this instanceof IFlyablePet) {
            if (pet.isVehicle()) {
                if (floatDown) {
                    if (!this.onGround && this.motY < 0.0D) {
                        this.motY *= 0.4;
                    }
                }
            }
        }
        if (pet.getOwner() != null) {
            Player p = pet.getOwner();
            if (((CraftPlayer) p).getHandle().isInvisible() != this.isInvisible()) {
                this.setInvisible(!this.isInvisible());
                if (isInvisible()) {
                    if (this instanceof IEntityControllerPet) {
                        ((EntityControllerPet) this).getVisibleEntity().getEntity().setGlowing(true);
                    } else {
                        glowing = true;
                    }
                } else {
                    if (this instanceof IEntityControllerPet) {
                        ((EntityControllerPet) this).getVisibleEntity().getEntity().setGlowing(false);
                    } else {
                        glowing = false;
                    }
                }
            }

            if (pet.isHat()) {
                this.lastYaw = this.yaw = p.getLocation().getYaw();
            }

            double current = getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).getValue();
            if (isOwnerRiding()) {
                if (current != rideSpeed)
                    getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(rideSpeed);
            } else {
                if (current != walkSpeed)
                    getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(walkSpeed);
            }
        }
    }

    @Override
    public void move(EnumMoveType enummovetype, double d0, double d1, double d2) {
        if (pet == null) return;
        if (pet.getOwner() == null) return;
        PetMoveEvent event = new PetMoveEvent(this, PetMoveEvent.Cause.RIDE);
        Bukkit.getServer().getPluginManager().callEvent(event);
        super.move(enummovetype, d0, d1, d2);
    }

    @Override
    public void n() {
        super.n();
        repeatTask();
    }

    protected boolean isOwnerRiding() {
        if (this.passengers.size() == 0)
            return false;
        EntityPlayer owner = ((CraftPlayer) getOwner()).getHandle();
        for (Entity passenger : this.passengers) {
            if (passenger.getUniqueID().equals(owner.getUniqueID())) {

                return true;
            }
        }
        return false;
    }

    public void g(float sideMot, float forwMot) {
        if (pet == null) return;
        if (pet.getOwner() == null) return;
        if (this.bx().isEmpty()) {
            this.P = 0.5F;
            this.aR = 0.02F;
            super.g(sideMot, forwMot);
        } else {
            if (this.pet == null) {
                this.P = 0.5F;
                this.aR = 0.02F;
                super.g(sideMot, forwMot);
                return;
            }
            if (!isOwnerRiding()) {
                this.P = 0.5F;
                this.aR = 0.02F;
                super.g(sideMot, forwMot);
                return;
            }
            EntityPlayer owner = ((CraftPlayer) getOwner()).getHandle();
            if (fieldAccessor != null) {
                if (fieldAccessor.hasField(owner)) {
                    if (fieldAccessor.get(owner)) {
                        if (isOnGround(this)) {
                            this.motY = 0.5D;
                        } else {
                            if (pet.getPetType().canFly(pet.getOwner())) {
                                this.motY = 0.5D;
                            }
                        }
                    }
                }
            }
            this.yaw = owner.yaw;
            this.lastYaw = this.yaw;
            this.pitch = owner.pitch * 0.5F;
            this.setYawPitch(this.yaw, this.pitch);
            this.aO = this.aM = this.yaw;
            this.P = 1.0F;
            sideMot = owner.be * 0.5F;
            forwMot = owner.bf;
            if (forwMot <= 0.0F) {
                forwMot *= (0.25 * rideSpeed);
            } else {
                forwMot *= rideSpeed;
            }

            if (!(this instanceof IEntityHorsePet))
                sideMot *= 0.75F;
            this.l((float) getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).getValue());
            if (!world.isClientSide) {
                PetMoveEvent event = new PetMoveEvent(this, PetMoveEvent.Cause.RIDE);
                Bukkit.getServer().getPluginManager().callEvent(event);
                super.g(sideMot, forwMot);
                if (this instanceof IEntityHorsePet) {
                    Location location = getBukkitEntity().getLocation();
                    setPosition(location.getX(), location.getY(), location.getZ());
                    PacketPlayOutEntityTeleport packet = new PacketPlayOutEntityTeleport(this);
                    owner.playerConnection.sendPacket(packet);
                    if (forwMot > 0.0F) {
                        float f3 = MathHelper.sin(this.yaw * 0.017453292F);
                        float f4 = MathHelper.cos(this.yaw * 0.017453292F);
                        this.motX += (double) (-0.4F * f3 * 0.0F);
                        this.motZ += (double) (0.4F * f4 * 0.0F);
                    }
                }
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
        }
    }
}
