package simplepets.brainsynder.versions.v1_16_R3.entity;

import lib.brainsynder.nbt.StorageTagCompound;
import net.minecraft.server.v1_16_R3.DataWatcher;
import net.minecraft.server.v1_16_R3.DataWatcherObject;
import net.minecraft.server.v1_16_R3.EntityInsentient;
import net.minecraft.server.v1_16_R3.EntityTypes;
import simplepets.brainsynder.api.entity.misc.ITameable;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.versions.v1_16_R3.utils.DataWatcherWrapper;

import java.util.Optional;
import java.util.UUID;

public class EntityTameablePet extends EntityAgeablePet implements ITameable {
    private static final DataWatcherObject<Byte> TAMEABLE_FLAGS;
    private static final DataWatcherObject<Optional<UUID>> OWNER_UUID;

    public EntityTameablePet(EntityTypes<? extends EntityInsentient> entitytypes, PetType type, PetUser user) {
        super(entitytypes, type, user);
    }

    @Override
    protected void registerDatawatchers() {
        super.registerDatawatchers();
        this.datawatcher.register(TAMEABLE_FLAGS, (byte) 0);
        this.datawatcher.register(OWNER_UUID, Optional.empty());
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = super.asCompound();
        object.setBoolean("tamed", isTamed());
        object.setBoolean("sitting", isSitting());
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("tamed")) setTamed(object.getBoolean("tamed"));
        if (object.hasKey("sitting")) setSitting(object.getBoolean("sitting"));
        super.applyCompound(object);
    }

    @Override
    public boolean isTamed() {
        return (this.datawatcher.get(TAMEABLE_FLAGS) & 4) != 0;
    }

    @Override
    public void setTamed(boolean flag) {
        byte i = this.datawatcher.get(TAMEABLE_FLAGS);
        if (flag) {
            this.datawatcher.set(TAMEABLE_FLAGS, (byte) (i | 4));
        } else {
            this.datawatcher.set(TAMEABLE_FLAGS, (byte) (i & -5));
        }
    }

    @Override
    public boolean isSitting() {
        return (this.datawatcher.get(TAMEABLE_FLAGS) & 1) != 0;
    }

    @Override
    public void setSitting(boolean flag) {
        byte i = this.datawatcher.get(TAMEABLE_FLAGS);
        if (flag) {
            this.datawatcher.set(TAMEABLE_FLAGS, (byte) (i | 1));
        } else {
            this.datawatcher.set(TAMEABLE_FLAGS, (byte) (i & -2));
        }
    }

    static {
        TAMEABLE_FLAGS = DataWatcher.a(EntityTameablePet.class, DataWatcherWrapper.BYTE);
        OWNER_UUID = DataWatcher.a(EntityTameablePet.class, DataWatcherWrapper.UUID);
    }
}
