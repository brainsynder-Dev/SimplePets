package simplepets.brainsynder.nms.entities.v1_11_R1.impossamobs;

import net.minecraft.server.v1_11_R1.*;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_11_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Shulker;
import org.bukkit.event.entity.CreatureSpawnEvent;
import simple.brainsynder.nbt.StorageTagCompound;
import simple.brainsynder.nms.IActionMessage;
import simple.brainsynder.utils.Reflection;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.entity.hostile.IEntityShulkerPet;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.entities.v1_11_R1.list.EntityControllerPet;
import simplepets.brainsynder.player.PetOwner;
import simplepets.brainsynder.wrapper.DyeColorWrapper;
import simplepets.brainsynder.wrapper.EntityWrapper;

/**
 * This is a beta mob, Simply because this does not work like the other mobs.
 */
public class EntityShulkerPet extends EntityShulker implements IEntityShulkerPet {
    private boolean isCustom = false;
    private boolean rainbow = false;
    private int toggle = 0;
    private int r = 0;
    private int l = 0;
    private boolean closed = true;
    private boolean didClick = false;
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

    public boolean damageEntity(DamageSource var1, float var2) {
        if (isCustom) {
            if (var1 instanceof EntityDamageSource) {
                EntityDamageSource entityDamageSource = (EntityDamageSource) var1;
                if (entityDamageSource.getEntity() instanceof EntityHuman) {
                    EntityHuman human = (EntityHuman) entityDamageSource.getEntity();
                    if (human.getName().equals(getOwner().getName())) {
                        if (getOwner().isSneaking()) {
                            if (l == 0) {
                                l = 1;
                                return false;
                            }
                            if (l != 0) l = 0;
                            if (r != 0) r = 0;
                        }
                        if (rainbow) {
                            l = 0;
                            r = 0;
                            rainbow = false;
                        }
                    }
                }
            }
            return false;
        }
        return super.damageEntity(var1, var2);
    }

    @Override
    public void n() {
        super.n();
        if (isCustom) {
            this.motX = 0;
            this.motY = 0;
            this.motZ = 0;
            if (!rainbow) {
                if (l == 1) {
                    if (r == 1) {
                        IActionMessage message = Reflection.getActionMessage();
                        message.sendMessage(getOwner(), "§4§lR§c§lA§6§lI§e§lN§a§lB§2§lO§b§lW §f§lmode Activated. L/R Click to cancel");
                        rainbow = true;
                    }
                }
            }
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
    public EnumInteractionResult a(EntityHuman human, Vec3D vec3d, EnumHand enumhand) {
        if (isCustom)
            return this.onInteract((Player) human.getBukkitEntity()) ? EnumInteractionResult.SUCCESS : EnumInteractionResult.FAIL;
        return super.a(human, vec3d, enumhand);
    }

    public boolean onInteract(Player p) {
        if (pet != null) {
            if (p.getName().equals(pet.getOwner().getName())) {
                if (!didClick) {
                    if (p.isSneaking()) {
                        if (l == 1) {
                            if (r == 0) {
                                r = 1;
                                return false;
                            }
                        }
                        if (l != 0) l = 0;
                        if (r != 0) r = 0;
                    }
                    if (rainbow) {
                        l = 0;
                        r = 0;
                        rainbow = false;
                    }
                    PetCore.get().getInvLoaders().PET_DATA.open(PetOwner.getPetOwner(getOwner()));
                }
                didClick = (!didClick);
            }
        }
        return false;
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
        object.setBoolean("Rainbow", rainbow);
        object.setString("color", color.name());
        object.setBoolean("Closed", isClosed());
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("Rainbow")) rainbow = object.getBoolean("Rainbow");
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
}
