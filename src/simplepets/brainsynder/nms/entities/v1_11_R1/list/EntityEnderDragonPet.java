package simplepets.brainsynder.nms.entities.v1_11_R1.list;

import com.google.common.collect.Lists;
import net.minecraft.server.v1_11_R1.*;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftPlayer;
import org.bukkit.util.Vector;
import simple.brainsynder.sound.SoundMaker;
import simplepets.brainsynder.nms.entities.type.IEntityEnderDragonPet;
import simplepets.brainsynder.nms.entities.v1_11_R1.EntityNoClipPet;
import simplepets.brainsynder.pet.IPet;

import java.util.ArrayList;
import java.util.Iterator;

public class EntityEnderDragonPet extends EntityNoClipPet implements IComplex, IEntityEnderDragonPet {
    public static final DataWatcherObject<Integer> PHASE;

    static {
        PHASE = DataWatcher.a(EntityEnderDragonPet.class, DataWatcherRegistry.b);
    }

    public int bl = -1;
    public double[][] bk = new double[64][3];
    private double a;
    private double b;
    private double c;
    private EntityComplexPart[] children;
    private EntityComplexPart head;
    private EntityComplexPart body;
    private EntityComplexPart tail1;
    private EntityComplexPart tail2;
    private EntityComplexPart tail3;
    private EntityComplexPart wing1;
    private EntityComplexPart wing2;
    private float bu;
    private float bv;
    private boolean bw;
    private boolean bx;
    private Entity bA;

    public EntityEnderDragonPet(World world, IPet pet) {
        super(world, pet);
        this.head = new EntityComplexPart(this, "head", 6.0F, 6.0F);
        this.body = new EntityComplexPart(this, "body", 8.0F, 8.0F);
        this.tail1 = new EntityComplexPart(this, "tail", 4.0F, 4.0F);
        this.tail2 = new EntityComplexPart(this, "tail", 4.0F, 4.0F);
        this.tail3 = new EntityComplexPart(this, "tail", 4.0F, 4.0F);
        this.wing1 = new EntityComplexPart(this, "wing", 4.0F, 4.0F);
        this.wing2 = new EntityComplexPart(this, "wing", 4.0F, 4.0F);
        this.children = new EntityComplexPart[]{
                this.head,
                this.body,
                this.tail1,
                this.tail2,
                this.tail3,
                this.wing1,
                this.wing2
        };
        this.noClip(false);
        this.b = 100.0D;
        this.ah = true;
    }

    @Override
    protected void registerDatawatchers() {
        super.registerDatawatchers();
        this.getDataWatcher().register(PHASE, DragonControllerPhase.k.b());
    }

    public void resizeBoundingBox(boolean flag) {
        this.setSize(flag ? 8.0F : 16.0F, flag ? 4.0F : 8.0F);
    }

    public double[] b(int i, float f) {
        if (this.getHealth() <= 0.0F) {
            f = 0.0F;
        }

        f = 1.0F - f;
        int j = this.bl - i * 1 & 63;
        int k = this.bl - i * 1 - 1 & 63;
        double[] adouble = new double[3];
        double d0 = this.bk[j][0];
        double d1 = MathHelper.g(this.bk[k][0] - d0);
        adouble[0] = d0 + d1 * (double) f;
        d0 = this.bk[j][1];
        d1 = this.bk[k][1] - d0;
        adouble[1] = d0 + d1 * (double) f;
        adouble[2] = this.bk[j][2] + (this.bk[k][2] - this.bk[j][2]) * (double) f;
        return adouble;
    }

    @Override
    public void n() {
        Entity passenger = this.bx().size() > 0 ? this.bx().get(0) : null;
        if (passenger != null && passenger instanceof EntityHuman) {
            EntityHuman human = (EntityHuman) passenger;
            if (human.getBukkitEntity() == this.getOwner()) {
                float forw = ((EntityLiving) passenger).be;
                float side = ((EntityLiving) passenger).bf;
                Vector v = new Vector();
                Location l = new Location(this.world.getWorld(), this.locX, this.locY, this.locZ);
                if (side < 0.0F) {
                    l.setYaw(passenger.yaw - 90.0F);
                    v.add(l.getDirection().normalize().multiply(-0.5D));
                } else if (side > 0.0F) {
                    l.setYaw(passenger.yaw + 90.0F);
                    v.add(l.getDirection().normalize().multiply(-0.5D));
                }

                if (forw < 0.0F) {
                    l.setYaw(passenger.yaw);
                    v.add(l.getDirection().normalize().multiply(0.5D));
                } else if (forw > 0.0F) {
                    l.setYaw(passenger.yaw);
                    v.add(l.getDirection().normalize().multiply(0.5D));
                }

                this.lastYaw = this.yaw = passenger.yaw - 180.0F;
                this.pitch = passenger.pitch * 0.5F;
                this.setYawPitch(this.yaw, this.pitch);
                this.aO = this.aM = this.yaw;
                if (fieldAccessor != null) {
                    if (fieldAccessor.get(passenger)) {
                        v.setY(0.5F);
                    } else if (((EntityLiving) passenger).pitch >= 50.0F) {
                        v.setY(-0.4F);
                    }
                }

                l.add(v.multiply(Math.pow(getPet().getPetType().getRideSpeed(), getPet().getPetType().getRideSpeed())));
                this.setPos(l.getX(), l.getY(), l.getZ());
                this.updateComplexParts();
                return;
            }
        }

        float f;
        float f1;
        if (this.world.isClientSide) {
            f = MathHelper.cos(this.bv * 3.1415927F * 2.0F);
            f1 = MathHelper.cos(this.bu * 3.1415927F * 2.0F);
            if (f1 <= -0.3F && f >= -0.3F && !this.isSilent()) {
                SoundMaker.ENTITY_ENDERDRAGON_FLAP.playSound(getEntity().getLocation(), 5.0F, 0.8F);
            }
        }

        this.bu = this.bv;
        if (this.getHealth() <= 0.0F) {
            f = (this.random.nextFloat() - 0.5F) * 8.0F;
            f1 = (this.random.nextFloat() - 0.5F) * 4.0F;
            float f2 = (this.random.nextFloat() - 0.5F) * 8.0F;
            this.world.addParticle(EnumParticle.EXPLOSION_LARGE, this.locX + (double) f, this.locY + 2.0D + (double) f1, this.locZ + (double) f2, 0.0D, 0.0D, 0.0D);
        } else {
            f = 0.2F / (MathHelper.sqrt(this.motX * this.motX + this.motZ * this.motZ) * 10.0F + 1.0F);
            f *= (float) Math.pow(2.0D, this.motY);
            if (this.bx) {
                this.bv += f * 0.5F;
            } else {
                this.bv += f;
            }

            this.yaw = MathHelper.g(this.yaw);
            if (this.hasAI()) {
                this.bv = 0.5F;
            } else {
                if (this.bl < 0) {
                    for (int i = 0; i < this.bk.length; ++i) {
                        this.bk[i][0] = (double) this.yaw;
                        this.bk[i][1] = this.locY;
                    }
                }

                if (++this.bl == this.bk.length) {
                    this.bl = 0;
                }
            }

            this.bk[this.bl][0] = (double) this.yaw;
            this.bk[this.bl][1] = this.locY;
            double d3;
            double d0;
            double d1;
            double d2;
            if (this.world.isClientSide) {
                if (this.bg > 0.0F) {
                    d3 = this.locX + ((double) this.bh - this.locX) / (double) this.bg;
                    d0 = this.locY + (this.bi - this.locY) / (double) this.bg;
                    d1 = this.locZ + (this.bj - this.locZ) / (double) this.bg;
                    d2 = (double) MathHelper.g(this.bg - this.yaw);
                    this.yaw = (float) ((double) this.yaw + d2 / (double) this.bg);
                    this.pitch += ((float) this.bl - this.pitch) / this.bg;
                    --this.bg;
                    this.setPosition(d3, d0, d1);
                    this.setYawPitch(this.yaw, this.pitch);
                }
            } else {
                d3 = this.a - this.locX;
                d0 = this.b - this.locY;
                d1 = this.c - this.locZ;
                d2 = d3 * d3 + d0 * d0 + d1 * d1;
                double d4;
                if (this.bA != null) {
                    this.a = this.bA.locX;
                    this.c = this.bA.locZ;
                    double d5 = this.a - this.locX;
                    double d6 = this.c - this.locZ;
                    double d7 = Math.sqrt(d5 * d5 + d6 * d6);
                    d4 = 0.4000000059604645D + d7 / 80.0D - 1.0D;
                    if (d4 > 10.0D) {
                        d4 = 10.0D;
                    }

                    this.b = this.bA.getBoundingBox().b + d4;
                } else {
                    this.a += this.random.nextGaussian() * 2.0D;
                    this.c += this.random.nextGaussian() * 2.0D;
                }

                if (this.bw || d2 < 100.0D || d2 > 22500.0D || this.positionChanged || this.E) {
                    this.target();
                }

                d0 /= (double) MathHelper.sqrt(d3 * d3 + d1 * d1);
                float f3 = 0.6F;
                d0 = MathHelper.a(d0, (double) (-f3), (double) f3);
                this.motY += d0 * 0.10000000149011612D;
                this.yaw = MathHelper.g(this.yaw);
                double d8 = 180.0D - MathHelper.a(d3, d1) * 180.0D / 3.1415927410125732D;
                double d9 = MathHelper.g(d8 - (double) this.yaw);
                if (d9 > 50.0D) {
                    d9 = 50.0D;
                }

                if (d9 < -50.0D) {
                    d9 = -50.0D;
                }

                Vec3D vec3d = (new Vec3D(this.a - this.locX, this.b - this.locY, this.c - this.locZ)).a();
                d4 = (double) (-MathHelper.cos(this.yaw * 3.1415927F / 180.0F));
                Vec3D vec3d1 = (new Vec3D((double) MathHelper.sin(this.yaw * 3.1415927F / 180.0F), this.motY, d4)).a();
                float f4 = ((float) vec3d1.b(vec3d) + 0.5F) / 1.5F;
                if (f4 < 0.0F) {
                    f4 = 0.0F;
                }

                this.bf *= 0.8F;
                float f5 = MathHelper.sqrt(this.motX * this.motX + this.motZ * this.motZ) * 1.0F + 1.0F;
                double d10 = Math.sqrt(this.motX * this.motX + this.motZ * this.motZ) * 1.0D + 1.0D;
                if (d10 > 40.0D) {
                    d10 = 40.0D;
                }

                this.bf = (float) ((double) this.bf + d9 * (0.699999988079071D / d10 / (double) f5));
                this.yaw += this.bf * 0.1F;
                float f6 = (float) (2.0D / (d10 + 1.0D));
                float f7 = 0.06F;
                this.a(0.0F, -1.0F, f7 * (f4 * f6 + (1.0F - f6)));
                if (this.bx) {
                    this.move(EnumMoveType.SELF, this.motX * 0.800000011920929D, this.motY * 0.800000011920929D, this.motZ * 0.800000011920929D);
                } else {
                    this.move(EnumMoveType.SELF, this.motX, this.motY, this.motZ);
                }

                Vec3D vec3d2 = (new Vec3D(this.motX, this.motY, this.motZ)).a();
                float f8 = ((float) vec3d2.b(vec3d1) + 1.0F) / 2.0F;
                f8 = 0.8F + 0.15F * f8;
                this.motX *= (double) f8;
                this.motZ *= (double) f8;
                this.motY *= 0.9100000262260437D;
            }

            this.updateComplexParts();
        }

    }

    private void setPos(double x, double y, double z) {
        double[] d0 = new double[]{x, y, z};
        double[] d1 = new double[]{this.locX, this.locY, this.locZ};

        for (int i = 0; i < 3; ++i) {
            if (this.world.getWorld().getBlockAt((int) x, (int) y, (int) z).getType().isSolid()) {
                d0[i] = d1[i];
            }
        }

        this.setPosition(d0[0], d0[1], d0[2]);
    }

    private void updateComplexParts() {
        if (this.children != null) {
            this.aP = this.yaw;
            this.head.width = this.head.length = 3.0F;
            this.tail1.width = this.tail1.length = 2.0F;
            this.tail2.width = this.tail2.length = 2.0F;
            this.tail3.width = this.tail3.length = 2.0F;
            this.body.length = 3.0F;
            this.body.width = 5.0F;
            this.wing1.length = 2.0F;
            this.wing1.width = 4.0F;
            this.wing2.length = 3.0F;
            this.wing2.width = 4.0F;
            float f1 = (float) (this.b(5, 1.0F)[1] - this.b(10, 1.0F)[1]) * 10.0F / 180.0F * 3.1415927F;
            float f2 = MathHelper.cos(f1);
            float f9 = -MathHelper.sin(f1);
            float f10 = this.yaw * 3.1415927F / 180.0F;
            float f11 = MathHelper.sin(f10);
            float f12 = MathHelper.cos(f10);
            this.body.A_();
            this.body.setPositionRotation(this.locX + (double) (f11 * 0.5F), this.locY, this.locZ - (double) (f12 * 0.5F), 0.0F, 0.0F);
            this.wing1.A_();
            this.wing1.setPositionRotation(this.locX + (double) (f12 * 4.5F), this.locY + 2.0D, this.locZ + (double) (f11 * 4.5F), 0.0F, 0.0F);
            this.wing2.A_();
            this.wing2.setPositionRotation(this.locX - (double) (f12 * 4.5F), this.locY + 2.0D, this.locZ - (double) (f11 * 4.5F), 0.0F, 0.0F);
            double[] adouble = this.b(5, 1.0F);
            double[] adouble1 = this.b(0, 1.0F);
            float f3 = MathHelper.sin(this.yaw * 3.1415927F / 180.0F - this.bf * 0.01F);
            float f13 = MathHelper.cos(this.yaw * 3.1415927F / 180.0F - this.bf * 0.01F);
            this.head.A_();
            this.head.setPositionRotation(this.locX + (double) (f3 * 5.5F * f2), this.locY + (adouble1[1] - adouble[1]) * 1.0D + (double) (f9 * 5.5F), this.locZ - (double) (f13 * 5.5F * f2), 0.0F, 0.0F);

            for (int j = 0; j < 3; ++j) {
                EntityComplexPart entitycomplexpart = null;
                if (j == 0) {
                    entitycomplexpart = this.tail1;
                }

                if (j == 1) {
                    entitycomplexpart = this.tail2;
                }

                if (j == 2) {
                    entitycomplexpart = this.tail3;
                }

                double[] adouble2 = this.b(12 + j * 2, 1.0F);
                float f14 = this.yaw * 3.1415927F / 180.0F + (float) MathHelper.g(adouble2[0] - adouble[0]) * 3.1415927F / 180.0F * 1.0F;
                float f15 = MathHelper.sin(f14);
                float f16 = MathHelper.cos(f14);
                float f17 = 1.5F;
                float f18 = (float) (j + 1) * 2.0F;
                entitycomplexpart.A_();
                entitycomplexpart.setPositionRotation(this.locX - (double) ((f11 * f17 + f15 * f18) * f2), this.locY + (adouble2[1] - adouble[1]) * 1.0D - (double) ((f18 + f17) * f9) + 1.5D, this.locZ + (double) ((f12 * f17 + f16 * f18) * f2), 0.0F, 0.0F);
            }
        }

    }

    private void target() {
        this.bw = false;
        ArrayList<EntityHuman> arraylist = Lists.newArrayList(this.world.players);
        Iterator iterator = arraylist.iterator();

        while (iterator.hasNext()) {
            if (((EntityHuman) iterator.next()).isSpectator()) {
                iterator.remove();
            }
        }

        if (this.random.nextInt(2) == 0 && !this.world.players.isEmpty()) {
            this.bA = ((CraftPlayer) this.getOwner()).getHandle();
        } else {
            boolean flag;
            do {
                this.a = 0.0D;
                this.b = (double) (70.0F + this.random.nextFloat() * 50.0F);
                this.c = 0.0D;
                this.a += (double) (this.random.nextFloat() * 120.0F - 60.0F);
                this.c += (double) (this.random.nextFloat() * 120.0F - 60.0F);
                double d0 = this.locX - this.a;
                double d1 = this.locY - this.b;
                double d2 = this.locZ - this.c;
                flag = d0 * d0 + d1 * d1 + d2 * d2 > 100.0D;
            } while (!flag);

            this.bA = null;
        }

    }

    public World a() {
        return this.world;
    }

    public boolean a(EntityComplexPart entityComplexPart, DamageSource damageSource, float f) {
        if (entityComplexPart != this.head) {
            f = f / 4.0F + 1.0F;
        }

        float f1 = this.yaw * 3.1415927F / 180.0F;
        float f2 = MathHelper.sin(f1);
        float f3 = MathHelper.cos(f1);
        this.a = this.locX + (double) (f2 * 5.0F) + (this.random.nextFloat() - 0.5F) * 2.0F;
        this.b = this.locY + this.random.nextFloat() * 3.0F + 1.0D;
        this.c = this.locZ - (double) (f3 * 5.0F) + (double) ((this.random.nextFloat() - 0.5F) * 2.0F);
        this.bA = null;
        if (!(damageSource.getEntity() instanceof EntityHuman) && damageSource.isExplosion()) {
        }
        return true;
    }
}