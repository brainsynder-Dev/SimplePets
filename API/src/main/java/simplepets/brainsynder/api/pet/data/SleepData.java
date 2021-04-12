package simplepets.brainsynder.api.pet.data;

import lib.brainsynder.item.ItemBuilder;
import org.bukkit.Material;
import simplepets.brainsynder.api.Namespace;
import simplepets.brainsynder.api.entity.misc.ISleeper;
import simplepets.brainsynder.api.pet.PetData;

@Namespace(namespace = "sleep")
public class SleepData extends PetData<ISleeper> {
    public SleepData() {
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
    public void onLeftClick(ISleeper entity) {
        entity.setPetSleeping(!entity.isPetSleeping());
    }

    @Override
    public Object value(ISleeper entity) {
        return entity.isPetSleeping();
    }
}
