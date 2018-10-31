package simplepets.brainsynder.nms.v1_11_R1.entities.impossamobs;

import net.minecraft.server.v1_11_R1.EntityShulker;
import net.minecraft.server.v1_11_R1.EnumMoveType;
import net.minecraft.server.v1_11_R1.World;
import net.minecraft.server.v1_11_R1.WorldServer;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_11_R1.CraftWorld;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Shulker;
import org.bukkit.event.entity.CreatureSpawnEvent;
import simple.brainsynder.nbt.StorageTagCompound;
import simplepets.brainsynder.api.entity.hostile.IEntityShulkerPet;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.v1_11_R1.entities.list.EntityControllerPet;
import simplepets.brainsynder.wrapper.DyeColorWrapper;
import simplepets.brainsynder.wrapper.EntityWrapper;

/**
 * This is a beta mob, Simply because this does not work like the other mobs.
 */
public class EntityShulkerPet extends EntityShulker implements IEntityShulkerPet {
    private boolean isCustom = false;
    private boolean rainbow = false;
    private int toggle = 0;
    private boolean closed = true;
    private DyeColorWrapper color = DyeColorWrapper.PURPLE;
    private EntityControllerPet pet;

    public EntityShulkerPet(World world) {
        super(world);
    }

    public EntityShulkerPet(World world, EntityControllerPet pet) {
        super(world);
        this.pet = pet;
    }

    public static Shulker spawn(Location location, EntityControllerPet pet) {
        EntityShulkerPet shulker = new EntityShulkerPet(((CraftWorld) location.getWorld()).getHandle(), pet);
        shulker.setCustom(true);
        shulker.setAI(false);
        shulker.setSilent(true);
        WorldServer worldServer = ((CraftWorld) location.getWorld()).getHandle();
        shulker.setPosition(location.getX(), location.getY(), location.getZ());
        worldServer.addEntity(shulker, CreatureSpawnEvent.SpawnReason.CUSTOM);
        return ((Shulker) shulker.getBukkitEntity());
    }

    @Override
    public void n() {
        super.n();
        if (isCustom) {
            this.motX = 0;
            this.motY = 0;
            this.motZ = 0;
            if (rainbow) {
                if (toggle == 4) {
                    setColor(DyeColorWrapper.getNext(color));
                    toggle = 0;
                }
                toggle++;
            }
        }
    }

    @Override
    public boolean isClosed() {
        return (dj() == 0);
    }

    @Override
    public void setClosed(boolean var) {
        if (var != closed)
            closed = var;
        if (var) {
            a(0);
        } else {
            a(100);
        }
    }

    @Override
    public void move(EnumMoveType var1, double var2, double var4, double var6) {
        if (isCustom)
            return;
        super.move(var1, var2, var4, var6);
    }

    @Override
    public void a(int var1) {
        if (isCustom) {
            this.datawatcher.set(c, (byte) var1);
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
    public Entity getEntity() {
        return getBukkitEntity();
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = new StorageTagCompound();
        object.setBoolean("rainbow", rainbow);
        if (!rainbow)
            object.setString("color", color.name());
        object.setBoolean("Closed", isClosed());
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("rainbow")) rainbow = object.getBoolean("rainbow");
        if (object.hasKey("Closed")) setClosed(object.getBoolean("Closed"));
        if (object.hasKey("color")) setColor(DyeColorWrapper.valueOf(String.valueOf(object.getString("color"))));
    }

    @Override
    public DyeColorWrapper getColor() {
        return color;
    }

    @Override
    public void setColor(DyeColorWrapper color) {
        this.color = color;
        this.datawatcher.set(bw, color.getWoolData());
    }

    @Override
    public EntityWrapper getEntityType() {
        return pet.getEntityType();
    }

    public boolean isCustom() {return this.isCustom;}

    public void setCustom(boolean isCustom) {this.isCustom = isCustom; }

    @Override
    public boolean isRainbow() {
        return rainbow;
    }

    @Override
    public void setRainbow(boolean rainbow) {
        this.rainbow = rainbow;
    }
}
