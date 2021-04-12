package simplepets.brainsynder.api.pet.data.horse;

import lib.brainsynder.item.ItemBuilder;
import org.bukkit.Material;
import simplepets.brainsynder.api.Namespace;
import simplepets.brainsynder.api.entity.passive.IEntityHorsePet;
import simplepets.brainsynder.api.pet.PetData;
import simplepets.brainsynder.api.wrappers.horse.HorseColorType;

@Namespace(namespace = "color")
public class HorseColorData extends PetData<IEntityHorsePet> {
    public HorseColorData() {
        for (HorseColorType color : HorseColorType.values()) {
            addDefaultItem(color.name(), new ItemBuilder(Material.PLAYER_HEAD)
                    .withName("&#c8c8c8{name}: &a"+color.name())
                    .setTexture(color.getTexture()));
        }
    }

    @Override
    public Object getDefaultValue() {
        return HorseColorType.WHITE;
    }

    @Override
    public void onLeftClick(IEntityHorsePet entity) {
        entity.setColor(HorseColorType.getNext(entity.getColor()));
    }

    @Override
    public void onRightClick(IEntityHorsePet entity) {
        entity.setColor(HorseColorType.getPrevious(entity.getColor()));
    }

    @Override
    public Object value(IEntityHorsePet entity) {
        return entity.getColor().name();
    }
}
