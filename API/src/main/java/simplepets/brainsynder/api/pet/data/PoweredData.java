package simplepets.brainsynder.api.pet.data;

import lib.brainsynder.item.ItemBuilder;
import org.bukkit.Material;
import simplepets.brainsynder.api.Namespace;
import simplepets.brainsynder.api.entity.misc.IPowered;
import simplepets.brainsynder.api.pet.PetData;

@Namespace(namespace = "powered")
public class PoweredData extends PetData<IPowered> {
    public PoweredData() {
        addDefaultItem("true", new ItemBuilder(Material.GUNPOWDER)
                .withName("&#c8c8c8{name}: &atrue"));
        addDefaultItem("false", new ItemBuilder(Material.GUNPOWDER)
                .withName("&#c8c8c8{name}: &cfalse"));
    }

    @Override
    public Object getDefaultValue() {
        return false;
    }

    @Override
    public void onLeftClick(IPowered entity) {
        entity.setPowered(!entity.isPowered());
    }

    @Override
    public Object value(IPowered entity) {
        return entity.isPowered();
    }
}
