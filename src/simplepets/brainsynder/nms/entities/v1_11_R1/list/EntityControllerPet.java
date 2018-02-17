package simplepets.brainsynder.nms.entities.v1_11_R1.list;

import net.minecraft.server.v1_11_R1.PacketPlayOutEntityTeleport;
import net.minecraft.server.v1_11_R1.World;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import simple.brainsynder.nbt.StorageTagCompound;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.entity.IEntityControllerPet;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.pet.PetType;
import simplepets.brainsynder.reflection.ReflectionUtil;

import java.util.ArrayList;
import java.util.List;

public class EntityControllerPet extends EntityZombiePet implements IEntityControllerPet {
    private List<Entity> entities = new ArrayList<>();
    private Entity displayEntity;
    private Location previus;
    private boolean moving = false;

    public EntityControllerPet(World world, IPet pet) {
        super(world, pet);
    }

    public EntityControllerPet(World world) {
        super(world);
    }

    @Override
    public void g(float sideMot, float forwMot) {
        super.g(sideMot, forwMot);
        reloadLocation();
    }

    @Override
    public StorageTagCompound asCompound() {
        return getVisibleEntity().asCompound();
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        getVisibleEntity().applyCompound(object);
    }

    @Override
    public void repeatTask() {
        super.repeatTask();
        if (!this.isInvisible())
            this.setInvisible(true);
        if (getPet() != null) if (isBaby()) setBaby((getPet().getPetType() == PetType.SHULKER));
        Player p = getPet().getOwner();
        if (hasCustomName()) {
            setCustomNameVisible(false);
            if (displayEntity.getPassenger() != null) {
                this.displayEntity.getPassenger().setCustomName(getCustomName());
                this.displayEntity.getPassenger().setCustomNameVisible(true);
            } else {
                this.displayEntity.setCustomName(getCustomName());
                this.displayEntity.setCustomNameVisible(true);
            }
        }
        if (this.displayEntity != null) {
            if (this.displayEntity.isValid()) {
                if (displayEntity.getPassenger() == null) {
                    reloadLocation();
                    net.minecraft.server.v1_11_R1.Entity displayEntity = ((CraftEntity) this.displayEntity).getHandle();
                    if (((CraftPlayer) getOwner()).getHandle().isInvisible() != displayEntity.isInvisible()) {
                        displayEntity.setInvisible(!displayEntity.isInvisible());
                    }
                    return;
                }
                if (this.displayEntity.getPassenger() != null) {
                    net.minecraft.server.v1_11_R1.Entity displayEntity = ((CraftEntity) this.displayEntity.getPassenger()).getHandle();
                    if (((CraftPlayer) p).getHandle().isInvisible() != displayEntity.isInvisible()) {
                        displayEntity.setInvisible(!displayEntity.isInvisible());
                    }
                }
            }
        }
    }

    @Override
    public Entity getDisplayEntity() {
        return displayEntity;
    }

    @Override
    public void setDisplayEntity(Entity entity) {
        if (!entities.contains(entity))
            entities.add(entity);
        if (entity.getPassenger() != null)
            if (!entities.contains(entity.getPassenger()))
                entities.add(entity.getPassenger());

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
            net.minecraft.server.v1_11_R1.Entity displayEntity = ((CraftEntity) this.displayEntity).getHandle();
            Location loc = getBukkitEntity().getLocation().clone().subtract(0, 0.735, 0);
            displayEntity.setPositionRotation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
            if (!loc.getWorld().getPlayers().isEmpty()) {
                for (Player player : loc.getWorld().getPlayers()) {
                    PacketPlayOutEntityTeleport packet = new PacketPlayOutEntityTeleport(displayEntity);
                    ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
                }
            }
            return;
        }
        net.minecraft.server.v1_11_R1.Entity displayEntity = ((CraftEntity) this.displayEntity).getHandle();
        Location loc = getBukkitEntity().getLocation();//.clone().add(0, 0.75, 0);
        displayEntity.setPositionRotation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
        if (!loc.getWorld().getPlayers().isEmpty()) {
            for (Player player : loc.getWorld().getPlayers()) {
                PacketPlayOutEntityTeleport packet = new PacketPlayOutEntityTeleport(displayEntity);
                ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
            }
        }
    }

    @Override
    public IEntityPet getVisibleEntity() {
        Object handle = ReflectionUtil.getEntityHandle(displayEntity);
        if (handle instanceof IEntityPet) {
            return (IEntityPet) handle;
        } else {
            if (displayEntity.getPassenger() != null) {
                Object h = ReflectionUtil.getEntityHandle(displayEntity.getPassenger());
                if (h instanceof IEntityPet) {
                    return (IEntityPet) h;
                }
            }
        }
        return this;
    }

    public boolean isMoving() {return this.moving;}
}
