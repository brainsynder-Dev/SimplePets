package simplepets.brainsynder.versions.v1_16_R3.entity.list;

import lib.brainsynder.nbt.StorageTagCompound;
import net.minecraft.server.v1_16_R3.DataWatcher;
import net.minecraft.server.v1_16_R3.DataWatcherObject;
import net.minecraft.server.v1_16_R3.EntityTypes;
import simplepets.brainsynder.api.entity.passive.IEntityBeePet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.versions.v1_16_R3.entity.EntityAgeablePet;
import simplepets.brainsynder.versions.v1_16_R3.utils.DataWatcherWrapper;

/**
 * NMS: {@link net.minecraft.server.v1_16_R3.EntityBee}
 */
public class EntityBeePet extends EntityAgeablePet implements IEntityBeePet {
    private static final DataWatcherObject<Byte> FLAGS;
    private static final DataWatcherObject<Integer> ANGER;

    public EntityBeePet(PetType type, PetUser user) {
        super(EntityTypes.BEE, type, user);
    }

    @Override
    protected void registerDatawatchers() {
        super.registerDatawatchers();
        this.datawatcher.register(FLAGS, (byte) 4);
        this.datawatcher.register(ANGER, 0);
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = super.asCompound();
        object.setBoolean("angry", isAngry());
        object.setBoolean("nectar", hasNectar());
        object.setBoolean("stinger", hasStung());
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("angry")) setAngry(object.getBoolean("angry"));
        if (object.hasKey("nectar")) setHasNectar(object.getBoolean("nectar"));
        if (object.hasKey("stinger")) setHasStung(object.getBoolean("stinger"));
        super.applyCompound(object);
    }

    @Override
    public boolean isAngry() {
        return datawatcher.get(ANGER) > 0;
    }

    @Override
    public void setAngry(boolean angry) {
        datawatcher.set(ANGER, (angry) ? 25562256 : 0);
    }

    @Override
    public void setSpecialFlag(int flag, boolean value) {
        byte flagByte = datawatcher.get(FLAGS);
        if (value) {
            flagByte = (byte)(flagByte | flag);
        } else {
            flagByte = (byte)(flagByte & ~flag);
        }

        if (flagByte != datawatcher.get(FLAGS)) this.datawatcher.set(FLAGS, flagByte);
    }

    @Override
    public boolean getSpecialFlag(int flag) {
        return (this.datawatcher.get(FLAGS) & flag) != 0;
    }


    static {
        FLAGS = DataWatcher.a(EntityBeePet.class, DataWatcherWrapper.BYTE);
        ANGER = DataWatcher.a(EntityBeePet.class, DataWatcherWrapper.INT);
    }
}
