package simplepets.brainsynder.api.pet.data;

import lib.brainsynder.item.ItemBuilder;
import org.bukkit.Material;
import simplepets.brainsynder.api.Namespace;
import simplepets.brainsynder.api.entity.misc.IShaking;
import simplepets.brainsynder.api.pet.PetData;

@Namespace(namespace = "shaking")
public class ShakeData extends PetData<IShaking> {
    public ShakeData() {
        addDefaultItem("true", new ItemBuilder(Material.ENCHANTED_GOLDEN_APPLE)
                .withName("&#c8c8c8{name}: &atrue"));
        addDefaultItem("false", new ItemBuilder(Material.ENCHANTED_GOLDEN_APPLE)
                .withName("&#c8c8c8{name}: &cfalse"));
    }

    @Override
    public Object getDefaultValue() {
        return false;
    }

    @Override
    public void onLeftClick(IShaking entity) {
        entity.setShaking(!entity.isShaking());
    }

    @Override
    public Object value(IShaking entity) {
        return entity.isShaking();
    }
}
