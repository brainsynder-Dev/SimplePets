package simplepets.brainsynder.api.pet.data;

import lib.brainsynder.item.ItemBuilder;
import org.bukkit.Material;
import simplepets.brainsynder.api.Namespace;
import simplepets.brainsynder.api.entity.misc.IHorseAbstract;
import simplepets.brainsynder.api.pet.PetData;

@Namespace(namespace = "eating")
public class EatingData extends PetData<IHorseAbstract> {
    public EatingData() {
        addDefaultItem("true", new ItemBuilder(Material.APPLE)
                .withName("&#c8c8c8{name}: &atrue"));
        addDefaultItem("false", new ItemBuilder(Material.APPLE)
                .withName("&#c8c8c8{name}: &cfalse"));
    }

    @Override
    public Object getDefaultValue() {
        return false;
    }

    @Override
    public void onLeftClick(IHorseAbstract entity) {
        entity.setEating(!entity.isEating());
    }

    @Override
    public Object value(IHorseAbstract entity) {
        return entity.isEating();
    }
}
