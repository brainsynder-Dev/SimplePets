package simplepets.brainsynder.api.pet.data;

import lib.brainsynder.item.ItemBuilder;
import org.bukkit.Material;
import simplepets.brainsynder.api.Namespace;
import simplepets.brainsynder.api.entity.passive.IEntityCatPet;
import simplepets.brainsynder.api.pet.PetData;
import simplepets.brainsynder.api.wrappers.CatType;

@Namespace(namespace = "type")
public class CatTypeData extends PetData<IEntityCatPet> {
    public CatTypeData() {
        for (CatType type : CatType.values()) {
            addDefaultItem(type.name(), new ItemBuilder(Material.PLAYER_HEAD)
                    .withName("&#c8c8c8{name}: &a"+type.name())
                    .setTexture(type.getTexture()));
        }
    }

    @Override
    public Object getDefaultValue() {
        return CatType.TABBY;
    }

    @Override
    public void onLeftClick(IEntityCatPet entity) {
        entity.setCatType(CatType.getNext(entity.getCatType()));
    }

    @Override
    public Object value(IEntityCatPet entity) {
        return entity.getCatType();
    }
}
