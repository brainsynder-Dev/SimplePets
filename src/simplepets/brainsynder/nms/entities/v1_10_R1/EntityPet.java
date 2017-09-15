package simplepets.brainsynder.nms.entities.v1_10_R1;

import net.minecraft.server.v1_10_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftCreature;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import simple.brainsynder.nbt.StorageTagCompound;
import simplepets.brainsynder.menu.PetDataMenu;
import simplepets.brainsynder.nms.entities.type.main.IEntityPet;
import simplepets.brainsynder.nms.entities.type.main.IFlyablePet;
import simplepets.brainsynder.pet.IPet;
import simplepets.brainsynder.pet.PetMoveEvent;
import simplepets.brainsynder.reflection.FieldAccessor;
import simplepets.brainsynder.wrapper.EntityWrapper;

@Deprecated
public abstract class EntityPet extends EntityCreature implements IAnimal,
        IEntityPet {
    protected FieldAccessor<Boolean> fieldAccessor;
    private IPet pet;

    public EntityPet(World world, IPet pet) {
        super(world);

        this.pet = pet;
        this.collides = false;
        this.setSize(0.5F, 0.5F);
        this.noclip = false;
        fieldAccessor = FieldAccessor.getField(EntityLiving.class, "be", Boolean.TYPE);

    }

    @Override
    public EntityWrapper getEntityType() {
        return pet.getPetType().getType();
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = new StorageTagCompound();
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {

    }

    public void setPassenger(int pos, org.bukkit.entity.Entity entity, org.bukkit.entity.Entity passenger) {
        ((CraftEntity) entity).getHandle().passengers.add(pos, ((CraftEntity) passenger).getHandle());
        PacketPlayOutMount packet = new PacketPlayOutMount(((CraftEntity) entity).getHandle());
        if (entity instanceof Player) {
            ((CraftPlayer) entity).getHandle().playerConnection.sendPacket(packet);
        } else {
            ((CraftPlayer) getOwner()).getHandle().playerConnection.sendPacket(packet);
        }
    }

    public void removePassenger(org.bukkit.entity.Entity entity) {
        ((CraftEntity) entity).getHandle().passengers.clear();
        PacketPlayOutMount packet = new PacketPlayOutMount(((CraftEntity) entity).getHandle());
        if (entity instanceof Player) {
            ((CraftPlayer) entity).getHandle().playerConnection.sendPacket(packet);
        } else {
            ((CraftPlayer) getOwner()).getHandle().playerConnection.sendPacket(packet);
        }
    }

    @Override
    public boolean startRiding(Entity entity) {
        return false;
    }

    protected void initDataWatcher() {
    }

    @Override
    protected void i() {
        super.i();
        initDataWatcher();
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
        org.bukkit.block.Block block = entity.getBukkitEntity().getLocation().subtract(0, 0.4, 0).getBlock();
        return block.getType().isSolid();
    }

    public boolean a(EntityHuman human) {
        return this.onInteract((Player) human.getBukkitEntity());
    }

    public void repeatTask() {
        if (this instanceof IFlyablePet) {
            if (getPet().isVehicle()) {
                if (!this.onGround && this.motY < 0.0D) {
                    this.motY *= 0.6D;
                }
            }
        }
        if (pet.getOwner() != null) {
            Player p = pet.getOwner();
            if (((CraftPlayer) p).getHandle().isInvisible() != this.isInvisible()) {
                this.setInvisible(!this.isInvisible());
            }

            if (this.getPet().isHat()) {
                this.lastYaw = this.yaw = p.getLocation().getYaw();
            }
        }
    }

    @Override
    public void m() {
        super.m();
        repeatTask();
    }

    public boolean onInteract(Player p) {
        if (p.getName().equals(pet.getOwner().getName())) {
            PetDataMenu data = new PetDataMenu(pet);
            data.showTo(p);
            return true;
        } else {
            return false;
        }

    }

    public boolean isOwnerRiding() {
        EntityPlayer owner = ((CraftPlayer) getOwner()).getHandle();
        for (Entity passenger : this.passengers) {
            if (passenger == owner) {
                return true;
            }
        }
        return false;
    }

    public void g(float sideMot, float forwMot) {
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
            this.l(getPet().getPetType().getRideSpeed());
            EntityPlayer owner = ((CraftPlayer) getOwner()).getHandle();
            this.lastYaw = this.yaw = owner.yaw;
            this.pitch = owner.pitch * 0.5F;
            this.setYawPitch(this.yaw, this.pitch);
            this.aP = this.aN = this.yaw;
            this.P = 1.0F;
            sideMot = owner.bf * 0.5F;
            forwMot = owner.bg;
            if (forwMot <= 0.0F) {
                forwMot *= 0.25F;
            }

            sideMot *= 0.75F;

            k(0.2F);
            if (!world.isClientSide) {
                PetMoveEvent event = new PetMoveEvent(this, PetMoveEvent.Cause.RIDE);
                Bukkit.getServer().getPluginManager().callEvent(event);
                super.g(sideMot, forwMot);
            }
            if (fieldAccessor != null) {
                if (fieldAccessor.hasField(owner)) {
                    if (fieldAccessor.get(owner)) {
                        if (isOnGround(this)) {
                            this.motY = 0.3D;
                        } else {
                            if (pet.getPetType().canFly(getOwner()))
                                this.motY = 0.5D;
                        }
                    }
                }
            }
        }
    }
}
