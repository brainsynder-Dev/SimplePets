package simplepets.brainsynder.versions.v1_17_R1.entity.list;

import lib.brainsynder.nbt.StorageTagCompound;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import simplepets.brainsynder.api.entity.hostile.IEntityVexPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.versions.v1_17_R1.entity.EntityPet;

/**
 * NMS: {@link net.minecraft.server.v1_16_R3.EntityVex}
 */
public class EntityVexPet extends EntityPet implements IEntityVexPet {
    protected static final EntityDataAccessor<Byte> VEX_FLAGS;

    public EntityVexPet(PetType type, PetUser user) {
        super(EntityType.VEX, type, user);
    }

    protected void registerDatawatchers() {
        super.registerDatawatchers();
        this.entityData.define(VEX_FLAGS, (byte) 0);
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
    public boolean isPowered() {
        return (entityData.get(VEX_FLAGS) & 1) != 0;
    }

    @Override
    public void setPowered(boolean value) {
        byte flag = this.entityData.get(VEX_FLAGS);
        int j;
        if (value) {
            j = flag | 1;
        } else {
            j = flag & ~1;
        }

        this.entityData.set(VEX_FLAGS, (byte) (j & 255));
    }

    static {
        VEX_FLAGS = SynchedEntityData.defineId(EntityVexPet.class, EntityDataSerializers.BYTE);
    }
}
