package simplepets.brainsynder.api.pet.data;

import lib.brainsynder.item.ItemBuilder;
import org.bukkit.Material;
import simplepets.brainsynder.api.Namespace;
import simplepets.brainsynder.api.entity.passive.IEntityCopperGolemPet;
import simplepets.brainsynder.api.pet.PetData;
import simplepets.brainsynder.api.wrappers.OxidationWrapper;

@Namespace(namespace = "oxidation")
public class OxidationData extends PetData<IEntityCopperGolemPet> {
    public OxidationData() {
        for (OxidationWrapper type : OxidationWrapper.values()) {
            addDefaultItem(type.name(), new ItemBuilder(Material.PLAYER_HEAD)
                .withName("&#c8c8c8{name}: &a" + type.name())
                .setTexture(type.getTexture()));
        }
    }

    @Override
    public Object getDefaultValue() {
        return OxidationWrapper.UNAFFECTED;
    }

    @Override
    public void onRightClick(IEntityCopperGolemPet entity) {
        entity.setOxidation(OxidationWrapper.getPrevious(entity.getOxidation()));
    }

    @Override
    public void onLeftClick(IEntityCopperGolemPet entity) {
        entity.setOxidation(OxidationWrapper.getNext(entity.getOxidation()));
    }

    @Override
    public Object value(IEntityCopperGolemPet entity) {
        return entity.getOxidation();
    }
}
