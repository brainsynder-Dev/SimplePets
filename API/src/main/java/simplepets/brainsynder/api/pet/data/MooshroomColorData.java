package simplepets.brainsynder.api.pet.data;

import simplepets.brainsynder.api.Namespace;
import simplepets.brainsynder.api.entity.passive.IEntityMooshroomPet;
import simplepets.brainsynder.api.pet.PetData;
import simplepets.brainsynder.api.wrappers.MooshroomType;

@Namespace(namespace = "type")
public class MooshroomColorData extends PetData<IEntityMooshroomPet> {
    public MooshroomColorData() {
        for (MooshroomType color : MooshroomType.values()) {
            addDefaultItem(color.name(), color.getIcon()
                    .withName("&#c8c8c8{name}: &a"+color.name()));
        }
    }

    @Override
    public Object getDefaultValue() {
        return MooshroomType.RED;
    }

    @Override
    public void onLeftClick(IEntityMooshroomPet entity) {
        entity.setMooshroomType(MooshroomType.getNext(entity.getMooshroomType()));
    }

    @Override
    public Object value(IEntityMooshroomPet entity) {
        return entity.getMooshroomType().name();
    }
}
