package simplepets.brainsynder.versions.v1_17_R1.entity.list;

import lib.brainsynder.nbt.StorageTagCompound;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.Level;
import simplepets.brainsynder.api.entity.passive.IEntityParrotPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.api.wrappers.ParrotVariant;
import simplepets.brainsynder.versions.v1_17_R1.entity.EntityTameablePet;

/**
 * NMS: {@link net.minecraft.server.v1_16_R3.EntityParrot}
 */
public class EntityParrotPet extends EntityTameablePet implements IEntityParrotPet {
    private static final EntityDataAccessor<Integer> TYPE;
    private boolean rainbow = false;
    private int toggle = 0;

    public EntityParrotPet(PetType type, PetUser user) {
        super(EntityType.PARROT, type, user);
        this.moveControl = new FlyingMoveControl(this, 10, false);
    }

    @Override
    public void tick() {
        super.tick();
        if (rainbow) {
            if (toggle == 4) {
                setVariant(ParrotVariant.getNext(getVariant()));
                getPetUser().updateDataMenu();
                toggle = 0;
            }
            toggle++;
        }
    }

    @Override
    protected void registerDatawatchers() {
        super.registerDatawatchers();
        this.entityData.define(TYPE, 0);
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("variant")) setVariant(ParrotVariant.getByName(object.getString("variant")));
        if (object.hasKey("rainbow")) rainbow = object.getBoolean("rainbow");
        super.applyCompound(object);
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = super.asCompound();
        if (!rainbow) object.setString("variant", getVariant().name());
        object.setBoolean("rainbow", rainbow);
        return object;
    }

    @Override
    public ParrotVariant getVariant() {
        return ParrotVariant.getById(entityData.get(TYPE));
    }

    @Override
    public void setVariant(ParrotVariant variant) {
        this.entityData.set(TYPE, variant.ordinal());
    }

    @Override
    public boolean isRainbow() {
        return rainbow;
    }

    @Override
    public void setRainbow(boolean rainbow) {
        this.rainbow = rainbow;
    }

    @Override
    protected PathNavigation createNavigation(Level var1) {
        FlyingPathNavigation navigationflying = new FlyingPathNavigation(this, var1);
        navigationflying.setCanOpenDoors(false);
        navigationflying.setCanFloat(true);
        navigationflying.setCanPassDoors(true);
        return navigationflying;
    }

    static {
        TYPE = SynchedEntityData.defineId(EntityParrotPet.class, EntityDataSerializers.INT);
    }
}
