package simplepets.brainsynder.nms.v1_15_R1.entities;

import com.google.common.collect.Maps;
import lib.brainsynder.nbt.StorageTagCompound;
import lib.brainsynder.sounds.SoundMaker;
import net.minecraft.server.v1_15_R1.*;
import org.apache.commons.lang.reflect.FieldUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.entity.IEntityControllerPet;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.misc.IFlyablePet;
import simplepets.brainsynder.api.entity.passive.IEntityHorsePet;
import simplepets.brainsynder.api.event.pet.PetMoveEvent;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.player.PetOwner;
import simplepets.brainsynder.reflection.FieldAccessor;
import simplepets.brainsynder.wrapper.EntityWrapper;

import java.util.Map;

public abstract class EntityPet extends EntityCreature implements IEntityPet {
    private IPet pet;
    private Location walkTo = null;
    private double walkSpeed = 0.6000000238418579, rideSpeed = 0.4000000238418579;

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

    public EntityPet(EntityTypes<? extends EntityCreature> type, World world, IPet pet) {
        super(type, world);
        this.pet = pet;
        this.collides = false;

        this.noclip = false;
        fieldAccessor = FieldAccessor.getField(EntityLiving.class, "jumping", Boolean.TYPE);

        if (this instanceof IFlyablePet) {
            this.getAttributeMap().b(GenericAttributes.FLYING_SPEED);
            this.getAttributeInstance(GenericAttributes.FLYING_SPEED).setValue(0.4000000059604645D);
        }
        this.getAttributeInstance(GenericAttributes.MAX_HEALTH).setValue(20.0D);
        walkSpeed = pet.getPetType().getSpeed();
        rideSpeed = pet.getPetType().getRideSpeed();
        hideName = PetCore.get().getConfiguration().getBoolean("PetToggles.HideNameOnShift");
        tickDelay = PetCore.get().getConfiguration().getInt("PetToggles.AutoRemove.TickDelay", 10000);
        autoRemove = PetCore.get().getConfiguration().getBoolean("PetToggles.AutoRemove.Enabled");
        canGlow = PetCore.get().getConfiguration().getBoolean("PetToggles.GlowWhenVanished");
        floatDown = pet.getPetType().canFloat();
    }

    public EntityPet(EntityTypes<? extends EntityCreature> type, World world) {
        super(type, world);
        if (getBukkitEntity() != null) getBukkitEntity().remove();
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
     *
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
        } catch (IllegalAccessException ignored) {}
    }

    private void handleInvisible (boolean glow, Entity pet) throws IllegalAccessException {
        DataWatcher toCloneDataWatcher = pet.getDataWatcher();
        DataWatcher newDataWatcher = new DataWatcher(pet);
        String fieldName = "d";

        Int2ObjectOpenHashMap<DataWatcher.Item<?>> currentHashMap = new Int2ObjectOpenHashMap<>();
        try {
            Map<Integer, DataWatcher.Item<?>> map = (Map<Integer, DataWatcher.Item<?>>)FieldUtils.readDeclaredField(toCloneDataWatcher, fieldName, true);
            for (Integer integer : map.keySet()) {
                currentHashMap.put(integer, map.get(integer).d());
            }
        }catch (Exception e){
            fieldName = "entries";
            try {
                currentHashMap = (Int2ObjectOpenHashMap<DataWatcher.Item<?>>)FieldUtils.readDeclaredField(toCloneDataWatcher, fieldName, true);
            }catch (Exception f){
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
        }else{
            FieldUtils.writeDeclaredField(newDataWatcher, fieldName, newHashMap, true);
        }

        PacketPlayOutEntityMetadata metadataPacket = new PacketPlayOutEntityMetadata(pet.getId(), newDataWatcher, true);

        ((CraftPlayer) getOwner()).getHandle().playerConnection.sendPacket(metadataPacket);
        this.aK = (this.aI = this.yaw);

    }

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
                    setMot(getMot().x, getMot().y*0.4, getMot().z);
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
                    if (isGlowing && (!ownerVanish)) glowHandler(false);  // If the pet is glowing & owner is not vanished
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
     * Class: {@link net.minecraft.server.v1_15_R1.EntityInsentient}
     */
    @Override
    public void B() {
        if (pet == null) return;
        if (silent) return ;
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
     *
     * NMS Translations (Thanks Forge):
     *   a(float,float,float) = travel(float,float,float)
     *   aF = prevLimbSwingAmount
     *   aG = limbSwingAmount
     *   aR = jumpMovementFactor
     *
     * Search for: !this.isInWater() || this instanceof EntityHuman && ((EntityHuman)this).abilities.isFlying
     * Class: {@link net.minecraft.server.v1_15_R1.EntityLiving}
     */
    @Override
    //public void a(float strafe, float vertical, float forward) {
    public void e(Vec3D vec3D) {
        double strafe = vec3D.x;
        double vertical = vec3D.y;
        double forward = vec3D.z;

        if (passengers == null) {
            this.H = (float) 0.5;
            this.aM = (float) 0.02;
            super.e(vec3D);
        } else {
            if (this.pet == null) {
                this.H = (float) 0.5;
                this.aM = (float) 0.02;
                super.e(vec3D);
                return;
            }
            if (!isOwnerRiding()) {
                this.H = (float) 0.5;
                this.aM = (float) 0.02;
                super.e(vec3D);
                return;
            }
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
            this.pitch = owner.pitch * 0.5F;
            this.setYawPitch(this.yaw, this.pitch);
            this.aI = this.yaw;
            this.aK = this.aI;
            strafe = owner.aZ * 0.5F;
            forward = owner.bb;
            if (forward <= 0.0F) {
                forward *= 0.25F;
            }
            //this.aL = this.aD = this.yaw;
            //this.H = 1.0F;

            Vec3D vec = new Vec3D(strafe, vertical, forward);
            //if (!(this instanceof IEntityHorsePet)) vec3D.vec3D.x *= 0.75;
            this.o((float) getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).getValue());
            super.e(vec);
            if (!world.isClientSide) {
                if (this instanceof IEntityHorsePet) {
                    Location location = getBukkitEntity().getLocation();
                    setPosition(location.getX(), location.getY(), location.getZ());
                    PacketPlayOutEntityTeleport packet = new PacketPlayOutEntityTeleport(this);
                    owner.playerConnection.sendPacket(packet);
                    if (forward > 0.0F) {
                        float f = MathHelper.sin((float) (this.yaw * 0.017453292));
                        float f1 = MathHelper.cos((float) (this.yaw * 0.017453292));
                        setMot(getMot().add( (-0.4 * f * 0.0), 0, (0.4 * f1 * 0.0) )); // This would be 0 anyways?
                    }

                    this.aM = this.dt() * 0.1F;
                    if (this.cj()) {
                        this.o((float)this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).getValue());
                        super.e(new Vec3D(strafe, vertical, forward));
                    } else if (owner instanceof EntityHuman) {
                        this.setMot(Vec3D.a);
                    }

                    this.aC = this.aD; // this.prevLimbSwingAmount = this.limbSwingAmount;
                    double d0 = this.locX() - this.lastX;
                    double d1 = this.locZ() - this.lastZ;
                    float f5 = (float) (MathHelper.sqrt(d0 * d0 + d1 * d1) * 4.0);
                    if (f5 > 1.0) {
                        f5 = (float) 1.0;
                    }

                    this.aD += (f5 - this.aD) * 0.4; // this.limbSwingAmount += (f5 - this.limbSwingAmount) * 0.4F;
                    this.aE += this.aD; // this.limbSwing += this.limbSwingAmount;
                }
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
            try {
                PetMoveEvent event = new PetMoveEvent(this, PetMoveEvent.Cause.RIDE);
                Bukkit.getServer().getPluginManager().callEvent(event);
            }catch (Throwable ignored) {}
        }
    }

    @Override
    public void move(EnumMoveType enummovetype, Vec3D vec3d) {
        super.move(enummovetype, vec3d);
        try {
            PetMoveEvent event = new PetMoveEvent(this, PetMoveEvent.Cause.WALK);
            Bukkit.getServer().getPluginManager().callEvent(event);
        }catch (Throwable ignored) {}
    }


    /**
     * Runs per-tick
     *
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
    @Override
    public void a(NBTTagCompound nbttagcompound){
    }

    @Override
    public void b(NBTTagCompound nbttagcompound){
    }

    @Override
    public boolean c(NBTTagCompound nbttagcompound){// Calls e
        return false;
    }

    @Override
    public boolean d(NBTTagCompound nbttagcompound){// Calls e
        return false;
    }

    @Override
    public NBTTagCompound save(NBTTagCompound nbttagcompound){// Saving
        return nbttagcompound;
    }

    @Override
    public void f(NBTTagCompound nbttagcompound){// Loading
    }

    public CraftEntity getBukkitEntity() {
        return new CraftLivingEntity(world.getServer(), this) {
            @Override
            public EntityType getType() {
                return getPetEntityType().toEntityType();
            }
        };
    }
}
