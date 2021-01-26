package simplepets.brainsynder.versions.v1_16_R3.entity.list;

import lib.brainsynder.nbt.StorageTagCompound;
import net.minecraft.server.v1_16_R3.DataWatcher;
import net.minecraft.server.v1_16_R3.DataWatcherObject;
import net.minecraft.server.v1_16_R3.EntityTypes;
import simplepets.brainsynder.api.entity.hostile.IEntityWitherPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.versions.v1_16_R3.entity.EntityPet;
import simplepets.brainsynder.versions.v1_16_R3.utils.DataWatcherWrapper;

public class EntityWitherPet extends EntityPet implements IEntityWitherPet {
    private static final DataWatcherObject<Integer> FIRST_HEAD_TARGET;
    private static final DataWatcherObject<Integer> SECOND_HEAD_TARGET;
    private static final DataWatcherObject<Integer> THIRD_HEAD_TARGET;
    private static final DataWatcherObject<Integer> INVULNERABILITY_TIME;

    public EntityWitherPet(PetType type, PetUser user) {
        super(EntityTypes.WITHER, type, user);
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
    public boolean isShielded() {
        return this.datawatcher.get(INVULNERABILITY_TIME) == 1;
    }

    @Override
    public void setShielded(boolean flag) {
        this.datawatcher.set(INVULNERABILITY_TIME, flag ? 1 : 0);
        this.setHealth((float) (flag ? 150 : 300));
        getPetUser().updateDataMenu();
    }

    @Override
    public boolean isSmall() {
        return (datawatcher.get(INVULNERABILITY_TIME) == 600);
    }

    @Override
    public void setSmall(boolean var) {
        this.datawatcher.set(INVULNERABILITY_TIME, var ? 600 : 0);
        getPetUser().updateDataMenu();
    }


    static {
        FIRST_HEAD_TARGET = DataWatcher.a(EntityWitherPet.class, DataWatcherWrapper.INT);
        SECOND_HEAD_TARGET = DataWatcher.a(EntityWitherPet.class, DataWatcherWrapper.INT);
        THIRD_HEAD_TARGET = DataWatcher.a(EntityWitherPet.class, DataWatcherWrapper.INT);
        INVULNERABILITY_TIME = DataWatcher.a(EntityWitherPet.class, DataWatcherWrapper.INT);
    }
}
