package simplepets.brainsynder.nms.entity.branch;

import lib.brainsynder.nbt.StorageTagCompound;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import simplepets.brainsynder.api.entity.misc.IChestedAbstractPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;

public class EntityDonkeyAbstractPet extends EntityHorseAbstractPet implements IChestedAbstractPet {
    private static final EntityDataAccessor<Boolean> CHEST;

    public EntityDonkeyAbstractPet(EntityType<? extends Mob> entitytypes, PetType type, PetUser user) {
        super(entitytypes, type, user);
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = super.asCompound();
        object.setBoolean("chest", isChested());
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        setChested(object.getBoolean("chest", false));
        super.applyCompound(object);
    }

    @Override
    protected void registerDatawatchers() {
        super.registerDatawatchers();
        this.entityData.define(CHEST, Boolean.FALSE);
    }

    @Override
    public boolean isChested() {
        return entityData.get(CHEST);
    }

    @Override
    public void setChested(boolean flag) {
        this.entityData.set(CHEST, flag);
    }

    static {
        CHEST = SynchedEntityData.defineId(EntityDonkeyAbstractPet.class, EntityDataSerializers.BOOLEAN);
    }
}