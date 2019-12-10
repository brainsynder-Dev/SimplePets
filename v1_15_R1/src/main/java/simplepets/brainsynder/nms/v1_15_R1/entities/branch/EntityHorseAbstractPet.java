package simplepets.brainsynder.nms.v1_15_R1.entities.branch;

import net.minecraft.server.v1_15_R1.*;
import simple.brainsynder.nbt.StorageTagCompound;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.entity.misc.IHorseAbstract;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.v1_15_R1.entities.AgeableEntityPet;
import simplepets.brainsynder.nms.v1_15_R1.utils.DataWatcherWrapper;
import simplepets.brainsynder.player.PetOwner;

import java.util.Optional;
import java.util.UUID;

/**
 * NMS: {@link net.minecraft.server.v1_15_R1.EntityHorseAbstract}
 */
public abstract class EntityHorseAbstractPet extends AgeableEntityPet implements IHorseAbstract {
    private static final DataWatcherObject<Byte> STATUS;
    private static final DataWatcherObject<Optional<UUID>> OWNER_UNIQUE_ID;
    private boolean saddle = false;

    static {
        STATUS = DataWatcher.a(EntityHorseAbstractPet.class, DataWatcherWrapper.BYTE);
        OWNER_UNIQUE_ID = DataWatcher.a(EntityHorseAbstractPet.class, DataWatcherWrapper.UUID);
    }

    public EntityHorseAbstractPet(EntityTypes<? extends EntityCreature> type, World world) {
        super(type, world);
    }

    public EntityHorseAbstractPet(EntityTypes<? extends EntityCreature> type, World world, IPet pet) {
        super(type, world, pet);
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = super.asCompound();
        object.setBoolean("saddled", isSaddled());
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        super.applyCompound(object);
        if (object.hasKey("saddled")) {
            setSaddled(object.getBoolean("saddled"));
        }
    }

    @Override
    protected void registerDatawatchers() {
        super.registerDatawatchers();
        this.datawatcher.register(STATUS, (byte) 0);
        this.datawatcher.register(OWNER_UNIQUE_ID, Optional.empty());
    }

    @Override
    public boolean isCollidable() {
        return false;
    }

    @Override
    public boolean isSaddled() {
        return getFlag(4);
    }

    @Override
    public void setSaddled(boolean flag) {
        this.setFlag(4, flag);
        this.a(SoundEffects.ENTITY_HORSE_SADDLE, 0.5F, 1.0F);
        PetCore.get().getInvLoaders().PET_DATA.update(PetOwner.getPetOwner(getOwner()));
    }

    @Override
    public boolean getFlag(int i) {
        return (this.datawatcher.get(STATUS) & i) != 0;
    }

    /**
     * 2: Is tamed
     * 4: Saddle
     * 8: Has Chest - Separate datawatcher in 1.11+
     * 16: Bred - 8 in 1.11+
     * 32: Eating haystack
     * 64: Rear
     * 128: Mouth open
     */
    @Override
    public void setFlag(int i, boolean flag) {
        byte b0 = this.datawatcher.get(STATUS);
        if(flag){
            this.datawatcher.set(STATUS, (byte) (b0 | i));
        }else{
            this.datawatcher.set(STATUS, (byte) (b0 & (~i)));
        }
    }
}
