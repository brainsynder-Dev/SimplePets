package simplepets.brainsynder.versions.v1_17_R1.entity.list;

import com.mojang.authlib.GameProfile;
import lib.brainsynder.item.ItemBuilder;
import lib.brainsynder.nbt.StorageTagCompound;
import lib.brainsynder.reflection.Reflection;
import lib.brainsynder.utils.Base64Wrapper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.Rotations;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.game.ClientboundAddMobPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.decoration.ArmorStand;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_17_R1.inventory.CraftItemStack;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.util.EulerAngle;
import simplepets.brainsynder.api.entity.ambient.IEntityArmorStandPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.utils.animation.AnimationCycle;
import simplepets.brainsynder.utils.animation.AnimationManager;
import simplepets.brainsynder.versions.v1_17_R1.entity.EntityPet;
import simplepets.brainsynder.versions.v1_17_R1.entity.special.EntityControllerPet;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class EntityArmorStandPet extends ArmorStand implements IEntityArmorStandPet {

    private boolean isSpecial = false;
    private EntityControllerPet pet;
    private Location previus;
    private boolean moving = false;
    private boolean store = true;
    private boolean minime = false;
    private AnimationCycle walking = null;
    private AnimationCycle arm_swing = null;
    private boolean restricted;
    private final List<ItemStack> cachedItems = new ArrayList<>();

    private static final EntityDataAccessor<Byte> FLAGS;
    private static final EntityDataAccessor<Rotations> HEAD_POSE;
    private static final EntityDataAccessor<Rotations> BODY_POSE;
    private static final EntityDataAccessor<Rotations> LEFT_ARM_POSE;
    private static final EntityDataAccessor<Rotations> RIGHT_ARM_POSE;
    private static final EntityDataAccessor<Rotations> LEFT_LEG_POSE;
    private static final EntityDataAccessor<Rotations> RIGHT_LEG_POSE;

    static {
        EntityDataAccessor<Byte> FLAGS1;
        try {
            FLAGS1 = (EntityDataAccessor<Byte>) Reflection.getField(Mob.class, "b").get(null);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            FLAGS1 = SynchedEntityData.defineId(EntityArmorStandPet.class, EntityDataSerializers.BYTE);
        }
        FLAGS = FLAGS1;
        HEAD_POSE = SynchedEntityData.defineId(EntityArmorStandPet.class, EntityDataSerializers.ROTATIONS);
        BODY_POSE = SynchedEntityData.defineId(EntityArmorStandPet.class, EntityDataSerializers.ROTATIONS);
        LEFT_ARM_POSE = SynchedEntityData.defineId(EntityArmorStandPet.class, EntityDataSerializers.ROTATIONS);
        RIGHT_ARM_POSE = SynchedEntityData.defineId(EntityArmorStandPet.class, EntityDataSerializers.ROTATIONS);
        LEFT_LEG_POSE = SynchedEntityData.defineId(EntityArmorStandPet.class, EntityDataSerializers.ROTATIONS);
        RIGHT_LEG_POSE = SynchedEntityData.defineId(EntityArmorStandPet.class, EntityDataSerializers.ROTATIONS);
    }

    public EntityArmorStandPet(ServerLevel world) {
        super(EntityType.ZOMBIE, world);
    }

    public EntityArmorStandPet(EntityControllerPet pet, PetUser user) {
        super(EntityType.ZOMBIE, PetType.ARMOR_STAND, user);
        this.pet = pet;
    }

    protected void registerDatawatchers() {
        super.registerDatawatchers();
        //this.entityData.define(FLAGS, (byte) 0);
        this.entityData.define(HEAD_POSE, new Rotations(0f, 0f, 0f));
        this.entityData.define(BODY_POSE, new Rotations(0f, 0f, 0f));
        this.entityData.define(LEFT_ARM_POSE, new Rotations(-10f, 0f, -10f));
        this.entityData.define(RIGHT_ARM_POSE, new Rotations(-15f, 0f, 10f));
        this.entityData.define(LEFT_LEG_POSE, new Rotations(-1f, 0f, -1f));
        this.entityData.define(RIGHT_LEG_POSE, new Rotations(1f, 0f, 1f));
    }

    public static org.bukkit.entity.ArmorStand spawn(Location location, EntityControllerPet pet) {
        EntityArmorStandPet stand = new EntityArmorStandPet(pet, pet.getPetUser());
        stand.setSpecial(true);
        ServerLevel worldServer = ((CraftWorld) location.getWorld()).getHandle();
        stand.setPos(location.getX(), location.getY(), location.getZ());
        worldServer.addEntity(stand, CreatureSpawnEvent.SpawnReason.CUSTOM);
        CompoundTag compound = new CompoundTag();
        compound.putBoolean("invulnerable", true);
        compound.putBoolean("PersistenceRequired", true);
        compound.putBoolean("NoBasePlate", true);
        stand.load(compound);
        return ((org.bukkit.entity.ArmorStand) stand.getBukkitEntity());
    }

    @Override
    public org.bukkit.entity.EntityType getPetEntityType() {
        return org.bukkit.entity.EntityType.ARMOR_STAND;
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
        if (walking == null) walking = new AnimationCycle(AnimationManager.walk);
        if (arm_swing == null) arm_swing = new AnimationCycle(AnimationManager.arm_swing);

        // Handles Armor copying
        if (getPetUser().getPlayer().isValid()) {
            if (!getPetUser().getPlayer().isDead()) {
                // If cloning the user
                if (minime) {
                    handleCloning();
                }
            }
        }

        // Handles Limb movement
        if (getEntity().isInsideVehicle()) {
            if (arm_swing.isRunning(this)) arm_swing.toggle(this, false);
            if (walking.isRunning(this)) walking.toggle(this, false);
            this.yBodyRot = this.yHeadRot = getPetUser().getPlayer().getLocation().getYaw();
            setRightLegPose(new EulerAngle(-1.553343034274955D, 0.13962634015954636D, 0.0D));
            setLeftLegPose(new EulerAngle(-1.5009831567151235D, -0.12217304763960307D, 0.0D));
            return;
        }

        if (previus != null) moving = (previus.getX() != getEntity().getLocation().getX()) || (previus.getZ() != getEntity().getLocation().getZ());
        if (store) previus = getEntity().getLocation();

        store = !store;
        if (moving) {
            if (!walking.isRegistered(this)) walking.register(this, 1);
            if (!arm_swing.isRegistered(this)) arm_swing.register(this, 1);

            walking.toggle(this, true);
            arm_swing.toggle(this, true);
        } else {
            if (arm_swing.isRunning(this)) arm_swing.toggle(this, false);
            if (walking.isRunning(this)) walking.toggle(this, false);

            setLeftLegPose(new EulerAngle(0.0D, 0.0D, 0.0D));
            setRightLegPose(new EulerAngle(0.0D, 0.0D, 0.0D));
            setLeftArmPose(new EulerAngle(0.0D, 0.0D, 0.0D));
            setRightArmPose(new EulerAngle(0.0D, 0.0D, 0.0D));
        }

        // Updates the size of the controller
        if (pet.isBaby() != isSmall()) pet.setBaby(isSmall());

        // Updates The Pets Name
        String name = pet.getEntity().getCustomName();
        if (name == null) return;
        if (name.isEmpty()) return;
        if (name.equals(getEntity().getCustomName())) return;
        getEntity().setCustomName(name);
    }

    @Override
    public PetType getPetType() {
        return PetType.ARMOR_STAND;
    }

    @Override
    public org.bukkit.entity.ArmorStand getEntity() {
        return (org.bukkit.entity.ArmorStand) getBukkitEntity();
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = pet.asCompound();
        object.setBoolean("small", isSmall());
        object.setBoolean("clone", isOwner());
        object.setBoolean("restricted", restricted);
        if (isInvisible()) object.setBoolean("invisible", isInvisible());

        StorageTagCompound items = new StorageTagCompound();
        if (getHeadItem() != null) items.setString("head", parseItem(getHeadItem()));
        if (getBodyItem() != null) items.setString("body", parseItem(getBodyItem()));
        if (getLegItem() != null) items.setString("legs", parseItem(getLegItem()));
        if (getFootItem() != null) items.setString("boots", parseItem(getFootItem()));
        if (getLeftArmItem() != null) items.setString("left_arm", parseItem(getLeftArmItem()));
        if (getRightArmItem() != null) items.setString("right_arm", parseItem(getRightArmItem()));
        if (!items.hasNoTags()) object.setTag("items", items);

        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("restricted")) setRestricted(object.getBoolean("restricted"));
        if (object.hasKey("small")) setSmall(object.getBoolean("small"));
        if (object.hasKey("clone")) setOwner(object.getBoolean("clone"));
        if (object.hasKey("invisible")) setInvisible(object.getBoolean("invisible"));
        if (object.hasKey("items")) {
            StorageTagCompound items = object.getCompoundTag("items");
            if (items.hasKey("head")) setHeadItem(parseString(items.getString("head")));
            if (items.hasKey("body")) setBodyItem(parseString(items.getString("body")));
            if (items.hasKey("legs")) setLegItem(parseString(items.getString("legs")));
            if (items.hasKey("boots")) setFootItem(parseString(items.getString("boots")));
            if (items.hasKey("left_arm")) setLeftArmItem(parseString(items.getString("left_arm")));
            if (items.hasKey("right_arm")) setRightArmItem(parseString(items.getString("right_arm")));
        }
        pet.applyCompound(object);
    }

    @Override
    public boolean isSmall() {
        return false;
    }

    @Override
    public void setSmall(boolean flag) {

    }

    @Override
    public Packet<?> getAddEntityPacket() {
        try {
            ClientboundAddMobPacket packet = new ClientboundAddMobPacket(this);
            Field type = packet.getClass().getDeclaredField("c");
            type.setAccessible(true);
            type.set(packet, Registry.ENTITY_TYPE.getId(EntityType.ARMOR_STAND));
            return packet;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ClientboundAddEntityPacket(this, EntityType.ARMOR_STAND, 0, new BlockPos(getX(), getY(), getZ()));
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
    public void setHeadPose(EulerAngle angle) {
        this.entityData.set(HEAD_POSE, toNMS(angle));
    }

    @Override
    public void setBodyPose(EulerAngle angle) {
        this.entityData.set(BODY_POSE, toNMS(angle));
    }

    @Override
    public void setLeftArmPose(EulerAngle angle) {
        this.entityData.set(LEFT_ARM_POSE, toNMS(angle));
    }

    @Override
    public void setRightArmPose(EulerAngle angle) {
        this.entityData.set(RIGHT_ARM_POSE, toNMS(angle));
    }

    @Override
    public void setLeftLegPose(EulerAngle angle) {
        this.entityData.set(LEFT_LEG_POSE, toNMS(angle));
    }

    @Override
    public void setRightLegPose(EulerAngle angle) {
        this.entityData.set(RIGHT_LEG_POSE, toNMS(angle));
    }

    @Override
    public EulerAngle getHeadPose() {
        return toBukkit(this.entityData.get(HEAD_POSE));
    }

    @Override
    public EulerAngle getBodyPose() {
        return toBukkit(this.entityData.get(BODY_POSE));
    }

    @Override
    public EulerAngle getLeftArmPose() {
        return toBukkit(this.entityData.get(LEFT_ARM_POSE));
    }

    @Override
    public EulerAngle getRightArmPose() {
        return toBukkit(this.entityData.get(RIGHT_ARM_POSE));
    }

    @Override
    public EulerAngle getLeftLegPose() {
        return toBukkit(this.entityData.get(LEFT_LEG_POSE));
    }

    @Override
    public EulerAngle getRightLegPose() {
        return toBukkit(this.entityData.get(RIGHT_LEG_POSE));
    }

    @Override
    public void setBasePlate(boolean flag) {

    }

    @Override
    public boolean hasBasePlate() {
        return (this.entityData.get(FLAGS) & 8) == 0;
    }

    @Override
    public boolean hasArms() {
        return true;
    }

    @Override
    public void setArms(boolean flag) {

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
        return CraftItemStack.asBukkitCopy(stack);
    }
    private net.minecraft.world.item.ItemStack toNMS(ItemStack stack) {
        return CraftItemStack.asNMSCopy(stack);
    }

    public void setSlot(EquipmentSlot enumitemslot, ItemStack itemstack) {
        super.setSlot(enumitemslot, toNMS(itemstack), false);
    }

    private ItemStack checkItem(ItemStack item) {
        if (item == null) return new ItemStack(Material.AIR);
        if (item.getType() == Material.AIR) return new ItemStack(Material.AIR);
        return item;
    }

    private String parseItem (ItemStack stack) {
        YamlConfiguration config = new YamlConfiguration();
        config.set("item", stack);
        return Base64Wrapper.encodeString(config.saveToString());
    }

    private ItemStack parseString (String string) {
        YamlConfiguration config = new YamlConfiguration();
        try {
            config.loadFromString(Base64Wrapper.decodeString(string));
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
        }
        return config.getItemStack("item");
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
}
