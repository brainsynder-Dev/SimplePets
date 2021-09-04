package simplepets.brainsynder.versions.v1_17_R1.entity.special;

import lib.brainsynder.nbt.StorageTagCompound;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.misc.IEntityControllerPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.versions.v1_17_R1.entity.EntityPet;
import simplepets.brainsynder.versions.v1_17_R1.entity.list.EntityArmorStandPet;
import simplepets.brainsynder.versions.v1_17_R1.entity.list.EntityZombiePet;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EntityControllerPet extends EntityZombiePet implements IEntityControllerPet {

    private final List<Entity> entities = new ArrayList<>();
    private LivingEntity pet;
    private Entity displayEntity, displayRider = null;
    private final boolean moving = false;

    public EntityControllerPet(PetType type, PetUser user) {
        super(EntityType.ZOMBIE, type, user);
        switch (type) {
            case ARMOR_STAND:
                pet = new EntityArmorStandPet(this, user);
                break;
            case SHULKER:
                throw new IllegalStateException("Not yet initialised!");
            default:
                throw new IllegalStateException("This pet does not use controller pets!");
        }
    }

    public EntityControllerPet(World world) {
        super(EntityType.ZOMBIE, world);
    }

    @Override
    public List<Entity> getEntities() {
        return entities;
    }

    @Override
    public StorageTagCompound asCompound() {
        return super.asCompound();
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        super.applyCompound(object);
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.isInvisible()) this.setInvisible(true);
        if (!isSilent()) this.setSilent(true);
        if (pet != null) if (isBaby()) setBaby((getPet().getPetType() instanceof ShulkerPet));
        Player p = pet.getOwner();

        if (this.displayEntity != null) {
            if (this.displayEntity.isValid()) {
                net.minecraft.server.v1_16_R3.Entity entity = ((CraftEntity) displayEntity).getHandle();
                if (!displayEntity.getPassengers().isEmpty()){
                    if (displayRider == null) displayRider = displayEntity.getPassengers().get(0);
                    entity = ((CraftEntity) displayRider).getHandle();
                }
                updateName(entity);
                reloadLocation();
                if (!canIgnoreVanish()) {
                    if (((CraftPlayer) p).getHandle().isInvisible() != entity.isInvisible()) entity.setInvisible(!entity.isInvisible());
                }
            }
        }
        PetType type = getVisibleEntity().getPet().getPetType();
        double current = getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).getValue();
        double rideSpeed = type.getRideSpeed();
        double walkSpeed = type.getSpeed();
        if (isOwnerRiding()) {
            if (current != rideSpeed)
                getAttribute(Attribute.MOVEMENT_SPEED).setValue(rideSpeed);
        } else {
            if (current != walkSpeed)
                getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(walkSpeed);
        }
    }

    public void updateName(net.minecraft.server.v1_16_R3.Entity entity) {
        if (hasCustomName()) {
            setCustomNameVisible(false);
            entity.setCustomName(getCustomName());
        }
    }

    @Override
    protected boolean isOwnerRiding() {
        if (displayEntity == null || getVisibleEntity() == null) return false;
        if (getOwner() == null) return false;
        if (displayEntity.getPassengers().size() == 0)
            return false;
        EntityPlayer owner = ((CraftPlayer) getOwner()).getHandle();
        for (Entity passenger : displayEntity.getPassengers()) {
            if (passenger.getUniqueId().equals(owner.getUniqueID())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Optional<Entity> getDisplayEntity() {
        return displayEntity;
    }

    @Override
    public Optional<Entity> getDisplayRider() {
        return displayRider;
    }

    @Override
    public void setDisplayEntity(Entity entity) {
        if (!entities.contains(entity))
            entities.add(entity);
        if (entity.getPassenger() != null) {
            displayRider = entity.getPassenger();
            if (!entities.contains(entity.getPassenger()))
                entities.add(entity.getPassenger());
        }

        displayEntity = entity;
        for (Entity e : entities) {
            e.setMetadata("NO_DAMAGE", new FixedMetadataValue(PetCore.get(), "NO_DAMAGE"));
        }
    }

    @Override
    public void remove() {
        getBukkitEntity().remove();
        for (Entity ent : entities) ent.remove();
    }

    @Override
    public void reloadLocation() {
        if (displayEntity.getPassenger() != null) {
            net.minecraft.server.v1_16_R3.Entity displayEntity = ((CraftEntity) this.displayEntity).getHandle();
            Location loc;
            if (this.displayRider != null) {
                if (this.displayRider.getType().equals(EntityType.SHULKER)) {
                    loc = getBukkitEntity().getLocation().clone().subtract(0, 0.735, 0);
                } else {
                    loc = getBukkitEntity().getLocation().clone();
                }
            } else {
                loc = getBukkitEntity().getLocation().clone();
            }

            displayEntity.setPositionRotation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
            loc.getWorld().getNearbyEntities(loc, 100, 100, 100).forEach(entity -> {
                if (entity instanceof Player) {
                    Player player = (Player) entity;
                    PacketPlayOutEntityTeleport packet = new PacketPlayOutEntityTeleport(displayEntity);
                    ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
                }
            });
            return;
        }


        net.minecraft.server.v1_16_R3.Entity displayEntity = ((CraftEntity) this.displayEntity).getHandle();
        Location loc;
        if (this.displayRider != null) {
            if (this.displayRider.getType().equals(EntityType.SHULKER)) {
                loc = getBukkitEntity().getLocation().clone().add(0, 0.75, 0);
            } else {
                loc = getBukkitEntity().getLocation().clone();
            }
        } else {
            loc = getBukkitEntity().getLocation().clone();
        }

        displayEntity.setPositionRotation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
        loc.getWorld().getNearbyEntities(loc, 100, 100, 100).forEach(entity -> {
            if (entity instanceof Player) {
                Player player = (Player) entity;
                PacketPlayOutEntityTeleport packet = new PacketPlayOutEntityTeleport(displayEntity);
                ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
            }
        });
    }

    @Override
    public void addPassenger(Entity passenger) {

        ((CraftEntity) passenger).getHandle().passengers.add(0, this);
        if (passenger instanceof Player) {
            PetCore.get().getUtilities().sendMountPacket((Player) passenger, this.getBukkitEntity());
        }
    }

    @Override
    public boolean isMoving() {
        return false;
    }

    @Override
    public void updateName() {

    }

    @Override
    public IEntityPet getVisibleEntity() {
        Object handle = PetCore.getHandle(displayEntity);
        if (handle instanceof IEntityPet) {
            return (IEntityPet) handle;
        } else {
            if (displayEntity.getPassenger() != null) {
                Object h = PetCore.getHandle(displayEntity.getPassenger());
                if (h instanceof IEntityPet) {
                    return (IEntityPet) h;
                }
            }
        }
        return this;
    }
}
