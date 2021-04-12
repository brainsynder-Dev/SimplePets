package simplepets.brainsynder.api.pet.data;

import lib.brainsynder.item.ItemBuilder;
import org.bukkit.Material;
import simplepets.brainsynder.api.Namespace;
import simplepets.brainsynder.api.entity.passive.IEntityPolarBearPet;
import simplepets.brainsynder.api.pet.PetData;

@Namespace(namespace = "standing")
public class StandingData extends PetData<IEntityPolarBearPet> {
    public StandingData() {
        addDefaultItem("true", new ItemBuilder(Material.IRON_LEGGINGS)
                .withName("&#c8c8c8{name}: &atrue"));
        addDefaultItem("false", new ItemBuilder(Material.IRON_LEGGINGS)
                .withName("&#c8c8c8{name}: &cfalse"));
    }

    @Override
    public Object getDefaultValue() {
        return false;
    }

    @Override
    public void onLeftClick(IEntityPolarBearPet entity) {
        entity.setStandingUp(!entity.isStanding());
    }

    @Override
    public Object value(IEntityPolarBearPet entity) {
        return entity.isStanding();
    }
}
