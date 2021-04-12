package simplepets.brainsynder.api.pet.data;

import lib.brainsynder.item.ItemBuilder;
import org.bukkit.Material;
import simplepets.brainsynder.api.Namespace;
import simplepets.brainsynder.api.entity.misc.ITameable;
import simplepets.brainsynder.api.pet.PetData;

@Namespace(namespace = "tamed")
public class TamedData extends PetData<ITameable> {
    public TamedData() {
        addDefaultItem("true", new ItemBuilder(Material.BONE)
                .withName("&#c8c8c8{name}: &atrue"));
        addDefaultItem("false", new ItemBuilder(Material.BONE)
                .withName("&#c8c8c8{name}: &cfalse"));
    }

    @Override
    public Object getDefaultValue() {
        return false;
    }

    @Override
    public void onLeftClick(ITameable entity) {
        entity.setTamed(!entity.isTamed());
    }

    @Override
    public Object value(ITameable entity) {
        return entity.isTamed();
    }
}
