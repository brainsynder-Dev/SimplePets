package simplepets.brainsynder.api.pet.data;

import lib.brainsynder.item.ItemBuilder;
import org.bukkit.Material;
import simplepets.brainsynder.api.Namespace;
import simplepets.brainsynder.api.entity.misc.IAgeablePet;
import simplepets.brainsynder.api.pet.PetData;

@Namespace(namespace = "baby")
public class AgeData extends PetData<IAgeablePet> {
    public AgeData() {
        addDefaultItem("true", new ItemBuilder(Material.WHEAT)
                .withName("&#c8c8c8Baby: &atrue"));
        addDefaultItem("false", new ItemBuilder(Material.WHEAT)
                .withName("&#c8c8c8Baby: &cfalse"));
    }

    @Override
    public void onLeftClick(IAgeablePet entity) {
        entity.setBaby(!entity.isBaby());
    }

    @Override
    public Object value(IAgeablePet entity) {
        return entity.isBaby();
    }
}
