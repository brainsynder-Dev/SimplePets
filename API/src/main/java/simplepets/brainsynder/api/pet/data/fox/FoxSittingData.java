package simplepets.brainsynder.api.pet.data.fox;

import lib.brainsynder.item.ItemBuilder;
import org.bukkit.Material;
import simplepets.brainsynder.api.Namespace;
import simplepets.brainsynder.api.entity.passive.IEntityFoxPet;
import simplepets.brainsynder.api.pet.PetData;

@Namespace(namespace = "sitting")
public class FoxSittingData extends PetData<IEntityFoxPet> {
    public FoxSittingData() {
        addDefaultItem("true", new ItemBuilder(Material.OAK_STAIRS)
                .withName("&#c8c8c8{name}: &atrue"));
        addDefaultItem("false", new ItemBuilder(Material.OAK_STAIRS)
                .withName("&#c8c8c8{name}: &cfalse"));
    }

    @Override
    public Object getDefaultValue() {
        return false;
    }

    @Override
    public void onLeftClick(IEntityFoxPet entity) {
        entity.setSitting(!entity.isSitting());
    }

    @Override
    public Object value(IEntityFoxPet entity) {
        return entity.isSitting();
    }
}
