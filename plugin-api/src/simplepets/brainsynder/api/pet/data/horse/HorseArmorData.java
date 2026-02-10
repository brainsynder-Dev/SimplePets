package simplepets.brainsynder.api.pet.data.horse;

import lib.brainsynder.item.ItemBuilder;
import org.bukkit.Material;
import simplepets.brainsynder.api.Namespace;
import simplepets.brainsynder.api.entity.passive.IEntityHorsePet;
import simplepets.brainsynder.api.pet.PetData;
import simplepets.brainsynder.api.wrappers.horse.HorseArmorType;

@Namespace(namespace = "armor")
public class HorseArmorData extends PetData<IEntityHorsePet> {
    public HorseArmorData() {
        addDefaultItem(HorseArmorType.NONE.name(), new ItemBuilder(Material.BARRIER)
            .withName("&#c8c8c8{name}: &aNONE"));

        for (HorseArmorType armor : HorseArmorType.values()) {
            if (!armor.isSupported()) continue;
            if (armor == HorseArmorType.NONE) continue;
            addDefaultItem(armor.name(), new ItemBuilder(armor.itemType().asMaterial())
                .withName("&#c8c8c8{name}: &a" + armor.name()));
        }
    }

    @Override
    public Object getDefaultValue() {
        return HorseArmorType.NONE;
    }

    @Override
    public void onLeftClick(IEntityHorsePet entity) {
        entity.setArmor(HorseArmorType.getNext(entity.getArmor()));
    }

    @Override
    public void onRightClick(IEntityHorsePet entity) {
        entity.setArmor(HorseArmorType.getPrevious(entity.getArmor()));
    }

    @Override
    public Object value(IEntityHorsePet entity) {
        return entity.getArmor().name();
    }
}
