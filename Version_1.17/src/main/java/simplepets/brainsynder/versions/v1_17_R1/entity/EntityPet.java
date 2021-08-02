package simplepets.brainsynder.versions.v1_17_R1.entity;

import lib.brainsynder.nbt.StorageTagCompound;
import lib.brainsynder.reflection.Reflection;
import lib.brainsynder.sounds.SoundMaker;
import lib.brainsynder.utils.Colorize;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang.reflect.FieldUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.misc.IEntityControllerPet;
import simplepets.brainsynder.api.event.entity.EntityNameChangeEvent;
import simplepets.brainsynder.api.event.entity.PetMoveEvent;
import simplepets.brainsynder.api.event.entity.movment.PetJumpEvent;
import simplepets.brainsynder.api.event.entity.movment.PetRideEvent;
import simplepets.brainsynder.api.pet.IPetConfig;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.plugin.SimplePets;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.files.Config;
import simplepets.brainsynder.managers.ParticleManager;
import simplepets.brainsynder.versions.v1_17_R1.pathfinder.PathfinderGoalLookAtOwner;
import simplepets.brainsynder.versions.v1_17_R1.pathfinder.PathfinderWalkToPlayer;
import simplepets.brainsynder.versions.v1_17_R1.utils.EntityUtils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

public abstract class EntityPet extends Mob implements IEntityPet {

    private EntityType<? extends Mob> entityType;
    private EntityType<? extends Mob> originalEntityType;
    private PetUser user;
    private PetType petType;
    private Map<String, StorageTagCompound> additional;
    private String petName = null;


    private final double jumpHeight = 0.5D;
    private boolean isGlowing = false;
    private boolean frozen = false;
    private boolean silent = false;
    private boolean ignoreVanish = false;
    private int standTime = 0;
    private int blockX = 0;
    private int blockZ = 0;
    private int blockY = 0;

    // Some entities can be controlled by the client when the player is riding
    // them, such as pigs, horses and ravagers. This is determined by
    // Mob#canBeControlledByRider (see the overrides). To prevent the client
    // from taking control, we use an intermediate 'seat' entity. If this is
    // true, a 'seat' entity is used.
    protected boolean doIndirectAttach;

    // Theses fields are based off config options
    private double walkSpeed = 0.6000000238418579;
    private double rideSpeed = 0.4000000238418579;
    private boolean floatDown = false;
    private boolean pushable = false;
    private boolean canGlow = true;
    private boolean autoRemove = true;
    private boolean displayName = true;
    private boolean hideNameShifting = true;
    private int tickDelay = 10000;

    public EntityPet(EntityType<? extends Mob> entitytypes, Level world) {
        super(entitytypes, world);
        entityType = getEntityType(entitytypes);
        originalEntityType = entitytypes;
        getBukkitEntity().remove();
    }

    public EntityPet(EntityType<? extends Mob> entitytypes, PetType type, PetUser user) {
        super(entitytypes, ((CraftWorld) user.getPlayer().getLocation().getWorld()).getHandle());
        this.user = user;
        this.petType = type;
        entityType = getEntityType(entitytypes);
        originalEntityType = entitytypes;

        this.additional = new HashMap<>();

        maxUpStep = 1;
        this.collides = false;
        this.noPhysics = false;


        Config configuration = PetCore.getInstance().getConfiguration();
        pushable = configuration.getBoolean(Config.PUSH_PETS, false);
        canGlow = configuration.getBoolean("PetToggles.GlowWhenVanished", true);
        autoRemove = configuration.getBoolean("PetToggles.AutoRemove.Enabled", true);
        tickDelay = configuration.getInt("PetToggles.AutoRemove.TickDelay", 10000);
        canGlow = configuration.getBoolean("PetToggles.GlowWhenVanished", true);//
        hideNameShifting = configuration.getBoolean("PetToggles.HideNameOnShift", true);
        displayName = configuration.getBoolean("PetToggles.ShowPetNames", true);

        SimplePets.getPetConfigManager().getPetConfig(type).ifPresent(config -> {
            // TODO: fill in values
            this.walkSpeed = config.getWalkSpeed();
            this.rideSpeed = config.getRideSpeed();
            this.floatDown = config.canFloat();
        });

        // needs to be faster but less then 6
        getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.500000238418579);
    }

    public boolean isJumping() {
        return jumping;
    }

    @Override
    public void teleportToOwner() {
        user.getUserLocation().ifPresent(location -> {
            setPos(location.getX(), location.getY(), location.getZ());
            PetCore.getInstance().getParticleHandler().sendParticle(ParticleManager.Reason.TELEPORT, user.getPlayer(), location);
        });
    }

    @Override
    protected boolean damageEntity0(DamageSource damagesource, float f) {
        return false;
    }

    @Override
    protected void registerGoals() {
        goalSelector.addGoal(1, new FloatGoal(this));
        goalSelector.addGoal(2, new PathfinderWalkToPlayer(this, 3, 10));
        goalSelector.addGoal(3, new PathfinderGoalLookAtOwner(this, 3f, 0.2f));
        goalSelector.addGoal(3, new RandomLookAroundGoal(this));
    }

    @Override
    public boolean isBurning() {
        return hasVisualFire;
    }

    @Override
    public void setBurning(boolean var) {
        this.hasVisualFire = var;
    }

    @Override
    public boolean isFrozen() {
        return isFullyFrozen();
    }

    @Override
    public void setFrozen(boolean frozen) {
        this.frozen = frozen;
        setTicksFrozen(frozen ? 150 : 0);
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
        } else {
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
        object.setUniqueId("uuid", getUUID());
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

        object.setBoolean("frozen", isFrozen());
        object.setBoolean("burning", isBurning());
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("uuid")) setUUID(object.getUniqueId("uuid")); // Sets the Entities UUID
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

        if (object.hasKey("frozen")) setFrozen(object.getBoolean("frozen", false));
        if (object.hasKey("burning")) setBurning(object.getBoolean("burning", false));
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
    public boolean attachOwner() {
        ejectPassengers();
        var owner = user.getPlayer();
        if (owner != null) {
            if (!doIndirectAttach) {
                return getBukkitEntity().addPassenger(owner);
            } else {
                return SeatEntity.attach(((CraftPlayer) owner).getHandle(), this);
            }
        }
        return false;
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
    public org.bukkit.entity.EntityType getPetEntityType() {
        return petType.getEntityType();
    }


    /**
     * Handles the registration of DataWatchers
     * <p>
     * Search for: this.entityData.register
     * Class: EntityLiving
     */
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
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

    private boolean isOnGround(net.minecraft.world.entity.Entity entity) {
        org.bukkit.block.Block block = entity.getBukkitEntity().getLocation().subtract(0, 0.5, 0).getBlock();
        return block.getType().isSolid();
    }

    protected boolean isOwnerRiding() {
        if (passengers.size() == 0) return false;
        net.minecraft.world.entity.player.Player owner = ((CraftPlayer) getPetUser().getPlayer()).getHandle();
        return getSelfAndPassengers().anyMatch(e -> e.equals(owner));
    }

    @Override
    public void travel(Vec3 vec3d) {
        if ((petType == null) || (user == null)) return;

        if ((passengers == null)
                || (!isOwnerRiding())) {
            super.travel(vec3d);
            return;
        }

        ServerPlayer passenger = ((CraftPlayer) getPetUser().getPlayer()).getHandle();

        if (doIndirectAttach) {
            if (getFirstPassenger() instanceof SeatEntity seat) {
                // orient the seat entity correctly. Seems to fix the issue
                // where ridden horses are not oriented properly
                seat.setYRot(passenger.getYRot());
                seat.yRotO = this.getYRot();
                seat.setXRot(passenger.getXRot() * 0.5F);
                seat.setRot(this.getYRot(), this.getXRot());
                seat.yHeadRot = this.yBodyRot = this.getYRot();
            }
        }

        this.setYRot(passenger.getYRot());
        this.yRotO = this.getYRot();
        this.setXRot(passenger.getXRot() * 0.5F);
        this.setRot(this.getYRot(), this.getXRot());
        this.yHeadRot = this.yBodyRot = this.getYRot();

        double strafe = passenger.xxa * 0.5;
        double vertical = vec3d.y;
        double forward = passenger.zza;
        if (forward <= 0) {
            forward *= 0.25F;
        }

        PetMoveEvent moveEvent = new PetRideEvent(this);
        Bukkit.getServer().getPluginManager().callEvent(moveEvent);
        if (moveEvent.isCancelled()) return;

        double speed = getAttribute(Attributes.MOVEMENT_SPEED).getValue();

        Field jumpField = EntityUtils.getJumpingField();
        if ((jumpField != null) && (!passengers.isEmpty())) {
            SimplePets.getPetConfigManager().getPetConfig(getPetType()).ifPresent(config -> {
                try {
                    boolean flight = false;
                    double height = jumpHeight;

                    if (config.canFly(getPetUser().getPlayer())) {
                        flight = true;
                        height = 0.3;
                    }

                    PetJumpEvent jumpEvent = new PetJumpEvent(this, height);
                    Bukkit.getServer().getPluginManager().callEvent(jumpEvent);
                    if ((!jumpEvent.isCancelled()) && jumpField.getBoolean(passenger)) {
                        if (flight || this.onGround) {
                            setDeltaMovement(getDeltaMovement().x, jumpEvent.getJumpHeight(), getDeltaMovement().z);
                            this.hasImpulse = true;
                        }
                    }
                } catch (IllegalArgumentException | IllegalStateException | IllegalAccessException ignored) {}
            });
        }

        this.setSpeed((float) speed);
        super.travel(new Vec3(strafe, vertical, forward));
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
        if (user == null || user.getPlayer() == null || !user.getPlayer().isOnline()) {
            if (bukkitEntity != null)
                bukkitEntity.remove();
            return;
        }

        if (user.isPetVehicle(getPetType())) {
            if (floatDown) {
                if (!isOnGround(this)) {
                    setDeltaMovement(getDeltaMovement().x, getDeltaMovement().y * 0.4, getDeltaMovement().z);
                }
            }
        }

        if (this.frozen && (getTicksFrozen() < 140)) setTicksFrozen(150);

        if (user.getPlayer() != null) {
            Player player = user.getPlayer();
            boolean shifting = player.isSneaking();
            if (displayName && hideNameShifting)
                getEntity().setCustomNameVisible((!shifting));

            if (!canIgnoreVanish()) {
                boolean ownerVanish = ((CraftPlayer) player).getHandle().isInvisible();
                if (ownerVanish != this.isInvisible()) { // If Owner is invisible & pet is not
                    if (isGlowing && (!ownerVanish))
                        glowHandler(false);  // If the pet is glowing & owner is not vanished
                    this.setInvisible(!this.isInvisible());
                } else {
                    if (ownerVanish && canGlow)
                        if (((CraftPlayer) player).getHandle().isInvisible())
                            glowHandler(true);
                }
            }

            if (user.isPetHat(getPetType())) {
                setYRot(player.getLocation().getYaw());
                this.yRotO = getYRot();
            }

            double current = getAttribute(Attributes.MOVEMENT_SPEED).getValue();
            if (isOwnerRiding()) {
                if (current != rideSpeed)
                    getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(rideSpeed);
            } else {
                if (current != walkSpeed)
                    getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(walkSpeed);
            }
        }
    }

    // TODO: Handles Ambient sound
    /**
     * Handles the Ambient Sound playing
     * <p>
     * Search for: SoundEvent soundeffect = this.getAmbientSound
     * Class: {@link Mob}
     */
    @Override
    public void playAmbientSound() {
        if (silent) return;
        SimplePets.getPetConfigManager().getPetConfig(getPetType()).ifPresent(config -> {
            SoundMaker sound = config.getSound();
            if (sound != null) sound.playSound(getEntity());
        });
    }

    @Override
    public EntityType<?> getType() {
        return entityType;
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return new ClientboundAddEntityPacket(this, originalEntityType, 0, new BlockPos(getX(), getY(), getZ()));
    }

    private void glowHandler(boolean glow) {
        try {
            net.minecraft.world.entity.Entity pet = this;
            if (this instanceof IEntityControllerPet) {
                org.bukkit.entity.Entity ent = ((IEntityControllerPet) this).getVisibleEntity().getEntity();
                pet = ((CraftEntity) ent).getHandle();
            }
            handleInvisible(glow, pet);
        } catch (IllegalAccessException ignored) {}
    }

    private void handleInvisible (boolean glow, net.minecraft.world.entity.Entity pet) throws IllegalAccessException {
        SynchedEntityData toCloneDataWatcher = pet.getEntityData();
        SynchedEntityData newDataWatcher = new SynchedEntityData(pet);

        String fieldName = "f";
        Int2ObjectOpenHashMap<SynchedEntityData.DataItem<?>> currentHashMap;
        try {
            currentHashMap = (Int2ObjectOpenHashMap<SynchedEntityData.DataItem<?>>)FieldUtils.readDeclaredField(toCloneDataWatcher, fieldName, true);
        }catch (Exception f){
            return;
        }

        Int2ObjectOpenHashMap<SynchedEntityData.DataItem<?>> newHashMap = new Int2ObjectOpenHashMap<>();
        for (Integer integer : currentHashMap.keySet()) {
            newHashMap.put(integer, currentHashMap.get(integer).copy());
        }

        SynchedEntityData.DataItem item = newHashMap.get(0);
        byte initialBitMask = (Byte) item.getValue();

        // @link net.minecraft.world.entity.Entity#setGlowingTag(boolean)
        byte bitMaskIndex = (byte) 6;
        isGlowing = glow;
        if (glow) {
            item.setValue((byte) (initialBitMask | 1 << bitMaskIndex));
        } else {
            item.setValue((byte) (initialBitMask & ~(1 << bitMaskIndex)));
        }
        FieldUtils.writeDeclaredField(newDataWatcher, fieldName, newHashMap, true);


        ClientboundSetEntityDataPacket metadataPacket = new ClientboundSetEntityDataPacket(pet.getId(), newDataWatcher, true);
        ((CraftPlayer) getPetUser().getPlayer()).getHandle().connection.send(metadataPacket);
    }

    // TODO: This literally fixed the shit with p2 and i'm so fucking mad
    public CraftEntity getBukkitEntity() {
        return new CraftLivingEntity(level.getCraftServer(), this) {
            @Override
            public org.bukkit.entity.EntityType getType() {
                return petType.getEntityType();
            }
        };
    }

    @Override
    public void move(MoverType enummovetype, Vec3 vec3d) {
        PetMoveEvent moveEvent = new PetMoveEvent(this);
        Bukkit.getServer().getPluginManager().callEvent(moveEvent);
        if (moveEvent.isCancelled()) return;
        super.move(enummovetype, vec3d);
    }

    /**
     * Used to stop the pet from moving when its pushed
     * <p>
     * Search for: this.hasImpulse = true;
     * Class: Entity
     */
    @Override
    public void push(double x, double y, double z) {
        if (!pushable) return;
        super.push(x, y, z);
    }

    private EntityType<? extends Mob> getEntityType(EntityType<? extends Mob> originalType)  {
        try {
            Field field = EntityType.class.getDeclaredField("bm");
            field.setAccessible(true);
            EntityType.Builder<? extends Mob> builder = EntityType.Builder.of((EntityType.EntityFactory<? extends Mob>) field.get(originalType), MobCategory.AMBIENT);
            builder.sized(0.1f, 0.1f);
            return builder.build(petType.name().toLowerCase());
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
            return originalType;
        }
    }


    // TODO: These methods prevent pets from being saved in the worlds
    @Override
    public boolean saveAsPassenger(CompoundTag nbttagcompound) {// Calls e
        return false;
    }

    @Override
    public boolean save(CompoundTag nbttagcompound) {// Calls e
        return false;
    }

    @Override
    public void load(CompoundTag nbttagcompound) {
    }

    @Override
    public boolean isOnPortalCooldown() {
        return true; // Prevents pets from teleporting from a portal
    }
}
