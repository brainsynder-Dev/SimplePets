package simplepets.brainsynder.nms.v1_14_R1.entities.branch;

import net.minecraft.server.v1_14_R1.*;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.v1_14_R1.utils.DataWatcherWrapper;

/**
 * NMS: {@link net.minecraft.server.v1_14_R1.EntityIllagerAbstract}
 */
public abstract class EntityIllagerAbstractPet extends EntityRaiderPet {
    protected static final DataWatcherObject<Byte> AGGRESSIVE;

    static {
        AGGRESSIVE = DataWatcher.a(EntityIllagerAbstractPet.class, DataWatcherWrapper.BYTE);
    }

    public EntityIllagerAbstractPet(EntityTypes<? extends EntityCreature> type, World world) {
        super(type, world);
    }

    public EntityIllagerAbstractPet(EntityTypes<? extends EntityCreature> type, World world, IPet pet) {
        super(type, world, pet);
    }

    @Override
    protected void registerDatawatchers() {
        super.registerDatawatchers();
        this.datawatcher.register(AGGRESSIVE, (byte) 0);
    }

    protected void a(int var1, boolean var2) {
        byte var3 = this.datawatcher.get(AGGRESSIVE);
        int var4;
        if (var2) {
            var4 = var3 | var1;
        } else {
            var4 = var3 & ~var1;
        }

        this.datawatcher.set(AGGRESSIVE, (byte) (var4 & 255));
    }
}
