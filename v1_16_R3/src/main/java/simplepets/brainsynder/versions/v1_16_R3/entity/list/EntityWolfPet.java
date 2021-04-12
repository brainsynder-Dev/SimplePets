package simplepets.brainsynder.versions.v1_16_R3.entity.list;

import lib.brainsynder.nbt.StorageTagCompound;
import lib.brainsynder.utils.DyeColorWrapper;
import net.minecraft.server.v1_16_R3.DataWatcher;
import net.minecraft.server.v1_16_R3.DataWatcherObject;
import net.minecraft.server.v1_16_R3.EntityTypes;
import simplepets.brainsynder.api.entity.passive.IEntityWolfPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.versions.v1_16_R3.entity.EntityTameablePet;
import simplepets.brainsynder.versions.v1_16_R3.utils.DataWatcherWrapper;

/**
 * NMS: {@link net.minecraft.server.v1_16_R3.EntityWolf}
 */
public class EntityWolfPet extends EntityTameablePet implements IEntityWolfPet {
    private static final DataWatcherObject<Boolean> BEGGING;
    private static final DataWatcherObject<Integer> COLLAR_COLOR;
    private static final DataWatcherObject<Integer> ANGER_TIME;

    private boolean angry = false;
    private boolean furWet = false;
    private int ticks = -1;

    public EntityWolfPet(PetType type, PetUser user) {
        super(EntityTypes.WOLF, type, user);
    }

    @Override
    public void tick() {
        super.tick();
        if (furWet) {
            if (ticks == -1) {
                this.world.broadcastEntityEffect(this, (byte)8);
                ticks = 0;
            }

            ticks++;
            if (ticks >= 27) {
                ticks = 0;
                this.world.broadcastEntityEffect(this, (byte)8); // shaking effect
            }
        }
        if (this.angry && (datawatcher.get(ANGER_TIME) < 50)) datawatcher.set(ANGER_TIME, 500);
    }

    @Override
    protected void registerDatawatchers() {
        super.registerDatawatchers();
        datawatcher.register(BEGGING, false);
        datawatcher.register(COLLAR_COLOR, DyeColorWrapper.WHITE.getWoolData());
        datawatcher.register(ANGER_TIME, 0);
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound compound = super.asCompound();
        compound.setString("color", getColor().name().toLowerCase());
        compound.setBoolean("angry", isAngry());
        compound.setBoolean("tilted", isHeadTilted());
        compound.setBoolean("shaking", furWet);
        return compound;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("color")) setColor(DyeColorWrapper.getByName(object.getString("color")));
        if (object.hasKey("angry")) setAngry(object.getBoolean("angry", false));
        if (object.hasKey("tilted")) setHeadTilted(object.getBoolean("tilted", false));
        if (object.hasKey("shaking")) setShaking(object.getBoolean("shaking", false));
        super.applyCompound(object);
    }

    @Override
    public boolean isHeadTilted() {
        return datawatcher.get(BEGGING);
    }

    @Override
    public void setHeadTilted(boolean headTilted) {
        datawatcher.set(BEGGING, headTilted);
    }

    @Override
    public boolean isAngry() {
        return datawatcher.get(ANGER_TIME) > 0;
    }

    @Override
    public void setAngry(boolean angry) {
        this.angry = angry;
        datawatcher.set(ANGER_TIME, angry ? 500 : 0);
    }

    @Override
    public DyeColorWrapper getColor() {
        return DyeColorWrapper.getByWoolData((byte) ((int) getDataWatcher().get(COLLAR_COLOR)));
    }

    @Override
    public void setColor(DyeColorWrapper color) {
        this.datawatcher.set(COLLAR_COLOR, color.getWoolData());
    }

    static {
        BEGGING = DataWatcher.a(EntityWolfPet.class, DataWatcherWrapper.BOOLEAN);
        COLLAR_COLOR = DataWatcher.a(EntityWolfPet.class, DataWatcherWrapper.INT);
        ANGER_TIME = DataWatcher.a(EntityWolfPet.class, DataWatcherWrapper.INT);
    }

    @Override
    public boolean isShaking() {
        return furWet;
    }

    @Override
    public void setShaking(boolean shaking) {
        this.furWet = shaking;

        if (!shaking) this.world.broadcastEntityEffect(this, (byte) 56);
        this.ticks = -1;
    }
}
