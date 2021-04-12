package simplepets.brainsynder.api.pet.data;

import lib.brainsynder.item.ItemBuilder;
import org.bukkit.Material;
import simplepets.brainsynder.api.Namespace;
import simplepets.brainsynder.api.entity.passive.IEntityParrotPet;
import simplepets.brainsynder.api.pet.PetData;
import simplepets.brainsynder.api.wrappers.ParrotVariant;

@Namespace(namespace = "variant")
public class ParrotColorData extends PetData<IEntityParrotPet> {
    public ParrotColorData() {
        for (ParrotVariant variant : ParrotVariant.values()) {
            addDefaultItem(variant.name(), new ItemBuilder(Material.PLAYER_HEAD)
                    .withName("&#c8c8c8{name}: &a"+variant.name())
                    .setTexture(variant.getTexture()));
        }
    }

    @Override
    public Object getDefaultValue() {
        return ParrotVariant.RED;
    }

    @Override
    public void onLeftClick(IEntityParrotPet entity) {
        entity.setVariant(ParrotVariant.getNext(entity.getVariant()));
    }

    @Override
    public Object value(IEntityParrotPet entity) {
        return entity.getVariant().name();
    }
}
