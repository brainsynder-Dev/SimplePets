package simplepets.brainsynder.nms.entities.v1_10_R1.list;

import net.minecraft.server.v1_10_R1.DataWatcher;
import net.minecraft.server.v1_10_R1.DataWatcherObject;
import net.minecraft.server.v1_10_R1.DataWatcherRegistry;
import net.minecraft.server.v1_10_R1.World;
import simplepets.brainsynder.nms.entities.type.IEntitySnowmanPet;
import simplepets.brainsynder.nms.entities.v1_10_R1.EntityPet;
import simplepets.brainsynder.pet.IPet;

@Deprecated
public class EntitySnowmanPet extends EntityPet implements IEntitySnowmanPet {
    private static final DataWatcherObject<Byte> PUMPKIN;

    static {
        PUMPKIN = DataWatcher.a(EntitySnowmanPet.class, DataWatcherRegistry.a);
    }

    public EntitySnowmanPet(World world, IPet pet) {
        super(world, pet);
    }

    protected void initDataWatcher() {
        super.initDataWatcher();
        this.datawatcher.register(PUMPKIN, (byte) 0);
    }

    public boolean hasPumpkin() {
        return (this.datawatcher.get(PUMPKIN).byteValue() & 16) != 0;
    }

    public void setHasPumpkin(boolean flag) {
        byte b0 = this.datawatcher.get(PUMPKIN).byteValue();
        if (flag) {
            this.datawatcher.set(PUMPKIN, Byte.valueOf((byte) (b0 | 16)));
        } else {
            this.datawatcher.set(PUMPKIN, Byte.valueOf((byte) (b0 & -17)));
        }
    }
}
