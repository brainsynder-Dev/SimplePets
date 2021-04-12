package simplepets.brainsynder.api.pet.data;

import lib.brainsynder.item.ItemBuilder;
import org.bukkit.Material;
import simplepets.brainsynder.api.Namespace;
import simplepets.brainsynder.api.entity.misc.ISaddle;
import simplepets.brainsynder.api.pet.PetData;

@Namespace(namespace = "saddled")
public class SaddleData extends PetData<ISaddle> {
    public SaddleData() {
        addDefaultItem("true", new ItemBuilder(Material.SADDLE)
                .withName("&#c8c8c8{name}: &atrue"));
        addDefaultItem("false", new ItemBuilder(Material.SADDLE)
                .withName("&#c8c8c8{name}: &cfalse"));
    }

    @Override
    public Object getDefaultValue() {
        return false;
    }

    @Override
    public void onLeftClick(ISaddle entity) {
        entity.setSaddled(!entity.isSaddled());
    }

    @Override
    public Object value(ISaddle entity) {
        return entity.isSaddled();
    }
}
