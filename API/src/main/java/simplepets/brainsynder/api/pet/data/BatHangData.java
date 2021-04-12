package simplepets.brainsynder.api.pet.data;

import lib.brainsynder.item.ItemBuilder;
import org.bukkit.Material;
import simplepets.brainsynder.api.Namespace;
import simplepets.brainsynder.api.entity.passive.IEntityBatPet;
import simplepets.brainsynder.api.pet.PetData;

@Namespace(namespace = "hanging")
public class BatHangData extends PetData<IEntityBatPet> {
    public BatHangData() {
        addDefaultItem("true", new ItemBuilder(Material.FEATHER)
                .withName("&#c8c8c8{name}: &atrue"));
        addDefaultItem("false", new ItemBuilder(Material.FEATHER)
                .withName("&#c8c8c8{name}: &cfalse"));
    }

    @Override
    public Object getDefaultValue() {
        return false;
    }

    @Override
    public void onLeftClick(IEntityBatPet entity) {
        entity.setHanging(!entity.isHanging());
    }

    @Override
    public Object value(IEntityBatPet entity) {
        return entity.isHanging();
    }
}
