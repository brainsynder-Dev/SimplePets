package simplepets.brainsynder.api.pet.data.horse;

import lib.brainsynder.item.ItemBuilder;
import org.bukkit.Material;
import simplepets.brainsynder.api.Namespace;
import simplepets.brainsynder.api.entity.misc.IChestedAbstractPet;
import simplepets.brainsynder.api.pet.PetData;

@Namespace(namespace = "chest")
public class ChestData extends PetData<IChestedAbstractPet> {
    public ChestData() {
        addDefaultItem("true", new ItemBuilder(Material.CHEST)
                .withName("&#c8c8c8{name}: &atrue"));
        addDefaultItem("false", new ItemBuilder(Material.CHEST)
                .withName("&#c8c8c8{name}: &cfalse"));
    }

    @Override
    public Object getDefaultValue() {
        return false;
    }

    @Override
    public void onLeftClick(IChestedAbstractPet entity) {
        entity.setChested(!entity.isChested());
    }

    @Override
    public Object value(IChestedAbstractPet entity) {
        return entity.isChested();
    }
}
