package simplepets.brainsynder.api.pet.data;

import lib.brainsynder.item.ItemBuilder;
import org.bukkit.Material;
import simplepets.brainsynder.api.Namespace;
import simplepets.brainsynder.api.entity.hostile.IEntityPiglinPet;
import simplepets.brainsynder.api.pet.PetData;

@Namespace(namespace = "charging")
public class ChargingData extends PetData<IEntityPiglinPet> {
    public ChargingData() {
        addDefaultItem("true", new ItemBuilder(Material.IRON_SWORD)
                .withName("&#c8c8c8{name}: &atrue"));
        addDefaultItem("false", new ItemBuilder(Material.IRON_SWORD)
                .withName("&#c8c8c8{name}: &cfalse"));
    }

    @Override
    public Object getDefaultValue() {
        return false;
    }

    @Override
    public void onLeftClick(IEntityPiglinPet entity) {
        entity.setCharging(!entity.isCharging());
    }

    @Override
    public Object value(IEntityPiglinPet entity) {
        return entity.isCharging();
    }
}
