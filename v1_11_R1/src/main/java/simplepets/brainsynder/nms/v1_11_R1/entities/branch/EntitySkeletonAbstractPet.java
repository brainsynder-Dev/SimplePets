package simplepets.brainsynder.nms.v1_11_R1.entities.branch;

import lib.brainsynder.nbt.StorageTagCompound;
import net.minecraft.server.v1_11_R1.DataWatcher;
import net.minecraft.server.v1_11_R1.DataWatcherObject;
import net.minecraft.server.v1_11_R1.DataWatcherRegistry;
import net.minecraft.server.v1_11_R1.World;
import org.bukkit.entity.Skeleton;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.v1_11_R1.entities.EntityPet;

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
        object.setInteger("SkeletonType", getSkeletonType().ordinal());
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("SkeletonType")) {
            setSkeletonType(Skeleton.SkeletonType.values()[object.getInteger("SkeletonType")]);
        }
        super.applyCompound(object);
    }

    @Override
    protected void registerDatawatchers() {
        super.registerDatawatchers();
        this.datawatcher.register(b, false);
    }
}
