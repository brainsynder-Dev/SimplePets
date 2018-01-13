package simplepets.brainsynder.nms.entities.v1_9_R1;

import com.google.common.base.Optional;
import net.minecraft.server.v1_9_R1.DataWatcher;
import net.minecraft.server.v1_9_R1.DataWatcherObject;
import net.minecraft.server.v1_9_R1.DataWatcherRegistry;
import net.minecraft.server.v1_9_R1.World;
import simple.brainsynder.nbt.StorageTagCompound;
import simplepets.brainsynder.api.entity.ITameable;
import simplepets.brainsynder.api.pet.IPet;

import java.util.UUID;

/**
 * @Deprecated Will be removed when MC version 1.13 is released
 */
@Deprecated
public class EntityTameablePet extends AgeableEntityPet implements ITameable {
    protected static final DataWatcherObject<Byte> TAME_SIT;
    protected static final DataWatcherObject<Optional<UUID>> bw;

    static {
        TAME_SIT = DataWatcher.a(EntityTameablePet.class, DataWatcherRegistry.a);
        bw = DataWatcher.a(EntityTameablePet.class, DataWatcherRegistry.m);
    }

    public EntityTameablePet(World world, IPet pet) {
        super(world, pet);
    }

    @Override
    protected void initDataWatcher() {
        super.initDataWatcher();
        this.datawatcher.register(TAME_SIT, Byte.valueOf((byte) 0));
        this.datawatcher.register(bw, Optional.absent());
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = super.asCompound();
        object.setBoolean("Tamed", isTamed());
        object.setBoolean("Sitting", isSitting());
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("Tamed")) {
            setTamed(object.getBoolean("Tamed"));
        }
        if (object.hasKey("Sitting")) {
            setSitting(object.getBoolean("Sitting"));
        }
        super.applyCompound(object);
    }

    public boolean isTamed() {
        return (this.datawatcher.get(TAME_SIT).byteValue() & 4) != 0;
    }

    public void setTamed(boolean paramBoolean) {
        byte i = this.datawatcher.get(TAME_SIT).byteValue();
        if (paramBoolean) {
            this.datawatcher.set(TAME_SIT, Byte.valueOf((byte) (i | 4)));
        } else {
            this.datawatcher.set(TAME_SIT, Byte.valueOf((byte) (i & -5)));
        }

    }

    public boolean isSitting() {
        return (this.datawatcher.get(TAME_SIT).byteValue() & 1) != 0;
    }

    public void setSitting(boolean paramBoolean) {
        byte i = this.datawatcher.get(TAME_SIT).byteValue();
        if (paramBoolean) {
            this.datawatcher.set(TAME_SIT, Byte.valueOf((byte) (i | 1)));
        } else {
            this.datawatcher.set(TAME_SIT, Byte.valueOf((byte) (i & -2)));
        }

    }

    public UUID getOwnerUUID() {
        return (UUID) ((Optional) this.datawatcher.get(bw)).orNull();
    }

    public void setOwnerUUID(UUID paramUUID) {
        this.datawatcher.set(bw, Optional.fromNullable(paramUUID));
    }

}
