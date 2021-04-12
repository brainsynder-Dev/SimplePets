package simplepets.brainsynder.api.pet.data.panda;

import simplepets.brainsynder.api.Namespace;
import simplepets.brainsynder.api.entity.passive.IEntityPandaPet;
import simplepets.brainsynder.api.pet.PetData;
import simplepets.brainsynder.api.wrappers.PandaGene;

@Namespace(namespace = "type")
public class PandaTypeData extends PetData<IEntityPandaPet> {
    public PandaTypeData() {
        for (PandaGene gene : PandaGene.values()) {
            addDefaultItem(gene.name(), gene.getIcon()
                    .withName("&#c8c8c8{name}: &a"+gene.name()));
        }
    }

    @Override
    public Object getDefaultValue() {
        return PandaGene.NORMAL;
    }

    @Override
    public void onLeftClick(IEntityPandaPet entity) {
        entity.setGene(PandaGene.getNext(entity.getGene()));
    }

    @Override
    public void onRightClick(IEntityPandaPet entity) {
        entity.setGene(PandaGene.getPrevious(entity.getGene()));
    }

    @Override
    public Object value(IEntityPandaPet entity) {
        return entity.getGene();
    }
}
