package simplepets.brainsynder.versions.v1_17_R1.entity;

import com.google.common.collect.Maps;
import lib.brainsynder.nbt.StorageTagCompound;
import lib.brainsynder.sounds.SoundMaker;
import lib.brainsynder.utils.Colorize;
import net.minecraft.server.v1_16_R3.*;
import org.apache.commons.lang.reflect.FieldUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.misc.IEntityControllerPet;
import simplepets.brainsynder.api.entity.passive.IEntityHorsePet;
import simplepets.brainsynder.api.event.entity.EntityNameChangeEvent;
import simplepets.brainsynder.api.pet.IPetConfig;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.plugin.SimplePets;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.files.Config;
import simplepets.brainsynder.managers.ParticleManager;
import simplepets.brainsynder.versions.v1_17_R1.pathfinder.PathfinderGoalLookAtOwner;
import simplepets.brainsynder.versions.v1_17_R1.pathfinder.PathfinderWalkToPlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

public abstract class EntityPet extends EntityInsentient implements IEntityPet {
    private PetUser user;
    private PetType petType;
    private Map<String, StorageTagCompound> additional;
    private String petName = null;


    private final double walkSpeed = 0.6000000238418579;
    private final double rideSpeed = 0.4000000238418579;
    private final boolean floatDown = false;
    private final boolean canGlow = true;
    private boolean isGlowing = false;
    private final boolean autoRemove = true;
    private boolean silent = false;
    private boolean ignoreVanish = false;
    private int standTime = 0;
    private int blockX = 0;
    private int blockZ = 0;
    private int blockY = 0;
    private final int tickDelay = 10000;

    public EntityPet(EntityTypes<? extends EntityInsentient> entitytypes, World world) {
        super(entitytypes, world);
        getBukkitEntity().remove();
    }

    public EntityPet(EntityTypes<? extends EntityInsentient> entitytypes, PetType type, PetUser user) {
        super(entitytypes, ((CraftWorld) ((Player) user.getPlayer()).getLocation().getWorld()).getHandle());
        this.user = user;
        this.petType = type;

        this.additional = new HashMap<>();

        this.collides = false;
        this.noclip = false;


        // needs to be faster but less then 6
        getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(0.500000238418579);
    }

    public boolean isJumping () {
        return jumping;
    }

    @Override
    public void teleportToOwner() {
        user.getUserLocation().ifPresent(location -> {
            setPositionRotation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
            PetCore.getInstance().getParticleHandler().sendParticle(ParticleManager.Reason.TELEPORT, (Player) user.getPlayer(), location);
        });
    }

    @Override
    public boolean damageEntity(DamageSource damagesource, float f) {
        return false;
    }

    @Override
    protected void initPathfinder() {
        goalSelector.a(1, new PathfinderGoalFloat(this));
        goalSelector.a(2, new PathfinderWalkToPlayer(this, 3, 10));
        goalSelector.a(3, new PathfinderGoalLookAtOwner(this, 3f, 0.2f));
        goalSelector.a(3, new PathfinderGoalRandomLookaround(this));
    }

    // TODO: Needs to be set up when Spigot 1.17 is out
    @Override
    public boolean isFrozen() {
        return false;
    }

    @Override
    public void setFrozen(boolean frozen) {

    }

    @Override
    public void setPetName(String name) {
        if ((name == null) || name.isEmpty()) {
            Optional<IPetConfig> config = SimplePets.getPetConfigManager().getPetConfig(petType);
            if (config.isPresent()) {
                name = config.get().getDisplayName();
            }
        }
        String newName = name.replace("%player%", user.getPlayer().getName());

        EntityNameChangeEvent event = new EntityNameChangeEvent(this, newName);
        Bukkit.getServer().getPluginManager().callEvent(event);

        petName = Colorize.translateBungeeHex(event.getPrefix())
                + translateName(event.getName())
                + Colorize.translateBungeeHex(event.getSuffix());
        getBukkitEntity().setCustomNameVisible(PetCore.getInstance().getConfiguration().getBoolean("PetToggles.ShowPetNames", true));
        getBukkitEntity().setCustomName(petName);
    }

    public String translateName(String name) {
        boolean color = PetCore.getInstance().getConfiguration().getBoolean(Config.COLOR);
        boolean magic = PetCore.getInstance().getConfiguration().getBoolean(Config.MAGIC);
        if (!magic) name = name.replace("&k", "");
        if (!color) return name;

        if (PetCore.getInstance().getConfiguration().getBoolean(Config.HEX)) {
            name = Colorize.translateBungeeHex(name);
        }else{
            name = Colorize.translateBukkit(name);
        }
        return name;
    }

    @Override
    public Optional<String> getPetName() {
        if (petName != null) return Optional.of(petName);

        if (user.getPetName(petType).isPresent())
            return user.getPetName(petType);
        return Optional.empty();
    }

    @Override
    public void handleAdditionalStorage(String pluginKey, Function<StorageTagCompound, StorageTagCompound> compound) {
        additional.put(pluginKey, compound.apply(additional.getOrDefault(pluginKey, new StorageTagCompound())));
    }

    @Override
    public UUID getOwnerUUID() {
        return user.getPlayer().getUniqueId();
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = new StorageTagCompound();
        object.setString("PetType", getPetType().getName());
        object.setUniqueId("uuid", getUniqueID());
        object.setFloat("health", getHealth());
        object.setString("ownerName", getPetUser().getPlayer().getName());
        user.getPetName(getPetType()).ifPresent(name -> {
            object.setString("name", name.replace('§', '&'));
        });
        object.setBoolean("silent", silent);

        if (!additional.isEmpty()) {
            StorageTagCompound additional = new StorageTagCompound();
            this.additional.forEach(additional::setTag);
            object.setTag("additional", additional);
        }
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("uuid")) a_(object.getUniqueId("uuid")); // Sets the Entities UUID
        if (object.hasKey("health")) {
            float health = object.getFloat("health", 20);
            if (health >= 2048) health = 2047; // Prevents crash caused by spigot
            if (health < 1) health = 1; // Prevents pets from instant death
            setHealth(health);
        }
        if (object.hasKey("name")) {
            String name = object.getString("name");
            if (name != null) name = name.replace("~", " ");
            setPetName(name);
        }

        if (object.hasKey("silent")) silent = object.getBoolean("silent");

        if (object.hasKey("additional")) {
            StorageTagCompound additional = object.getCompoundTag("additional");
            additional.getKeySet().forEach(pluginKey -> this.additional.put(pluginKey, additional.getCompoundTag(pluginKey)));
        }
    }

    @Override
    public boolean isPetSilent() {
        return silent;
    }

    @Override
    public void setPetSilent(boolean silent) {
        this.silent = silent;
    }

    @Override
    public Entity getEntity() {
        return getBukkitEntity();
    }

    @Override
    public PetType getPetType() {
        return petType;
    }

    @Override
    public PetUser getPetUser() {
        return user;
    }

    @Override
    public EntityType getPetEntityType() {
        return petType.getEntityType();
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

    protected void registerDatawatchers() {
    }

    public void setIgnoreVanish(boolean ignoreVanish) {
        this.ignoreVanish = ignoreVanish;
    }

    public boolean canIgnoreVanish() {
        return ignoreVanish;
    }

    private boolean isOnGround(net.minecraft.server.v1_16_R3.Entity entity) {
        org.bukkit.block.Block block = entity.getBukkitEntity().getLocation().subtract(0, 0.5, 0).getBlock();
        return block.getType().isSolid();
    }

    protected boolean isOwnerRiding() {
        if (passengers.size() == 0) return false;
        EntityPlayer owner = ((CraftPlayer) getPetUser().getPlayer()).getHandle();
        for (net.minecraft.server.v1_16_R3.Entity passenger : this.passengers) {
            if (passenger.getUniqueID().equals(owner.getUniqueID())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void g(Vec3D vec3D) {
        super.g(vec3D);
        if ((petType == null) || (user == null)) return;

        if ((passengers == null)
                || (!isOwnerRiding())) {
            this.G = (float) 0.5;
            this.aL = (float) 0.02;
            super.g(vec3D);
            return;
        }

        double strafe = vec3D.x;
        double vertical = vec3D.y;
        double forward = vec3D.z;

        EntityPlayer owner = ((CraftPlayer) user.getPlayer()).getHandle();
        if (jumping) {
            if (isOnGround(this)) {
                setMot(getMot().x, 0.5, getMot().z);
            } else {
                SimplePets.getPetConfigManager().getPetConfig(getPetType()).ifPresent(config -> {
                    if (config.canFly((Player) user.getPlayer())) {
                        setMot(getMot().x, 0.3, getMot().z);
                    }
                });
            }
        }

        this.yaw = owner.yaw;
        this.lastYaw = this.yaw;
        this.pitch = owner.pitch * 0.5F;
        this.setYawPitch(this.yaw, this.pitch);
        this.aC = (this.aA = this.yaw); // Translation: this.yHeadRot = (this.yBodyRot = this.yaw);
        strafe = owner.aR * 0.5F; // Translation: double motionSideways = passenger.xxa * walkSpeed;
        forward = owner.aT; // Translation: double motionForward = passenger.zza;
        if (forward <= 0.0F) {
            forward *= 0.25F;
        }
        //this.aL = this.aD = this.yaw;
        //this.H = 1.0F;

        Vec3D vec = new Vec3D(strafe, vertical, forward);
        //if (!(this instanceof IEntityHorsePet)) vec3D.vec3D.x *= 0.75;
        this.n((float) getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).getValue());
        super.g(vec);
        if (!world.isClientSide) {
            if (this instanceof IEntityHorsePet) {
                Location location = getBukkitEntity().getLocation();
                setPosition(location.getX(), location.getY(), location.getZ());
                PacketPlayOutEntityTeleport packet = new PacketPlayOutEntityTeleport(this);
                owner.playerConnection.sendPacket(packet);
                if (forward > 0.0F) {
                    float f = MathHelper.sin((float) (this.yaw * 0.017453292));
                    float f1 = MathHelper.cos((float) (this.yaw * 0.017453292));
                    setMot(getMot().add((-0.4 * f * 0.0), 0, (0.4 * f1 * 0.0))); // This would be 0 anyways?
                }

                this.aL = this.dM() * 0.1F;
                if (this.cj()) {
                    this.n((float) this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).getValue());
                    super.g(new Vec3D(strafe, vertical, forward));
                } else if (owner instanceof EntityHuman) {
                    this.setMot(new Vec3D(0, 0, 0));
                }

                this.aB = this.aC; // this.prevLimbSwingAmount = this.limbSwingAmount;
                double d0 = this.locX() - this.lastX;
                double d1 = this.locZ() - this.lastZ;
                float f5 = (float) (MathHelper.sqrt(d0 * d0 + d1 * d1) * 4.0);
                if (f5 > 1.0) {
                    f5 = (float) 1.0;
                }

                this.aC += (f5 - this.aC) * 0.4; // this.limbSwingAmount += (f5 - this.limbSwingAmount) * 0.4F;
                this.aD += this.aC; // this.limbSwing += this.limbSwingAmount;
            }
        }
    }

    @Override
    public void tick() {
        super.tick();

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
                    if (user != null) {
                        user.removePet(getPetType());
                    } else {
                        bukkitEntity.remove();
                    }
                }
                standTime++;
            }
        }

        // Handles all other Pet Tasks...
        if (user == null) {
            if (bukkitEntity != null)
                bukkitEntity.remove();
            return;
        }

        if (user.isPetVehicle(getPetType())) {
            if (floatDown) {
                if (!isOnGround(this)) {
                    setMot(getMot().x, getMot().y*0.4, getMot().z);
                }
            }
        }

        if (user.getPlayer() != null) {
            Player player = (Player) user.getPlayer();
            boolean shifting = player.isSneaking();
            if (PetCore.getInstance().getConfiguration().getBoolean("PetToggles.ShowPetNames", true))
                getEntity().setCustomNameVisible((!shifting));

            if (!canIgnoreVanish()) {
                boolean ownerVanish = ((CraftPlayer) player).getHandle().isInvisible();
                if (ownerVanish != this.isInvisible()) { // If Owner is invisible & pet is not
                    if (isGlowing && (!ownerVanish)) glowHandler(false);  // If the pet is glowing & owner is not vanished
                    this.setInvisible(!this.isInvisible());
                } else {
                    if (ownerVanish && canGlow)
                        if (((CraftPlayer) player).getHandle().isInvisible())
                            glowHandler(true);
                }
            }

            if (user.isPetHat(getPetType())) {
                this.lastYaw = this.yaw = player.getLocation().getYaw();
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

    // TODO: Handles Ambient sound
    /**
     * Handles the Ambient Sound playing
     * <p>
     * Search for: SoundEffect soundeffect = this.
     * Class: {@link EntityInsentient}
     */
    @Override
    public void F() {
        if (silent) return;
        SimplePets.getPetConfigManager().getPetConfig(getPetType()).ifPresent(config -> {
            SoundMaker sound = config.getSound();
            if (sound != null) sound.playSound(getEntity());
        });
    }


    // TODO: Handles glow effect
    private void glowHandler(boolean glow) {
        try {
            net.minecraft.server.v1_16_R3.Entity pet = this;
            if (this instanceof IEntityControllerPet) {
                Entity ent = ((IEntityControllerPet) this).getVisibleEntity().getEntity();
                pet = ((CraftEntity) ent).getHandle();
            }
            handleInvisible(glow, pet);
        } catch (IllegalAccessException ignored) {
        }
    }

    private void handleInvisible(boolean glow, net.minecraft.server.v1_16_R3.Entity pet) throws IllegalAccessException {
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

        ((CraftPlayer) getPetUser().getPlayer()).getHandle().playerConnection.sendPacket(metadataPacket);

    }


    // TODO: This literally fixed the shit with p2 and i'm so fucking mad
    public CraftEntity getBukkitEntity() {
        return new CraftLivingEntity(world.getServer(), this) {
            @Override
            public EntityType getType() {
                return petType.getEntityType();
            }
        };
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


    // TODO: These methods prevent pets from being saved in the worlds
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
    public void load(NBTTagCompound nbttagcompound) {
    }

    @Override
    public boolean canPortal() {
        return false; // Prevents pets from teleporting from a portal
    }
}