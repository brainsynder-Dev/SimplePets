package simplepets.brainsynder.nms.entities.v1_12_R1.list;

import net.minecraft.server.v1_12_R1.DataWatcher;
import net.minecraft.server.v1_12_R1.DataWatcherObject;
import net.minecraft.server.v1_12_R1.DataWatcherRegistry;
import net.minecraft.server.v1_12_R1.World;
import simple.brainsynder.nbt.StorageTagCompound;
import simplepets.brainsynder.api.Size;
import simplepets.brainsynder.api.entity.hostile.IEntityZombieVillagerPet;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.entities.v1_12_R1.AgeableEntityPet;
import simplepets.brainsynder.wrapper.ProfessionWrapper;

@Size(width = 0.6F, length = 1.8F)
public class EntityZombieVillagerPet extends AgeableEntityPet implements IEntityZombieVillagerPet {
    private static final DataWatcherObject<Integer> PROFESSION;
    private static final DataWatcherObject<Boolean> CONVERTING;

    static {
        PROFESSION = DataWatcher.a(EntityZombieVillagerPet.class, DataWatcherRegistry.b);
        CONVERTING = DataWatcher.a(EntityZombieVillagerPet.class, DataWatcherRegistry.h);
    }

    private ProfessionWrapper profession;

    public EntityZombieVillagerPet(World world) {
        super(world);
    }
    public EntityZombieVillagerPet(World world, IPet pet) {
        super(world, pet);
    }

    @Override
    protected void registerDatawatchers() {
        super.registerDatawatchers();
        profession = ProfessionWrapper.FARMER;
        this.datawatcher.register(PROFESSION, profession.ordinal());
        datawatcher.register(CONVERTING, false);
    }

    public int getProfessionInt() {
        return Math.max(this.datawatcher.get(PROFESSION) % 6, 0);
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = super.asCompound();
        object.setString("profession", profession.name());
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("profession")) {
            setProfession(ProfessionWrapper.getProfession(object.getString("profession")));
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
