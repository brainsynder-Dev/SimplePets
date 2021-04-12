package simplepets.brainsynder.api.pet.data;

import lib.brainsynder.item.ItemBuilder;
import org.bukkit.Material;
import simplepets.brainsynder.api.Namespace;
import simplepets.brainsynder.api.entity.hostile.IEntityVindicatorPet;
import simplepets.brainsynder.api.pet.PetData;

@Namespace(namespace = "johnny")
public class JohnnyData extends PetData<IEntityVindicatorPet> {
    public JohnnyData() {
        addDefaultItem("true", new ItemBuilder(Material.IRON_AXE)
                .withName("&#c8c8c8{name}: &atrue"));
        addDefaultItem("false", new ItemBuilder(Material.IRON_AXE)
                .withName("&#c8c8c8{name}: &cfalse"));
    }

    @Override
    public Object getDefaultValue() {
        return false;
    }

    @Override
    public void onLeftClick(IEntityVindicatorPet entity) {
        entity.setJohnny(!entity.isJohnny());
    }

    @Override
    public Object value(IEntityVindicatorPet entity) {
        return entity.isJohnny();
    }
}
