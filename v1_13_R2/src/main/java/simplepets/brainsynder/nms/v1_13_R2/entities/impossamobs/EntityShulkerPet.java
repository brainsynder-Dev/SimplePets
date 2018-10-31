package simplepets.brainsynder.nms.v1_13_R2.entities.impossamobs;

import net.minecraft.server.v1_13_R2.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_13_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.entity.Shulker;
import org.bukkit.event.entity.CreatureSpawnEvent;
import simple.brainsynder.nbt.StorageTagCompound;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.entity.hostile.IEntityShulkerPet;
import simplepets.brainsynder.api.event.pet.PetMoveEvent;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.v1_13_R2.entities.list.EntityControllerPet;
import simplepets.brainsynder.player.PetOwner;
import simplepets.brainsynder.reflection.FieldAccessor;
import simplepets.brainsynder.wrapper.DyeColorWrapper;
import simplepets.brainsynder.wrapper.EntityWrapper;

public class EntityShulkerPet extends EntityShulker implements IEntityShulkerPet {
    private boolean isCustom = false;
    private boolean rainbow = false;
    private int toggle = 0;
    private DyeColorWrapper color = DyeColorWrapper.PURPLE;
    private EntityControllerPet pet;
    private FieldAccessor<Boolean> fieldAccessor;

    public EntityShulkerPet(World world) {
        super(world);
    }

    public EntityShulkerPet(World world, EntityControllerPet pet) {
        super(world);
        this.pet = pet;
        fieldAccessor = FieldAccessor.getField(EntityLiving.class, "bg", Boolean.TYPE);
    }

    public static Shulker spawn(Location location, EntityControllerPet pet) {
        EntityShulkerPet shulker = new EntityShulkerPet(((CraftWorld) location.getWorld()).getHandle(), pet);
        shulker.setCustom(true);
        shulker.setNoAI(false);
        shulker.setSilent(true);
        WorldServer worldServer = ((CraftWorld) location.getWorld()).getHandle();
        shulker.setPosition(location.getX(), location.getY(), location.getZ());
        worldServer.addEntity(shulker, CreatureSpawnEvent.SpawnReason.CUSTOM);
        return ((Shulker) shulker.getBukkitEntity());
    }

    public void addPassenger(org.bukkit.entity.Entity entity, org.bukkit.entity.Entity passenger) {
        ((CraftEntity) entity).getHandle().passengers.add(((CraftEntity) passenger).getHandle());
        PacketPlayOutMount packet = new PacketPlayOutMount(((CraftEntity) entity).getHandle());
        if (entity instanceof Player) {
            ((CraftPlayer) entity).getHandle().playerConnection.sendPacket(packet);
        }
    }

    public void removePassenger(org.bukkit.entity.Entity entity) {
        ((CraftEntity) entity).getHandle().passengers.clear();
        PacketPlayOutMount packet = new PacketPlayOutMount(((CraftEntity) entity).getHandle());
        if (entity instanceof Player) {
            ((CraftPlayer) entity).getHandle().playerConnection.sendPacket(packet);
        }
    }

    /**
     * Search for: if (Math.abs(this.motX) < 0.003D) {
     * Class: Entity
     */
    @Override
    public void tick() {
        super.tick();
        if (isCustom) {
            this.motX = 0;
            this.motY = 0;
            this.motZ = 0;
            if (rainbow) {
                if (toggle == 4) {
                    setColor(DyeColorWrapper.getNext(color));
                    PetCore.get().getInvLoaders().PET_DATA.update(PetOwner.getPetOwner(getOwner()));
                    toggle = 0;
                }
                toggle++;
            }
        }
    }

    @Override
    public void stopRiding() {
        if (!this.a(TagsFluid.WATER)) super.stopRiding();
    }

    @Override
    public boolean isClosed() {
        return (dA() == 0);
    }

    @Override
    public void setClosed(boolean var) {
        if (var) {
            a(0);
        } else {
            a(100);
        }
    }

    @Override
    public void move(EnumMoveType var1, double var2, double var4, double var6) {
        if (isCustom)
            return;
        super.move(var1, var2, var4, var6);
    }


    /**
     * Search for: this.getAttributeInstance(GenericAttributes.
     * Class: EntityShulker
     */
    @Override
    public void a(int var1) {
        if (isCustom) {
            this.datawatcher.set(c, (byte) var1);
            return;
        }
        super.a(var1);
    }

    @Override
    public Player getOwner() {
        return pet.getOwner();
    }

    @Override
    public IPet getPet() {
        return pet.getPet();
    }

    @Override
    public org.bukkit.entity.Entity getEntity() {
        return getBukkitEntity();
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = pet.asCompound();
        object.setBoolean("rainbow", rainbow);
        if (!rainbow)
            object.setString("color", color.name());
        object.setBoolean("closed", isClosed());
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("rainbow")) rainbow = object.getBoolean("rainbow");
        if (object.hasKey("closed")) setClosed(object.getBoolean("closed"));
        if (object.hasKey("color")) setColor(DyeColorWrapper.getByName(object.getString("color")));
        pet.applyCompound(object);
    }

    @Override
    public DyeColorWrapper getColor() {
        return color;
    }

    @Override
    public void setColor(DyeColorWrapper color) {
        this.color = color;
        this.datawatcher.set(COLOR, color.getWoolData());
    }

    @Override
    public EntityWrapper getEntityType() {
        return pet.getEntityType();
    }

    public boolean isCustom() {return this.isCustom;}

    public void setCustom(boolean isCustom) {this.isCustom = isCustom; }

    private boolean isOwnerRiding() {
        if (pet == null) return false;
        if (getOwner() == null) return false;
        if (passengers.size() == 0)
            return false;
        EntityPlayer owner = ((CraftPlayer) getOwner()).getHandle();
        for (net.minecraft.server.v1_13_R2.Entity passenger : this.passengers) {
            if (passenger.getUniqueID().equals(owner.getUniqueID())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void a(float strafe, float vertical, float forward) {
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
                            if (pet.getPet().getPetType().canFly(pet.getOwner())) {
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
            forward = owner.bi;
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

            if (getOwner() == null) {
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

    private boolean isOnGround(net.minecraft.server.v1_13_R2.Entity entity) {
        org.bukkit.block.Block block = entity.getBukkitEntity().getLocation().subtract(0, 0.5, 0).getBlock();
        return block.getType().isSolid();
    }

    @Override
    public boolean isRainbow() {
        return rainbow;
    }

    @Override
    public void setRainbow(boolean rainbow) {
        this.rainbow=rainbow;
    }
}

