package simplepets.brainsynder.nms.entity.list;

import lib.brainsynder.json.JsonObject;
import lib.brainsynder.nbt.StorageTagCompound;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Bucketable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import simplepets.brainsynder.api.entity.passive.IEntityAxolotlPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.api.wrappers.AxolotlVariant;
import simplepets.brainsynder.nms.entity.EntityAgeablePet;
import simplepets.brainsynder.nms.utils.PetDataAccess;

/**
 * NMS: {@link net.minecraft.world.entity.animal.axolotl.Axolotl}
 */
// Implement Bucketable so the server resends the entity when the client tries
// to pick it up with a bucket
public class EntityAxolotlPet extends EntityAgeablePet implements IEntityAxolotlPet, Bucketable {
    private static final EntityDataAccessor<Integer> DATA_VARIANT = SynchedEntityData.defineId(EntityAxolotlPet.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> DATA_PLAYING_DEAD = SynchedEntityData.defineId(EntityAxolotlPet.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> FROM_BUCKET = SynchedEntityData.defineId(EntityAxolotlPet.class, EntityDataSerializers.BOOLEAN);

    public EntityAxolotlPet(PetType type, PetUser user) {
        super(EntityType.AXOLOTL, type, user);
    }

    @Override
    public void populateDataAccess(PetDataAccess dataAccess) {
        super.populateDataAccess(dataAccess);
        dataAccess.define(DATA_VARIANT, 0);
        dataAccess.define(DATA_PLAYING_DEAD, false);
        dataAccess.define(FROM_BUCKET, false);
    }

    @Override
    public void fetchPetData(JsonObject data) {
        super.fetchPetData(data);
        data.add("variant", getVariant().name());
        data.add("playing-dead", isPlayingDead());
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

    @Override
    public boolean fromBucket() {
        return entityData.get(FROM_BUCKET);
    }

    @Override
    public void setFromBucket(boolean b) {
        entityData.set(FROM_BUCKET, b);
    }

    @Override
    public void saveToBucketTag(ItemStack itemStack) {
    }

    @Override
    public void loadFromBucketTag(CompoundTag compoundTag) {
    }

    @Override
    public ItemStack getBucketItemStack() {
        return new ItemStack(Items.WATER_BUCKET);
    }

    @Override
    public SoundEvent getPickupSound() {
        return SoundEvents.BUCKET_FILL_FISH;
    }
}
