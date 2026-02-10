package simplepets.brainsynder.api.pet.data;

import lib.brainsynder.ServerVersion;
import lib.brainsynder.SupportedVersion;
import lib.brainsynder.item.ItemBuilder;
import org.bukkit.Material;
import simplepets.brainsynder.api.Namespace;
import simplepets.brainsynder.api.entity.passive.IEntityNautilusPet;
import simplepets.brainsynder.api.pet.PetData;
import simplepets.brainsynder.api.wrappers.NautilusArmorType;
import simplepets.brainsynder.api.wrappers.horse.HorseArmorType;

@SupportedVersion(version = ServerVersion.v1_21_11)
@Namespace(namespace = "armor")
public class NautilusArmorData extends PetData<IEntityNautilusPet> {
    public NautilusArmorData() {
        addDefaultItem(HorseArmorType.NONE.name(), new ItemBuilder(Material.BARRIER)
            .withName("&#c8c8c8{name}: &aNONE"));

        for (NautilusArmorType armor : NautilusArmorType.values()) {
            if (armor == NautilusArmorType.NONE) continue;
            addDefaultItem(armor.name(), new ItemBuilder(armor.itemType().asMaterial())
                .withName("&#c8c8c8{name}: &a" + armor.name()));
        }
    }

    @Override
    public Object getDefaultValue() {
        return HorseArmorType.NONE;
    }

    @Override
    public void onLeftClick(IEntityNautilusPet entity) {
        entity.setArmor(NautilusArmorType.getNext(entity.getArmor()));
    }

    @Override
    public void onRightClick(IEntityNautilusPet entity) {
        entity.setArmor(NautilusArmorType.getPrevious(entity.getArmor()));
    }

    @Override
    public Object value(IEntityNautilusPet entity) {
        return entity.getArmor().name();
    }
}
