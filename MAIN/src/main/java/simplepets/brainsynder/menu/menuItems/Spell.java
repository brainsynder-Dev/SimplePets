package simplepets.brainsynder.menu.menuItems;

import lib.brainsynder.item.ItemBuilder;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Material;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.misc.IEntityWizard;
import simplepets.brainsynder.menu.menuItems.base.MenuItemAbstract;
import simplepets.brainsynder.pet.PetType;
import simplepets.brainsynder.utils.ValueType;
import simplepets.brainsynder.wrapper.WizardSpell;

import java.util.ArrayList;
import java.util.List;

@ValueType(def = "NONE", target = "https://github.com/brainsynder-Dev/SimplePets/blob/master/MAIN/src/main/java/simplepets/brainsynder/wrapper/WizardSpell.java")
public class Spell extends MenuItemAbstract {

    public Spell(PetType type, IEntityPet entityPet) {
        super(type, entityPet);
    }
    public Spell(PetType type) {
        super(type);
    }

    @Override
    public ItemBuilder getItem() {
        ItemBuilder item = null;
        if (entityPet instanceof IEntityWizard) {
            IEntityWizard var = (IEntityWizard) entityPet;
            WizardSpell typeID = var.getSpell();
            switch (typeID) {
                case BLINDNESS:
                    item = type.getDataItemByName("spell", 0);
                    break;
                case DISAPPEAR:
                    item = type.getDataItemByName("spell", 1);
                    break;
                case FANGS:
                    item = type.getDataItemByName("spell", 2);
                    break;
                case NONE:
                    item = type.getDataItemByName("spell", 3);
                    break;
                case SUMMON_VEX:
                    item = type.getDataItemByName("spell", 4);
                    break;
                case WOLOLO:
                    item = type.getDataItemByName("spell", 5);
                    break;
            }
        }
        return item;
    }

    @Override
    public List<ItemBuilder> getDefaultItems() {
        List<ItemBuilder> items = new ArrayList<>();
        ItemBuilder item = lib.brainsynder.nms.DataConverter.getColoredMaterial(lib.brainsynder.nms.DataConverter.MaterialType.INK_SACK, 0);
        item.withName(WordUtils.capitalize(WizardSpell.BLINDNESS.name().toLowerCase().replace('_', ' ')));
        items.add(item);
        item = lib.brainsynder.nms.DataConverter.getColoredMaterial(lib.brainsynder.nms.DataConverter.MaterialType.INK_SACK, 12);
        item.withName(WordUtils.capitalize(WizardSpell.DISAPPEAR.name().toLowerCase().replace('_', ' ')));
        items.add(item);
        item = lib.brainsynder.nms.DataConverter.getColoredMaterial(lib.brainsynder.nms.DataConverter.MaterialType.INK_SACK, 3);
        item.withName(WordUtils.capitalize(WizardSpell.FANGS.name().toLowerCase().replace('_', ' ')));
        items.add(item);
        item = new ItemBuilder(Material.BARRIER);
        item.withName(WordUtils.capitalize(WizardSpell.NONE.name().toLowerCase().replace('_', ' ')));
        items.add(item);
        item = lib.brainsynder.nms.DataConverter.getColoredMaterial(lib.brainsynder.nms.DataConverter.MaterialType.INK_SACK, 8);
        item.withName(WordUtils.capitalize(WizardSpell.SUMMON_VEX.name().toLowerCase().replace('_', ' ')));
        items.add(item);
        item = lib.brainsynder.nms.DataConverter.getColoredMaterial(lib.brainsynder.nms.DataConverter.MaterialType.INK_SACK, 14);
        item.withName(WordUtils.capitalize(WizardSpell.WOLOLO.name().toLowerCase().replace('_', ' ')));
        items.add(item);
        return items;
    }

    @Override
    public void onLeftClick() {
        if (entityPet instanceof IEntityWizard) {
            IEntityWizard var = (IEntityWizard) entityPet;
            WizardSpell wrapper = WizardSpell.NONE;
            if (var.getSpell() != null)
                wrapper = var.getSpell();
            var.setSpell(WizardSpell.getNext(wrapper));
        }
    }

    @Override
    public void onRightClick() {
        if (entityPet instanceof IEntityWizard) {
            IEntityWizard var = (IEntityWizard) entityPet;
            WizardSpell wrapper = WizardSpell.NONE;
            if (var.getSpell() != null)
                wrapper = var.getSpell();
            var.setSpell(WizardSpell.getPrevious(wrapper));
        }
    }
}
