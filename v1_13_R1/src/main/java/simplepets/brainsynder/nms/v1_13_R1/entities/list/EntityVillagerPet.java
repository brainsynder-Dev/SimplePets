package simplepets.brainsynder.nms.v1_13_R1.entities.list;

import lib.brainsynder.nbt.StorageTagCompound;
import net.minecraft.server.v1_13_R1.DataWatcher;
import net.minecraft.server.v1_13_R1.DataWatcherObject;
import net.minecraft.server.v1_13_R1.EntityTypes;
import net.minecraft.server.v1_13_R1.World;
import simple.brainsynder.sound.SoundMaker;
import simplepets.brainsynder.api.Size;
import simplepets.brainsynder.api.entity.passive.IEntityVillagerPet;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.v1_13_R1.entities.AgeableEntityPet;
import simplepets.brainsynder.nms.v1_13_R1.utils.DataWatcherWrapper;
import simplepets.brainsynder.wrapper.ProfessionWrapper;

@Size(width = 0.6F, length = 1.8F)
public class EntityVillagerPet extends AgeableEntityPet implements IEntityVillagerPet {
    private static final DataWatcherObject<Integer> PROFESSION;

    static {
        PROFESSION = DataWatcher.a(EntityVillagerPet.class, DataWatcherWrapper.INT);
    }

    public EntityVillagerPet(EntityTypes<?> type, World world) {
        super(type, world);
    }

    public EntityVillagerPet(EntityTypes<?> type, World world, IPet pet) {
        super(type, world, pet);
    }

    @Override
    protected void registerDatawatchers() {
        super.registerDatawatchers();
        this.datawatcher.register(PROFESSION, 0);
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = super.asCompound();
        object.setString("profession", getProfession().name());
        return object;
    }

    @Override
    public void applyCompound(StorageTagCompound object) {
        if (object.hasKey("profession")) setProfession(ProfessionWrapper.getProfession(object.getString("profession")));
        super.applyCompound(object);
    }

    public ProfessionWrapper getProfession() {
        return ProfessionWrapper.getById(datawatcher.get(PROFESSION));
    }

    public void setProfession(ProfessionWrapper wrapper) {
        this.datawatcher.set(PROFESSION, wrapper.ordinal());
        if (wrapper == ProfessionWrapper.NITWIT) {
            SoundMaker.ENTITY_VILLAGER_NO.playSound(getEntity());
        } else if (wrapper == ProfessionWrapper.LIBRARIAN) {
            SoundMaker.ENTITY_VILLAGER_YES.playSound(getEntity());
        } else {
            SoundMaker.ENTITY_VILLAGER_TRADING.playSound(getEntity());
        }
    }
}
