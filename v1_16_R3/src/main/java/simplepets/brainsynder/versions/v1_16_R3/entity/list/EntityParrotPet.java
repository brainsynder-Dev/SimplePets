package simplepets.brainsynder.versions.v1_16_R3.entity.list;

import lib.brainsynder.nbt.StorageTagCompound;
import net.minecraft.server.v1_16_R3.*;
import simplepets.brainsynder.api.entity.passive.IEntityParrotPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.api.wrappers.ParrotVariant;
import simplepets.brainsynder.versions.v1_16_R3.entity.EntityTameablePet;
import simplepets.brainsynder.versions.v1_16_R3.entity.controller.ParrotController;
import simplepets.brainsynder.versions.v1_16_R3.utils.DataWatcherWrapper;

/**
 * NMS: {@link net.minecraft.server.v1_16_R3.EntityParrot}
 */
public class EntityParrotPet extends EntityTameablePet implements IEntityParrotPet {
    private static final DataWatcherObject<Integer> TYPE;
    private boolean rainbow = false;
    private int toggle = 0;

    public EntityParrotPet(PetType type, PetUser user) {
        super(EntityTypes.PARROT, type, user);
        moveController = new ParrotController(this);
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
        this.datawatcher.register(TYPE, 0);
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
        return ParrotVariant.getById(getDataWatcher().get(TYPE));
    }

    @Override
    public void setVariant(ParrotVariant variant) {
        this.datawatcher.set(TYPE, variant.ordinal());
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
    protected NavigationAbstract b(World var1) {
        NavigationFlying var2 = new NavigationFlying(this, var1);
        var2.a(false);
        var2.d(true);
        var2.b(true);
        return var2;
    }

    static {
        TYPE = DataWatcher.a(EntityParrotPet.class, DataWatcherWrapper.INT);
    }
}
