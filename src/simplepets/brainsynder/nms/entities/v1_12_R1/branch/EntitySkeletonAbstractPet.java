package simplepets.brainsynder.nms.entities.v1_12_R1.branch;

import net.minecraft.server.v1_12_R1.DataWatcher;
import net.minecraft.server.v1_12_R1.DataWatcherObject;
import net.minecraft.server.v1_12_R1.DataWatcherRegistry;
import net.minecraft.server.v1_12_R1.World;
import org.bukkit.entity.Skeleton;
import simple.brainsynder.nbt.StorageTagCompound;
import simplepets.brainsynder.nms.entities.v1_12_R1.EntityPet;
import simplepets.brainsynder.pet.IPet;

public abstract class EntitySkeletonAbstractPet extends EntityPet {
    private static final DataWatcherObject<Boolean> b;

    static {
        b = DataWatcher.a(EntitySkeletonAbstractPet.class, DataWatcherRegistry.h);
    }


    public EntitySkeletonAbstractPet(World world) {
        super(world);
    }

    public EntitySkeletonAbstractPet(World world, IPet pet) {
        super(world, pet);
    }

    public Skeleton.SkeletonType getSkeletonType() {
        return Skeleton.SkeletonType.NORMAL;
    }

    public void setSkeletonType(Skeleton.SkeletonType type) {
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = super.asCompound();
        object.setString("SkeletonType", getSkeletonType().name());
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("SkeletonType")) {
            setSkeletonType(Skeleton.SkeletonType.valueOf(object.getString("SkeletonType")));
        }
        super.applyCompound(object);
    }

    @Override
    protected void registerDatawatchers() {
        super.registerDatawatchers();
        this.datawatcher.register(b, Boolean.valueOf(false));
    }
}
