package simplepets.brainsynder.nms.entities.v1_11_R1;

import net.minecraft.server.v1_11_R1.DataWatcher;
import net.minecraft.server.v1_11_R1.DataWatcherObject;
import net.minecraft.server.v1_11_R1.DataWatcherRegistry;
import net.minecraft.server.v1_11_R1.World;
import simple.brainsynder.nbt.StorageTagCompound;
import simplepets.brainsynder.pet.IPet;

public abstract class AgeableEntityPet extends EntityPet {
    private static final DataWatcherObject<Boolean> BABY;

    static {
        BABY = DataWatcher.a(AgeableEntityPet.class, DataWatcherRegistry.h);
    }

    protected int age;
    private boolean ageLocked = true;

    public AgeableEntityPet(World world) {
        super(world);
    }

    public AgeableEntityPet(World world, IPet pet) {
        super(world, pet);
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = super.asCompound();
        object.setBoolean("baby", isBaby());
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("baby")) {
            setBaby(object.getBoolean("baby"));
        }
        super.applyCompound(object);
    }

    public int getAge() {
        return this.datawatcher.get(BABY) ? -1 : this.age;
    }

    public void setAge(int i) {
        this.setAge(i, false);
    }

    public void setAge(int i, boolean flag) {
        int j = this.getAge();
        j += i * 20;
        if (j > 0) {
            j = 0;
        }

        this.setAgeRaw(j);
    }

    public void setAgeRaw(int i) {
        this.datawatcher.set(BABY, i < 0);
        this.age = i;
    }

    public boolean isAgeLocked() {
        return this.ageLocked;
    }

    public void setAgeLocked(boolean ageLocked) {
        this.ageLocked = ageLocked;
    }

    @Override
    protected void registerDatawatchers() {
        super.registerDatawatchers();
        this.datawatcher.register(BABY, Boolean.FALSE);
    }

    public void U() {
        super.U();
        if (!this.world.isClientSide && !this.ageLocked) {
            int i = this.getAge();
            if (i < 0) {
                ++i;
                this.setAge(i);
            } else if (i > 0) {
                --i;
                this.setAge(i);
            }
        }

    }

    public boolean isBaby() {
        return this.datawatcher.get(BABY);
    }

    public void setBaby(boolean flag) {
        this.datawatcher.set(BABY, flag);
    }

}
