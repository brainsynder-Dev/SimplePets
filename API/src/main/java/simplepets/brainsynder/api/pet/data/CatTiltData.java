package simplepets.brainsynder.api.pet.data;

import lib.brainsynder.item.ItemBuilder;
import org.bukkit.Material;
import simplepets.brainsynder.api.Namespace;
import simplepets.brainsynder.api.entity.passive.IEntityCatPet;
import simplepets.brainsynder.api.pet.PetData;

@Namespace(namespace = "head_up")
public class CatTiltData extends PetData<IEntityCatPet> {
    public CatTiltData() {
        addDefaultItem("true", new ItemBuilder(Material.SKELETON_SKULL)
                .withName("&#c8c8c8{name}: &atrue"));
        addDefaultItem("false", new ItemBuilder(Material.SKELETON_SKULL)
                .withName("&#c8c8c8{name}: &cfalse"));
    }

    @Override
    public Object getDefaultValue() {
        return false;
    }

    @Override
    public void onLeftClick(IEntityCatPet entity) {
        entity.setHeadUp(!entity.isHeadUp());
    }

    @Override
    public Object value(IEntityCatPet entity) {
        return entity.isHeadUp();
    }
}
