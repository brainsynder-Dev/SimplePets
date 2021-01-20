package simplepets.brainsynder.nms.v1_16_R2.entities;

import com.google.common.collect.Maps;
import lib.brainsynder.nbt.StorageTagCompound;
import lib.brainsynder.particle.DustOptions;
import lib.brainsynder.particle.Particle;
import lib.brainsynder.particle.ParticleMaker;
import lib.brainsynder.sounds.SoundMaker;
import net.minecraft.server.v1_16_R2.*;
import org.apache.commons.lang.reflect.FieldUtils;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import org.bukkit.craftbukkit.v1_16_R2.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_16_R2.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_16_R2.entity.CraftPlayer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.entity.IEntityControllerPet;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.misc.IJump;
import simplepets.brainsynder.api.entity.passive.IEntityHorsePet;
import simplepets.brainsynder.api.event.pet.PetMoveEvent;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.v1_16_R2.entities.list.EntityStriderPet;
import simplepets.brainsynder.player.PetOwner;
import simplepets.brainsynder.reflection.FieldAccessor;
import simplepets.brainsynder.wrapper.EntityWrapper;

import java.util.Map;

public abstract class EntityPet extends EntityInsentient implements IEntityPet {
    private IPet pet;
    private Location walkTo = null;
    private double walkSpeed = 0.6000000138418579;
    private double rideSpeed = 0.2000000238418579;
    private final double flySpeed = 0.4000000059604645D; // Default Fly speed for Parrots

    private boolean floatDown = false,
            canGlow = true,
            isGlowing = false,
            autoRemove = true,
            hideName = true,
            silent = false, ignoreVanish = false;
    private int standTime = 0,
            blockX = 0,
            blockZ = 0,
            blockY = 0,
            tickDelay = 10000;
    private FieldAccessor<Boolean> fieldAccessor;

    public EntityPet(EntityTypes<? extends EntityInsentient> type, World world, IPet pet) {
        super(type, world);
        this.pet = pet;
        this.collides = false;

        this.noclip = false;
        fieldAccessor = FieldAccessor.getField(EntityLiving.class, "jumping", Boolean.TYPE);

        // Throws NPW on the setValue line for some reason....
//        if (this instanceof IFlyablePet) {
//            this.getAttributeMap().b(GenericAttributes.FLYING_SPEED);
//            this.getAttributeInstance(GenericAttributes.FLYING_SPEED).setValue(0.4000000059604645D);
//        }
        this.getAttributeInstance(GenericAttributes.MAX_HEALTH).setValue(20.0D);
        walkSpeed = pet.getPetType().getSpeed();
        rideSpeed = pet.getPetType().getRideSpeed();
        hideName = PetCore.get().getConfiguration().getBoolean("PetToggles.HideNameOnShift");
        tickDelay = PetCore.get().getConfiguration().getInt("PetToggles.AutoRemove.TickDelay", 10000);
        autoRemove = PetCore.get().getConfiguration().getBoolean("PetToggles.AutoRemove.Enabled");
        canGlow = PetCore.get().getConfiguration().getBoolean("PetToggles.GlowWhenVanished");
        floatDown = pet.getPetType().canFloat();
    }

    public EntityPet(EntityTypes<? extends EntityInsentient> type, World world) {
        super(type, world);
        if (pet == null) getBukkitEntity().remove();
    }

    @Override
    public void setWalkSpeed(double walkSpeed) {
        this.walkSpeed = walkSpeed;
    }

    @Override
    public double getWalkSpeed() {
        return walkSpeed;
    }

    @Override
    public void setRideSpeed(double rideSpeed) {
        this.rideSpeed = rideSpeed;
    }

    @Override
    public double getRideSpeed() {
        return rideSpeed;
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
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("name")) {
            String name = object.getString("name");
            PetOwner owner = PetOwner.getPetOwner(getOwner());
            if (!owner.getPetName().equals(name)) owner.setPetName(name, true);
        }

        if (object.hasKey("silent")) silent = object.getBoolean("silent");
    }

    @Override
    public boolean isPetSilent() {
        return silent;
    }

    @Override
    public void setPetSilent(boolean silent) {
        this.silent = silent;
    }

    /**
     * Handles the registration of DataWatchers
     * <p>
     * Search for: this.datawatcher.register
     * Class: EntityLiving
     */
    @Override
    protected void initDatawatcher() { // entityInit
        super.initDatawatcher();
        registerDatawatchers();
    }

    @Override
    public Player getOwner() {
        if (pet == null) return null;
        return pet.getOwner();
    }

    public void setIgnoreVanish(boolean ignoreVanish) {
        this.ignoreVanish = ignoreVanish;
    }

    public boolean canIgnoreVanish() {
        return ignoreVanish;
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
            Entity pet = this;
            if (this instanceof IEntityControllerPet) {
                org.bukkit.entity.Entity ent = ((IEntityControllerPet) this).getVisibleEntity().getEntity();
                pet = ((CraftEntity) ent).getHandle();
            }
            handleInvisible(glow, pet);
        } catch (IllegalAccessException ignored) {
        }
    }

    private void handleInvisible(boolean glow, Entity pet) throws IllegalAccessException {
        DataWatcher toCloneDataWatcher = pet.getDataWatcher();
        DataWatcher newDataWatcher = new DataWatcher(pet);
        String fieldName = "d";

        Int2ObjectOpenHashMap<DataWatcher.Item<?>> currentHashMap = new Int2ObjectOpenHashMap<>();
        try {
            Map<Integer, DataWatcher.Item<?>> map = (Map<Integer, DataWatcher.Item<?>>) FieldUtils.readDeclaredField(toCloneDataWatcher, fieldName, true);
            for (Integer integer : map.keySet()) {
                currentHashMap.put(integer, map.get(integer).d());
            }
        } catch (Exception e) {
            fieldName = "entries";
            try {
                currentHashMap = (Int2ObjectOpenHashMap<DataWatcher.Item<?>>) FieldUtils.readDeclaredField(toCloneDataWatcher, fieldName, true);
            } catch (Exception f) {
                // Failed to get any of the fields
                return;
            }
        }

        Int2ObjectOpenHashMap<DataWatcher.Item<?>> newHashMap = new Int2ObjectOpenHashMap<>();
        for (Integer integer : currentHashMap.keySet()) {
            newHashMap.put(integer, currentHashMap.get(integer).d());
        }

        DataWatcher.Item item = newHashMap.get(0);
        byte initialBitMask = (Byte) item.b();
        byte bitMaskIndex = (byte) 6;
        isGlowing = glow;
        if (glow) {
            item.a((byte) (initialBitMask | 1 << bitMaskIndex));
        } else {
            item.a((byte) (initialBitMask & ~(1 << bitMaskIndex)));
        }

        if (fieldName.equals("d")) {
            Map<Integer, DataWatcher.Item<?>> newMap = Maps.newHashMap();
            for (Integer integer : newHashMap.keySet()) {
                newMap.put(integer, newHashMap.get(integer).d());
            }
            FieldUtils.writeDeclaredField(newDataWatcher, fieldName, newMap, true);
        } else {
            FieldUtils.writeDeclaredField(newDataWatcher, fieldName, newHashMap, true);
        }

        PacketPlayOutEntityMetadata metadataPacket = new PacketPlayOutEntityMetadata(pet.getId(), newDataWatcher, true);

        ((CraftPlayer) getOwner()).getHandle().playerConnection.sendPacket(metadataPacket);

    }

    ParticleMaker lime = new ParticleMaker(Particle.REDSTONE).setCount(2).setDustOptions(new DustOptions(Color.LIME, 1F));
    ParticleMaker purple = new ParticleMaker(Particle.REDSTONE).setCount(2).setDustOptions(new DustOptions(Color.PURPLE, 1F));

    public void repeatTask() {
        CraftEntity bukkitEntity = getBukkitEntity();
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
                    setMot(getMot().x, getMot().y * 0.4, getMot().z);
                }
            }
        }

        if (pet.getOwner() != null) {
            Player p = pet.getOwner();
            boolean shifting = p.isSneaking();
            if (hideName) pet.getVisableEntity().getEntity().setCustomNameVisible((!shifting));

            if (!canIgnoreVanish()) {
                boolean ownerVanish = ((CraftPlayer) p).getHandle().isInvisible();
                if (ownerVanish != this.isInvisible()) { // If Owner is invisible & pet is not
                    if (isGlowing && (!ownerVanish))
                        glowHandler(false);  // If the pet is glowing & owner is not vanished
                    this.setInvisible(!this.isInvisible());
                } else {
                    if (ownerVanish && canGlow)
                        if (((CraftPlayer) p).getHandle().isInvisible())
                            glowHandler(true);
                }
            }

            if (pet.isHat()) {
                this.lastYaw = this.yaw = p.getLocation().getYaw();
            }

            double current = getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).getValue();
            if (isOwnerRiding()) {
                // These were used to debug the pet riding
                //lime.sendToLocation(p.getLocation().add(0, 1.7, 0));
                //purple.sendToLocation(bukkitEntity.getLocation().add(0, 2, 0));
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
     * <p>
     * Search for: SoundEffect soundeffect = this.
     * Class: {@link net.minecraft.server.v1_16_R2.EntityInsentient}
     */
    @Override
    public void F() {
        if (pet == null) return;
        if (silent) return;
        SoundMaker sound = pet.getPetType().getSound();
        if (sound != null) sound.playSound(getEntity());
    }

    protected boolean isOwnerRiding() {
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
     * <p>
     * NMS Translations (Thanks Forge):
     * a(float,float,float) = travel(float,float,float)
     * aF = prevLimbSwingAmount
     * aG = limbSwingAmount
     * aR = jumpMovementFactor
     * <p>
     * Search for: !this.isInWater() || this instanceof EntityHuman && ((EntityHuman)this).abilities.isFlying
     * Class: {@link net.minecraft.server.v1_16_R2.EntityLiving}
     */

    private double liquidFloat = 0.4;
    @Override
    //public void a(float strafe, float vertical, float forward) {
    public void g(Vec3D vec3D) {
        if (!this.isVehicle()) {
            super.g(vec3D);
            return;
        }

        CraftEntity bukkitEntity = getBukkitEntity();
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
        Location location = bukkitEntity.getLocation();
        //setPositionRotation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch()); // Prevents pets from moving... no idea why...
        double strafe = vec3D.x;
        double vertical = vec3D.y;
        double forward = vec3D.z;

        if (passengers == null) return;
        if ((this.pet == null) || (!isOwnerRiding())) return;

        if (this.onGround && this.isFlying) {
            isFlying = false;
            this.fallDistance = 0;
        }

        EntityLiving passenger = (EntityLiving) this.getPassengers().get(0);

        // Will make pets float near the surface of the water
        if (this.a(TagsFluid.WATER)) {
            // Allows pets to bob on the water surface (like a player would)
            this.setMot(this.getMot().add(0, liquidFloat, 0));
            if (liquidFloat >= 0.4){
                liquidFloat = liquidFloat-0.05;
            }else if (liquidFloat <= 0.05){
                liquidFloat = liquidFloat+0.05;
            }
        }else if ((this instanceof EntityStriderPet) && this.a(TagsFluid.LAVA)) {
            // Allows Strider to walk on lava, without getting kicked off
            this.setMot(this.getMot().a(0.5D).add(0.0D, 0.05D, 0.0D));
        }

        PacketPlayOutEntityTeleport petPacket = new PacketPlayOutEntityTeleport(this);
        PacketPlayOutEntityTeleport playerPacket = new PacketPlayOutEntityTeleport(this);
        EntityPlayer owner = ((CraftPlayer) getOwner()).getHandle();
//        if (fieldAccessor != null) {
//            if (fieldAccessor.hasField(owner)) {
//                if (fieldAccessor.get(owner)) {
//                    if (isOnGround(this)) {
//                        setMot(getMot().x, 0.5, getMot().z);
//                    } else {
//                        if (pet.getPetType().canFly(pet.getOwner())) {
//                            setMot(getMot().x, 0.3, getMot().z);
//                        }
//                    }
//                }
//            }
//        }
        this.yaw = owner.yaw;
        this.lastYaw = this.yaw;
        this.pitch = owner.pitch * 0.5F;
        this.setYawPitch(this.yaw, this.pitch);
        this.aA = this.yaw;
        this.aC = this.aA;
        strafe = owner.aR * 0.5F;
        forward = owner.aT;
        if (forward <= 0.0F) {
            forward *= 0.25F;
        }

        //if (!(this instanceof IEntityHorsePet)) vec3D.vec3D.x *= 0.75;
        if (this instanceof IEntityHorsePet) {
            if (forward > 0.0F) {
                float f = MathHelper.sin((float) (this.yaw * 0.017453292));
                float f1 = MathHelper.cos((float) (this.yaw * 0.017453292));
                setMot(getMot().add((-0.4 * f * 0.0), 0, (0.4 * f1 * 0.0))); // This would be 0 anyways?
            }

            this.aE = this.dM() * 0.1F;
            this.q((float) this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).getValue());
            super.g(new Vec3D(strafe, vertical, forward));
            doJump(owner);
            location = bukkitEntity.getLocation();
            //setPositionRotation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
//                if (this.cr()) {
//                    this.q((float) this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).getValue());
//                    super.g(new Vec3D(strafe, vertical, forward));
//                } else if (owner instanceof EntityHuman) {
//                    this.setMot(new Vec3D(0, 0, 0));
//                }

/*
                    this.aB = this.aC; // this.prevLimbSwingAmount = this.limbSwingAmount;
                    double d0 = this.locX() - this.lastX;
                    double d1 = this.locZ() - this.lastZ;
                    float f5 = (float) (MathHelper.sqrt(d0 * d0 + d1 * d1) * 4.0);
                    if (f5 > 1.0) {
                        f5 = (float) 1.0;
                    }

                    this.aC += (f5 - this.aC) * 0.4; // this.limbSwingAmount += (f5 - this.limbSwingAmount) * 0.4F;
                    this.aD += this.aC; // this.limbSwing += this.limbSwingAmount;
*/
            this.a(this, false);
//                    owner.playerConnection.sendPacket(petPacket);
//                    owner.playerConnection.sendPacket(playerPacket);
        } else {
            this.q((float) getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).getValue());
            super.g(new Vec3D(strafe, vertical, forward));
            doJump(owner);
        }

        try {
            PetMoveEvent event = new PetMoveEvent(this, PetMoveEvent.Cause.RIDE);
            Bukkit.getServer().getPluginManager().callEvent(event);
        } catch (Throwable ignored) {
        }
    }

    private void doJump (Entity passenger) {
        if (fieldAccessor != null && this.isVehicle()) {
            boolean doJump = false;

            if (this instanceof IJump) {
                if (this.jumpPower > 0.0F) {
                    doJump = true;
                    this.jumpPower = 0.0F;
                } else if (!this.onGround && fieldAccessor != null) {
                    doJump = fieldAccessor.get(passenger);
                }
            } else {
                if (fieldAccessor != null) {
                    doJump = fieldAccessor.get(passenger);
                }
            }

            if (doJump) {
                if (onGround) {
                    Double jumpVelocity = 0.8;
                    jumpVelocity = jumpVelocity == null ? 0.44161199999510264 : jumpVelocity;
                    if (this instanceof IJump) {
                        getAttributeInstance(GenericAttributes.JUMP_STRENGTH).setValue(jumpVelocity);
                    }
                    this.setMot(this.getMot().x, jumpVelocity, this.getMot().z);
                }
            }
        }
    }

    //TODO: Horse code
/*    public void g(Vec3D vec3d) {
        if (this.isAlive()) {
            if (this.isVehicle() && this.er()) {
                EntityLiving entityliving = (EntityLiving)this.getRidingPassenger();
                this.yaw = entityliving.yaw;
                this.lastYaw = this.yaw;
                this.pitch = entityliving.pitch * 0.5F;
                this.setYawPitch(this.yaw, this.pitch);
                this.aA = this.yaw;
                this.aC = this.aA;
                float f = entityliving.aR * 0.5F;
                float f1 = entityliving.aT;
                if (f1 <= 0.0F) {
                    f1 *= 0.25F;
                    this.bv = 0;
                }

                if (this.onGround && this.jumpPower == 0.0F && !this.canSlide) {
                    f = 0.0F;
                    f1 = 0.0F;
                }

                if (this.jumpPower > 0.0F && !this.eY() && this.onGround) {
                    double d0 = b(GenericAttributes.JUMP_STRENGTH) * (double)this.jumpPower * (double)this.getBlockJumpFactor();
                    double d1;
                    if (this.hasEffect(MobEffects.JUMP)) {
                        d1 = d0 + (double)((float)(this.getEffect(MobEffects.JUMP).getAmplifier() + 1) * 0.1F);
                    } else {
                        d1 = d0;
                    }

                    Vec3D vec3d1 = this.getMot();
                    this.setMot(vec3d1.x, d1, vec3d1.z);
                    this.v(true);
                    this.impulse = true;
                    if (f1 > 0.0F) {
                        float f2 = MathHelper.sin(this.yaw * 0.017453292F);
                        float f3 = MathHelper.cos(this.yaw * 0.017453292F);
                        this.setMot(this.getMot().add((double)(-0.4F * f2 * this.jumpPower), 0.0D, (double)(0.4F * f3 * this.jumpPower)));
                    }

                    this.jumpPower = 0.0F;
                }

                this.aE = this.dM() * 0.1F;
                if (this.cr()) {
                    this.q((float)this.b(GenericAttributes.MOVEMENT_SPEED));
                    super.g(new Vec3D((double)f, vec3d.y, (double)f1));
                } else if (entityliving instanceof EntityHuman) {
                    this.setMot(Vec3D.a);
                }

                if (this.onGround) {
                    this.jumpPower = 0.0F;
                    this.v(false);
                }

                this.a(this, false);
            } else {
                this.aE = 0.02F;
                super.g(vec3d);
            }
        }

    }*/
    protected float jumpPower;
    private boolean canSlide;
    protected int bv;
    protected boolean bq;

    public boolean eY() {
        return this.bq;
    }

    public void v(boolean flag) {
        this.bq = flag;
    }

    protected boolean isFlying = false;
/*

    //TODO: Spigot Code
    protected boolean hasRider = false;
    protected float jumpPower = 0;

    @Override
    public void g(Vec3D vec3d) {
        if (!this.isVehicle()) {
            super.g(vec3d);
            return;
        }

        if (this.onGround && this.isFlying) {
            isFlying = false;
            this.fallDistance = 0;
        }

        EntityLiving passenger = (EntityLiving) this.getPassengers().get(0);

        if (this.a(TagsFluid.WATER)) {
            this.setMot(this.getMot().add(0, 0.4, 0));
        }

        // apply pitch & yaw
        this.lastYaw = (this.yaw = passenger.yaw);
        this.pitch = passenger.pitch * 0.5F;
        setYawPitch(this.yaw, this.pitch);
        this.aC = (this.aA = this.yaw);

        // get motion from passenger (player)
        double motionSideways = passenger.aR * 0.5F;
        double motionForward = passenger.aT;

        // backwards is slower
        if (motionForward <= 0.0F) {
            motionForward *= 0.25F;
        }
        // sideways is slower too but not as slow as backwards
        motionSideways *= 0.85F;

        float speed = 0.22222F * (1F + (5));
        double jumpHeight = jumpPower;
        ride(motionSideways, motionForward, vec3d.y, speed); // apply motion

        // throw player move event
        if (this instanceof EntityGiantPet) {
            double delta = Math.pow(this.locX() - this.lastX, 2.0D) + Math.pow(this.locY() - this.lastY, 2.0D)
                    + Math.pow(this.locZ() - this.lastZ, 2.0D);
            float deltaAngle = Math.abs(this.yaw - lastYaw) + Math.abs(this.pitch - lastPitch);
            if (delta > 0.00390625D || deltaAngle > 10.0F) {
                Location to = getBukkitEntity().getLocation();
                Location from = new Location(world.getWorld(), this.lastX, this.lastY, this.lastZ, this.lastYaw,
                        this.lastPitch);
                if (from.getX() != Double.MAX_VALUE) {
                    Location oldTo = to.clone();
                    PlayerMoveEvent event = new PlayerMoveEvent((Player) passenger.getBukkitEntity(), from, to);
                    Bukkit.getPluginManager().callEvent(event);
                    if (event.isCancelled()) {
                        passenger.getBukkitEntity().teleport(from);
                        return;
                    }
                    if ((!oldTo.equals(event.getTo())) && (!event.isCancelled())) {
                        passenger.getBukkitEntity().teleport(event.getTo(), PlayerTeleportEvent.TeleportCause.UNKNOWN);
                        return;
                    }
                }
            }
        }

        if (fieldAccessor != null && this.isVehicle()) {
            boolean doJump = false;
            if (this instanceof IJumpable) {
                if (this.jumpPower > 0.0F) {
                    doJump = true;
                    this.jumpPower = 0.0F;
                } else if (!this.onGround && fieldAccessor != null) {
                    doJump = fieldAccessor.get(passenger);
                }
            } else {
                if (fieldAccessor != null) {
                    doJump = fieldAccessor.get(passenger);
                }
            }

            if (doJump) {
                if (onGround) {
                    jumpHeight = new BigDecimal(jumpHeight).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
                    Double jumpVelocity = 0.8;
                    jumpVelocity = jumpVelocity == null ? 0.44161199999510264 : jumpVelocity;
                    if (this instanceof IJumpable) {
                        getAttributeInstance(GenericAttributes.JUMP_STRENGTH).setValue(jumpVelocity);
                    }
                    this.setMot(this.getMot().x, jumpVelocity, this.getMot().z);
                }
            }

        }
        super.g(vec3d);
    }

    private void ride(double motionSideways, double motionForward, double motionUpwards, float speedModifier) {
        double locY;
        float f2;
        float speed;
        float swimSpeed;

        if (this.a(TagsFluid.WATER, 0.014D)) {
            locY = this.locY();
            speed = 0.8F;
            swimSpeed = 0.02F;

            this.a(swimSpeed, new Vec3D(motionSideways, motionUpwards, motionForward));
            this.move(EnumMoveType.SELF, this.getMot());
            double motX = this.getMot().x * speed;
            double motY = this.getMot().y * 0.800000011920929D;
            double motZ = this.getMot().z * speed;
            motY -= 0.02D;
            if (this.positionChanged && this.e(this.getMot().x,
                    this.getMot().y + 0.6000000238418579D - this.locY() + locY, this.getMot().z)) {
                motY = 0.30000001192092896D;
            }
            this.setMot(motX, motY, motZ);
        } else if (this.a(TagsFluid.LAVA, 0.014D)) {
            locY = this.locY();
            this.a(0.02F, new Vec3D(motionSideways, motionUpwards, motionForward));
            this.move(EnumMoveType.SELF, this.getMot());
            double motX = this.getMot().x * 0.5D;
            double motY = this.getMot().y * 0.5D;
            double motZ = this.getMot().z * 0.5D;
            motY -= 0.02D;
            if (this.positionChanged && this.e(this.getMot().x,
                    this.getMot().y + 0.6000000238418579D - this.locY() + locY, this.getMot().z)) {
                motY = 0.30000001192092896D;
            }
            this.setMot(motX, motY, motZ);
        } else {
            float friction = 0.91F;

            speed = speedModifier * (0.16277136F / (friction * friction * friction));

            this.a(speed, new Vec3D(motionSideways, motionUpwards, motionForward));
            friction = 0.91F;

            double motX = this.getMot().x;
            double motY = this.getMot().y;
            double motZ = this.getMot().z;

            if (this.isClimbing()) {
                swimSpeed = 0.15F;
                motX = MathHelper.a(motX, -swimSpeed, swimSpeed);
                motZ = MathHelper.a(motZ, -swimSpeed, swimSpeed);
                this.fallDistance = 0.0F;
                if (motY < -0.15D) {
                    motY = -0.15D;
                }
            }

            Vec3D mot = new Vec3D(motX, motY, motZ);

            this.move(EnumMoveType.SELF, mot);
            if (this.positionChanged && this.isClimbing()) {
                motY = 0.2D;
            }

            motY -= 0.08D;

            motY *= 0.9800000190734863D;
            motX *= friction;
            motZ *= friction;

            this.setMot(motX, motY, motZ);
        }

        this.au = this.av;
        locY = this.locX() - this.lastX;
        double d1 = this.locZ() - this.lastZ;
        f2 = MathHelper.sqrt(locY * locY + d1 * d1) * 4.0F;
        if (f2 > 1.0F) {
            f2 = 1.0F;
        }

        this.av += (f2 - this.av) * 0.4F;
        this.aw += this.av;
    }*/

    @Override
    public void move(EnumMoveType enummovetype, Vec3D vec3d) {
        super.move(enummovetype, vec3d);
        try {
            PetMoveEvent event = new PetMoveEvent(this, PetMoveEvent.Cause.WALK);
            Bukkit.getServer().getPluginManager().callEvent(event);
        } catch (Throwable ignored) {
        }
    }


    /**
     * Runs per-tick
     * <p>
     * Search for: ("entityBaseTick");
     * Class: Entity
     */
    @Override
    public void entityBaseTick() { // onEntityUpdate
        super.entityBaseTick();
        if (pet == null) {
            if (getBukkitEntity() != null)
                getBukkitEntity().remove();
            return;
        }
        repeatTask();
    }

    protected void registerDatawatchers() {
    }

    /**
     * Used to stop the pet from moving when its pushed
     * <p>
     * Search for: this.impulse = true;
     * Class: Entity
     */
    @Override
    public void f(double x, double y, double z) {
    }


    /**
     * Pets should NEVER be saved in the world
     */
    @Override
    public boolean a_(NBTTagCompound nbttagcompound) {// Calls e
        return false;
    }

    @Override
    public boolean d(NBTTagCompound nbttagcompound) {// Calls e
        return false;
    }

    @Override
    public NBTTagCompound save(NBTTagCompound nbttagcompound) {// Saving
        return nbttagcompound;
    }

    @Override
    public void load(NBTTagCompound nbttagcompound) {// Loading
    }

    // this literally fixed the shit with p2 and i'm so fucking mad
    public CraftEntity getBukkitEntity() {
        return new CraftLivingEntity(world.getServer(), this) {
            @Override
            public EntityType getType() {
                return getPetEntityType().toEntityType();
            }
        };
    }
}
