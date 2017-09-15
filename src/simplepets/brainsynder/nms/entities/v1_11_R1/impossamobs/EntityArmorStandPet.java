package simplepets.brainsynder.nms.entities.v1_11_R1.impossamobs;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.server.v1_11_R1.*;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_11_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftPlayer;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.EulerAngle;
import simple.brainsynder.api.ItemMaker;
import simple.brainsynder.api.SkullMaker;
import simple.brainsynder.api.WebAPI;
import simple.brainsynder.nbt.StorageTagCompound;
import simplepets.brainsynder.menu.PetDataMenu;
import simplepets.brainsynder.nms.entities.type.IEntityArmorStandPet;
import simplepets.brainsynder.nms.entities.v1_11_R1.list.EntityControllerPet;
import simplepets.brainsynder.pet.IPet;
import simplepets.brainsynder.utils.AnimationCycle;
import simplepets.brainsynder.utils.AnimationManager;
import simplepets.brainsynder.utils.IStandMethod;
import simplepets.brainsynder.wrapper.EntityWrapper;

public class EntityArmorStandPet extends EntityArmorStand implements IEntityArmorStandPet {
    @Getter
    @Setter
    private boolean isSpecial = false;
    private EntityControllerPet pet;
    private Location previus;
    private boolean moving = false;
    private boolean store = true;
    private boolean minime = false;
    private AnimationCycle walking = null;
    private AnimationCycle arm_swing = null;

    public EntityArmorStandPet(World world) {
        super(world);
    }

    public EntityArmorStandPet(World world, EntityControllerPet pet) {
        super(world);
        this.pet = pet;
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
    public EntityWrapper getEntityType() {
        return pet.getEntityType();
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
                PetDataMenu data = new PetDataMenu(getPet());
                data.showTo(p);
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
                if (arm_swing.isRunning(getEntity()))
                    arm_swing.toggle(getEntity(), false);
                if (walking.isRunning(getEntity()))
                    walking.toggle(getEntity(), false);
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
                onStart(new IStandMethod() {
                    @Override
                    public void run() {
                        if (!walking.isRegistered(getEntity())) walking.register(getEntity(), 1);
                        if (!arm_swing.isRegistered(getEntity())) arm_swing.register(getEntity(), 1);

                        walking.toggle(getEntity(), true);
                        arm_swing.toggle(getEntity(), true);
                    }
                });
            } else {
                onStop(new IStandMethod() {
                    @Override
                    public void run() {
                        if (arm_swing.isRunning(getEntity())) arm_swing.toggle(getEntity(), false);
                        if (walking.isRunning(getEntity())) walking.toggle(getEntity(), false);
                    }
                });
                getEntity().setLeftLegPose(new EulerAngle(0.0D, 0.0D, 0.0D));
                getEntity().setRightLegPose(new EulerAngle(0.0D, 0.0D, 0.0D));
                getEntity().setLeftArmPose(new EulerAngle(0.0D, 0.0D, 0.0D));
                getEntity().setRightArmPose(new EulerAngle(0.0D, 0.0D, 0.0D));
            }
        }
    }

    private void onStop(IStandMethod method) {
        method.run();
    }

    private void onStart(IStandMethod method) {
        method.run();
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
            SkullMaker maker = new SkullMaker();
            maker.setUrl(WebAPI.getData(WebAPI.Type.SKIN_URL, getOwner().getName()));
            getEntity().setHelmet(maker.create());
            getEntity().setChestplate(new ItemMaker(Material.DIAMOND_CHESTPLATE).create());
            getEntity().setLeggings(new ItemMaker(Material.IRON_LEGGINGS).create());
            getEntity().setBoots(new ItemMaker(Material.GOLD_BOOTS).create());
        }
    }
}
