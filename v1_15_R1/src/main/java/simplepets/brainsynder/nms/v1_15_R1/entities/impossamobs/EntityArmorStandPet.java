package simplepets.brainsynder.nms.v1_15_R1.entities.impossamobs;

import lib.brainsynder.item.ItemBuilder;
import lib.brainsynder.nbt.StorageTagCompound;
import net.minecraft.server.v1_15_R1.*;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_15_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_15_R1.inventory.CraftItemStack;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.EulerAngle;
import simple.brainsynder.api.WebAPI;
import simple.brainsynder.utils.Base64Wrapper;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.entity.ambient.IEntityArmorStandPet;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.v1_15_R1.entities.list.EntityControllerPet;
import simplepets.brainsynder.player.PetOwner;
import simplepets.brainsynder.reflection.FieldAccessor;
import simplepets.brainsynder.utils.AnimationCycle;
import simplepets.brainsynder.utils.AnimationManager;
import simplepets.brainsynder.wrapper.EntityWrapper;

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
    private ItemStack head, body, legs, boots, left_arm, right_arm;

    public EntityArmorStandPet(EntityTypes<? extends EntityArmorStand> entitytypes, World world) {
        super(entitytypes, world);
    }

    private EntityArmorStandPet(EntityTypes<? extends EntityArmorStand> entitytypes, World world, EntityControllerPet pet) {
        super(entitytypes, world);
        this.pet = pet;
        pet.setIgnoreVanish(true);
        fieldAccessor = FieldAccessor.getField(EntityLiving.class, "jumping", Boolean.TYPE);
        head = new ItemStack(Material.AIR);
        body = new ItemStack(Material.AIR);
        legs = new ItemStack(Material.AIR);
        boots = new ItemStack(Material.AIR);
        left_arm = new ItemStack(Material.AIR);
        right_arm = new ItemStack(Material.AIR);
    }

    public static ArmorStand spawn(Location location, EntityControllerPet pet) {
        EntityArmorStandPet stand = new EntityArmorStandPet(EntityTypes.ARMOR_STAND, ((CraftWorld) location.getWorld()).getHandle(), pet);
        stand.setSpecial(true);
        WorldServer worldServer = ((CraftWorld) location.getWorld()).getHandle();
        stand.setPosition(location.getX(), location.getY(), location.getZ());
        worldServer.addEntity(stand, CreatureSpawnEvent.SpawnReason.CUSTOM);
        NBTTagCompound compound = new NBTTagCompound();
        compound.setBoolean("invulnerable", true);
        compound.setBoolean("PersistenceRequired", true);
        compound.setBoolean("NoBasePlate", true);
        stand.a(compound);
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
                if (!minime) {
                    org.bukkit.inventory.PlayerInventory inventory = getOwner().getInventory();
                    if (!getItems(EnumItemSlot.HEAD).isSimilar(checkItem(inventory.getHelmet())))
                        setSlot(EnumItemSlot.HEAD, checkItem(inventory.getHelmet()));
                    if (!getItems(EnumItemSlot.CHEST).isSimilar(checkItem(inventory.getChestplate())))
                        setSlot(EnumItemSlot.CHEST, checkItem(inventory.getChestplate()));
                    if (!getItems(EnumItemSlot.LEGS).isSimilar(checkItem(inventory.getLeggings())))
                        setSlot(EnumItemSlot.LEGS, checkItem(inventory.getLeggings()));
                    if (!getItems(EnumItemSlot.FEET).isSimilar(checkItem(inventory.getBoots())))
                        setSlot(EnumItemSlot.FEET, checkItem(inventory.getBoots()));
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
        if (head != null) items.setString("head", parseItem(head));
        if (body != null) items.setString("body", parseItem(body));
        if (legs != null) items.setString("legs", parseItem(legs));
        if (boots != null) items.setString("boots", parseItem(boots));
        if (left_arm != null) items.setString("left_arm", parseItem(left_arm));
        if (right_arm != null) items.setString("right_arm", parseItem(right_arm));
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
            ItemBuilder builder = new ItemBuilder(Material.PLAYER_HEAD);
            getEntity().setChestplate(new ItemBuilder(Material.DIAMOND_CHESTPLATE).build());
            getEntity().setLeggings(new ItemBuilder(Material.IRON_LEGGINGS).build());
            getEntity().setBoots(new ItemBuilder(Material.GOLDEN_BOOTS).build());
            WebAPI.findTexture(getOwner().getUniqueId().toString(), PetCore.get(), texture -> {
                builder.setTexture(texture);
                if (isOwner()) getEntity().setHelmet(builder.build());
            });
        }else{
            ItemStack air = new ItemStack(Material.AIR);
            getEntity().setHelmet(air);
            getEntity().setChestplate(air);
            getEntity().setLeggings(air);
            getEntity().setBoots(air);


            org.bukkit.inventory.PlayerInventory inventory = getOwner().getInventory();
            if (!getItems(EnumItemSlot.HEAD).isSimilar(checkItem(inventory.getHelmet())))
                setSlot(EnumItemSlot.HEAD, checkItem(inventory.getHelmet()));
            if (!getItems(EnumItemSlot.CHEST).isSimilar(checkItem(inventory.getChestplate())))
                setSlot(EnumItemSlot.CHEST, checkItem(inventory.getChestplate()));
            if (!getItems(EnumItemSlot.LEGS).isSimilar(checkItem(inventory.getLeggings())))
                setSlot(EnumItemSlot.LEGS, checkItem(inventory.getLeggings()));
            if (!getItems(EnumItemSlot.FEET).isSimilar(checkItem(inventory.getBoots())))
                setSlot(EnumItemSlot.FEET, checkItem(inventory.getBoots()));

        }
    }

    @Override
    public void setHeadItem(ItemStack item) {
        this.head = item;
        getEntity().getEquipment().setHelmet((item));
    }
    @Override
    public void setBodyItem(ItemStack item) {
        this.body = item;
        getEntity().getEquipment().setChestplate((item));
    }
    @Override
    public void setLegItem(ItemStack item) {
        this.legs = item;
        getEntity().getEquipment().setLeggings((item));
    }
    @Override
    public void setFootItem(ItemStack item) {
        this.boots = item;
        getEntity().getEquipment().setBoots((item));
    }
    @Override
    public void setLeftArmItem(ItemStack item) {
        this.left_arm = item;
        getEntity().getEquipment().setItemInOffHand((item));
    }
    @Override
    public void setRightArmItem(ItemStack item) {
        this.right_arm = item;
        getEntity().getEquipment().setItemInMainHand((item));
    }

    @Override
    public ItemStack getHeadItem() {
        return head;
    }
    @Override
    public ItemStack getBodyItem() {
        return body;
    }
    @Override
    public ItemStack getLegItem() {
        return legs;
    }
    @Override
    public ItemStack getFootItem() {
        return boots;
    }
    @Override
    public ItemStack getLeftArmItem() {
        return left_arm;
    }
    @Override
    public ItemStack getRightArmItem() {
        return right_arm;
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
    public void e(Vec3D vec3D) {
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
            this.aU = this.yaw;
            this.H = (float) 1.0;
            float strafe = (float) (owner.aZ * 0.5);
            float forward = owner.bb;
            if (forward <= 0.0) {
                forward *= 0.25;
            }
            Vec3D vec = new Vec3D(strafe, vec3D.y, forward);
            this.o((float) getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).getValue());
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
    private EulerAngle toBukkit (net.minecraft.server.v1_15_R1.Vector3f vector3f) {
        return new EulerAngle(vector3f.getX(), vector3f.getY(), vector3f.getZ());
    }
    private net.minecraft.server.v1_15_R1.Vector3f toNMS (EulerAngle angle) {
        return new net.minecraft.server.v1_15_R1.Vector3f((float)angle.getX(), (float)angle.getY(), (float)angle.getZ());
    }
    public ItemStack getItems(net.minecraft.server.v1_15_R1.EnumItemSlot enumitemslot) {
        return toBukkit(getEquipment(enumitemslot));
    }
    private ItemStack toBukkit (net.minecraft.server.v1_15_R1.ItemStack stack) {
        return CraftItemStack.asBukkitCopy(stack);
    }
    private net.minecraft.server.v1_15_R1.ItemStack toNMS (ItemStack stack) {
        return CraftItemStack.asNMSCopy(stack);
    }

    public void setSlot(net.minecraft.server.v1_15_R1.EnumItemSlot enumitemslot, ItemStack itemstack) {
        setEquipment(enumitemslot, toNMS(itemstack));
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
}
