package simplepets.brainsynder.api.pet.data.panda;

import lib.brainsynder.item.ItemBuilder;
import org.bukkit.Material;
import simplepets.brainsynder.api.Namespace;
import simplepets.brainsynder.api.entity.passive.IEntityPandaPet;
import simplepets.brainsynder.api.pet.PetData;

@Namespace(namespace = "sitting")
public class PandaSittingData extends PetData<IEntityPandaPet> {
    public PandaSittingData() {
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
    public void onLeftClick(IEntityPandaPet entity) {
        entity.setSitting(!entity.isSitting());
    }

    @Override
    public Object value(IEntityPandaPet entity) {
        return entity.isSitting();
    }
}
