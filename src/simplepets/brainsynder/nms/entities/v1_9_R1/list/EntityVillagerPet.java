package simplepets.brainsynder.nms.entities.v1_9_R1.list;

import net.minecraft.server.v1_9_R1.DataWatcher;
import net.minecraft.server.v1_9_R1.DataWatcherObject;
import net.minecraft.server.v1_9_R1.DataWatcherRegistry;
import net.minecraft.server.v1_9_R1.World;
import simple.brainsynder.nbt.StorageTagCompound;
import simplepets.brainsynder.api.entity.passive.IEntityVillagerPet;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.entities.v1_9_R1.AgeableEntityPet;
import simplepets.brainsynder.wrapper.ProfessionWrapper;

/**
 * @Deprecated Will be removed when MC version 1.13 is released
 */
@Deprecated
public class EntityVillagerPet extends AgeableEntityPet implements IEntityVillagerPet {
    private static final DataWatcherObject<Integer> PROFESSION;

    static {
        PROFESSION = DataWatcher.a(EntityVillagerPet.class, DataWatcherRegistry.b);
    }

    private ProfessionWrapper profession;

    public EntityVillagerPet(World world, IPet pet) {
        super(world, pet);
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = super.asCompound();
        object.setInteger("Profession", profession.ordinal());
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("Profession")) {
            setProfession(ProfessionWrapper.values()[object.getInteger("Profession")]);
        }
        super.applyCompound(object);
    }

    public ProfessionWrapper getProfession() {
        return profession;
    }

    public void setProfession(ProfessionWrapper wrapper) {
        profession = wrapper;
        this.datawatcher.set(PROFESSION, wrapper.getId());
    }

    public void initDatawatcher() {
        super.initDataWatcher();
        this.datawatcher.register(PROFESSION, Integer.valueOf(0));
    }
}
