package simplepets.brainsynder.nms.v1_11_R1.entities;

import com.google.common.base.Optional;
import net.minecraft.server.v1_11_R1.DataWatcher;
import net.minecraft.server.v1_11_R1.DataWatcherObject;
import net.minecraft.server.v1_11_R1.DataWatcherRegistry;
import net.minecraft.server.v1_11_R1.World;
import simple.brainsynder.nbt.StorageTagCompound;
import simplepets.brainsynder.api.entity.misc.ITameable;
import simplepets.brainsynder.api.pet.IPet;

import java.util.UUID;

public class EntityTameablePet extends AgeableEntityPet implements ITameable {
    protected static final DataWatcherObject<Byte> TAME_SIT;
    protected static final DataWatcherObject<Optional<UUID>> OWNER;

    static {
        TAME_SIT = DataWatcher.a(EntityTameablePet.class, DataWatcherRegistry.a);
        OWNER = DataWatcher.a(EntityTameablePet.class, DataWatcherRegistry.m);
    }


    public EntityTameablePet(World world) {
        super(world);
    }

    public EntityTameablePet(World world, IPet pet) {
        super(world, pet);
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

    protected void registerDatawatchers() {
        super.registerDatawatchers();
        this.datawatcher.register(TAME_SIT, (byte) 0);
        this.datawatcher.register(OWNER, Optional.absent());
    }

    public boolean isTamed() {
        return (this.datawatcher.get(TAME_SIT) & 4) != 0;
    }

    public void setTamed(boolean paramBoolean) {
        byte i = this.datawatcher.get(TAME_SIT);
        if (paramBoolean) {
            this.datawatcher.set(TAME_SIT, (byte) (i | 4));
        } else {
            this.datawatcher.set(TAME_SIT, (byte) (i & -5));
        }

    }

    public boolean isSitting() {
        return (this.datawatcher.get(TAME_SIT) & 1) != 0;
    }

    public void setSitting(boolean paramBoolean) {
        byte i = this.datawatcher.get(TAME_SIT);
        if (paramBoolean) {
            this.datawatcher.set(TAME_SIT, (byte) (i | 1));
        } else {
            this.datawatcher.set(TAME_SIT, (byte) (i & -2));
        }

    }

    public UUID getOwnerUUID() {
        return (UUID) ((Optional) this.datawatcher.get(OWNER)).orNull();
    }

    public void setOwnerUUID(UUID paramUUID) {
        this.datawatcher.set(OWNER, Optional.fromNullable(paramUUID));
    }
}
