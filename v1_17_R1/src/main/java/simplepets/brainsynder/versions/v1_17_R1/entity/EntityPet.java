package simplepets.brainsynder.versions.v1_17_R1.entity;

import lib.brainsynder.nbt.StorageTagCompound;
import lib.brainsynder.sounds.SoundMaker;
import lib.brainsynder.utils.Colorize;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.entity.IEntityPet;
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

public abstract class EntityPet extends Mob implements IEntityPet {
    private PetUser user;
    private PetType petType;
    private Map<String, StorageTagCompound> additional;
    private String petName = null;


    private final double walkSpeed = 0.6000000238418579;
    private final double rideSpeed = 0.4000000238418579;
    private final boolean floatDown = false;
    private final boolean canGlow = true;
    private final boolean isGlowing = false;
    private final boolean autoRemove = true;
    private boolean silent = false;
    private boolean ignoreVanish = false;
    private int standTime = 0;
    private int blockX = 0;
    private int blockZ = 0;
    private int blockY = 0;
    private final int tickDelay = 10000;

    public EntityPet(EntityType<? extends Mob> entitytypes, Level world) {
        super(entitytypes, world);
        getBukkitEntity().remove();
    }

    public EntityPet(EntityType<? extends Mob> entitytypes, PetType type, PetUser user) {
        super(entitytypes, ((CraftWorld) ((Player) user.getPlayer()).getLocation().getWorld()).getHandle());
        this.user = user;
        this.petType = type;

        this.additional = new HashMap<>();

        this.collides = false;
        this.noPhysics = false;


        // needs to be faster but less then 6
        getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.500000238418579);
    }

    public boolean isJumping () {
        return jumping;
    }

    @Override
    public void teleportToOwner() {
        user.getUserLocation().ifPresent(location -> {
            setPos(location.getX(), location.getY(), location.getZ());
            PetCore.getInstance().getParticleHandler().sendParticle(ParticleManager.Reason.TELEPORT, (Player) user.getPlayer(), location);
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
    public boolean isFrozen() {
        return isFullyFrozen();
    }

    @Override
    public void setFrozen(boolean frozen) {
        setTicksFrozen(frozen ? 140 : 0);
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
        for (net.minecraft.world.entity.Entity passenger : this.passengers) {
            if (passenger.getUUID().equals(owner.getUUID())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void travel(Vec3 vec3D) {
        super.travel(vec3D);
        if ((petType == null) || (user == null)) return;

        if ((passengers == null)
                || (!isOwnerRiding())) {
            super.travel(vec3D);
            return;
        }

        double strafe = vec3D.x;
        double vertical = vec3D.y;
        double forward = vec3D.z;

        net.minecraft.world.entity.player.Player owner = ((CraftPlayer) user.getPlayer()).getHandle();
        if (jumping) {
            if (isOnGround(this)) {
                setDeltaMovement(getDeltaMovement().x, 0.5, getDeltaMovement().z);
            } else {
                SimplePets.getPetConfigManager().getPetConfig(getPetType()).ifPresent(config -> {
                    if (config.canFly((Player) user.getPlayer())) {
                        setDeltaMovement(getDeltaMovement().x, 0.3, getDeltaMovement().z);
                    }
                });
            }
        }

        setYRot(owner.getYRot());
        this.yRotO = getYRot();
        setXRot(owner.getXRot() * 0.5F);
        this.setRot(this.getYRot(), this.getXRot());
        this.yHeadRot = (this.yBodyRot = this.getYRot()); // Translation: this.yHeadRot = (this.yBodyRot = this.yRot);
        strafe = owner.xxa * 0.5F; // Translation: double motionSideways = passenger.xxa * walkSpeed;
        forward = owner.zza; // Translation: double motionForward = passenger.zza;
        if (forward <= 0.0F) {
            forward *= 0.25F;
        }
        //this.aL = this.aD = this.yRot;
        //this.H = 1.0F;

        Vec3 vec = new Vec3(strafe, vertical, forward);
        //if (!(this instanceof IEntityHorsePet)) vec3D.vec3D.x *= 0.75;
        this.setSpeed((float) getAttribute(Attributes.MOVEMENT_SPEED).getValue());
        super.travel(vec);
//        if (!level.isClientSide) {
//            if (this instanceof IEntityHorsePet) {
//                Location location = getBukkitEntity().getLocation();
//                setPosition(location.getX(), location.getY(), location.getZ());
//                PacketPlayOutEntityTeleport packet = new PacketPlayOutEntityTeleport(this);
//                owner.playerConnection.sendPacket(packet);
//                if (forward > 0.0F) {
//                    float f = MathHelper.sin((float) (this.yRot * 0.017453292));
//                    float f1 = MathHelper.cos((float) (this.yRot * 0.017453292));
//                    setMot(getMot().add((-0.4 * f * 0.0), 0, (0.4 * f1 * 0.0))); // This would be 0 anyways?
//                }
//
//                this.aL = this.dM() * 0.1F;
//                if (this.cj()) {
//                    this.n((float) this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).getValue());
//                    super.g(new Vec3D(strafe, vertical, forward));
//                } else if (owner instanceof EntityHuman) {
//                    this.setMot(new Vec3D(0, 0, 0));
//                }
//
//                this.aB = this.aC; // this.prevLimbSwingAmount = this.limbSwingAmount;
//                double d0 = this.locX() - this.lastX;
//                double d1 = this.locZ() - this.lastZ;
//                float f5 = (float) (MathHelper.sqrt(d0 * d0 + d1 * d1) * 4.0);
//                if (f5 > 1.0) {
//                    f5 = (float) 1.0;
//                }
//
//                this.limbSwingAmount += (f5 - this.aC) * 0.4; // this.limbSwingAmount += (f5 - this.limbSwingAmount) * 0.4F;
//                this.aD += this.aC; // this.limbSwing += this.limbSwingAmount;
//            }
//        }
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
                    setDeltaMovement(getDeltaMovement().x, getDeltaMovement().y*0.4, getDeltaMovement().z);
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


    // TODO: Handles glow effect
    private void glowHandler(boolean glow) {
        // TODO: This code is broken a bit, needs updating
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


    /**
     * Used to stop the pet from moving when its pushed
     * <p>
     * Search for: this.hasImpulse = true;
     * Class: Entity
     */
    @Override
    public void push(double x, double y, double z) {
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