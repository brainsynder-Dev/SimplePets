package simplepets.brainsynder.nms.v1_16_R2.entities.list;

import lib.brainsynder.nbt.StorageTagCompound;
import net.minecraft.server.v1_16_R2.*;
import simplepets.brainsynder.api.Size;
import simplepets.brainsynder.api.entity.passive.IEntityBeePet;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.v1_16_R2.entities.AgeableEntityPet;
import simplepets.brainsynder.nms.v1_16_R2.utils.DataWatcherWrapper;

/**
 * NMS: {@link EntityBee}
 */
@Size(length = 0.9F)
public class EntityBeePet extends AgeableEntityPet implements IEntityBeePet {
    private static final DataWatcherObject<Byte> FLAGS;
    private static final DataWatcherObject<Integer> ANGER;

    static {
        FLAGS = DataWatcher.a(EntityBeePet.class, DataWatcherWrapper.BYTE);
        ANGER = DataWatcher.a(EntityBeePet.class, DataWatcherWrapper.INT);
    }

    public EntityBeePet(World world) {
        super(EntityTypes.BEE, world);
    }

    public EntityBeePet(World world, IPet pet) {
        super(EntityTypes.BEE, world, pet);
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
        object.setBoolean("stung", hasStung());
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("angry")) setAngry(object.getBoolean("angry"));
        if (object.hasKey("nectar")) setHasNectar(object.getBoolean("nectar"));
        if (object.hasKey("stung")) setHasStung(object.getBoolean("stung"));
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

    // java you are a goddamn nightmare and i hate you so much - TM
    public void setSpecialFlag(int i, boolean flag) {
        byte value = datawatcher.get(FLAGS);
        if (flag) {
            value = (byte)(value | i);
        } else {
            value = (byte)(value & ~i);
        }

        if (value != datawatcher.get(FLAGS)) {
            this.datawatcher.set(FLAGS, value);
            //Bukkit.broadcastMessage("Bee DataWatcher: "+datawatcher.get(FLAGS));
        }
    }

    @Override
    public boolean getSpecialFlag(int i) {
        return (this.datawatcher.get(FLAGS) & i) != 0;
    }
}
