package simplepets.brainsynder.nms.entities.v1_12_R1.list;

import net.minecraft.server.v1_12_R1.*;
import simple.brainsynder.nbt.StorageTagCompound;
import simplepets.brainsynder.nms.entities.type.IEntityWolfPet;
import simplepets.brainsynder.nms.entities.v1_12_R1.EntityTameablePet;
import simplepets.brainsynder.pet.IPet;
import simplepets.brainsynder.wrapper.DyeColorWrapper;

public class EntityWolfPet extends EntityTameablePet implements IEntityWolfPet {
    private static final DataWatcherObject<Float> DATA_HEALTH;
    private static final DataWatcherObject<Boolean> bA;
    private static final DataWatcherObject<Integer> COLLAR_COLOR;

    static {
        DATA_HEALTH = DataWatcher.a(EntityWolfPet.class, DataWatcherRegistry.c);
        bA = DataWatcher.a(EntityWolfPet.class, DataWatcherRegistry.h);
        COLLAR_COLOR = DataWatcher.a(EntityWolfPet.class, DataWatcherRegistry.b);
    }

    private boolean wet;
    private boolean shaking;
    private float shakeCount;
    public EntityWolfPet(World world) {
        super(world);
    }
    public EntityWolfPet(World world, IPet pet) {
        super(world, pet);
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = super.asCompound();
        object.setInteger("CollarColor", getColor().ordinal());
        object.setBoolean("Angry", isAngry());
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("Angry")) {
            setAngry(object.getBoolean("Angry"));
        }
        if (object.hasKey("CollarColor"))
            setColor(DyeColorWrapper.values()[object.getInteger("CollarColor")]);
        super.applyCompound(object);
    }

    public void setTamed(boolean flag) {
        if (this.isAngry() && flag) {
            this.setAngry(false);
        }

        super.setTamed(flag);
    }

    public boolean isAngry() {
        return (this.datawatcher.get(TAME_SIT) & 2) != 0;
    }

    public void setAngry(boolean flag) {
        if (this.isTamed() && flag) {
            this.setTamed(false);
        }

        byte b0 = this.datawatcher.get(TAME_SIT);
        if (flag) {
            this.datawatcher.set(TAME_SIT, (byte) (b0 | 2));
        } else {
            this.datawatcher.set(TAME_SIT, (byte) (b0 & -3));
        }

    }

    @Override
    public DyeColorWrapper getColor() {
        return DyeColorWrapper.getByDyeData((byte) ((int) getDataWatcher().get(COLLAR_COLOR)));
    }

    public void setColor(DyeColorWrapper dc) {
        if (isTamed()) {
            this.datawatcher.set(COLLAR_COLOR, (int) dc.getDyeData());
        }

    }

    public void repeatTask() {
        super.repeatTask();
        if (this.inWater) {
            this.wet = true;
            this.shaking = false;
            this.shakeCount = 0.0F;
        } else if ((this.wet || this.shaking) && this.shaking) {

            this.shakeCount += 0.05F;
            if (this.shakeCount - 0.05F >= 2.0F) {
                this.wet = false;
                this.shaking = false;
                this.shakeCount = 0.0F;
            }

            if (this.shakeCount > 0.4F) {
                float f = (float) this.getBoundingBox().b;
                int i = (int) (MathHelper.sin((this.shakeCount - 0.4F) * 3.1415927F) * 7.0F);

                for (int j = 0; j < i; ++j) {
                    float f1 = (this.random.nextFloat() * 2.0F - 1.0F) * this.width * 0.5F;
                    float f2 = (this.random.nextFloat() * 2.0F - 1.0F) * this.width * 0.5F;
                    this.world.addParticle(EnumParticle.WATER_SPLASH, this.locX + (double) f1, (double) (f + 0.8F), this.locZ + (double) f2, this.motX, this.motY, this.motZ);
                }
            }
        }
    }

    protected void registerDatawatchers() {
        super.registerDatawatchers();
        this.datawatcher.register(DATA_HEALTH, this.getHealth());
        this.datawatcher.register(bA, Boolean.FALSE);
        this.datawatcher.register(COLLAR_COLOR, EnumColor.RED.getInvColorIndex());
    }
}
