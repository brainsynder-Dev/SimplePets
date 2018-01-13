package simplepets.brainsynder.nms.entities.v1_10_R1.list;

import net.minecraft.server.v1_10_R1.DataWatcher;
import net.minecraft.server.v1_10_R1.DataWatcherObject;
import net.minecraft.server.v1_10_R1.DataWatcherRegistry;
import net.minecraft.server.v1_10_R1.World;
import simple.brainsynder.nbt.StorageTagCompound;
import simplepets.brainsynder.api.entity.hostile.IEntityGuardianPet;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.entities.v1_10_R1.EntityPet;

/**
 * @Deprecated Will be removed when MC version 1.13 is released
 */
@Deprecated
public class EntityGuardianPet extends EntityPet implements IEntityGuardianPet {
    private static final DataWatcherObject<Byte> a;
    private static final DataWatcherObject<Integer> b;

    static {
        a = DataWatcher.a(EntityGuardianPet.class, DataWatcherRegistry.a);
        b = DataWatcher.a(EntityGuardianPet.class, DataWatcherRegistry.b);
    }

    public EntityGuardianPet(World world, IPet pet) {
        super(world, pet);
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = super.asCompound();
        object.setBoolean("Elder", isElder());
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("Elder"))
            setElder(object.getBoolean("Elder"));
        super.applyCompound(object);
    }

    @Override
    protected void initDataWatcher() {
        super.initDataWatcher();
        this.datawatcher.register(a, (byte) 0);
        this.datawatcher.register(b, 0);
    }

    public boolean isElder() {
        return this.a(4);
    }

    public void setElder(boolean var1) {
        this.a(4, var1);
    }

    private boolean a(int var1) {
        return (this.datawatcher.get(a) & var1) != 0;
    }

    private void a(int var1, boolean var2) {
        byte var3 = this.datawatcher.get(a);
        if (var2) {
            this.datawatcher.set(a, (byte) (var3 | var1));
        } else {
            this.datawatcher.set(a, (byte) (var3 & ~var1));
        }

    }
}
