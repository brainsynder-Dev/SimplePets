package simplepets.brainsynder.api.pet.data;

import lib.brainsynder.item.ItemBuilder;
import org.bukkit.Material;
import simplepets.brainsynder.api.Namespace;
import simplepets.brainsynder.api.entity.hostile.IEntityWitchPet;
import simplepets.brainsynder.api.pet.PetData;

@Namespace(namespace = "potion")
public class PotionData extends PetData<IEntityWitchPet> {
    public PotionData() {
        addDefaultItem("true", new ItemBuilder(Material.POTION)
                .withName("&#c8c8c8{name}: &atrue"));
        addDefaultItem("false", new ItemBuilder(Material.POTION)
                .withName("&#c8c8c8{name}: &cfalse"));
    }

    @Override
    public Object getDefaultValue() {
        return false;
    }

    @Override
    public void onLeftClick(IEntityWitchPet entity) {
        entity.setDrinkingPotion(!entity.isDrinkingPotion());
    }

    @Override
    public Object value(IEntityWitchPet entity) {
        return entity.isDrinkingPotion();
    }
}
