package simplepets.brainsynder.nms.entity.list;

import lib.brainsynder.nbt.StorageTagCompound;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Shulker;
import net.minecraft.world.phys.Vec3;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;
import simplepets.brainsynder.api.entity.hostile.IEntityShulkerPet;
import simplepets.brainsynder.api.entity.misc.IEntityControllerPet;
import simplepets.brainsynder.api.other.ParticleHandler;
import simplepets.brainsynder.api.pet.CommandReason;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.plugin.SimplePets;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.api.wrappers.ColorWrapper;
import simplepets.brainsynder.nms.VersionTranslator;
import simplepets.brainsynder.nms.entity.special.EntityControllerPet;
import simplepets.brainsynder.nms.entity.special.EntityGhostStand;

import java.util.*;
import java.util.function.Function;

public class EntityShulkerPet extends Shulker implements IEntityShulkerPet {
    private Map<String, StorageTagCompound> additional;

    private EntityControllerPet pet;
    private EntityGhostStand ghostStand;

    private boolean visible = true;
    private boolean frozen = false;
    private boolean isGlowing = false;
    private ChatColor glowColor = ChatColor.WHITE;
    private PetUser user;

    private boolean rainbow = false; // Off by default
    private boolean closed = true; // Closed by default
    private int toggle = 0;

    public EntityShulkerPet(ServerLevel world) {
        super(EntityType.SHULKER, world);
    }

    public EntityShulkerPet(EntityControllerPet pet, EntityGhostStand ghostStand, PetUser user) {
        super(EntityType.SHULKER, VersionTranslator.getEntityLevel(pet));
        this.pet = pet;
        this.ghostStand = ghostStand;
        pet.setBabySafe(true);
        this.user = user;
        this.additional = new HashMap<>();
    }

    @Override
    public EntityType<?> getType() {
        return EntityType.SHULKER;
    }

    public static EntityShulkerPet spawn(Location location, EntityControllerPet pet, EntityGhostStand ghostStand) {
        EntityShulkerPet shulker = new EntityShulkerPet(pet, ghostStand, pet.getPetUser());
        shulker.setPos(location.getX(), location.getY(), location.getZ());
        shulker.setInvulnerable(true);
        shulker.setNoAi(true);
        shulker.persist = true;
        VersionTranslator.addEntity(VersionTranslator.getWorldHandle(location.getWorld()), shulker, CreatureSpawnEvent.SpawnReason.CUSTOM);
        pet.setIgnoreVanish(true);
        return shulker;
    }

    @Override
    public void togglePetHatTask(boolean value) {
        if (!value) {
            VersionTranslator.getBukkitEntity(ghostStand).addPassenger(VersionTranslator.getBukkitEntity(this));
        }
    }

    @Override
    public void travel(Vec3 vec3d) {
        pet.travel(vec3d);
    }

    @Override
    public org.bukkit.entity.EntityType getPetEntityType() {
        return org.bukkit.entity.EntityType.SHULKER;
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
    public boolean isPetVisible() {
        return visible;
    }

    @Override
    public void setPetVisible(boolean visible) {
        this.visible = visible;
        setInvisible(!visible);
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

    @Override
    public boolean isShulkerClosed() {
        return closed;
    }

    @Override
    public void setShulkerClosed(boolean var) {
        closed = var;

        int value = 100;
        if (var) value = 0;
        setRawPeekAmount(value);
    }

    @Override
    public boolean isRainbow() {
        return rainbow;
    }

    @Override
    public void setRainbow(boolean rainbow) {
        this.rainbow = rainbow;
    }

    @Override
    public void setColorWrapper(ColorWrapper color) {
        getEntityData().set(DATA_COLOR_ID, ((color == null) || (color == ColorWrapper.NONE)) ? 16 : (byte)color.getWoolData());
    }

    @Override
    public ColorWrapper getColorWrapper() {
        byte rawColor = getEntityData().get(DATA_COLOR_ID);
        if (rawColor == 16) return ColorWrapper.NONE;
        return ColorWrapper.getByWoolData(rawColor);
    }

    /**
     * Runs per-tick
     *
     * Search for: this.world.methodProfiler.a("entityBaseTick");
     * Class: Entity
     */
    @Override
    public void tick() {
        super.tick();
        Entity bukkitEntity = VersionTranslator.getBukkitEntity(this);
        if ((this.pet == null)
                || (VersionTranslator.getBukkitEntity(pet).isDead())
                || (!VersionTranslator.getBukkitEntity(pet).isValid())) {
            kill();
            return;
        }

        // Handles all other Pet Tasks...
        if (user == null
                || user.getPlayer() == null
                || !user.getPlayer().isOnline()
                || !user.hasPet(PetType.SHULKER)) {
            if (bukkitEntity != null)
                bukkitEntity.remove();
            return;
        }
        if ((!this.closed) && (getRawPeekAmount() < 100)) setRawPeekAmount(100);

        if (this.frozen && (getTicksFrozen() < 140)) setTicksFrozen(150);

        // Updates the size of the controller
        if (!pet.isBabySafe()) pet.setBabySafe(true);
        if (isInvisible()) {
            if (!isGlowing) glowHandler(getPetUser().getPlayer(), true);
        } else {
            if (isGlowing) glowHandler(getPetUser().getPlayer(), false);
        }

        if (rainbow) {
            if (toggle == 4) {
                setColorWrapper(ColorWrapper.getNext(getColorWrapper()));
                getPetUser().updateDataMenu();
                toggle = 0;
            }
            toggle++;
        }

        // Updates The Pets Name
        String name = pet.getEntity().getCustomName();
        if (name == null) return;
        if (name.isEmpty()) return;
        if (name.equals(getEntity().getCustomName())) return;
        getEntity().setCustomName(name);
    }

    private void glowHandler(Player player, boolean glow) {
        try {
            Entity entity = getEntity();
            if (this instanceof IEntityControllerPet) {
                entity = ((IEntityControllerPet) this).getVisibleEntity().getEntity();
            }
            isGlowing = glow;
        } catch (Exception ignored) {}
    }

    @Override
    public void teleportToOwner() {
        user.getUserLocation().ifPresent(location -> {
            setPos(location.getX(), location.getY(), location.getZ());
            pet.setPos(location.getX(), location.getY(), location.getZ());
            SimplePets.getPetUtilities().runPetCommands(CommandReason.TELEPORT, user, getPetType());
            SimplePets.getParticleHandler().sendParticle(ParticleHandler.Reason.TELEPORT, user.getPlayer(), location);
        });
    }

    @Override
    public void handleAdditionalStorage(String pluginKey, Function<StorageTagCompound, StorageTagCompound> compound) {
        additional.put(pluginKey, compound.apply(additional.getOrDefault(pluginKey, new StorageTagCompound())));
    }

    @Override
    public UUID getOwnerUUID() {
        return user.getOwnerUUID();
    }

    @Override
    public PetUser getPetUser() {
        return user;
    }

    @Override
    public PetType getPetType() {
        return PetType.SHULKER;
    }

    @Override
    public org.bukkit.entity.Shulker getEntity() {
        return (org.bukkit.entity.Shulker) VersionTranslator.getBukkitEntity(this);
    }

    @Override
    public boolean attachOwner() {
        ejectPassengers();
        var owner = user.getPlayer();
        if (owner != null) {
            SimplePets.getPetUtilities().runPetCommands(CommandReason.RIDE, user, getPetType());
            return VersionTranslator.getBukkitEntity(this).addPassenger(owner);
        }
        return false;
    }

    @Override
    public List<Entity> getEntities() {
        return IEntityShulkerPet.super.getEntities();
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = pet.asCompound();
        object.setEnum("glow-color", getGlowColor());
        if (!isPetVisible()) object.setBoolean("visible", !isPetVisible());

        if (!additional.isEmpty()) {
            StorageTagCompound additional = new StorageTagCompound();
            this.additional.forEach(additional::setTag);
            object.setTag("additional", additional);
        }

        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("glow-color")) setGlowColor(object.getEnum("glow-color", ChatColor.class, ChatColor.WHITE));
        if (object.hasKey("invisible")) setPetVisible(!object.getBoolean("invisible"));
        if (object.hasKey("visible")) setPetVisible(object.getBoolean("visible"));
        if (object.hasKey("additional")) {
            StorageTagCompound additional = object.getCompoundTag("additional");
            additional.getKeySet().forEach(pluginKey -> this.additional.put(pluginKey, additional.getCompoundTag(pluginKey)));
        }
        pet.applyCompound(object);
    }

    @Override
    public boolean isBig() {
        return IEntityShulkerPet.super.isBig();
    }

    @Override
    public boolean isPetSilent() {
        return IEntityShulkerPet.super.isPetSilent();
    }

    @Override
    public void setPetSilent(boolean silent) {
        IEntityShulkerPet.super.setPetSilent(silent);
    }

    @Override
    public Optional<String> getPetName() {
        return Optional.empty();
    }

    @Override
    public void setPetName(String name) {
        pet.setPetName(name);
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return VersionTranslator.getAddEntityPacket(this, EntityType.SHULKER, VersionTranslator.getPosition(this));
    }

    @Override
    public boolean isBurning() {
        return super.hasVisualFire;
    }

    @Override
    public void setBurning(boolean var) {
        super.hasVisualFire = var;
    }


    /**
     * These methods prevent pets from being saved in the worlds
     */
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

    protected boolean damageEntity0(DamageSource damagesource, float f) {
        return false;
    }

    // God damnit Spigot changing the method name...
    // See: https://tiny.bsdevelopment.org/spigot-changed-damage-method
    protected boolean actuallyHurt(DamageSource damagesource, float f) {
        return false;
    }

    @Override
    public boolean alwaysAccepts() {
        return super.alwaysAccepts();
    }
}