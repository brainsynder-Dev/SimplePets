package simplepets.brainsynder.versions.v1_17_R1.entity.list;

import lib.brainsynder.nbt.StorageTagCompound;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import simplepets.brainsynder.api.entity.passive.IEntityAxolotlPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.api.wrappers.AxolotlVariant;
import simplepets.brainsynder.versions.v1_17_R1.entity.EntityAgeablePet;

/**
 * NMS: {@link net.minecraft.server.v1_16_R3.EntityAxolotl}
 */
public class EntityAxolotlPet extends EntityAgeablePet implements IEntityAxolotlPet {
    private static final EntityDataAccessor<Integer> DATA_VARIANT;
    private static final EntityDataAccessor<Boolean> DATA_PLAYING_DEAD;
    private static final EntityDataAccessor<Boolean> FROM_BUCKET;

    public EntityAxolotlPet(PetType type, PetUser user) {
        super(EntityType.AXOLOTL, type, user);
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = super.asCompound();
        object.setEnum("variant", getVariant());
        object.setBoolean("playing_dead", isPlayingDead());
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("variant")) setVariant(object.getEnum("variant", AxolotlVariant.class, AxolotlVariant.LUCY));
        if (object.hasKey("playing_dead")) setPlayingDead(object.getBoolean("playing_dead", false));
        super.applyCompound(object);
    }

    @Override
    protected void registerDatawatchers() {
        super.registerDatawatchers();
        entityData.define(DATA_VARIANT, 0);
        entityData.define(DATA_PLAYING_DEAD, false);
        entityData.define(FROM_BUCKET, false);
    }

    @Override
    public boolean isPlayingDead() {
        return entityData.get(DATA_PLAYING_DEAD);
    }

    @Override
    public void setPlayingDead(boolean playingDead) {
        entityData.set(DATA_PLAYING_DEAD, playingDead);
    }

    @Override
    public AxolotlVariant getVariant() {
        return AxolotlVariant.values()[entityData.get(DATA_VARIANT)];
    }

    @Override
    public void setVariant(AxolotlVariant variant) {
        entityData.set(DATA_VARIANT, variant.ordinal());
    }

    static {
        DATA_VARIANT = SynchedEntityData.defineId(EntityAxolotlPet.class, EntityDataSerializers.INT);
        DATA_PLAYING_DEAD = SynchedEntityData.defineId(EntityAxolotlPet.class, EntityDataSerializers.BOOLEAN);
        FROM_BUCKET = SynchedEntityData.defineId(EntityAxolotlPet.class, EntityDataSerializers.BOOLEAN);
    }
}
