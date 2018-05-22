package simplepets.brainsynder.nms.entities.v1_12_R1.branch;

import net.minecraft.server.v1_12_R1.DataWatcher;
import net.minecraft.server.v1_12_R1.DataWatcherObject;
import net.minecraft.server.v1_12_R1.DataWatcherRegistry;
import net.minecraft.server.v1_12_R1.World;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.entities.v1_12_R1.EntityPet;

public abstract class EntityIllagerAbstractPet extends EntityPet {
    protected static final DataWatcherObject<Byte> AGGRESSIVE;

    static {
        AGGRESSIVE = DataWatcher.a(EntityIllagerAbstractPet.class, DataWatcherRegistry.a);
    }

    public EntityIllagerAbstractPet(World world) {
        super(world);
    }

    public EntityIllagerAbstractPet(World world, IPet pet) {
        super(world, pet);
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