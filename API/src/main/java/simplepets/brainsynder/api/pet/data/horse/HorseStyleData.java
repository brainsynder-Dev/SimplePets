package simplepets.brainsynder.api.pet.data.horse;

import lib.brainsynder.item.ItemBuilder;
import org.bukkit.Material;
import simplepets.brainsynder.api.Namespace;
import simplepets.brainsynder.api.entity.passive.IEntityHorsePet;
import simplepets.brainsynder.api.pet.PetData;
import simplepets.brainsynder.api.wrappers.horse.HorseStyleType;

@Namespace(namespace = "style")
public class HorseStyleData extends PetData<IEntityHorsePet> {
    public HorseStyleData() {
        for (HorseStyleType styleType : HorseStyleType.values()) {
            addDefaultItem(styleType.name(), new ItemBuilder(Material.LEAD)
                    .withName("&#c8c8c8{name}: &a"+styleType.name()));
        }
    }

    @Override
    public Object getDefaultValue() {
        return HorseStyleType.NONE;
    }

    @Override
    public void onLeftClick(IEntityHorsePet entity) {
        entity.setStyle(HorseStyleType.getNext(entity.getStyle()));
    }

    @Override
    public void onRightClick(IEntityHorsePet entity) {
        entity.setStyle(HorseStyleType.getPrevious(entity.getStyle()));
    }

    @Override
    public Object value(IEntityHorsePet entity) {
        return entity.getStyle().name();
    }
}
