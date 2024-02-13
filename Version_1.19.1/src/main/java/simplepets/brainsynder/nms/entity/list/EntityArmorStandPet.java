package simplepets.brainsynder.nms.entity.list;

import com.mojang.authlib.GameProfile;
import lib.brainsynder.item.ItemBuilder;
import lib.brainsynder.nbt.StorageBase;
import lib.brainsynder.nbt.StorageTagCompound;
import lib.brainsynder.nbt.StorageTagString;
import lib.brainsynder.utils.Base64Wrapper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Rotations;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.phys.Vec3;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.util.EulerAngle;
import simplepets.brainsynder.api.entity.ambient.IEntityArmorStandPet;
import simplepets.brainsynder.api.entity.misc.IEntityControllerPet;
import simplepets.brainsynder.api.other.ParticleHandler;
import simplepets.brainsynder.api.pet.CommandReason;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.plugin.SimplePets;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.nms.VersionTranslator;
import simplepets.brainsynder.nms.entity.special.EntityControllerPet;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Function;

public class EntityArmorStandPet extends ArmorStand implements IEntityArmorStandPet {

    private boolean isSpecial = false;
    private Map<String, StorageTagCompound> additional;
    private EntityControllerPet pet;
    private Location previus;
    private boolean moving = false;
    private boolean visible = true;
    private boolean store = true;
    private boolean minime = false;
    private boolean frozen = false;
    private boolean isGlowing = false;
    private ChatColor glowColor = ChatColor.WHITE;
    private PetUser user;
    // private AnimationController walking = null; TODO: I disabled the animations
    private boolean restricted;
    private final List<ItemStack> cachedItems = new ArrayList<>();

    public EntityArmorStandPet(ServerLevel world) {
        super(EntityType.ARMOR_STAND, world);
    }

    public EntityArmorStandPet(EntityControllerPet pet, PetUser user) {
        super(EntityType.ARMOR_STAND, pet.level);
        this.pet = pet;
        this.user = user;
        this.additional = new HashMap<>();
        // walking = new AnimationController(this, AnimationManager.WALKING_ANIMATION); TODO: I disabled the animations
    }

    public static EntityArmorStandPet spawn(Location location, EntityControllerPet pet) {
        EntityArmorStandPet stand = new EntityArmorStandPet(pet, pet.getPetUser());
        stand.setPos(location.getX(), location.getY(), location.getZ());
        stand.setBasePlateVisibility(false);
        stand.setArmsVisibile(true);
        stand.setInvulnerable(true);
        stand.persist = true;
        stand.setSpecial(true);
        VersionTranslator.addEntity(VersionTranslator.getWorldHandle(location.getWorld()), stand, CreatureSpawnEvent.SpawnReason.CUSTOM);
        pet.setIgnoreVanish(true);
        return stand;
    }

    @Override
    public org.bukkit.entity.EntityType getPetEntityType() {
        return org.bukkit.entity.EntityType.ARMOR_STAND;
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
    public void move(MoverType enummovetype, Vec3 vec3d) {
        super.move(enummovetype, vec3d);
        // walking.cycleFrame(); TODO: I disabled the animations
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
                || !user.hasPet(PetType.ARMOR_STAND)) {
            if (bukkitEntity != null)
                bukkitEntity.remove();
            return;
        }

        // Handles Armor copying
        if (getPetUser().getPlayer().isValid()) {
            if (!getPetUser().getPlayer().isDead()) {
                // If cloning the user
                if (minime) {
                    handleCloning();
                }
            }
        }

        if (this.frozen && (getTicksFrozen() < 140)) setTicksFrozen(150);

        // Handles Limb movement
        if (getEntity().isInsideVehicle()) {
            // walking.reset(); TODO: I disabled the animations
            this.yBodyRot = this.yHeadRot = getPetUser().getPlayer().getLocation().getYaw();
            setRightLegAngle(new EulerAngle(-1.553343034274955D, 0.13962634015954636D, 0.0D));
            setLeftLegAngle(new EulerAngle(-1.5009831567151235D, -0.12217304763960307D, 0.0D));
            return;
        }

        if (previus != null) moving = (previus.getX() != getEntity().getLocation().getX()) || (previus.getZ() != getEntity().getLocation().getZ());
        if (store) previus = getEntity().getLocation();

        store = !store;
        if (!moving) {
            // walking.reset(); TODO: I disabled the animations

            setLeftLegAngle(new EulerAngle(0.0D, 0.0D, 0.0D));
            setRightLegAngle(new EulerAngle(0.0D, 0.0D, 0.0D));
            setLeftArmAngle(new EulerAngle(0.0D, 0.0D, 0.0D));
            setRightArmAngle(new EulerAngle(0.0D, 0.0D, 0.0D));
        }

        // Updates the size of the controller
        if (pet.isBaby() != isSmallStand()) pet.setBaby(isSmallStand());
        if (isInvisible()) {
            if (!isGlowing) glowHandler(getPetUser().getPlayer(), true);
        } else {
            if (isGlowing) glowHandler(getPetUser().getPlayer(), false);
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
        return PetType.ARMOR_STAND;
    }

    @Override
    public org.bukkit.entity.ArmorStand getEntity() {
        return (org.bukkit.entity.ArmorStand) VersionTranslator.getBukkitEntity(this);
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
    public StorageTagCompound asCompound() {
        StorageTagCompound object = pet.asCompound();
        object.setBoolean("small", isSmallStand());
        object.setBoolean("clone", isOwner());
        object.setBoolean("restricted", restricted);
        object.setEnum("glow-color", getGlowColor());
        if (!isPetVisible()) object.setBoolean("visible", !isPetVisible());

        StorageTagCompound items = new StorageTagCompound();
        if (getHeadItem() != null) items.setTag("head", parseItem(getHeadItem()));
        if (getBodyItem() != null) items.setTag("body", parseItem(getBodyItem()));
        if (getLegItem() != null) items.setTag("legs", parseItem(getLegItem()));
        if (getFootItem() != null) items.setTag("boots", parseItem(getFootItem()));
        if (getLeftArmItem() != null) items.setTag("left_arm", parseItem(getLeftArmItem()));
        if (getRightArmItem() != null) items.setTag("right_arm", parseItem(getRightArmItem()));
        if (!items.hasNoTags()) object.setTag("items", items);

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
        if (object.hasKey("restricted")) setRestricted(object.getBoolean("restricted"));
        if (object.hasKey("small")) setSmallStand(object.getBoolean("small"));
        if (object.hasKey("clone")) setOwner(object.getBoolean("clone"));
        if (object.hasKey("invisible")) setPetVisible(!object.getBoolean("invisible"));
        if (object.hasKey("visible")) setPetVisible(object.getBoolean("visible"));
        if (object.hasKey("items")) {
            StorageTagCompound items = object.getCompoundTag("items");
            if (items.hasKey("head")) setHeadItem(parseString(items.getTag("head")));
            if (items.hasKey("body")) setBodyItem(parseString(items.getTag("body")));
            if (items.hasKey("legs")) setLegItem(parseString(items.getTag("legs")));
            if (items.hasKey("boots")) setFootItem(parseString(items.getTag("boots")));
            if (items.hasKey("left_arm")) setLeftArmItem(parseString(items.getTag("left_arm")));
            if (items.hasKey("right_arm")) setRightArmItem(parseString(items.getTag("right_arm")));
        }

        if (object.hasKey("additional")) {
            StorageTagCompound additional = object.getCompoundTag("additional");
            additional.getKeySet().forEach(pluginKey -> this.additional.put(pluginKey, additional.getCompoundTag(pluginKey)));
        }
        pet.applyCompound(object);
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
    public boolean isSmallStand() {
        return super.isSmall();
    }

    @Override
    public void setSmallStand(boolean flag) {
        super.setSmall(flag);
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return VersionTranslator.getAddEntityPacket(this, EntityType.ARMOR_STAND, new BlockPos(getX(), getY(), getZ()));
    }

    @Override
    public boolean isOwner() {
        return minime;
    }

    @Override
    public void setOwner(boolean flag) {
        minime = flag;
        if (flag) {
            cachedItems.add(checkItem(getHeadItem()));
            cachedItems.add(checkItem(getBodyItem()));
            cachedItems.add(checkItem(getLegItem()));
            cachedItems.add(checkItem(getFootItem()));
            cachedItems.add(checkItem(getRightArmItem()));
            cachedItems.add(checkItem(getLeftArmItem()));
            handleCloning();
        } else if (cachedItems.size() > 5) {
            setHeadItem(cachedItems.get(0));
            setBodyItem(cachedItems.get(1));
            setLegItem(cachedItems.get(2));
            setFootItem(cachedItems.get(3));
            setRightArmItem(cachedItems.get(4));
            setLeftArmItem(cachedItems.get(5));
            cachedItems.clear();
        }
    }

    @Override
    public void setHeadItem(ItemStack item) {
        setSlot(EquipmentSlot.HEAD, item);
    }
    @Override
    public void setBodyItem(ItemStack item) {
        setSlot(EquipmentSlot.CHEST, item);
    }
    @Override
    public void setLegItem(ItemStack item) {
        setSlot(EquipmentSlot.LEGS, item);
    }
    @Override
    public void setFootItem(ItemStack item) {
        setSlot(EquipmentSlot.FEET, item);
    }
    @Override
    public void setLeftArmItem(ItemStack item) {
        setSlot(EquipmentSlot.OFFHAND, item);
    }
    @Override
    public void setRightArmItem(ItemStack item) {
        setSlot(EquipmentSlot.MAINHAND, item);
    }

    @Override
    public boolean isRestricted() {
        return restricted;
    }

    @Override
    public void setRestricted(boolean flag) {
        restricted = flag;
    }

    @Override
    public ItemStack getHeadItem() {
        return getItems(EquipmentSlot.HEAD);
    }
    @Override
    public ItemStack getBodyItem() {
        return getItems(EquipmentSlot.CHEST);
    }
    @Override
    public ItemStack getLegItem() {
        return getItems(EquipmentSlot.LEGS);
    }
    @Override
    public ItemStack getFootItem() {
        return getItems(EquipmentSlot.FEET);
    }
    @Override
    public ItemStack getLeftArmItem() {
        return getItems(EquipmentSlot.OFFHAND);
    }
    @Override
    public ItemStack getRightArmItem() {
        return getItems(EquipmentSlot.MAINHAND);
    }

    public void setSpecial(boolean isSpecial) {this.isSpecial = isSpecial; }

    @Override
    public void setHeadAngle(EulerAngle angle) {
        ((org.bukkit.entity.ArmorStand)VersionTranslator.getBukkitEntity(this)).setHeadPose(angle);
    }

    @Override
    public void setBodyAngle(EulerAngle angle) {
        ((org.bukkit.entity.ArmorStand)VersionTranslator.getBukkitEntity(this)).setBodyPose(angle);
    }

    @Override
    public void setLeftArmAngle(EulerAngle angle) {
        ((org.bukkit.entity.ArmorStand)VersionTranslator.getBukkitEntity(this)).setLeftArmPose(angle);
    }

    @Override
    public void setRightArmAngle(EulerAngle angle) {
        ((org.bukkit.entity.ArmorStand)VersionTranslator.getBukkitEntity(this)).setRightArmPose(angle);
    }

    @Override
    public void setLeftLegAngle(EulerAngle angle) {
        ((org.bukkit.entity.ArmorStand)VersionTranslator.getBukkitEntity(this)).setLeftLegPose(angle);
    }

    @Override
    public void setRightLegAngle(EulerAngle angle) {
        ((org.bukkit.entity.ArmorStand)VersionTranslator.getBukkitEntity(this)).setRightLegPose(angle);
    }

    @Override
    public EulerAngle getHeadAngle() {
        return ((org.bukkit.entity.ArmorStand)VersionTranslator.getBukkitEntity(this)).getHeadPose();
    }

    @Override
    public EulerAngle getBodyAngle() {
        return ((org.bukkit.entity.ArmorStand)VersionTranslator.getBukkitEntity(this)).getBodyPose();
    }

    @Override
    public EulerAngle getLeftArmAngle() {
        return ((org.bukkit.entity.ArmorStand)VersionTranslator.getBukkitEntity(this)).getLeftArmPose();
    }

    @Override
    public EulerAngle getRightArmAngle() {
        return ((org.bukkit.entity.ArmorStand)VersionTranslator.getBukkitEntity(this)).getRightArmPose();
    }

    @Override
    public EulerAngle getLeftLegAngle() {
        return ((org.bukkit.entity.ArmorStand)VersionTranslator.getBukkitEntity(this)).getLeftLegPose();
    }

    @Override
    public EulerAngle getRightLegAngle() {
        return ((org.bukkit.entity.ArmorStand)VersionTranslator.getBukkitEntity(this)).getRightLegPose();
    }

    @Override
    public void setBasePlateVisibility(boolean flag) {
        ((org.bukkit.entity.ArmorStand)VersionTranslator.getBukkitEntity(this)).setBasePlate(flag);
    }

    @Override
    public boolean hasBasePlateVisibility() {
        return ((org.bukkit.entity.ArmorStand)VersionTranslator.getBukkitEntity(this)).hasBasePlate();
    }

    @Override
    public boolean hasArmsVisibile() {
        return ((org.bukkit.entity.ArmorStand)VersionTranslator.getBukkitEntity(this)).hasArms();
    }

    @Override
    public void setArmsVisibile(boolean flag) {
        ((org.bukkit.entity.ArmorStand)VersionTranslator.getBukkitEntity(this)).setArms(flag);
    }

    // CONVERSIONS
    private EulerAngle toBukkit(Rotations vector3f) {
        return new EulerAngle(vector3f.getX(), vector3f.getY(), vector3f.getZ());
    }
    private Rotations toNMS(EulerAngle angle) {
        return new Rotations((float)angle.getX(), (float)angle.getY(), (float)angle.getZ());
    }

    public ItemStack getItems(EquipmentSlot enumitemslot) {
        return toBukkit(super.getItemBySlot(enumitemslot));
    }
    private ItemStack toBukkit (net.minecraft.world.item.ItemStack stack) {
        return VersionTranslator.toBukkit(stack);
    }
    private net.minecraft.world.item.ItemStack toNMS(ItemStack stack) {
        return VersionTranslator.toNMSStack(stack);
    }

    public void setSlot(EquipmentSlot enumitemslot, ItemStack itemstack) {
        VersionTranslator.setItemSlot(this, enumitemslot, toNMS(itemstack), false);
    }

    private ItemStack checkItem(ItemStack item) {
        if (item == null) return new ItemStack(Material.AIR);
        if (item.getType() == Material.AIR) return new ItemStack(Material.AIR);
        return item;
    }

    private StorageTagCompound parseItem (ItemStack stack) {
        return VersionTranslator.fromItemStack(stack);
    }

    private ItemStack parseString (StorageBase base) {
        if (base instanceof StorageTagCompound) {
            return VersionTranslator.toItemStack((StorageTagCompound) base);
        }else if (base instanceof StorageTagString){
            String string = ((StorageTagString) base).getString();
            if (!Base64Wrapper.isEncoded(string)) {
                throw new UnsupportedOperationException (String.format("'%s' is not a valid item format", string));
            }
            YamlConfiguration config = new YamlConfiguration();
            try {
                config.loadFromString(Base64Wrapper.decodeString(string));
            } catch (InvalidConfigurationException e1) {
                e1.printStackTrace();
            }
            return config.getItemStack("item");
        }
        throw new InputMismatchException("ArmorStand Pet item is not in the correct format, See: https://brainsynder.gitbook.io/simplepets/faq#how-can-i-make-it-so-the-armor-stand-has-items-when-it-spawns");
    }

    private void handleCloning() {
        org.bukkit.inventory.PlayerInventory inventory = getPetUser().getPlayer().getInventory();
        ItemStack head = checkItem(inventory.getHelmet());
        ItemStack chest = checkItem(inventory.getChestplate());
        ItemStack legs = checkItem(inventory.getLeggings());
        ItemStack boots = checkItem(inventory.getBoots());
        ItemStack mainHand = checkItem(inventory.getItemInMainHand());
        ItemStack offHand = checkItem(inventory.getItemInOffHand());

        if (head.getType() == Material.AIR) {
            head = getSkull();
        }

        if (!getItems(EquipmentSlot.HEAD).isSimilar(head)) {
            setSlot(EquipmentSlot.HEAD, head);
        }

        if (chest.getType() == Material.AIR) {
            chest = new ItemBuilder(Material.DIAMOND_CHESTPLATE).build();
        }

        if (!getItems(EquipmentSlot.CHEST).isSimilar(chest)) {
            setSlot(EquipmentSlot.CHEST, chest);
        }

        // hey this one doesn't have brackets
        if (legs.getType() == Material.AIR) {
            legs = new ItemBuilder(Material.IRON_LEGGINGS).build();
        }

        if (!getItems(EquipmentSlot.LEGS).isSimilar(legs)) {
            setSlot(EquipmentSlot.LEGS, legs);
        }

        if (boots.getType() == Material.AIR) {
            boots = new ItemBuilder(Material.GOLDEN_BOOTS).build();
        }

        if (!getItems(EquipmentSlot.FEET).isSimilar(boots)) {
            setSlot(EquipmentSlot.FEET, boots);
        }

        if (mainHand.getType() != Material.AIR) {
            if (!getItems(EquipmentSlot.MAINHAND).isSimilar(mainHand)) {
                setSlot(EquipmentSlot.MAINHAND, mainHand);
            }
        } else {
            setSlot(EquipmentSlot.MAINHAND, new ItemStack(Material.AIR));
        }

        if (offHand.getType() != Material.AIR) {
            if (!getItems(EquipmentSlot.OFFHAND).isSimilar(offHand)) {
                setSlot(EquipmentSlot.OFFHAND, offHand);
            }
        } else {
            setSlot(EquipmentSlot.OFFHAND, new ItemStack(Material.AIR));
        }
    }

    // TEMPORARY METHOD
    // Since skull fetching doesn't seem to be working properly, I've stuck in my own method from HeadsPlus. -TM
    public ItemStack getSkull() {
        // Allows textures to be instantly set; they aren't usually set with the UUID
        GameProfile profile = new GameProfile(getOwnerUUID(), getPetUser().getOwnerName());
        ItemStack item = new ItemBuilder(Material.PLAYER_HEAD).build();
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        try {
            Method profileF = meta.getClass().getDeclaredMethod("setProfile", GameProfile.class);
            profileF.setAccessible(true);
            profileF.invoke(meta, profile);
            item.setItemMeta(meta);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException noSuchFieldException) {
            // Print the stacktrace but continue without the texture
            noSuchFieldException.printStackTrace();
        }
        return item;
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
}