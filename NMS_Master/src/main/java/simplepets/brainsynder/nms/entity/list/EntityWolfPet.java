package simplepets.brainsynder.nms.entity.list;

import lib.brainsynder.nbt.StorageTagCompound;
import lib.brainsynder.utils.DyeColorWrapper;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import simplepets.brainsynder.api.entity.passive.IEntityWolfPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.nms.VersionTranslator;
import simplepets.brainsynder.nms.entity.EntityTameablePet;

/**
 * NMS: {@link net.minecraft.server.v1_16_R3.EntityWolf}
 */
public class EntityWolfPet extends EntityTameablePet implements IEntityWolfPet {
    private static final EntityDataAccessor<Boolean> BEGGING;
    private static final EntityDataAccessor<Integer> COLLAR_COLOR;
    private static final EntityDataAccessor<Integer> ANGER_TIME;

    private boolean angry = false;
    private boolean furWet = false;
    private int ticks = -1;

    public EntityWolfPet(PetType type, PetUser user) {
        super(EntityType.WOLF, type, user);
    }

    @Override
    public void tick() {
        super.tick();
        if (furWet) {
            if (ticks == -1) {
                VersionTranslator.getEntityLevel(this).broadcastEntityEvent(this, (byte)8); // Wolf shaking
                ticks = 0;
            }

            ticks++;
            if (ticks >= 27) {
                ticks = 0;
                VersionTranslator.getEntityLevel(this).broadcastEntityEvent(this, (byte)8); // Wolf shaking
            }
        }
        if (this.angry && (entityData.get(ANGER_TIME) < 50)) entityData.set(ANGER_TIME, 500);
    }

    @Override
    protected void registerDatawatchers() {
        super.registerDatawatchers();
        entityData.define(BEGGING, false);
        entityData.define(COLLAR_COLOR, DyeColorWrapper.WHITE.getWoolData());
        entityData.define(ANGER_TIME, 0);
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
        return entityData.get(BEGGING);
    }

    @Override
    public void setHeadTilted(boolean headTilted) {
        entityData.set(BEGGING, headTilted);
    }

    @Override
    public boolean isAngry() {
        return entityData.get(ANGER_TIME) > 0;
    }

    @Override
    public void setAngry(boolean angry) {
        this.angry = angry;
        entityData.set(ANGER_TIME, angry ? 500 : 0);
    }

    @Override
    public DyeColorWrapper getColor() {
        return DyeColorWrapper.getByWoolData((byte) ((int) entityData.get(COLLAR_COLOR)));
    }

    @Override
    public void setColor(DyeColorWrapper color) {
        this.entityData.set(COLLAR_COLOR, color.getWoolData());
    }

    static {
        BEGGING = SynchedEntityData.defineId(EntityWolfPet.class, EntityDataSerializers.BOOLEAN);
        COLLAR_COLOR = SynchedEntityData.defineId(EntityWolfPet.class, EntityDataSerializers.INT);
        ANGER_TIME = SynchedEntityData.defineId(EntityWolfPet.class, EntityDataSerializers.INT);
    }

    @Override
    public boolean isShaking() {
        return furWet;
    }

    @Override
    public void setShaking(boolean shaking) {
        this.furWet = shaking;

        if (!shaking) VersionTranslator.getEntityLevel(this).broadcastEntityEvent(this, (byte)56);
        this.ticks = -1;
    }
}
