package simplepets.brainsynder.nms.entities.v1_8_R3.list;

import net.minecraft.server.v1_8_R3.World;
import simple.brainsynder.nbt.StorageTagCompound;
import simplepets.brainsynder.nms.entities.type.IEntityVillagerPet;
import simplepets.brainsynder.nms.entities.v1_8_R3.AgeableEntityPet;
import simplepets.brainsynder.pet.IPet;
import simplepets.brainsynder.wrapper.ProfessionWrapper;

/**
 * @Deprecated Will be removed when MC version 1.13 is released
 */
@Deprecated
public class EntityVillagerPet extends AgeableEntityPet implements IEntityVillagerPet {
    public EntityVillagerPet(World world, IPet pet) {
        super(world, pet);
    }

    public EntityVillagerPet(World world) {
        super(world);
    }

    @Override
    public StorageTagCompound asCompound() {
        StorageTagCompound object = super.asCompound();
        object.setInteger("Profession", getProfession().ordinal());
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
        return ProfessionWrapper.getById(getDataWatcher().getInt(16));
    }

    public void setProfession(ProfessionWrapper wrapper) {
        this.datawatcher.watch(16, wrapper.getId());
    }

    public void initDatawatcher() {
        super.initDatawatcher();
        this.datawatcher.a(16, new Integer(0));
    }
}
