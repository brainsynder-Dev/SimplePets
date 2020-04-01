package simplepets.brainsynder.nms.v1_14_R1.entities.impossamobs;

import lib.brainsynder.nbt.StorageTagCompound;
import net.minecraft.server.v1_14_R1.*;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_14_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.entity.Shulker;
import org.bukkit.event.entity.CreatureSpawnEvent;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.entity.hostile.IEntityShulkerPet;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.v1_14_R1.entities.list.EntityControllerPet;
import simplepets.brainsynder.player.PetOwner;
import simplepets.brainsynder.reflection.FieldAccessor;
import simplepets.brainsynder.wrapper.DyeColorWrapper;
import simplepets.brainsynder.wrapper.EntityWrapper;

public class EntityShulkerPet extends EntityShulker implements IEntityShulkerPet {
    private boolean isCustom = false;
    private boolean rainbow = false;
    private int toggle = 0;
    private Location walkTo = null;
    private DyeColorWrapper color = DyeColorWrapper.PURPLE;
    private EntityControllerPet pet;
    private FieldAccessor<Boolean> fieldAccessor;

    public EntityShulkerPet(EntityTypes<? extends EntityShulker> entitytypes, World world) {
        super(entitytypes, world);
    }

    public EntityShulkerPet(EntityTypes<? extends EntityShulker> entitytypes, World world, EntityControllerPet pet) {
        super(entitytypes, world);
        this.pet = pet;
        fieldAccessor = FieldAccessor.getField(EntityLiving.class, "jumping", Boolean.TYPE);
    }

    public static Shulker spawn(Location location, EntityControllerPet pet) {
        EntityShulkerPet shulker = new EntityShulkerPet(EntityTypes.SHULKER, ((CraftWorld) location.getWorld()).getHandle(), pet);
        shulker.setCustom(true);
        shulker.setNoAI(false);
        shulker.setSilent(true);
        WorldServer worldServer = ((CraftWorld) location.getWorld()).getHandle();
        shulker.setPosition(location.getX(), location.getY(), location.getZ());
        worldServer.addEntity(shulker, CreatureSpawnEvent.SpawnReason.CUSTOM);
        return ((Shulker) shulker.getBukkitEntity());
    }

    @Override
    public Location getWalkToLocation() {
        return walkTo;
    }

    @Override
    public void setWalkToLocation(Location location) {
        walkTo = location;
    }

    public void addPassenger(org.bukkit.entity.Entity entity, org.bukkit.entity.Entity passenger) {
        ((CraftEntity) entity).getHandle().passengers.add(((CraftEntity) passenger).getHandle());
        PacketPlayOutMount packet = new PacketPlayOutMount(((CraftEntity) entity).getHandle());
        if (entity instanceof Player) {
            ((CraftPlayer) entity).getHandle().playerConnection.sendPacket(packet);
        }
    }

    public void removePassenger(org.bukkit.entity.Entity entity) {
        ((CraftEntity) entity).getHandle().passengers.clear();
        PacketPlayOutMount packet = new PacketPlayOutMount(((CraftEntity) entity).getHandle());
        if (entity instanceof Player) {
            ((CraftPlayer) entity).getHandle().playerConnection.sendPacket(packet);
        }
    }

    /**
     * Search for: if (Math.abs(this.motX) < 0.003D) {
     * Class: Entity
     */
    @Override
    public void tick() {
        super.tick();
        if (isCustom) {
            setMot(0,0,0);
            if (rainbow) {
                if (toggle == 4) {
                    setColor(DyeColorWrapper.getNext(color));
                    PetCore.get().getInvLoaders().PET_DATA.update(PetOwner.getPetOwner(getOwner()));
                    toggle = 0;
                }
                toggle++;
            }
        }
    }

    @Override
    public void stopRiding() {
        if (!this.a(TagsFluid.WATER)) super.stopRiding();
    }

    @Override
    public boolean isClosed() {
        return (this.datawatcher.get(d) == 0);
    }

    @Override
    public void setClosed(boolean var) {
        if (var) {
            a(0);
        } else {
            a(100);
        }
    }

    @Override
    public void move(EnumMoveType var1, Vec3D vec3D) {
        if (isCustom) return;
        super.move(var1, vec3D);
    }


    /**
     * Search for: this.getAttributeInstance(GenericAttributes.
     * Class: EntityShulker
     */
    @Override
    public void a(int var1) {
        if (isCustom) {
            this.datawatcher.set(d, (byte) var1);
            return;
        }
        super.a(var1);
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
    public org.bukkit.entity.Entity getEntity() {
        return getBukkitEntity();
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = pet.asCompound();
        object.setBoolean("rainbow", rainbow);
        if (!rainbow)
            object.setString("color", color.name());
        object.setBoolean("closed", isClosed());
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("rainbow")) rainbow = object.getBoolean("rainbow");
        if (object.hasKey("closed")) setClosed(object.getBoolean("closed"));
        if (object.hasKey("color")) setColor(DyeColorWrapper.getByName(object.getString("color")));
        pet.applyCompound(object);
    }

    @Override
    public DyeColorWrapper getColor() {
        return color;
    }

    @Override
    public void setColor(DyeColorWrapper color) {
        this.color = color;
        this.datawatcher.set(COLOR, color.getWoolData());
    }

    @Override
    public EntityWrapper getPetEntityType() {
        return pet.getPetEntityType();
    }

    public boolean isCustom() {return this.isCustom;}

    public void setCustom(boolean isCustom) {this.isCustom = isCustom; }

    private boolean isOwnerRiding() {
        if (pet == null) return false;
        if (getOwner() == null) return false;
        if (passengers.size() == 0)
            return false;
        EntityPlayer owner = ((CraftPlayer) getOwner()).getHandle();
        for (net.minecraft.server.v1_14_R1.Entity passenger : this.passengers) {
            if (passenger.getUniqueID().equals(owner.getUniqueID())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void e(Vec3D vec3D) {
        if (passengers == null) {
            this.K = (float) 0.5;
            this.aU = (float) 0.02;
            super.e(vec3D);
        } else {
            if (this.pet == null) {
                this.K = (float) 0.5;
                this.aU = (float) 0.02;
                super.e(vec3D);
                return;
            }
            if (!isOwnerRiding()) {
                this.K = (float) 0.5;
                this.aU = (float) 0.02;
                super.e(vec3D);
                return;
            }
            EntityPlayer owner = ((CraftPlayer) getOwner()).getHandle();
            if (fieldAccessor != null) {
                if (fieldAccessor.hasField(owner)) {
                    if (fieldAccessor.get(owner)) {
                        if (isOnGround(this)) {
                            setMot(getMot().x, 1, getMot().z);
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
            //this.aL = this.aJ = this.yaw;
            this.aL = this.yaw;
            this.K = (float) 1.0;
        }
    }

    private boolean isOnGround(net.minecraft.server.v1_14_R1.Entity entity) {
        org.bukkit.block.Block block = entity.getBukkitEntity().getLocation().subtract(0, 0.5, 0).getBlock();
        return block.getType().isSolid();
    }

    @Override
    public boolean isRainbow() {
        return rainbow;
    }

    @Override
    public void setRainbow(boolean rainbow) {
        this.rainbow=rainbow;
    }
}

