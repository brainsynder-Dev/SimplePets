package simplepets.brainsynder.versions.v1_17_R1.entity.list;

import lib.brainsynder.nbt.StorageTagCompound;
import net.minecraft.server.v1_16_R3.DataWatcher;
import net.minecraft.server.v1_16_R3.DataWatcherObject;
import net.minecraft.server.v1_16_R3.EntityTypes;
import simplepets.brainsynder.api.entity.passive.IEntityAxolotlPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.api.wrappers.AxolotlVariant;
import simplepets.brainsynder.versions.v1_17_R1.entity.EntityAgeablePet;
import simplepets.brainsynder.versions.v1_17_R1.utils.DataWatcherWrapper;

/**
 * NMS: {@link net.minecraft.server.v1_16_R3.EntityAxolotl}
 */
public class EntityAxolotlPet extends EntityAgeablePet implements IEntityAxolotlPet {
    private static final DataWatcherObject<Integer> DATA_VARIANT;
    private static final DataWatcherObject<Boolean> DATA_PLAYING_DEAD;
    private static final DataWatcherObject<Boolean> FROM_BUCKET;

    public EntityAxolotlPet(PetType type, PetUser user) {
        super(EntityTypes.BAT, type, user);
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
        this.datawatcher.register(DATA_VARIANT, 0);
        this.datawatcher.register(DATA_PLAYING_DEAD, false);
        this.datawatcher.register(FROM_BUCKET, false);
    }

    @Override
    public boolean isPlayingDead() {
        return datawatcher.get(DATA_PLAYING_DEAD);
    }

    @Override
    public void setPlayingDead(boolean playingDead) {
        datawatcher.set(DATA_PLAYING_DEAD, playingDead);
    }

    @Override
    public AxolotlVariant getVariant() {
        return AxolotlVariant.values()[datawatcher.get(DATA_VARIANT)];
    }

    @Override
    public void setVariant(AxolotlVariant variant) {
        datawatcher.set(DATA_VARIANT, variant.ordinal());
    }

    static {
        DATA_VARIANT = DataWatcher.a(EntityAxolotlPet.class, DataWatcherWrapper.INT);
        DATA_PLAYING_DEAD = DataWatcher.a(EntityAxolotlPet.class, DataWatcherWrapper.BOOLEAN);
        FROM_BUCKET = DataWatcher.a(EntityAxolotlPet.class, DataWatcherWrapper.BOOLEAN);
    }
}
