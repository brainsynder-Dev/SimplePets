package simplepets.brainsynder.nms.entities.v1_8_R3;

import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftCreature;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import simple.brainsynder.nbt.StorageTagCompound;
import simplepets.brainsynder.menu.PetDataMenu;
import simplepets.brainsynder.nms.entities.type.main.IEntityPet;
import simplepets.brainsynder.nms.entities.type.main.IFlyablePet;
import simplepets.brainsynder.pet.IPet;
import simplepets.brainsynder.pet.PetMoveEvent;
import simplepets.brainsynder.wrapper.EntityWrapper;

import java.lang.reflect.Field;

/**
 * @Deprecated Will be removed when MC version 1.13 is released
 */
@Deprecated
public abstract class EntityPet extends EntityCreature implements IAnimal,
        IEntityPet {
    public IPet pet;
    private Field JUMP_FIELD = null;

    public EntityPet(World world, IPet pet) {
        super(world);
        if (pet == null) {
            return;
        }

        this.pet = pet;
        try {
            JUMP_FIELD = EntityLiving.class.getDeclaredField("aY");
        } catch (NoSuchFieldException | SecurityException e1) {
            e1.printStackTrace();
        }
        JUMP_FIELD.setAccessible(true);
    }

    public EntityPet(World world) {
        this(world, null);
    }

    @Override
    public EntityWrapper getEntityType() {
        return pet.getPetType().getType();
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = new StorageTagCompound();
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {

    }

    public void setPassenger(int pos, org.bukkit.entity.Entity entity, org.bukkit.entity.Entity passenger) {
        ((CraftEntity) entity).getHandle().passenger = ((CraftEntity) passenger).getHandle();
    }

    public void removePassenger(org.bukkit.entity.Entity entity) {
        ((CraftEntity) entity).getHandle().passenger = null;
    }

    @Override
    public org.bukkit.entity.Entity getEntity() {
        return super.getBukkitEntity();
    }

    public void setLocation(Location l) {
        this.setLocation(l.getX(), l.getY(), l.getZ(), l.getYaw(), l.getPitch());
        this.world = ((CraftWorld) l.getWorld()).getHandle();
    }

    public void checkPlayer() {
        if (this instanceof IFlyablePet) {
            if (getPet().isVehicle()) {
                if (!this.onGround && this.motY < 0.0D) {
                    this.motY *= 0.6D;
                }
            }
        }
        if (pet != null) {
            if (pet.getOwner() != null) {
                Player p = pet.getOwner();
                if (((CraftPlayer) p).getHandle().isInvisible() != this.isInvisible()) {
                    this.setInvisible(!this.isInvisible());
                }

                if (this.getPet().isHat()) {
                    this.lastYaw = this.yaw = p.getLocation().getYaw();
                }
            }
        }
    }

    protected void a(BlockPosition blockposition, Block block) {
        super.a(blockposition, block);
        this.a(blockposition.getX(), blockposition.getY(), blockposition.getZ(), block);
    }

    protected void a(int i, int j, int k, Block block) {
        super.a(new BlockPosition(i, j, k), block);
        this.makeStepSound(i, j, k, block);
    }

    protected void makeStepSound(int i, int j, int k, Block block) {
        this.makeStepSound();
    }

    protected void doJumpAnimation() {
    }

    public void b(NBTTagCompound nbttagcompound) {
    }

    public boolean c(NBTTagCompound nbttagcompound) {
        return false;
    }

    public void a(NBTTagCompound nbttagcompound) {
    }

    public boolean d(NBTTagCompound nbttagcompound) {
        return false;
    }

    public void e(NBTTagCompound nbttagcompound) {
    }

    protected void makeStepSound() {
    }

    protected void h() {
        super.h();
        this.initDatawatcher();
    }

    protected void initDatawatcher() {
    }

    public CraftCreature getBukkitEntity() {
        return (CraftCreature) super.getBukkitEntity();
    }

    @Override
    public void t_() {
        super.t_();
        checkPlayer();
    }

    @Override
    public Player getOwner() {
        return pet.getOwner();
    }

    @Override
    public IPet getPet() {
        return pet;
    }

    private boolean isOnGround(Entity entity) {
        org.bukkit.block.Block block = entity.getBukkitEntity().getLocation().subtract(0, 0.4, 0).getBlock();
        return block.getType().isSolid();
    }

    public boolean a(EntityHuman human) {
        return this.onInteract((Player) human.getBukkitEntity());
    }


    public boolean onInteract(Player p) {
        if (pet != null) {
            if (p.getName().equals(pet.getOwner().getName())) {
                PetDataMenu data = new PetDataMenu(pet);
                data.showTo(p);
                return true;
            }
            return false;
        }
        return false;
    }

    public boolean isDead() {
        return this.dead;
    }

    public boolean attack(Entity entity, DamageSource damageSource, float damage) {
        return entity.damageEntity(damageSource, damage);
    }

    protected String z() {
        return getIdleSound();
    }

    protected String bo() {
        return getDeathSound();
    }

    protected String getIdleSound() {
        return null;
    }

    protected String getDeathSound() {
        return null;
    }


    public void g(float sideMot, float forMot) {
        if (pet == null) {
            super.g(sideMot, forMot);
            return;
        }

        if ((this.passenger != null) && ((this.passenger instanceof EntityHuman))) {
            this.lastYaw = (this.yaw = this.passenger.yaw);
            this.pitch = (this.passenger.pitch * 0.5F);
            setYawPitch(this.yaw, this.pitch);
            this.aI = (this.aG = this.yaw);
            sideMot = ((EntityLiving) this.passenger).aZ * 0.5F;
            forMot = ((EntityLiving) this.passenger).ba;
            if ((JUMP_FIELD != null) && (isOnGround(this))) {
                try {
                    if (JUMP_FIELD.getBoolean(this.passenger)) {
                        this.motY = 0.3D;
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            } else if ((!isOnGround(this)) && (this instanceof IFlyablePet) && (pet.getPetType().canFly(getOwner()))) {
                try {
                    if (JUMP_FIELD.getBoolean(this.passenger)) {
                        this.motY = 0.5D;
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }

            this.S = 1.0F;
            this.aK = this.yaw;
            if (!this.world.isClientSide) {
                k(0.2F);

                if (bM()) {
                    if (V()) {
                        double d0 = this.locY;
                        float f3 = 0.8F;
                        float f4 = 0.02F;
                        float f2 = EnchantmentManager.b(this);
                        if (f2 > 3.0F) {
                            f2 = 3.0F;
                        }

                        if (f2 > 0.0F) {
                            f3 += (0.5460001F - f3) * f2 / 3.0F;
                            f4 += (bI() * 1.0F - f4) * f2 / 3.0F;
                        }

                        a(sideMot, forMot, f4);
                        move(this.motX, this.motY, this.motZ);
                        this.motX *= f3;
                        this.motY *= 0.800000011920929D;
                        this.motZ *= f3;
                        this.motY -= 0.02D;
                        if ((this.positionChanged) && (c(this.motX, this.motY + 0.6000000238418579D - this.locY + d0, this.motZ))) {
                            this.motY = 0.300000011920929D;
                        }
                    } else if (ab()) {
                        double d0 = this.locY;
                        a(sideMot, forMot, 0.02F);
                        move(this.motX, this.motY, this.motZ);
                        this.motX *= 0.5D;
                        this.motY *= 0.5D;
                        this.motZ *= 0.5D;
                        this.motY -= 0.02D;
                        if ((this.positionChanged) && (c(this.motX, this.motY + 0.6000000238418579D - this.locY + d0, this.motZ))) {
                            this.motY = 0.300000011920929D;
                        }
                    } else {
                        float f5 = this.world.getType(new BlockPosition(MathHelper.floor(this.locX), MathHelper.floor(getBoundingBox().b) - 1, MathHelper.floor(this.locZ))).getBlock().frictionFactor * 0.91F;

                        float f6 = 0.162771F / (f5 * f5 * f5);
                        float f3 = bI() * f6;

                        a(sideMot, forMot, f3);
                        f5 = this.world.getType(new BlockPosition(MathHelper.floor(this.locX), MathHelper.floor(getBoundingBox().b) - 1, MathHelper.floor(this.locZ))).getBlock().frictionFactor * 0.91F;

                        if (k_()) {
                            float f4 = 0.15F;
                            this.motX = MathHelper.a(this.motX, -f4, f4);
                            this.motZ = MathHelper.a(this.motZ, -f4, f4);
                            this.fallDistance = 0.0F;
                            if (this.motY < -0.15D) {
                                this.motY = -0.15D;
                            }

                            if (this.motY < 0.0D) {
                                this.motY = 0.0D;
                            }
                        }

                        move(this.motX, this.motY, this.motZ);
                        if ((this.positionChanged) && (k_())) {
                            this.motY = 0.2D;
                        }

                        if ((this.world.isClientSide) && ((!this.world.isLoaded(new BlockPosition((int) this.locX, 0, (int) this.locZ))) || (!this.world.getChunkAtWorldCoords(new BlockPosition((int) this.locX, 0, (int) this.locZ)).o()))) {
                            if (this.locY > 0.0D) {
                                this.motY = -0.1D;
                            } else {
                                this.motY = 0.0D;
                            }
                        } else {
                            this.motY += 0.0D;
                        }

                        this.motY *= 0.9800000190734863D;
                        this.motX *= f5;
                        this.motZ *= f5;
                    }
                }

                this.ay = this.az;
                double d0 = this.locX - this.lastX;
                double d1 = this.locZ - this.lastZ;

                float f2 = MathHelper.sqrt(d0 * d0 + d1 * d1) * 4.0F;
                if (f2 > 1.0F) {
                    f2 = 1.0F;
                }

                this.az += (f2 - this.az) * 0.4F;
                this.aA += this.az;

                if (!world.isClientSide) {
                    PetMoveEvent event = new PetMoveEvent(this, PetMoveEvent.Cause.RIDE);
                    Bukkit.getServer().getPluginManager().callEvent(event);
                    super.g(sideMot, forMot);
                }
            }

            this.ay = this.az;
            double d0 = this.locX - this.lastX;
            double d1 = this.locZ - this.lastZ;
            float f4 = MathHelper.sqrt(d0 * d0 + d1 * d1) * 4.0F;
            if (f4 > 1.0F) {
                f4 = 1.0F;
            }

            this.az += (f4 - this.az) * 0.4F;
            this.aA += this.az;
        } else {
            this.S = 0.5F;
            this.aK = 0.02F;
            super.g(sideMot, forMot);
        }
    }


}
