package simplepets.brainsynder.nms.entity;

import lib.brainsynder.json.JsonObject;
import lib.brainsynder.nbt.StorageTagCompound;
import lib.brainsynder.sounds.SoundMaker;
import lib.brainsynder.utils.Colorize;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.misc.IEntityControllerPet;
import simplepets.brainsynder.api.event.entity.EntityNameChangeEvent;
import simplepets.brainsynder.api.event.entity.PetMoveEvent;
import simplepets.brainsynder.api.other.ParticleHandler;
import simplepets.brainsynder.api.pet.CommandReason;
import simplepets.brainsynder.api.pet.IPetConfig;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.plugin.SimplePets;
import simplepets.brainsynder.api.plugin.config.ConfigOption;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.nms.VersionTranslator;
import simplepets.brainsynder.nms.pathfinder.LegacyPathfinderFollowPlayer;
import simplepets.brainsynder.nms.pathfinder.PathfinderFollowPlayer;
import simplepets.brainsynder.nms.pathfinder.PathfinderGoalLookAtOwner;
import simplepets.brainsynder.nms.utils.EntityUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

public abstract class EntityPet extends EntityBase implements IEntityPet {
    private Map<String, StorageTagCompound> additional;
    private String petName = null;
    private final EntityType<? extends Mob> rawEntityType;


    private final double jumpHeight = 0.5D;
    private boolean isGlowing = false;
    private boolean frozen = false;
    private boolean onFire = false;
    private boolean silent = false;
    private boolean visible = true;
    private ChatColor glowColor = ChatColor.WHITE;
    private boolean ignoreVanish = false;
    private int standStillTicks = 0;
    private int hoverTickCount = 0;
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
    protected double walkSpeed = 0.6000000238418579;
    protected double rideSpeed = 0.4000000238418579;
    protected double flySpeed = 0.10000000149011612;
    private boolean floatDown = false;
    private boolean glowVanishToggle = true;
    private boolean autoRemoveToggle = true;
    private boolean displayNameVisibility = true;
    private boolean hideNameShifting = true;
    private int autoRemoveTick = 10000;
    private final int hoverRemoveTick = 6000;

    private boolean verticalWorldConfines = false;
    private int maxHeight;
    private int minHeight;

    public EntityPet(EntityType<? extends Mob> entitytypes, Level world) {
        super(entitytypes, world);
        rawEntityType = EntityType.PIG;
    }

    public EntityPet(EntityType<? extends Mob> entitytypes, PetType type, PetUser user) {
        super(entitytypes, type, user);
        rawEntityType = entitytypes;
        this.additional = new HashMap<>();

        VersionTranslator.setMapUpStep(this, 1);
        this.collides = false;
        this.noPhysics = false;

        VersionTranslator.overrideAttributeMap(this);

        glowVanishToggle = ConfigOption.INSTANCE.PET_TOGGLES_GLOW_VANISH.getValue();
        autoRemoveToggle = ConfigOption.INSTANCE.AUTO_REMOVE_ENABLED.getValue();
        autoRemoveTick = ConfigOption.INSTANCE.AUTO_REMOVE_TICK.getValue();
        hideNameShifting = ConfigOption.INSTANCE.PET_TOGGLES_SHIFT_HIDDEN_NAMES.getValue();
        displayNameVisibility = ConfigOption.INSTANCE.PET_TOGGLES_SHOW_NAMES.getValue();

        SimplePets.getPetConfigManager().getPetConfig(type).ifPresent(config -> {
            this.walkSpeed = config.getWalkSpeed();
            this.rideSpeed = config.getRideSpeed();
            this.flySpeed = config.getFlySpeed();
            this.floatDown = config.canFloat();
        });

        VersionTranslator.setAttributes(this, walkSpeed, flySpeed);
        EntityUtils.fetchTeam(user.getPlayer()).addEntry(getUUID().toString());

        verticalWorldConfines = ConfigOption.INSTANCE.MISC_TOGGLES_WORLD_CONFINES_PET_LIMITS.getValue();
        maxHeight = getEntity().getWorld().getMaxHeight();
        minHeight = getEntity().getWorld().getMinHeight();
    }

    @Override
    public void fetchPetDebugInformation(JsonObject debugInfo) {
        ServerPlayer player = VersionTranslator.getEntityHandle(getPetUser().getPlayer());

        debugInfo.set("display-name", new JsonObject()
                .add("name", petName)
                .add("display-name-visibility (config)", displayNameVisibility)
                .add("hide-name-while-shifting (config)", hideNameShifting)
                .add("actually-visible", isCustomNameVisible())
        );
        debugInfo.set("data", new JsonObject()
                .add("jump-height", jumpHeight)
                .add("frozen", frozen)
                .add("on-fire", onFire)
                .add("silent", silent)
                .add("visible", visible)
                .add("auto-remove", new JsonObject()
                        .add("auto-remove-enabled", autoRemoveToggle)
                        .add("stand-still-ticks", standStillTicks)
                        .add("auto-remove-tick", autoRemoveTick)
                )
                .add("hover-remove", new JsonObject()
                        .add("hover-ticks", hoverTickCount)
                        .add("hover-ticks-target", hoverRemoveTick)
                )
        );

        int maxRange = ConfigOption.INSTANCE.PATHFINDING_MAX_DISTANCE.getValue();
        debugInfo.set("follow-player", new JsonObject()
                .add("distance", distanceToSqr(player))
                .add("max-follow-distance (config)", maxRange)
                .add("should-follow", (distanceToSqr(player) < (double) (maxRange * maxRange)) )
        );

        int teleportDistance = ConfigOption.INSTANCE.PATHFINDING_TELEPORT_DISTANCE.getValue();
        debugInfo.set("teleport-pet", new JsonObject()
                .add("distance", distanceTo(player))
                .add("teleport-distance (config)", teleportDistance)
                .add("should-teleport", (distanceToSqr(player) >= teleportDistance) )
                .add("should-force-teleport", (distanceTo(player) >= 80) )
        );

        JsonObject petData = new JsonObject();
        if (this instanceof IEntityControllerPet controllerPet) {
            controllerPet.getVisibleEntity().fetchPetDebugInformation(petData);
        }else{
            fetchPetData(petData);
        }
        debugInfo.set("pet-"+getPetType().getName()+"-data", petData);
    }

    public void fetchPetData(JsonObject data) {}

    public void setDisplayName(boolean displayName) {
        this.displayNameVisibility = displayName;
    }

    public boolean isJumping() {
        return jumping;
    }

    public double getJumpHeight() {
        return jumpHeight;
    }

    @Override
    public void teleportToOwner() {
        getPetUser().getUserLocation().ifPresent(location -> {
            setPos(location.getX(), location.getY(), location.getZ());
            SimplePets.getPetUtilities().runPetCommands(CommandReason.TELEPORT, getPetUser(), getPetType());
            SimplePets.getParticleHandler().sendParticle(ParticleHandler.Reason.TELEPORT, getPetUser().getPlayer(), location);
        });
    }

    @Override
    public void setGlowColor(ChatColor glowColor) {
        if (this.glowColor == glowColor) return; // No need for redundant setting
        this.glowColor = glowColor;
    }

    @Override
    public ChatColor getGlowColor() {
        return glowColor;
    }

    // 1.20.3 - Changed method(s) AGAIN
    protected boolean actuallyHurt(ServerLevel worldserver, DamageSource damagesource, float f, EntityDamageEvent event) {
        return false;
    }

    protected boolean damageEntity0(DamageSource damagesource, float f) {
        return false;
    }

    // God damnit Spigot changing the method name...
    // See: https://tiny.bsdevelopment.org/spigot-changed-damage-method
    protected boolean actuallyHurt(DamageSource damagesource, float f) {
        return false;
    }

    // Method signature changed again
    // https://hub.spigotmc.org/stash/projects/SPIGOT/repos/craftbukkit/commits/764a541c5b5fd872ec3cacfc3d51d88e8599d569#nms-patches%2Fnet%2Fminecraft%2Fworld%2Fentity%2FEntityLiving.patch?t=872
    protected boolean actuallyHurt(DamageSource damageSource, float f, EntityDamageEvent event) {
        return false;
    }

    @Override
    protected void registerGoals() {
        goalSelector.addGoal(1, new FloatGoal(this));
        if (getPetType() != PetType.SHULKER) {
            if (ConfigOption.INSTANCE.LEGACY_PATHFINDING_ENABLED.getValue()) {
                goalSelector.addGoal(2, new LegacyPathfinderFollowPlayer(this, 3, 10));
            } else {
                goalSelector.addGoal(2, new PathfinderFollowPlayer(this));
            }
        }
        goalSelector.addGoal(3, new PathfinderGoalLookAtOwner(this, 3f, 0.2f));
        goalSelector.addGoal(3, new RandomLookAroundGoal(this));
    }

    @Override
    public boolean isBurning() {
        return onFire;
    }

    @Override
    public void setBurning(boolean var) {
        this.onFire = var;
        setRemainingFireTicks(var ? 150 : 0);
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
            Optional<IPetConfig> config = SimplePets.getPetConfigManager().getPetConfig(getPetType());
            if (config.isPresent()) {
                name = config.get().getDisplayName();
            }
        }
        String newName = name.replace("%player%", getPetUser().getPlayer().getName());

        EntityNameChangeEvent event = new EntityNameChangeEvent(this, newName);
        Bukkit.getServer().getPluginManager().callEvent(event);

        petName = Colorize.translateBungeeHex(event.getPrefix())
                + SimplePets.getPetUtilities().translatePetName(event.getName())
                + Colorize.translateBungeeHex(event.getSuffix());
        VersionTranslator.getBukkitEntity(this).setCustomNameVisible(ConfigOption.INSTANCE.PET_TOGGLES_SHOW_NAMES.getValue());
        VersionTranslator.getBukkitEntity(this).setCustomName(petName);
    }

    @Override
    public Optional<String> getPetName() {
        if (petName != null) return Optional.of(petName);

        if (getPetUser().getPetName(getPetType()).isPresent())
            return getPetUser().getPetName(getPetType());
        return Optional.empty();
    }

    @Override
    public void handleAdditionalStorage(String pluginKey, Function<StorageTagCompound, StorageTagCompound> compound) {
        additional.put(pluginKey, compound.apply(additional.getOrDefault(pluginKey, new StorageTagCompound())));
    }

    @Override
    public UUID getOwnerUUID() {
        return getPetUser().getPlayer().getUniqueId();
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = new StorageTagCompound();
        object.setString("PetType", getPetType().getName());
        object.setFloat("health", getHealth());
        object.setString("ownerName", getPetUser().getOwnerName());
        getPetUser().getPetName(getPetType()).ifPresent(name -> {
            object.setString("name", name.replace('§', '&'));
        });
        object.setBoolean("silent", silent);
        if (!isPetVisible()) object.setBoolean("visible", !isPetVisible());

        if (!additional.isEmpty()) {
            StorageTagCompound additional = new StorageTagCompound();
            this.additional.forEach(additional::setTag);
            object.setTag("additional", additional);
        }

        object.setDouble("scale", VersionTranslator.getScaleSize(this));
        object.setBoolean("frozen", isFrozen());
        object.setBoolean("burning", isBurning());
        object.setEnum("glow-color", getGlowColor());
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("health")) {
            float health = object.getFloat("health", 20);
            if (health >= 2048) health = 2047; // Prevents crash caused by spigot
            if (health < 1) health = 1; // Prevent pets from instant death
            setHealth(health);
        }
        if (object.hasKey("name")) {
            String name = object.getString("name");
            if (name != null) name = name.replace("~", " ");
            setPetName(name);
        }

        if (object.hasKey("glow-color")) setGlowColor(object.getEnum("glow-color", ChatColor.class, ChatColor.WHITE));
        if (object.hasKey("silent")) silent = object.getBoolean("silent");
        if (object.hasKey("visible")) setPetVisible(object.getBoolean("visible"));

        if (object.hasKey("additional")) {
            StorageTagCompound additional = object.getCompoundTag("additional");
            additional.getKeySet().forEach(pluginKey -> this.additional.put(pluginKey, additional.getCompoundTag(pluginKey)));
        }

        if (object.hasKey("frozen")) setFrozen(object.getBoolean("frozen", false));
        if (object.hasKey("burning")) setBurning(object.getBoolean("burning", false));
        if (object.hasKey("pose")) {
            Pose pose = object.getEnum("pose", Pose.class);
            if (pose != null) setPose(pose);
        }

        if (object.hasKey("walkSpeed")) {
            walkSpeed = object.getDouble("walkSpeed");
            VersionTranslator.setAttributes(this, walkSpeed, -1);
        }
        if (object.hasKey("rideSpeed")) rideSpeed = object.getDouble("rideSpeed");
        if (object.hasKey("flySpeed")) {
            flySpeed = object.getDouble("flySpeed");
            VersionTranslator.setAttributes(this, -1, flySpeed);
        }
        if (object.hasKey("scale")) VersionTranslator.setScaleSize(this, object.getDouble("scale"));
    }

    @Override
    public boolean isPetVisible() {
        return visible;
    }

    @Override
    public void setPetVisible(boolean visible) {
        this.visible = visible;
        setInvisible(!visible);
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
        return VersionTranslator.getBukkitEntity(this);
    }

    @Override
    public boolean attachOwner() {
        ejectPassengers();
        var owner = getPetUser().getPlayer();
        if (owner != null) {
            SimplePets.getPetUtilities().runPetCommands(CommandReason.RIDE, getPetUser(), getPetType());
            if (!doIndirectAttach) {
                // This was a test to see if using the startRiding method would work instead...
                return VersionTranslator.getEntityHandle(owner).startRiding(this);
            } else {
                return SeatEntity.attach(VersionTranslator.getEntityHandle(owner), this);
            }
        }
        return false;
    }

    @Override
    public PetType getPetType() {
        return super.getPetType();
    }

    @Override
    public PetUser getPetUser() {
        return super.getUser();
    }

    @Override
    public org.bukkit.entity.EntityType getPetEntityType() {
        return getPetType().getEntityType();
    }

    public void setIgnoreVanish(boolean ignoreVanish) {
        this.ignoreVanish = ignoreVanish;
    }

    public boolean canIgnoreVanish() {
        return ignoreVanish;
    }

    private boolean isOnGround(net.minecraft.world.entity.Entity entity) {
        org.bukkit.block.Block block = VersionTranslator.getBukkitEntity(entity).getLocation().subtract(0, 0.5, 0).getBlock();
        return block.getType().isSolid() || block.isLiquid();
    }

    protected boolean isOwnerRiding() {
        if (passengers.size() == 0) return false;
        net.minecraft.world.entity.player.Player owner = VersionTranslator.getEntityHandle(getPetUser().getPlayer());
        return getSelfAndPassengers().anyMatch(e -> e.equals(owner));
    }

    @Override
    public void tick() {
        super.tick();

        Entity bukkitEntity = VersionTranslator.getBukkitEntity(this);
        // This section handles the Auto-removal of pets after (tickDelay) Ticks of being stationary...
        if (autoRemoveToggle && (bukkitEntity != null)) {
            Location location = bukkitEntity.getLocation();
            if ((blockX != location.getBlockX())
                    || (blockY != location.getBlockY())
                    || (blockZ != location.getBlockZ())) {
                blockX = location.getBlockX();
                blockY = location.getBlockY();
                blockZ = location.getBlockZ();
                if (standStillTicks != 0) standStillTicks = 0;
            } else {
                if (standStillTicks == autoRemoveTick) {
                    if (getPetUser() != null) {
                        getPetUser().removePet(getPetType());
                    } else {
                        bukkitEntity.remove();
                    }
                }
                standStillTicks++;
            }
        }

        // Handles all other Pet Tasks...
        if (getPetUser() == null || getPetUser().getPlayer() == null || !getPetUser().getPlayer().isOnline()) {
            if (bukkitEntity != null)
                bukkitEntity.remove();
            return;
        }

        if (verticalWorldConfines && ((getY() > maxHeight) || (minHeight > getY()))) {
            getPetUser().removePet(getPetType());
            return;
        }

        // Ensures that pets that hover for too long are either removed
        // by the player or automatically deleted if they are not associated with any player
        if (isOnGround()) {
            if (hoverTickCount != 0) hoverTickCount = 0;
        } else {
            if (hoverTickCount == hoverRemoveTick) {
                if (getPetUser() != null) {
                    getPetUser().removePet(getPetType());
                } else {
                    bukkitEntity.remove();
                }
            }
            hoverTickCount++;
        }

        if (getPetUser().isPetVehicle(getPetType())) {
            if (floatDown) {
                if (!isOnGround(this)) {
                    setDeltaMovement(getDeltaMovement().x, getDeltaMovement().y * 0.4, getDeltaMovement().z);
                }
            }
        }

        if (this.frozen && (getTicksFrozen() < 140)) setTicksFrozen(150);
        if (this.onFire && (getRemainingFireTicks() < 140)) setRemainingFireTicks(150);

        if (getPetUser().getPlayer() != null) {
            Player player = getPetUser().getPlayer();
            boolean shifting = player.isSneaking();
            if ((displayNameVisibility && hideNameShifting) && (getPetType() != PetType.SHULKER))
                getEntity().setCustomNameVisible((!shifting));

            // Checks if the pet can actually be toggled to match their owners
            // player visibility status
            boolean ownerVanish = (VersionTranslator.getEntityHandle(player).isInvisible()
                    // Added this check for SuperVanish and PremiumVanish since they recommend using this method to check
                    || SimplePets.getPetUtilities().isVanished(player)
            );

            if (ownerVanish && ConfigOption.INSTANCE.MISC_TOGGLES_REMOVED_VANISH.getValue()) {
                getPetUser().removePet(getPetType());
                return;
            }

            if ((!canIgnoreVanish()) && ConfigOption.INSTANCE.MISC_TOGGLES_PET_VANISHING.getValue()) {
                if (isPetVisible()) {
                    if (ownerVanish != this.isInvisible()) { // If Owner is invisible & pet is not
                        if (isGlowing && (!ownerVanish))
                            glowHandler(player, false);  // If the pet is glowing & owner is not vanished
                        this.setInvisible(!this.isInvisible());
                    } else {
                        if (ownerVanish && glowVanishToggle)
                            if (VersionTranslator.getEntityHandle(player).isInvisible())
                                glowHandler(player, true);
                    }
                }
            }

            if (getPetUser().isPetHat(getPetType())) {
                setYRot(player.getLocation().getYaw());
                this.yRotO = getYRot();
            }

            double current = VersionTranslator.getWalkSpeed(this);
            if (isOwnerRiding()) {
                if (current != rideSpeed)
                    VersionTranslator.setAttributes(this, rideSpeed, -1);
            } else {
                if (current != walkSpeed)
                    VersionTranslator.setAttributes(this, walkSpeed, -1);
            }
        }
    }

    /**
     * Handles the Ambient Sound playing
     * <p>
     * Search for: SoundEvent soundeffect = this.getAmbientSound
     * Class: {@link Mob}
     */
    @Override
    public void playAmbientSound() {
        if (silent || isInvisible()) return;
        SimplePets.getPetConfigManager().getPetConfig(getPetType()).ifPresent(config -> {
            SoundMaker sound = config.getSound();
            if (sound != null) sound.playSound(getEntity());
        });
    }

    @Override
    public void playSound(SoundEvent soundeffect, float f, float f1) {
        if (silent || isInvisible()) return;
        SimplePets.getPetConfigManager().getPetConfig(getPetType()).ifPresent(config -> {
            SoundMaker sound = config.getSound();
            if (sound != null) sound.playSound(getEntity());
        });
    }

    @Override
    public EntityType<?> getType() {
        return rawEntityType;
    }

    private void glowHandler(Player player, boolean glow) {
        try {
            Entity entity = getEntity();
            if (this instanceof IEntityControllerPet controllerPet) {
                entity = controllerPet.getVisibleEntity().getEntity();
            }
            isGlowing = glow;

            if (EntityUtils.getGlowingInstance() != null) {
                if (glow) {
                    EntityUtils.getGlowingInstance().setGlowing(entity, player, getGlowColor());
                } else {
                    EntityUtils.getGlowingInstance().unsetGlowing(entity, player);
                }
            }
        } catch (Exception ignored) {
        }
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
//    Blame Paper for making the method final preventing me from overriding it...
//    @Override
//    public void push(double x, double y, double z) {
//        if (!immovable) return;
//        super.push(x, y, z);
//    }

    @Override
    public boolean isOnPortalCooldown() {
        return true; // Prevents pets from teleporting from a portal
    }

    @Override
    protected void handlePortal() {
        // Prevents pets from teleporting from a portal
    }

    // Added in 1.20
    public boolean isOnGround() {
        org.bukkit.block.Block block = VersionTranslator.getBukkitEntity(this).getLocation().subtract(0, 0.5, 0).getBlock();
        return block.getType().isSolid() || block.isLiquid();
    }
}
