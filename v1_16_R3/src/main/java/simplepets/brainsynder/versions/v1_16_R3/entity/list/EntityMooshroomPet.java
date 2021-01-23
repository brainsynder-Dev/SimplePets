package simplepets.brainsynder.versions.v1_16_R3.entity.list;

import lib.brainsynder.nbt.StorageTagCompound;
import net.minecraft.server.v1_16_R3.DataWatcher;
import net.minecraft.server.v1_16_R3.DataWatcherObject;
import net.minecraft.server.v1_16_R3.EntityTypes;
import simplepets.brainsynder.api.entity.passive.IEntityMooshroomPet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.user.PetUser;
import simplepets.brainsynder.api.wrappers.MooshroomType;
import simplepets.brainsynder.versions.v1_16_R3.entity.EntityAgeablePet;
import simplepets.brainsynder.versions.v1_16_R3.utils.DataWatcherWrapper;

public class EntityMooshroomPet extends EntityAgeablePet implements IEntityMooshroomPet {
    private static final DataWatcherObject<String> TYPE;

    public EntityMooshroomPet(PetType type, PetUser user) {
        super(EntityTypes.MOOSHROOM, type, user);
    }

    @Override
    protected void registerDatawatchers() {
        super.registerDatawatchers();
        datawatcher.register(TYPE, MooshroomType.RED.name());
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound compound = super.asCompound();
        compound.setString("type", getMooshroomType().name());
        return compound;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("type"))
            setMooshroomType(MooshroomType.valueOf(object.getString("type")));
        super.applyCompound(object);
    }

    @Override
    public void setMooshroomType(MooshroomType type) {
        datawatcher.set(TYPE, type.name().toLowerCase());
    }

    @Override
    public MooshroomType getMooshroomType() {
        return MooshroomType.valueOf(datawatcher.get(TYPE).toUpperCase());
    }

    static {
        TYPE = DataWatcher.a(EntityMooshroomPet.class, DataWatcherWrapper.STRING);
    }
}
