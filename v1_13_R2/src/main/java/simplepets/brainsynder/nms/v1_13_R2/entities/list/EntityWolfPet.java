package simplepets.brainsynder.nms.v1_13_R2.entities.list;

import net.minecraft.server.v1_13_R2.DataWatcher;
import net.minecraft.server.v1_13_R2.DataWatcherObject;
import net.minecraft.server.v1_13_R2.EnumColor;
import net.minecraft.server.v1_13_R2.World;
import simple.brainsynder.nbt.StorageTagCompound;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.Size;
import simplepets.brainsynder.api.entity.passive.IEntityWolfPet;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.v1_13_R2.entities.EntityTameablePet;
import simplepets.brainsynder.nms.v1_13_R2.registry.Types;
import simplepets.brainsynder.nms.v1_13_R2.utils.DataWatcherWrapper;
import simplepets.brainsynder.player.PetOwner;
import simplepets.brainsynder.wrapper.DyeColorWrapper;

/**
 * NMS: {@link net.minecraft.server.v1_13_R2.EntityWolf}
 */
@Size(width = 0.6F, length = 0.8F)
public class EntityWolfPet extends EntityTameablePet implements IEntityWolfPet {
    private static final DataWatcherObject<Float> DATA_HEALTH;
    private static final DataWatcherObject<Boolean> BEGGING;
    private static final DataWatcherObject<Integer> COLLAR_COLOR;

    static {
        DATA_HEALTH = DataWatcher.a(EntityWolfPet.class, DataWatcherWrapper.FLOAT);
        BEGGING = DataWatcher.a(EntityWolfPet.class, DataWatcherWrapper.BOOLEAN);
        COLLAR_COLOR = DataWatcher.a(EntityWolfPet.class, DataWatcherWrapper.INT);
    }

    public EntityWolfPet(World world) {
        super(Types.WOLF, world);
    }
    public EntityWolfPet(World world, IPet pet) {
        super(Types.WOLF, world, pet);
    }

    protected void registerDatawatchers() {
        super.registerDatawatchers();
        this.datawatcher.register(DATA_HEALTH, this.getHealth());
        this.datawatcher.register(BEGGING, Boolean.FALSE);
        this.datawatcher.register(COLLAR_COLOR, EnumColor.RED.getColorIndex());
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = super.asCompound();
        object.setInteger("color", getColor().ordinal());
        object.setBoolean("angry", isAngry());
        object.setBoolean("tilted", isHeadTilted());
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("angry")) setAngry(object.getBoolean("angry"));
        if (object.hasKey("tilted")) setHeadTilted(object.getBoolean("tilted"));
        if (object.hasKey("color")) setColor(DyeColorWrapper.values()[object.getInteger("color")]);
        super.applyCompound(object);
    }

    public void setTamed(boolean flag) {
        if (this.isAngry() && flag) {
            this.setAngry(false);
        }

        super.setTamed(flag);
        PetCore.get().getInvLoaders().PET_DATA.update(PetOwner.getPetOwner(getOwner()));
    }

    @Override
    public boolean isAngry() {
        return (this.datawatcher.get(TAME_SIT) & 2) != 0;
    }

    @Override
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
        PetCore.get().getInvLoaders().PET_DATA.update(PetOwner.getPetOwner(getOwner()));

    }

    @Override
    public DyeColorWrapper getColor() {
        return DyeColorWrapper.getByDyeData((byte) ((int) getDataWatcher().get(COLLAR_COLOR)));
    }

    public void setColor(DyeColorWrapper dc) {
        if (isTamed()) {
            this.datawatcher.set(COLLAR_COLOR, (int) dc.getDyeData());
            PetCore.get().getInvLoaders().PET_DATA.update(PetOwner.getPetOwner(getOwner()));
        }
    }

    @Override
    public boolean isHeadTilted() {
        return datawatcher.get(BEGGING);
    }

    @Override
    public void setHeadTilted(boolean var) {
        datawatcher.set(BEGGING, var);
    }
}