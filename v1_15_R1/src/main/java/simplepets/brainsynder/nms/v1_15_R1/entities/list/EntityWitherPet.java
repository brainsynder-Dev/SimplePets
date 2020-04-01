package simplepets.brainsynder.nms.v1_15_R1.entities.list;

import lib.brainsynder.nbt.StorageTagCompound;
import net.minecraft.server.v1_15_R1.*;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.Size;
import simplepets.brainsynder.api.entity.hostile.IEntityWitherPet;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.v1_15_R1.entities.EntityPet;
import simplepets.brainsynder.nms.v1_15_R1.utils.DataWatcherWrapper;
import simplepets.brainsynder.player.PetOwner;

/**
 * NMS: {@link net.minecraft.server.v1_15_R1.EntityWither}
 */
@Size(width = 0.9F, length = 4.0F)
public class EntityWitherPet extends EntityPet implements IEntityWitherPet {
    private static final DataWatcherObject<Integer> FIRST_HEAD_TARGET;
    private static final DataWatcherObject<Integer> SECOND_HEAD_TARGET;
    private static final DataWatcherObject<Integer> THIRD_HEAD_TARGET;
    private static final DataWatcherObject<Integer> INVULNERABILITY_TIME;

    static {
        FIRST_HEAD_TARGET = DataWatcher.a(EntityWitherPet.class, DataWatcherWrapper.INT);
        SECOND_HEAD_TARGET = DataWatcher.a(EntityWitherPet.class, DataWatcherWrapper.INT);
        THIRD_HEAD_TARGET = DataWatcher.a(EntityWitherPet.class, DataWatcherWrapper.INT);
        INVULNERABILITY_TIME = DataWatcher.a(EntityWitherPet.class, DataWatcherWrapper.INT);
    }

    public EntityWitherPet(EntityTypes<? extends EntityCreature> type, World world) {
        super(type, world);
    }

    public EntityWitherPet(EntityTypes<? extends EntityCreature> type, World world, IPet pet) {
        super(type, world, pet);
    }

    @Override
    protected void registerDatawatchers() {
        super.registerDatawatchers();
        this.datawatcher.register(FIRST_HEAD_TARGET, 0);
        this.datawatcher.register(SECOND_HEAD_TARGET, 0);
        this.datawatcher.register(THIRD_HEAD_TARGET, 0);
        this.datawatcher.register(INVULNERABILITY_TIME, 0);
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = super.asCompound();
        object.setBoolean("shielded", isShielded());
        object.setBoolean("small", isSmall());
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("shielded")) setShielded(object.getBoolean("shielded"));
        if (object.hasKey("small")) setSmall(object.getBoolean("small"));
        super.applyCompound(object);
    }

    @Override
    public boolean isSmall() {
        return (datawatcher.get(INVULNERABILITY_TIME) == 600);
    }

    @Override
    public void setSmall(boolean var) {
        this.datawatcher.set(INVULNERABILITY_TIME, var ? 600 : 0);
        PetCore.get().getInvLoaders().PET_DATA.update(PetOwner.getPetOwner(getOwner()));
    }

    @Override
    public boolean isShielded() {
        return this.datawatcher.get(INVULNERABILITY_TIME) == 1;
    }

    @Override
    public void setShielded(boolean flag) {
        this.datawatcher.set(INVULNERABILITY_TIME, flag ? 1 : 0);
        this.setHealth((float) (flag ? 150 : 300));
        PetCore.get().getInvLoaders().PET_DATA.update(PetOwner.getPetOwner(getOwner()));
    }
}
