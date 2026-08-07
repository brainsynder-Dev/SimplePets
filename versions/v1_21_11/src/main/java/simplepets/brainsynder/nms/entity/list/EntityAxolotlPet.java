package simplepets.brainsynder.nms.entity.list;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.animal.Bucketable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.bsdevelopment.nbt.StorageTagCompound;
import org.bsdevelopment.pluginutils.libs.json.JsonObject;
import simplepets.brainsynder.api.entity.misc.IWaterEntity;
import simplepets.brainsynder.api.entity.passive.IEntityAxolotlPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.api.wrappers.AxolotlVariant;
import simplepets.brainsynder.nms.EntitySelector;
import simplepets.brainsynder.nms.entity.EntityAgeablePet;
import simplepets.brainsynder.nms.utils.PetDataAccess;

import static simplepets.brainsynder.api.pet.PetDataRegistry.Axolotl.PLAY_DEAD;
import static simplepets.brainsynder.api.pet.PetDataRegistry.Axolotl.VARIANT;

/**
 * NMS: {@link net.minecraft.world.entity.animal.axolotl.Axolotl}
 */
// Implement Bucketable so the server resends the entity when the client tries
// to pick it up with a bucket
public class EntityAxolotlPet extends EntityAgeablePet implements IEntityAxolotlPet, IWaterEntity, Bucketable {
    private static final EntityDataAccessor<Integer> DATA_VARIANT = SynchedEntityData.defineId(EntityAxolotlPet.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> DATA_PLAYING_DEAD = SynchedEntityData.defineId(EntityAxolotlPet.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> FROM_BUCKET = SynchedEntityData.defineId(EntityAxolotlPet.class, EntityDataSerializers.BOOLEAN);

    public EntityAxolotlPet(PetType type, PetUser user) {
        super(EntitySelector.AXOLOTL, type, user);
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
        object.setEnum(VARIANT.namespace(), getVariant());
        object.setBoolean(PLAY_DEAD.namespace(), isPlayingDead());
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey(VARIANT.namespace())) setVariant(object.getEnum(VARIANT.namespace(), AxolotlVariant.class, AxolotlVariant.LUCY));
        if (object.hasKey(PLAY_DEAD.namespace())) setPlayingDead(object.getBoolean(PLAY_DEAD.namespace(), false));
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
