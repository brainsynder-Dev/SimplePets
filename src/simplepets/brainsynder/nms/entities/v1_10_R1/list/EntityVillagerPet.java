package simplepets.brainsynder.nms.entities.v1_10_R1.list;

import net.minecraft.server.v1_10_R1.DataWatcher;
import net.minecraft.server.v1_10_R1.DataWatcherObject;
import net.minecraft.server.v1_10_R1.DataWatcherRegistry;
import net.minecraft.server.v1_10_R1.World;
import simple.brainsynder.nbt.StorageTagCompound;
import simplepets.brainsynder.nms.entities.type.IEntityVillagerPet;
import simplepets.brainsynder.nms.entities.v1_10_R1.AgeableEntityPet;
import simplepets.brainsynder.pet.IPet;
import simplepets.brainsynder.wrapper.ProfessionWrapper;

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

    public void initDatawatcher() {
        super.initDataWatcher();
        getDataWatcher().register(PROFESSION, 0);
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
}
