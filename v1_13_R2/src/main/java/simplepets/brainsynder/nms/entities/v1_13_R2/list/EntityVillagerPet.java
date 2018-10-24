package simplepets.brainsynder.nms.entities.v1_13_R2.list;

import net.minecraft.server.v1_13_R2.DataWatcher;
import net.minecraft.server.v1_13_R2.DataWatcherObject;
import net.minecraft.server.v1_13_R2.DataWatcherRegistry;
import net.minecraft.server.v1_13_R2.World;
import simple.brainsynder.nbt.StorageTagCompound;
import simple.brainsynder.sound.SoundMaker;
import simplepets.brainsynder.api.Size;
import simplepets.brainsynder.api.entity.passive.IEntityVillagerPet;
import simplepets.brainsynder.api.pet.IPet;
import simplepets.brainsynder.nms.entities.v1_13_R2.AgeableEntityPet;
import simplepets.brainsynder.nms.registry.v1_13_R2.Types;
import simplepets.brainsynder.wrapper.ProfessionWrapper;

@Size(width = 0.6F, length = 1.8F)
public class EntityVillagerPet extends AgeableEntityPet implements IEntityVillagerPet {
    private static final DataWatcherObject<Integer> PROFESSION;

    static {
        PROFESSION = DataWatcher.a(EntityVillagerPet.class, DataWatcherRegistry.b);
    }

    public EntityVillagerPet(World world) {
        super(Types.VILLAGER, world);
    }

    public EntityVillagerPet(World world, IPet pet) {
        super(Types.VILLAGER, world, pet);
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