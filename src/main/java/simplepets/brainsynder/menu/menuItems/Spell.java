package simplepets.brainsynder.menu.menuItems;

import org.apache.commons.lang.WordUtils;
import org.bukkit.Material;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.IEntityWizard;
import simplepets.brainsynder.menu.menuItems.base.MenuItemAbstract;
import simplepets.brainsynder.pet.PetDefault;
import simplepets.brainsynder.utils.ItemBuilder;
import simplepets.brainsynder.utils.Utilities;
import simplepets.brainsynder.wrapper.WizardSpell;

import java.util.ArrayList;
import java.util.List;

public class Spell extends MenuItemAbstract {

    public Spell(PetDefault type, IEntityPet entityPet) {
        super(type, entityPet);
    }
    public Spell(PetDefault type) {
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
        ItemBuilder item = Utilities.getColoredMaterial(Utilities.MatType.INK_SACK, 0).toBuilder(1);
        item.withName(WordUtils.capitalize(WizardSpell.BLINDNESS.name().toLowerCase().replace('_', ' ')));
        items.add(item);
        item = Utilities.getColoredMaterial(Utilities.MatType.INK_SACK, 12).toBuilder(1);
        item.withName(WordUtils.capitalize(WizardSpell.DISAPPEAR.name().toLowerCase().replace('_', ' ')));
        items.add(item);
        item = Utilities.getColoredMaterial(Utilities.MatType.INK_SACK, 3).toBuilder(1);
        item.withName(WordUtils.capitalize(WizardSpell.FANGS.name().toLowerCase().replace('_', ' ')));
        items.add(item);
        item = new ItemBuilder(Material.BARRIER);
        item.withName(WordUtils.capitalize(WizardSpell.NONE.name().toLowerCase().replace('_', ' ')));
        items.add(item);
        item = Utilities.getColoredMaterial(Utilities.MatType.INK_SACK, 8).toBuilder(1);
        item.withName(WordUtils.capitalize(WizardSpell.SUMMON_VEX.name().toLowerCase().replace('_', ' ')));
        items.add(item);
        item = Utilities.getColoredMaterial(Utilities.MatType.INK_SACK, 14).toBuilder(1);
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
