package simplepets.brainsynder.api.pet.data;

import lib.brainsynder.item.ItemBuilder;
import org.bukkit.Material;
import simplepets.brainsynder.api.Namespace;
import simplepets.brainsynder.api.entity.passive.IEntityCamelPet;
import simplepets.brainsynder.api.pet.PetData;

@Namespace(namespace = "dash")
public class DashingData extends PetData<IEntityCamelPet> {
    public DashingData() {
        addDefaultItem("true", new ItemBuilder(Material.ANVIL)
                .withName("&#c8c8c8{name}: &atrue"));
        addDefaultItem("false", new ItemBuilder(Material.ANVIL)
                .withName("&#c8c8c8{name}: &cfalse"));
    }

    @Override
    public Object getDefaultValue() {
        return false;
    }

    @Override
    public void onLeftClick(IEntityCamelPet entity) {
        entity.setAngry(!entity.isAngry());
    }

    @Override
    public Object value(IEntityCamelPet entity) {
        return entity.isAngry();
    }
}
