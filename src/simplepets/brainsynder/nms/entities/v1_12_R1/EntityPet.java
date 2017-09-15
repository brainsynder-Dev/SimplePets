package simplepets.brainsynder.nms.entities.v1_12_R1;

import net.minecraft.server.v1_12_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftCreature;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import simple.brainsynder.nbt.StorageTagCompound;
import simple.brainsynder.sound.SoundMaker;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.files.PetTranslate;
import simplepets.brainsynder.nms.entities.type.IEntityHorsePet;
import simplepets.brainsynder.nms.entities.type.main.IEntityControllerPet;
import simplepets.brainsynder.nms.entities.type.main.IEntityPet;
import simplepets.brainsynder.nms.entities.type.main.IFlyablePet;
import simplepets.brainsynder.nms.entities.v1_12_R1.list.EntityControllerPet;
import simplepets.brainsynder.pet.IPet;
import simplepets.brainsynder.pet.PetMoveEvent;
import simplepets.brainsynder.reflection.FieldAccessor;
import simplepets.brainsynder.wrapper.EntityWrapper;

public abstract class EntityPet extends EntityCreature implements IAnimal,
        IEntityPet {
    private IPet pet;
    private double upSpeed, floatSpeed, walkSpeed, rideSpeed;
    private boolean floatDown;
    private FieldAccessor<Boolean> fieldAccessor;

    public EntityPet(World world, IPet pet) {
        super(world);
        this.pet = pet;
        this.collides = false;
        this.setSize(0.5F, 0.5F);
        this.noclip = false;
        fieldAccessor = FieldAccessor.getField(EntityLiving.class, "bd", Boolean.TYPE);

        if (this instanceof IFlyablePet) {
            this.getAttributeMap().b(GenericAttributes.e);
            this.getAttributeInstance(GenericAttributes.e).setValue(0.4000000059604645D);
        }
        this.getAttributeInstance(GenericAttributes.maxHealth).setValue(20.0D);
        walkSpeed = pet.getPetType().getSpeed();
        rideSpeed = pet.getPetType().getRideSpeed();
        if (pet.getPetType().canFlyDefault()) {
            floatDown = PetTranslate.getBoolean(pet.getPetType(), "Float-Down");
            upSpeed = PetTranslate.getDouble(pet.getPetType(), "Float-Down");
            floatSpeed = PetTranslate.getDouble(pet.getPetType(), "Up-Speed");
        }
    }

    public EntityPet(World world) {
        super(world);
        if (bukkitEntity != null)
            bukkitEntity.remove();
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = new StorageTagCompound();
        object.setString("PetType", pet.getPetType().name());
        return object;
    }

    @Override
    public EntityWrapper getEntityType() {
        return pet.getPetType().getType();
    }

    @Override
    public void applyCompound(StorageTagCompound object) {

    }

    public void setPassenger(int pos, org.bukkit.entity.Entity entity, org.bukkit.entity.Entity passenger) {
        try {
            ((CraftEntity) entity).getHandle().passengers.add(pos, ((CraftEntity) passenger).getHandle());
            PacketPlayOutMount packet = new PacketPlayOutMount(((CraftEntity) entity).getHandle());
            if (entity instanceof Player) {
                ((CraftPlayer) entity).getHandle().playerConnection.sendPacket(packet);
            }
        } catch (Exception e) {
            PetCore.get().debug(2, "Could not run method IEntityPet#removePassenger");
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

    private boolean isOnGround(Entity entity) {
        org.bukkit.block.Block block = entity.getBukkitEntity().getLocation().subtract(0, 0.5, 0).getBlock();
        return block.getType().isSolid();
    }

    @Override
    public boolean isCollidable() {
        return false;
    }

    public void setVelocity(Vector vel) {
        this.motX = vel.getX();
        this.motY = vel.getY();
        this.motZ = vel.getZ();
        this.velocityChanged = true;
    }

    public void repeatTask() {
        if (pet == null) {
            if (bukkitEntity != null)
                bukkitEntity.remove();
            return;
        }
        if (this instanceof IFlyablePet) {
            if (pet.isVehicle()) {
                if (floatDown) {
                    if (!this.onGround && this.motY < 0.0D) {
                        this.motY *= floatSpeed;
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
        PetMoveEvent event = new PetMoveEvent(this, PetMoveEvent.Cause.RIDE);
        Bukkit.getServer().getPluginManager().callEvent(event);
        super.move(enummovetype, d0, d1, d2);
    }

    @Override
    public void n() {
        super.n();
        if (pet == null) return;
        repeatTask();
    }

    protected SoundEffect F() {
        if (pet == null) return null;
        SoundMaker sound = pet.getPetType().getAmbientSound();
        if (sound != null)
            sound.playSound(getEntity());
        return null;
    }

    private boolean isOwnerRiding() {
        if (pet == null) return false;
        if (getOwner() == null) return false;
        if (passengers.size() == 0)
            return false;
        EntityPlayer owner = ((CraftPlayer) getOwner()).getHandle();
        for (Entity passenger : this.passengers) {
            if (passenger.getUniqueID().equals(owner.getUniqueID())) {
                return true;
            }
        }
        return false;
    }

    public void a(float f, float f1, float f2) {
        if (passengers.isEmpty()) {
            this.P = (float) 0.5;
            this.aR = (float) 0.02;
            super.a(f, f1, f2);
        } else {
            if (this.pet == null) {
                this.P = (float) 0.5;
                this.aR = (float) 0.02;
                super.a(f, f1, f2);
                return;
            }
            if (!isOwnerRiding()) {
                this.P = (float) 0.5;
                this.aR = (float) 0.02;
                super.a(f, f1, f2);
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
                                this.motY = 0.5;
                            }
                        }
                    }
                }
            }
            this.yaw = owner.yaw;
            this.lastYaw = this.yaw;
            this.pitch = (float) (owner.pitch * 0.5);
            this.setYawPitch(this.yaw, this.pitch);
            this.aO = this.aM = this.yaw;
            this.P = (float) 1.0;
            f = (float) (owner.be * 0.5);
            f2 = owner.bg;
            if (f2 <= 0.0) {
                f2 *= 0.25;
            }

            if (!(this instanceof IEntityHorsePet))
                f *= 0.75;
            this.k((float) getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).getValue());
            if (!world.isClientSide) {
                super.a(f, f1, f2);
                if (this instanceof IEntityHorsePet) {
                    Location location = getBukkitEntity().getLocation();
                    setPosition(location.getX(), location.getY(), location.getZ());
                    PacketPlayOutEntityTeleport packet = new PacketPlayOutEntityTeleport(this);
                    owner.playerConnection.sendPacket(packet);
                    if (f2 > 0.0F) {
                        float f3 = MathHelper.sin((float) (this.yaw * 0.017453292));
                        float f4 = MathHelper.cos((float) (this.yaw * 0.017453292));
                        this.motX += -0.4 * f3 * 0.0;
                        this.motZ += 0.4 * f4 * 0.0;
                    }
                    this.aF = this.aG;
                    double d0 = this.locX - this.lastX;
                    double d1 = this.locZ - this.lastZ;
                    float f5 = (float) (MathHelper.sqrt(d0 * d0 + d1 * d1) * 4.0);
                    if (f5 > 1.0) {
                        f5 = (float) 1.0;
                    }

                    this.aG += (f5 - this.aG) * 0.4;
                    this.aH += this.aG;
                }
            }
            PetMoveEvent event = new PetMoveEvent(this, PetMoveEvent.Cause.RIDE);
            Bukkit.getServer().getPluginManager().callEvent(event);
        }
    }
}
