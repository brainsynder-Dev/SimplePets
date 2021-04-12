package simplepets.brainsynder.api.pet.data;

import lib.brainsynder.item.ItemBuilder;
import org.bukkit.Material;
import simplepets.brainsynder.api.Namespace;
import simplepets.brainsynder.api.entity.passive.IEntityRabbitPet;
import simplepets.brainsynder.api.pet.PetData;
import simplepets.brainsynder.api.wrappers.RabbitType;

@Namespace(namespace = "variant")
public class RabbitColorData extends PetData<IEntityRabbitPet> {
    public RabbitColorData() {
        for (RabbitType variant : RabbitType.values()) {
            addDefaultItem(variant.name(), new ItemBuilder(Material.PLAYER_HEAD)
                    .withName("&#c8c8c8{name}: &a"+variant.name())
                    .setTexture(variant.getTexture()));
        }
    }

    @Override
    public Object getDefaultValue() {
        return RabbitType.BROWN;
    }

    @Override
    public void onLeftClick(IEntityRabbitPet entity) {
        entity.setRabbitType(RabbitType.getNext(entity.getRabbitType()));
    }

    @Override
    public Object value(IEntityRabbitPet entity) {
        return entity.getRabbitType().name();
    }
}
