package simplepets.brainsynder.versions.v1_16_R3.entity.list;

import lib.brainsynder.nbt.StorageTagCompound;
import net.minecraft.server.v1_16_R3.DataWatcher;
import net.minecraft.server.v1_16_R3.DataWatcherObject;
import net.minecraft.server.v1_16_R3.EntityTypes;
import simplepets.brainsynder.api.entity.hostile.IEntityCreeperPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.versions.v1_16_R3.entity.EntityPet;
import simplepets.brainsynder.versions.v1_16_R3.utils.DataWatcherWrapper;

/**
 * NMS: {@link net.minecraft.server.v1_16_R3.EntityCreeper}
 */
public class EntityCreeperPet extends EntityPet implements IEntityCreeperPet {
    protected static final DataWatcherObject<Integer> STATE;
    protected static final DataWatcherObject<Boolean> POWERED;
    protected static final DataWatcherObject<Boolean> IGNITED;

    public EntityCreeperPet(PetType type, PetUser user) {
        super(EntityTypes.CREEPER, type, user);
    }

    @Override
    protected void registerDatawatchers() {
        super.registerDatawatchers();
        this.datawatcher.register(STATE, -1);
        this.datawatcher.register(POWERED, false);
        this.datawatcher.register(IGNITED, false);
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = super.asCompound();
        object.setBoolean("powered", isPowered());
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("powered")) setPowered(object.getBoolean("powered"));
        super.applyCompound(object);
    }

    @Override
    public boolean isIgnited() {
        return datawatcher.get(IGNITED);
    }

    @Override
    public void setIgnited(boolean flag) {
        this.datawatcher.set(IGNITED, flag);
    }

    @Override
    public boolean isPowered() {
        return datawatcher.get(POWERED);
    }

    @Override
    public void setPowered(boolean flag) {
        this.datawatcher.set(POWERED, flag);
    }

    static {
        STATE = DataWatcher.a(EntityCreeperPet.class, DataWatcherWrapper.INT);
        POWERED = DataWatcher.a(EntityCreeperPet.class, DataWatcherWrapper.BOOLEAN);
        IGNITED = DataWatcher.a(EntityCreeperPet.class, DataWatcherWrapper.BOOLEAN);
    }
}
