package simplepets.brainsynder.nms.v1_16_R2.entities.impossamobs;

import com.mojang.authlib.GameProfile;
import lib.brainsynder.item.ItemBuilder;
import lib.brainsynder.nbt.StorageTagCompound;
import lib.brainsynder.utils.Base64Wrapper;
import net.minecraft.server.v1_16_R2.*;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_16_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R2.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_16_R2.inventory.CraftItemStack;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.util.EulerAngle;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.entity.ambient.IEntityArmorStandPet;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.v1_16_R2.entities.list.EntityControllerPet;
import simplepets.brainsynder.player.PetOwner;
import simplepets.brainsynder.reflection.FieldAccessor;
import simplepets.brainsynder.utils.AnimationCycle;
import simplepets.brainsynder.utils.AnimationManager;
import simplepets.brainsynder.wrapper.EntityWrapper;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class EntityArmorStandPet extends EntityArmorStand implements IEntityArmorStandPet {
    private boolean isSpecial = false;
    private EntityControllerPet pet;
    private Location previus;
    private Location walkTo = null;
    private boolean moving = false;
    private boolean store = true;
    private boolean minime = false;
    private AnimationCycle walking = null;
    private AnimationCycle arm_swing = null;
    protected FieldAccessor<Boolean> fieldAccessor;
    private boolean restricted;
    private final List<ItemStack> cachedItems = new ArrayList<>();

    public EntityArmorStandPet(EntityTypes<? extends EntityArmorStand> entitytypes, World world) {
        super(entitytypes, world);
    }

    private EntityArmorStandPet(EntityTypes<? extends EntityArmorStand> entitytypes, World world, EntityControllerPet pet) {
        super(entitytypes, world);
        this.pet = pet;
        pet.setIgnoreVanish(true);
        fieldAccessor = FieldAccessor.getField(EntityLiving.class, "jumping", Boolean.TYPE);
    }

    public static ArmorStand spawn(Location location, EntityControllerPet pet) {
        EntityArmorStandPet stand = new EntityArmorStandPet(EntityTypes.ARMOR_STAND, ((CraftWorld) location.getWorld()).getHandle(), pet);
        stand.setSpecial(true);
        WorldServer worldServer = ((CraftWorld) location.getWorld()).getHandle();
        stand.setPositionRotation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        worldServer.addEntity(stand, CreatureSpawnEvent.SpawnReason.CUSTOM);
        NBTTagCompound compound = new NBTTagCompound();
        compound.setBoolean("invulnerable", true);
        compound.setBoolean("PersistenceRequired", true);
        compound.setBoolean("NoBasePlate", true);
        stand.load(compound);
        return ((ArmorStand) stand.getBukkitEntity());
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
        return pet.getPetEntityType();
    }

    @Override
    public boolean damageEntity(DamageSource damagesource, float f) {
        if (isSpecial)
            return false;
        return super.damageEntity(damagesource, f);
    }

    public EnumInteractionResult a(EntityHuman human, Vec3D vec3d, EnumHand enumhand) {
        if (isSpecial)
            return this.onInteract((Player) human.getBukkitEntity()) ? EnumInteractionResult.SUCCESS : EnumInteractionResult.FAIL;
        return super.a(human, vec3d, enumhand);
    }

    public boolean onInteract(Player p) {
        if (pet != null) {
            if (p.getName().equals(pet.getOwner().getName())) {
                PetCore.get().getInvLoaders().PET_DATA.open(PetOwner.getPetOwner(getOwner()));
                return true;
            }
            return false;
        }
        return false;
    }

    /**
     * Runs per-tick
     *
     * Search for: this.world.methodProfiler.a("entityBaseTick");
     * Class: Entity
     */
    @Override
    public void entityBaseTick() {
        super.entityBaseTick();
        if (walking == null) walking = new AnimationCycle(AnimationManager.walk);
        if (arm_swing == null) arm_swing = new AnimationCycle(AnimationManager.arm_swing);

        // Handles Armor copying
        if (getOwner().isValid()) {
            if (!getOwner().isDead()) {
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
            this.lastYaw = this.yaw = getOwner().getLocation().getYaw();
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
    public Player getOwner() {
        return pet.getOwner();
    }

    @Override
    public IPet getPet() {
        return pet.getPet();
    }

    @Override
    public ArmorStand getEntity() {
        return (ArmorStand) getBukkitEntity();
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = pet.asCompound();
        object.setBoolean("small", isSmall());
        object.setBoolean("clone", isOwner());
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
            cachedItems.add(checkItem(CraftItemStack.asBukkitCopy(getItemInMainHand())));
            cachedItems.add(checkItem(CraftItemStack.asBukkitCopy(getItemInOffHand())));
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
        setSlot(EnumItemSlot.HEAD, item);
    }
    @Override
    public void setBodyItem(ItemStack item) {
        setSlot(EnumItemSlot.CHEST, item);
    }
    @Override
    public void setLegItem(ItemStack item) {
        setSlot(EnumItemSlot.LEGS, item);
    }
    @Override
    public void setFootItem(ItemStack item) {
        setSlot(EnumItemSlot.FEET, item);
    }
    @Override
    public void setLeftArmItem(ItemStack item) {
        setSlot(EnumItemSlot.OFFHAND, item);
    }
    @Override
    public void setRightArmItem(ItemStack item) {
        setSlot(EnumItemSlot.MAINHAND, item);
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
        return getItems(EnumItemSlot.HEAD);
    }
    @Override
    public ItemStack getBodyItem() {
        return getItems(EnumItemSlot.CHEST);
    }
    @Override
    public ItemStack getLegItem() {
        return getItems(EnumItemSlot.LEGS);
    }
    @Override
    public ItemStack getFootItem() {
        return getItems(EnumItemSlot.FEET);
    }
    @Override
    public ItemStack getLeftArmItem() {
        return getItems(EnumItemSlot.OFFHAND);
    }
    @Override
    public ItemStack getRightArmItem() {
        return getItems(EnumItemSlot.MAINHAND);
    }

    public void setSpecial(boolean isSpecial) {this.isSpecial = isSpecial; }

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

    @Override
    public void g(Vec3D vec3D) {
        if (passengers == null) {
            this.G = (float) 0.5;
            this.aL = (float) 0.02;
            super.g(vec3D);
        } else {
            if (this.pet == null) {
                this.G = (float) 0.5;
                this.aL = (float) 0.02;
                super.g(vec3D);
                return;
            }
            if (!isOwnerRiding()) {
                this.G = (float) 0.5;
                this.aL = (float) 0.02;
                super.g(vec3D);
                return;
            }
            EntityPlayer owner = ((CraftPlayer) getOwner()).getHandle();
            if (fieldAccessor != null) {
                if (fieldAccessor.hasField(owner)) {
                    if (fieldAccessor.get(owner)) {
                        if (isOnGround(this)) {
                            setMot(getMot().x, 0.5, getMot().z);
                        } else {
                            if (pet.getPet().getPetType().canFly(pet.getOwner())) {
                                setMot(getMot().x, 0.3, getMot().z);
                            }
                        }
                    }
                }
            }

            this.yaw = owner.yaw;
            this.lastYaw = this.yaw;
            this.pitch = (float) (owner.pitch * 0.5);
            this.setYawPitch(this.yaw, this.pitch);
            this.aA = this.yaw;
            this.aC = this.aA;
            float strafe = owner.aR * 0.5F;
            float forward = owner.aT;
            if (forward <= 0.0) {
                forward *= 0.25;
            }
            Vec3D vec = new Vec3D(strafe, vec3D.y, forward);
            this.n((float) getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).getValue());
            pet.move(vec, fieldAccessor);
        //    super.e(vec);
        }
    }

    private boolean isOnGround(Entity entity) {
        org.bukkit.block.Block block = entity.getBukkitEntity().getLocation().subtract(0, 0.5, 0).getBlock();
        return block.getType().isSolid();
    }

    @Override
    public void setHeadPose(EulerAngle angle) {
        getEntity().setHeadPose(angle);
    }
    @Override
    public void setBodyPose(EulerAngle angle) {
        getEntity().setBodyPose(angle);
    }
    @Override
    public void setLeftArmPose(EulerAngle angle) {
        getEntity().setLeftArmPose(angle);
    }
    @Override
    public void setRightArmPose(EulerAngle angle) {
        getEntity().setRightArmPose(angle);
    }
    @Override
    public void setLeftLegPose(EulerAngle angle) {
        getEntity().setLeftLegPose(angle);
    }
    @Override
    public void setRightLegPose(EulerAngle angle) {
        getEntity().setRightLegPose(angle);
    }



    @Override
    public EulerAngle getHeadPose() {
        return getEntity().getHeadPose();
    }

    @Override
    public EulerAngle getBodyPose() {
        return getEntity().getBodyPose();
    }

    @Override
    public EulerAngle getLeftArmPose() {
        return getEntity().getLeftArmPose();
    }

    @Override
    public EulerAngle getRightArmPose() {
        return getEntity().getRightArmPose();
    }

    @Override
    public EulerAngle getLeftLegPose() {
        return getEntity().getLeftLegPose();
    }

    @Override
    public EulerAngle getRightLegPose() {
        return getEntity().getRightLegPose();
    }


    // CONVERSIONS
    private EulerAngle toBukkit (net.minecraft.server.v1_16_R2.Vector3f vector3f) {
        return new EulerAngle(vector3f.getX(), vector3f.getY(), vector3f.getZ());
    }
    private net.minecraft.server.v1_16_R2.Vector3f toNMS (EulerAngle angle) {
        return new net.minecraft.server.v1_16_R2.Vector3f((float)angle.getX(), (float)angle.getY(), (float)angle.getZ());
    }
    public ItemStack getItems(net.minecraft.server.v1_16_R2.EnumItemSlot enumitemslot) {
        return toBukkit(super.getEquipment(enumitemslot));
    }
    private ItemStack toBukkit (net.minecraft.server.v1_16_R2.ItemStack stack) {
        return CraftItemStack.asBukkitCopy(stack);
    }
    private net.minecraft.server.v1_16_R2.ItemStack toNMS (ItemStack stack) {
        return CraftItemStack.asNMSCopy(stack);
    }

    public void setSlot(net.minecraft.server.v1_16_R2.EnumItemSlot enumitemslot, ItemStack itemstack) {
        super.setSlot(enumitemslot, toNMS(itemstack));
    }

    private ItemStack checkItem(ItemStack item) {
        if (item == null) return new ItemStack(Material.AIR);
        if (item.getType() == Material.AIR) return new ItemStack(Material.AIR);
        return item;
    }

    private String parseItem (ItemStack stack) {
        return Base64Wrapper.encodeString(PetCore.get().getUtilities().itemToString(stack));
    }

    private ItemStack parseString (String string) {
        return PetCore.get().getUtilities().stringToItem(Base64Wrapper.decodeString(string));
    }

    private void handleCloning() {
        org.bukkit.inventory.PlayerInventory inventory = getOwner().getInventory();
        ItemStack head = checkItem(inventory.getHelmet());
        ItemStack chest = checkItem(inventory.getChestplate());
        ItemStack legs = checkItem(inventory.getLeggings());
        ItemStack boots = checkItem(inventory.getBoots());
        ItemStack mainHand = checkItem(inventory.getItemInMainHand());
        ItemStack offHand = checkItem(inventory.getItemInOffHand());

        if (head.getType() == Material.AIR) {
            head = getSkull();
        }

        if (!getItems(EnumItemSlot.HEAD).isSimilar(head)) {
            setSlot(EnumItemSlot.HEAD, head);
        }

        if (chest.getType() == Material.AIR) {
            chest = new ItemBuilder(Material.DIAMOND_CHESTPLATE).build();
        }

        if (!getItems(EnumItemSlot.CHEST).isSimilar(chest)) {
            setSlot(EnumItemSlot.CHEST, chest);
        }

        // hey this one doesn't have brackets
        if (legs.getType() == Material.AIR) {
            legs = new ItemBuilder(Material.IRON_LEGGINGS).build();
        }

        if (!getItems(EnumItemSlot.LEGS).isSimilar(legs)) {
            setSlot(EnumItemSlot.LEGS, legs);
        }

        if (boots.getType() == Material.AIR) {
            boots = new ItemBuilder(Material.GOLDEN_BOOTS).build();
        }

        if (!getItems(EnumItemSlot.FEET).isSimilar(boots)) {
            setSlot(EnumItemSlot.FEET, boots);
        }

        if (mainHand.getType() != Material.AIR) {
            if (!getItems(EnumItemSlot.MAINHAND).isSimilar(mainHand)) {
                setSlot(EnumItemSlot.MAINHAND, mainHand);
            }
        } else {
            setSlot(EnumItemSlot.MAINHAND, new ItemStack(Material.AIR));
        }

        if (offHand.getType() != Material.AIR) {
            if (!getItems(EnumItemSlot.OFFHAND).isSimilar(offHand)) {
                setSlot(EnumItemSlot.OFFHAND, offHand);
            }
        } else {
            setSlot(EnumItemSlot.OFFHAND, new ItemStack(Material.AIR));
        }
    }

    // TEMPORARY METHOD
    // Since skull fetching doesn't seem to be working properly, I've stuck in my own method from HeadsPlus. -TM
    public ItemStack getSkull() {
        // Allows textures to be instantly set; they aren't usually set with the UUID
        GameProfile profile = new GameProfile(getOwner().getUniqueId(), getOwner().getName());
        ItemStack item = new ItemBuilder(Material.PLAYER_HEAD).build();
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        try {
            Field profileF = meta.getClass().getDeclaredField("profile");
            profileF.setAccessible(true);
            profileF.set(meta, profile);
            item.setItemMeta(meta);
        } catch (NoSuchFieldException | IllegalAccessException noSuchFieldException) {
            // Print the stacktrace but continue without the texture
            noSuchFieldException.printStackTrace();
        }
        return item;
    }
}
