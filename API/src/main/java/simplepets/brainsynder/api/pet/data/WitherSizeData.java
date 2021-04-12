package simplepets.brainsynder.api.pet.data;

import lib.brainsynder.item.ItemBuilder;
import org.bukkit.Material;
import simplepets.brainsynder.api.Namespace;
import simplepets.brainsynder.api.entity.hostile.IEntityWitherPet;
import simplepets.brainsynder.api.pet.PetData;

@Namespace(namespace = "small")
public class WitherSizeData extends PetData<IEntityWitherPet> {
    public WitherSizeData() {
        addDefaultItem("true", new ItemBuilder(Material.NETHER_STAR)
                .withName("&#c8c8c8{name}: &atrue"));
        addDefaultItem("false", new ItemBuilder(Material.NETHER_STAR)
                .withName("&#c8c8c8{name}: &cfalse"));
    }

    @Override
    public Object getDefaultValue() {
        return false;
    }

    @Override
    public void onLeftClick(IEntityWitherPet entity) {
        entity.setSmall(!entity.isSmall());
    }

    @Override
    public Object value(IEntityWitherPet entity) {
        return entity.isSmall();
    }
}
