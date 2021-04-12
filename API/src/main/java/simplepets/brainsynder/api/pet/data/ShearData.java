package simplepets.brainsynder.api.pet.data;

import lib.brainsynder.item.ItemBuilder;
import org.bukkit.Material;
import simplepets.brainsynder.api.Namespace;
import simplepets.brainsynder.api.entity.passive.IEntitySheepPet;
import simplepets.brainsynder.api.pet.PetData;

@Namespace(namespace = "sheared")
public class ShearData extends PetData<IEntitySheepPet> {
    public ShearData() {
        addDefaultItem("true", new ItemBuilder(Material.SHEARS)
                .withName("&#c8c8c8{name}: &atrue"));
        addDefaultItem("false", new ItemBuilder(Material.SHEARS)
                .withName("&#c8c8c8{name}: &cfalse"));
    }

    @Override
    public Object getDefaultValue() {
        return false;
    }

    @Override
    public void onLeftClick(IEntitySheepPet entity) {
        entity.setSheared(!entity.isSheared());
    }

    @Override
    public Object value(IEntitySheepPet entity) {
        return entity.isSheared();
    }
}
