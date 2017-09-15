package simplepets.brainsynder.nms.entities.v1_12_R1.branch;

import com.google.common.base.Optional;
import net.minecraft.server.v1_12_R1.DataWatcher;
import net.minecraft.server.v1_12_R1.DataWatcherObject;
import net.minecraft.server.v1_12_R1.DataWatcherRegistry;
import net.minecraft.server.v1_12_R1.World;
import simple.brainsynder.nbt.StorageTagCompound;
import simplepets.brainsynder.nms.entities.type.main.IHorseAbstract;
import simplepets.brainsynder.nms.entities.v1_12_R1.AgeableEntityPet;
import simplepets.brainsynder.pet.IPet;
import simplepets.brainsynder.wrapper.HorseColorType;
import simplepets.brainsynder.wrapper.HorseStyleType;

import java.util.UUID;

public abstract class EntityHorseAbstractPet extends AgeableEntityPet implements IHorseAbstract {
    private static final DataWatcherObject<Byte> VISUAL;
    private static final DataWatcherObject<Optional<UUID>> OWNER;

    static {
        VISUAL = DataWatcher.a(EntityHorseAbstractPet.class, DataWatcherRegistry.a);
        OWNER = DataWatcher.a(EntityHorseAbstractPet.class, DataWatcherRegistry.m);
    }

    protected float jumpPower = 0.0F;
    protected boolean bA;

    public EntityHorseAbstractPet(World world) {
        super(world);
        this.setSize(1.3964844F, 1.6F);
    }

    public EntityHorseAbstractPet(World world, IPet pet) {
        super(world, pet);
        this.setSize(1.3964844F, 1.6F);
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = super.asCompound();
        object.setBoolean("Saddled", isSaddled());
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        super.applyCompound(object);
        if (object.hasKey("Saddled")) {
            setSaddled(object.getBoolean("Saddled"));
        }
    }

    @Override
    protected void registerDatawatchers() {
        super.registerDatawatchers();
        this.datawatcher.register(VISUAL, (byte) 0);
        this.datawatcher.register(OWNER, Optional.absent());
    }

    @Override
    public boolean isCollidable() {
        return false;
    }

    @Override
    public boolean isSaddled() {
        return getHorseVisual(4);
    }

    public void setSaddled(boolean flag) {
        this.setHorseVisual(4, flag);
    }

    public boolean getHorseVisual(int i) {
        return (this.datawatcher.get(VISUAL) & i) != 0;
    }

    public void setHorseVisual(int i, boolean flag) {
        byte b0 = this.datawatcher.get(VISUAL);
        if (flag) {
            this.datawatcher.set(VISUAL, (byte) (b0 | i));
        } else {
            this.datawatcher.set(VISUAL, (byte) (b0 & ~i));
        }

    }

    public void setVariant(HorseColorType variant) {
    }

    @Override
    public void setVariant(HorseColorType var1, HorseStyleType var2) {

    }
}
