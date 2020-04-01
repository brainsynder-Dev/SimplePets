package simplepets.brainsynder.nms.v1_15_R1.entities.list;

import lib.brainsynder.nbt.StorageTagCompound;
import net.minecraft.server.v1_15_R1.*;
import simplepets.brainsynder.PetCore;
import simplepets.brainsynder.api.Size;
import simplepets.brainsynder.api.entity.passive.IEntityMooshroomPet;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.v1_15_R1.entities.AgeableEntityPet;
import simplepets.brainsynder.player.PetOwner;
import simplepets.brainsynder.wrapper.MooshroomType;

/**
 * NMS: {@link net.minecraft.server.v1_15_R1.EntityMushroomCow}
 */
@Size(width = 0.9F, length = 1.3F)
public class EntityMooshroomPet extends AgeableEntityPet implements IEntityMooshroomPet {
    private static final DataWatcherObject<String> TYPE;

    static {
        TYPE = DataWatcher.a(EntityMooshroomPet.class, DataWatcherRegistry.d);
    }

    @Override
    protected void registerDatawatchers() {
        super.registerDatawatchers();
        datawatcher.register(TYPE, MooshroomType.RED.name());
    }

    public EntityMooshroomPet(EntityTypes<? extends EntityCreature> type, World world, IPet pet) {
        super(type, world, pet);
    }
    public EntityMooshroomPet(EntityTypes<? extends EntityCreature> type, World world) {
        super(type, world);
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
    public MooshroomType getMooshroomType() {
        return MooshroomType.valueOf(datawatcher.get(TYPE).toUpperCase());
    }

    @Override
    public void setMooshroomType(MooshroomType type) {
        datawatcher.set(TYPE, type.name().toLowerCase());
        PetCore.get().getInvLoaders().PET_DATA.update(PetOwner.getPetOwner(getOwner()));
    }
}
