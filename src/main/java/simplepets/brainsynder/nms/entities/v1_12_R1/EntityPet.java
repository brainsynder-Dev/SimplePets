package simplepets.brainsynder.nms.entities.v1_12_R1;

import com.google.common.collect.Maps;
import net.minecraft.server.v1_12_R1.*;
import org.apache.commons.lang.reflect.FieldUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftCreature;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import simple.brainsynder.nbt.StorageTagCompound;
import simple.brainsynder.sound.SoundMaker;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.Size;
import simplepets.brainsynder.api.entity.IEntityControllerPet;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.IFlyablePet;
import simplepets.brainsynder.api.entity.passive.IEntityHorsePet;
import simplepets.brainsynder.api.event.pet.PetMoveEvent;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.entities.v1_12_R1.impossamobs.EntityArmorStandPet;
import simplepets.brainsynder.nms.entities.v1_12_R1.impossamobs.EntityShulkerPet;
import simplepets.brainsynder.player.PetOwner;
import simplepets.brainsynder.reflection.FieldAccessor;
import simplepets.brainsynder.wrapper.EntityWrapper;

import java.util.Map;

public abstract class EntityPet extends EntityCreature implements IAnimal,
        IEntityPet {
    private IPet pet;
    private double walkSpeed = 0.6000000238418579, rideSpeed = 0.4000000238418579;

    private boolean floatDown = false,
            canGlow = true,
            isGlowing = false,
            autoRemove = true,
            hideName = true,
            silent = false;
    private int standTime = 0,
            blockX = 0,
            blockZ = 0,
            blockY = 0,
            tickDelay = 10000;
    private FieldAccessor<Boolean> fieldAccessor;

    public EntityPet(World world, IPet pet) {
        super(world);
        this.pet = pet;
        this.collides = false;
        if (getClass().isAnnotationPresent(Size.class)) {
            Size size = getClass().getAnnotation(Size.class);
            setSize(size.length(), size.width());
        }

        this.noclip = false;
        fieldAccessor = FieldAccessor.getField(EntityLiving.class, "bd", Boolean.TYPE);

        if (this instanceof IFlyablePet) {
            this.getAttributeMap().b(GenericAttributes.e);
            this.getAttributeInstance(GenericAttributes.e).setValue(0.4000000059604645D);
        }
        this.getAttributeInstance(GenericAttributes.maxHealth).setValue(20.0D);
        walkSpeed = pet.getPetType().getSpeed();
        rideSpeed = pet.getPetType().getRideSpeed();
        hideName = PetCore.get().getConfiguration().getBoolean("PetToggles.HideNameOnShift");
        tickDelay = PetCore.get().getConfiguration().getInt("PetToggles.AutoRemove.TickDelay");
        autoRemove = PetCore.get().getConfiguration().getBoolean("PetToggles.AutoRemove");
        canGlow = PetCore.get().getConfiguration().getBoolean("PetToggles.GlowWhenVanished");
        floatDown = pet.getPetType().canFloat();
    }

    public EntityPet(World world) {
        super(world);
        if (bukkitEntity != null) bukkitEntity.remove();
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = new StorageTagCompound();
        object.setString("PetType", pet.getPetType().getConfigName());
        PetOwner owner = PetOwner.getPetOwner(getOwner());
        if (owner.getPetName() != null)
            object.setString("name", owner.getPetName().replace('§', '&'));
        object.setBoolean("silent", silent);
        return object;
    }

    @Override
    public EntityWrapper getEntityType() {
        return pet.getPetType().getEntityType();
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("name")) {
            String name = object.getString("name");
            PetOwner owner = PetOwner.getPetOwner(getOwner());
            if (!owner.getPetName().equals(name)) owner.setPetName(name, true);
        }

        if (object.hasKey("silent")) silent = object.getBoolean("silent");
    }

    /**
     * Handles the registration of DataWatchers
     *
     * Search for: this.datawatcher.register
     * Class: EntityLiving
     */
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
        if (pet == null) return null;
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

    private void glowHandler(boolean glow) {
        try {
            if (this instanceof IEntityControllerPet) {
                org.bukkit.entity.Entity ent = ((IEntityControllerPet) this).getVisibleEntity().getEntity();
                Entity handle = ((CraftEntity) ent).getHandle();
                if (handle instanceof EntityArmorStandPet) {
                    EntityArmorStandPet pet = (EntityArmorStandPet) handle;
                    DataWatcher toCloneDataWatcher = pet.getDataWatcher();
                    DataWatcher newDataWatcher = new DataWatcher(pet);

                    Map<Integer, DataWatcher.Item<?>> currentMap = (Map<Integer, DataWatcher.Item<?>>) FieldUtils.readDeclaredField(toCloneDataWatcher, "d", true);
                    Map<Integer, DataWatcher.Item<?>> newMap = Maps.newHashMap();

                    for (Integer integer : currentMap.keySet()) {
                        newMap.put(integer, currentMap.get(integer).d());
                    }

                    DataWatcher.Item item = newMap.get(0);
                    byte initialBitMask = (Byte) item.b();
                    byte bitMaskIndex = (byte) 6;
                    isGlowing = glow;
                    if (glow) {
                        item.a((byte) (initialBitMask | 1 << bitMaskIndex));
                    } else {
                        item.a((byte) (initialBitMask & ~(1 << bitMaskIndex)));
                    }
                    FieldUtils.writeDeclaredField(newDataWatcher, "d", newMap, true);

                    PacketPlayOutEntityMetadata metadataPacket = new PacketPlayOutEntityMetadata(pet.getId(), newDataWatcher, true);

                    ((CraftPlayer) getOwner()).getHandle().playerConnection.sendPacket(metadataPacket);
                } else if (handle instanceof EntityShulkerPet) {
                    EntityShulkerPet pet = (EntityShulkerPet) handle;

                    DataWatcher toCloneDataWatcher = pet.getDataWatcher();
                    DataWatcher newDataWatcher = new DataWatcher(pet);

                    Map<Integer, DataWatcher.Item<?>> currentMap = (Map<Integer, DataWatcher.Item<?>>) FieldUtils.readDeclaredField(toCloneDataWatcher, "d", true);
                    Map<Integer, DataWatcher.Item<?>> newMap = Maps.newHashMap();

                    for (Integer integer : currentMap.keySet()) {
                        newMap.put(integer, currentMap.get(integer).d());
                    }

                    DataWatcher.Item item = newMap.get(0);
                    byte initialBitMask = (Byte) item.b();
                    byte bitMaskIndex = (byte) 6;
                    isGlowing = glow;
                    if (glow) {
                        item.a((byte) (initialBitMask | 1 << bitMaskIndex));
                    } else {
                        item.a((byte) (initialBitMask & ~(1 << bitMaskIndex)));
                    }
                    FieldUtils.writeDeclaredField(newDataWatcher, "d", newMap, true);

                    PacketPlayOutEntityMetadata metadataPacket = new PacketPlayOutEntityMetadata(pet.getId(), newDataWatcher, true);

                    ((CraftPlayer) getOwner()).getHandle().playerConnection.sendPacket(metadataPacket);
                } else {
                    EntityPet pet = this;
                    DataWatcher toCloneDataWatcher = pet.getDataWatcher();
                    DataWatcher newDataWatcher = new DataWatcher(pet);

                    Map<Integer, DataWatcher.Item<?>> currentMap = (Map<Integer, DataWatcher.Item<?>>) FieldUtils.readDeclaredField(toCloneDataWatcher, "d", true);
                    Map<Integer, DataWatcher.Item<?>> newMap = Maps.newHashMap();

                    for (Integer integer : currentMap.keySet()) {
                        newMap.put(integer, currentMap.get(integer).d());
                    }

                    DataWatcher.Item item = newMap.get(0);
                    byte initialBitMask = (Byte) item.b();
                    byte bitMaskIndex = (byte) 6;
                    isGlowing = glow;
                    if (glow) {
                        item.a((byte) (initialBitMask | 1 << bitMaskIndex));
                    } else {
                        item.a((byte) (initialBitMask & ~(1 << bitMaskIndex)));
                    }
                    FieldUtils.writeDeclaredField(newDataWatcher, "d", newMap, true);

                    PacketPlayOutEntityMetadata metadataPacket = new PacketPlayOutEntityMetadata(pet.getId(), newDataWatcher, true);

                    ((CraftPlayer) getOwner()).getHandle().playerConnection.sendPacket(metadataPacket);
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void repeatTask() {
        // This section handles the Auto-removal of pets after (tickDelay) Ticks of being stationary...
        if (autoRemove && (bukkitEntity != null)) {
            Location location = bukkitEntity.getLocation();
            if ((blockX != location.getBlockX())
                    || (blockY != location.getBlockY())
                    || (blockZ != location.getBlockZ())) {
                blockX = location.getBlockX();
                blockY = location.getBlockY();
                blockZ = location.getBlockZ();
                if (standTime != 0) standTime = 0;
            } else {
                if (standTime == tickDelay) {
                    if (pet != null) {
                        if (pet.getOwner() != null) {
                            PetOwner.getPetOwner(pet.getOwner()).removePet();
                        }
                    } else {
                        bukkitEntity.remove();
                    }
                }
                standTime++;
            }
        }

        // Handles all other Pet Tasks...
        if (pet == null) {
            if (bukkitEntity != null)
                bukkitEntity.remove();
            return;
        }

        if (pet.isVehicle()) {
            if (floatDown) {
                if (!isOnGround(this)) {
                    motY *= 0.4;
                }
            }
        }

        if (pet.getOwner() != null) {
            Player p = pet.getOwner();
            boolean shifting = p.isSneaking();
            if (hideName) pet.getVisableEntity().getEntity().setCustomNameVisible((!shifting));

            boolean ownerVanish = ((CraftPlayer) p).getHandle().isInvisible();
            if (ownerVanish != this.isInvisible()) {
                if (isGlowing && (!ownerVanish)) glowHandler(false);
                this.setInvisible(!this.isInvisible());
            } else {
                if (ownerVanish && canGlow)
                    if (((CraftPlayer) p).getHandle().isInvisible())
                        glowHandler(true);
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

    /**
     * Handles the Ambient Sound playing
     *
     * Search for: SoundEffect soundeffect = this.
     * Class: EntityInsentient
     */
    @Override
    protected SoundEffect F() {
        if (pet == null) return null;
        if (silent) return null;
        SoundMaker sound = pet.getPetType().getSound();
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

    /**
     * This method handles the Pet riding
     *
     * NMS Translations (Thanks Forge):
     *   a(float,float,float) = travel(float,float,float)
     *   aF = prevLimbSwingAmount
     *   aG = limbSwingAmount
     *   aR = jumpMovementFactor
     *
     * Search for: !this.isInWater() || this instanceof EntityHuman && ((EntityHuman)this).abilities.isFlying
     * Class: EntityLiving
     */
    @Override
    public void a(float strafe, float vertical, float forward) {
        if (passengers == null) {
            this.P = (float) 0.5;
            this.aR = (float) 0.02;
            super.a(strafe, vertical, forward);
        } else {
            if (this.pet == null) {
                this.P = (float) 0.5;
                this.aR = (float) 0.02;
                super.a(strafe, vertical, forward);
                return;
            }
            if (!isOwnerRiding()) {
                this.P = (float) 0.5;
                this.aR = (float) 0.02;
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
            this.aP = this.aN = this.yaw;
            this.P = 1.0F;
            strafe = (float) (owner.be * 0.5);
            forward = owner.bg;
            if (forward <= 0.0) {
                forward *= 0.25;
            }

            if (!(this instanceof IEntityHorsePet)) strafe *= 0.75;
            this.k((float) getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).getValue());
            if (!world.isClientSide) {
                super.a(strafe, vertical, forward);
                if (this instanceof IEntityHorsePet) {
                    Location location = getBukkitEntity().getLocation();
                    setPosition(location.getX(), location.getY(), location.getZ());
                    PacketPlayOutEntityTeleport packet = new PacketPlayOutEntityTeleport(this);
                    owner.playerConnection.sendPacket(packet);
                    if (forward > 0.0F) {
                        float f = MathHelper.sin((float) (this.yaw * 0.017453292));
                        float f1 = MathHelper.cos((float) (this.yaw * 0.017453292));
                        this.motX += -0.4 * f * 0.0;
                        this.motZ += 0.4 * f1 * 0.0;
                    }
                    this.aF = this.aG; // this.prevLimbSwingAmount = this.limbSwingAmount;
                    double d0 = this.locX - this.lastX;
                    double d1 = this.locZ - this.lastZ;
                    float f5 = (float) (MathHelper.sqrt(d0 * d0 + d1 * d1) * 4.0);
                    if (f5 > 1.0) {
                        f5 = (float) 1.0;
                    }

                    this.aG += (f5 - this.aG) * 0.4; // this.limbSwingAmount += (f5 - this.limbSwingAmount) * 0.4F;
                    this.aH += this.aG; // this.limbSwing += this.limbSwingAmount;
                }
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

    @Override
    public void move(EnumMoveType enummovetype, double d0, double d1, double d2) {
        super.move(enummovetype, d0, d1, d2);
        try {
            PetMoveEvent event = new PetMoveEvent(this, PetMoveEvent.Cause.WALK);
            Bukkit.getServer().getPluginManager().callEvent(event);
        }catch (Throwable ignored) {}
    }


    /**
     * Runs per-tick
     *
     * Search for: this.world.methodProfiler.a("entityBaseTick");
     * Class: Entity
     */
    @Override
    public void Y() {
        super.Y();
        if (pet == null) {
            if (bukkitEntity != null)
                bukkitEntity.remove();
            return;
        }
        repeatTask();
    }

    protected void registerDatawatchers() {
    }

    /**
     * Used to stop the pet from moving when its pushed
     *
     * Search for: this.impulse = true;
     * Class: Entity
     */
    @Override
    public void f(double x, double y, double z) {
    }


    /**
     * Pets should NEVER be saved in the world
     */
    public void a(NBTTagCompound nbttagcompound){
    }

    public void b(NBTTagCompound nbttagcompound){
    }

    public boolean c(NBTTagCompound nbttagcompound){
        return false;
    }

    public boolean d(NBTTagCompound nbttagcompound){
        return false;
    }

    public NBTTagCompound e(NBTTagCompound nbttagcompound){
        return nbttagcompound;
    }

    public void f(NBTTagCompound nbttagcompound){
    }
}
