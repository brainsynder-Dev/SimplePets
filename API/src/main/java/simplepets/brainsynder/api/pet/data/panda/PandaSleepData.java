package simplepets.brainsynder.api.pet.data.panda;

import lib.brainsynder.item.ItemBuilder;
import org.bukkit.Material;
import simplepets.brainsynder.api.Namespace;
import simplepets.brainsynder.api.entity.passive.IEntityPandaPet;
import simplepets.brainsynder.api.pet.PetData;

@Namespace(namespace = "sleeping")
public class PandaSleepData extends PetData<IEntityPandaPet> {
    public PandaSleepData() {
        addDefaultItem("true", new ItemBuilder(Material.RED_BED)
                .withName("&#c8c8c8{name}: &atrue"));
        addDefaultItem("false", new ItemBuilder(Material.RED_BED)
                .withName("&#c8c8c8{name}: &cfalse"));
    }

    @Override
    public Object getDefaultValue() {
        return false;
    }

    @Override
    public void onLeftClick(IEntityPandaPet entity) {
        entity.setLyingOnBack(!entity.isLyingOnBack());
    }

    @Override
    public Object value(IEntityPandaPet entity) {
        return entity.isLyingOnBack();
    }
}
