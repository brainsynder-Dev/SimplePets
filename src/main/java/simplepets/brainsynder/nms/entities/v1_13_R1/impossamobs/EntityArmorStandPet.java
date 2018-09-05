package simplepets.brainsynder.nms.entities.v1_13_R1.impossamobs;

import net.minecraft.server.v1_13_R1.*;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_13_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_13_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_13_R1.inventory.CraftItemStack;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.EulerAngle;
import simple.brainsynder.api.ItemMaker;
import simple.brainsynder.api.SkullMaker;
import simple.brainsynder.api.WebAPI;
import simple.brainsynder.nbt.StorageTagCompound;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.entity.ambient.IEntityArmorStandPet;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.entities.v1_13_R1.list.EntityControllerPet;
import simplepets.brainsynder.player.PetOwner;
import simplepets.brainsynder.reflection.FieldAccessor;
import simplepets.brainsynder.utils.AnimationCycle;
import simplepets.brainsynder.utils.AnimationManager;
import simplepets.brainsynder.wrapper.EntityWrapper;

import java.util.UUID;

public class EntityArmorStandPet extends EntityArmorStand implements IEntityArmorStandPet {
    private boolean isSpecial = false;
    private EntityControllerPet pet;
    private Location previus;
    private boolean moving = false;
    private boolean store = true;
    private boolean minime = false;
    private AnimationCycle walking = null;
    private AnimationCycle arm_swing = null;
    private FieldAccessor<Boolean> fieldAccessor;

    public EntityArmorStandPet(World world) {
        super(world);
    }

    private EntityArmorStandPet(World world, EntityControllerPet pet) {
        super(world);
        this.pet = pet;
        fieldAccessor = FieldAccessor.getField(EntityLiving.class, "bg", Boolean.TYPE);
    }

    public static ArmorStand spawn(Location location, EntityControllerPet pet) {
        EntityArmorStandPet stand = new EntityArmorStandPet(((CraftWorld) location.getWorld()).getHandle(), pet);
        stand.setSpecial(true);
        stand.setSize(0.0F, 0.0F);
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
    public EntityWrapper getEntityType() {
        return pet.getEntityType();
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
    public void W() {
        super.W();
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
    public UUID getUniqueId() {
        return super.getUniqueID();
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
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("small")) setSmall(object.getBoolean("small"));
        if (object.hasKey("clone")) setOwner(object.getBoolean("clone"));
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
            SkullMaker maker = new SkullMaker();
            maker.setUrl(WebAPI.getData(WebAPI.Type.SKIN_URL, getOwner().getName()));
            getEntity().setHelmet(maker.create());
            getEntity().setChestplate(new ItemMaker(Material.DIAMOND_CHESTPLATE).create());
            getEntity().setLeggings(new ItemMaker(Material.IRON_LEGGINGS).create());
            getEntity().setBoots(new ItemMaker(Material.GOLDEN_BOOTS).create());
        }else{
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

    public boolean isSpecial() {return this.isSpecial;}

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
    public void a(float strafe, float vertical, float forward) {
        if (passengers == null) {
            this.Q = (float) 0.5;
            this.aU = (float) 0.02;
            super.a(strafe, vertical, forward);
        } else {
            if (this.pet == null) {
                this.Q = (float) 0.5;
                this.aU = (float) 0.02;
                super.a(strafe, vertical, forward);
                return;
            }
            if (!isOwnerRiding()) {
                this.Q = (float) 0.5;
                this.aU = (float) 0.02;
                super.a(strafe, vertical, forward);
                return;
            }
            EntityPlayer owner = ((CraftPlayer) getOwner()).getHandle();
            if (fieldAccessor != null) {
                if (fieldAccessor.hasField(owner)) {
                    if (fieldAccessor.get(owner)) {
                        if (isOnGround(this)) {
                            this.motY = 1;
                        } else {
                            if (pet.getPet().getPetType().canFly(pet.getOwner())) {
                                this.motY = 0.3;
                            }
                        }
                    }
                }
            }

            this.yaw = owner.yaw;
            this.lastYaw = this.yaw;
            this.pitch = (float) (owner.pitch * 0.5);
            this.setYawPitch(this.yaw, this.pitch);
            this.aR = this.aP = this.yaw;
            this.Q = (float) 1.0;
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
    private EulerAngle toBukkit (Vector3f vector3f) {
        return new EulerAngle(vector3f.getX(), vector3f.getY(), vector3f.getZ());
    }
    private Vector3f toNMS (EulerAngle angle) {
        return new Vector3f((float)angle.getX(), (float)angle.getY(), (float)angle.getZ());
    }
    public org.bukkit.inventory.ItemStack getItems(EnumItemSlot enumitemslot) {
        return toBukkit(getEquipment(enumitemslot));
    }
    private org.bukkit.inventory.ItemStack toBukkit (net.minecraft.server.v1_13_R1.ItemStack stack) {
        return CraftItemStack.asBukkitCopy(stack);
    }
    private net.minecraft.server.v1_13_R1.ItemStack toNMS (org.bukkit.inventory.ItemStack stack) {
        return CraftItemStack.asNMSCopy(stack);
    }

    public void setSlot(EnumItemSlot enumitemslot, org.bukkit.inventory.ItemStack itemstack) {
        setEquipment(enumitemslot, toNMS(itemstack));
    }

    private ItemStack checkItem(ItemStack item) {
        if (item == null) return new ItemStack(Material.AIR);
        if (item.getType() == Material.AIR) return new ItemStack(Material.AIR);
        return item;
    }
}
