package simplepets.brainsynder.nms.v1_11_R1.entities.impossamobs;

import lib.brainsynder.nbt.StorageTagCompound;
import net.minecraft.server.v1_11_R1.*;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_11_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_11_R1.inventory.CraftItemStack;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.EulerAngle;
import lib.brainsynder.item.ItemBuilder;
import simple.brainsynder.api.WebAPI;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.entity.ambient.IEntityArmorStandPet;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.v1_11_R1.entities.list.EntityControllerPet;
import simplepets.brainsynder.player.PetOwner;
import simplepets.brainsynder.reflection.FieldAccessor;
import simplepets.brainsynder.utils.AnimationCycle;
import simplepets.brainsynder.utils.AnimationManager;
import simplepets.brainsynder.utils.Utilities;
import simplepets.brainsynder.wrapper.EntityWrapper;

public class EntityArmorStandPet extends EntityArmorStand implements IEntityArmorStandPet {
    private boolean isSpecial = false;
    private EntityControllerPet pet;
    private Location previus;
    private boolean moving = false;
    private boolean store = true;
    private boolean minime = false;
    private Location walkTo = null;
    private AnimationCycle walking = null;
    private AnimationCycle arm_swing = null;
    private FieldAccessor<Boolean> fieldAccessor;

    public EntityArmorStandPet(World world) {
        super(world);
    }

    public EntityArmorStandPet(World world, EntityControllerPet pet) {
        super(world);
        this.pet = pet;
        fieldAccessor = FieldAccessor.getField(EntityLiving.class, "bd", Boolean.TYPE);
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
    public Location getWalkToLocation() {
        return walkTo;
    }

    @Override
    public void setWalkToLocation(Location location) {
        walkTo = location;
    }

    public void setPassenger(int pos, org.bukkit.entity.Entity entity, org.bukkit.entity.Entity passenger) {
        ((CraftEntity) entity).getHandle().passengers.add(pos, ((CraftEntity) passenger).getHandle());
        PacketPlayOutMount packet = new PacketPlayOutMount(((CraftEntity) entity).getHandle());
        if (entity instanceof Player) {
            ((CraftPlayer) entity).getHandle().playerConnection.sendPacket(packet);
        } else {
            ((CraftPlayer) getOwner()).getHandle().playerConnection.sendPacket(packet);
        }
    }

    public void removePassenger(org.bukkit.entity.Entity entity) {
        ((CraftEntity) entity).getHandle().passengers.clear();
        PacketPlayOutMount packet = new PacketPlayOutMount(((CraftEntity) entity).getHandle());
        if (entity instanceof Player) {
            ((CraftPlayer) entity).getHandle().playerConnection.sendPacket(packet);
        } else {
            ((CraftPlayer) getOwner()).getHandle().playerConnection.sendPacket(packet);
        }
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

    private ItemStack checkItem(ItemStack item) {
        if (item == null) return new ItemStack(Material.AIR);
        if (item.getType() == Material.AIR) return new ItemStack(Material.AIR);
        return item;
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

    @Override
    public void n() {
        super.n();
        if (isSpecial) {
            if (walking == null)
                walking = new AnimationCycle(AnimationManager.walk);
            if (arm_swing == null)
                arm_swing = new AnimationCycle(AnimationManager.arm_swing);
            if (getOwner().isValid()) {
                if (!getOwner().isDead()) {
                    if (!minime) {
                        org.bukkit.inventory.PlayerInventory inventory = getOwner().getInventory();
                        if (!getEntity().getHelmet().isSimilar(checkItem(inventory.getHelmet())))
                            getEntity().setHelmet(checkItem(inventory.getHelmet()));
                        if (!getEntity().getChestplate().isSimilar(checkItem(inventory.getChestplate())))
                            getEntity().setChestplate(checkItem(inventory.getChestplate()));
                        if (!getEntity().getLeggings().isSimilar(checkItem(inventory.getLeggings())))
                            getEntity().setLeggings(checkItem(inventory.getLeggings()));
                        if (!getEntity().getBoots().isSimilar(checkItem(inventory.getBoots())))
                            getEntity().setBoots(checkItem(inventory.getBoots()));
                    }
                }
            }
            if (getEntity().isInsideVehicle()) {
                if (arm_swing.isRunning(this))
                    arm_swing.toggle(this, false);
                if (walking.isRunning(this))
                    walking.toggle(this, false);
                this.lastYaw = this.yaw = getOwner().getLocation().getYaw();
                getEntity().setRightLegPose(new EulerAngle(-1.553343034274955D, 0.13962634015954636D, 0.0D));
                getEntity().setLeftLegPose(new EulerAngle(-1.5009831567151235D, -0.12217304763960307D, 0.0D));
                return;
            }
            if (previus != null) {
                moving = (previus.getX() != getEntity().getLocation().getX())
                        || (previus.getZ() != getEntity().getLocation().getZ());
            }
            if (store) {
                previus = getEntity().getLocation();
            }
            store = !store;
            if (moving) {
                if (!walking.isRegistered(this)) walking.register(this, 1);
                if (!arm_swing.isRegistered(this)) arm_swing.register(this, 1);

                walking.toggle(this, true);
                arm_swing.toggle(this, true);
            } else {
                if (arm_swing.isRunning(this)) arm_swing.toggle(this, false);
                if (walking.isRunning(this)) walking.toggle(this, false);
                getEntity().setLeftLegPose(new EulerAngle(0.0D, 0.0D, 0.0D));
                getEntity().setRightLegPose(new EulerAngle(0.0D, 0.0D, 0.0D));
                getEntity().setLeftArmPose(new EulerAngle(0.0D, 0.0D, 0.0D));
                getEntity().setRightArmPose(new EulerAngle(0.0D, 0.0D, 0.0D));
            }
        }
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
        StorageTagCompound object = new StorageTagCompound();
        object.setBoolean("Small", isSmall());
        object.setBoolean("MiniMe", isOwner());
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("Small")) setSmall(object.getBoolean("Small"));
        if (object.hasKey("MiniMe")) setOwner(object.getBoolean("MiniMe"));
    }

    @Override
    public boolean isOwner() {
        return minime;
    }

    @Override
    public void setOwner(boolean flag) {
        minime = flag;
        if (flag) {
            ItemBuilder builder = ItemBuilder.getSkull(simple.brainsynder.utils.SkullType.PLAYER);
            getEntity().setChestplate(new ItemBuilder(Material.DIAMOND_CHESTPLATE).build());
            getEntity().setLeggings(new ItemBuilder(Material.IRON_LEGGINGS).build());
            getEntity().setBoots(new ItemBuilder(Utilities.findMaterial("GOLD_BOOTS")).build());
            WebAPI.findTexture(getOwner().getUniqueId().toString(), PetCore.get(), texture -> {
                builder.setTexture(texture);
                if (isOwner()) getEntity().setHelmet(builder.build());
            });
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
    public void g(float f, float f2) {
        if (passengers == null) {
            this.P = (float) 0.5;
            this.aR = (float) 0.02;
            super.g(f, f2);
        } else {
            if (this.pet == null) {
                this.P = (float) 0.5;
                this.aR = (float) 0.02;
                super.g(f, f2);
                return;
            }
            if (!isOwnerRiding()) {
                this.P = (float) 0.5;
                this.aR = (float) 0.02;
                super.g(f, f2);
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
            this.aP = this.aN = this.yaw;
            this.P = (float) 1.0;

        }
    }

    private boolean isOnGround(Entity entity) {
        org.bukkit.block.Block block = entity.getBukkitEntity().getLocation().subtract(0, 0.5, 0).getBlock();
        return block.getType().isSolid();
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
    private org.bukkit.inventory.ItemStack toBukkit (net.minecraft.server.v1_11_R1.ItemStack stack) {
        return CraftItemStack.asBukkitCopy(stack);
    }
    private net.minecraft.server.v1_11_R1.ItemStack toNMS (org.bukkit.inventory.ItemStack stack) {
        return CraftItemStack.asNMSCopy(stack);
    }

    public void setSlot(EnumItemSlot enumitemslot, org.bukkit.inventory.ItemStack itemstack) {
        setEquipment(enumitemslot, toNMS(itemstack));
    }
}
