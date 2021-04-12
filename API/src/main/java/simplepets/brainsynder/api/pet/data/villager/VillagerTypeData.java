package simplepets.brainsynder.api.pet.data.villager;

import simplepets.brainsynder.api.Namespace;
import simplepets.brainsynder.api.entity.passive.IEntityVillagerPet;
import simplepets.brainsynder.api.pet.PetData;
import simplepets.brainsynder.api.wrappers.villager.VillagerType;

@Namespace(namespace = "profession")
public class VillagerTypeData extends PetData<IEntityVillagerPet> {
    public VillagerTypeData() {
        for (VillagerType type : VillagerType.values()) {
            addDefaultItem(type.name(), type.getIcon()
                    .withName("&#c8c8c8{name}: &a"+type.name()));
        }
    }

    @Override
    public Object getDefaultValue() {
        return VillagerType.NONE;
    }

    @Override
    public void onLeftClick(IEntityVillagerPet entity) {
        entity.setVillagerType(VillagerType.getNext(entity.getVillagerType()));
    }

    @Override
    public void onRightClick(IEntityVillagerPet entity) {
        entity.setVillagerType(VillagerType.getPrevious(entity.getVillagerType()));
    }

    @Override
    public Object value(IEntityVillagerPet entity) {
        return entity.getVillagerType();
    }
}
