package simplepets.brainsynder.menu.menuItems;

import org.apache.commons.lang.WordUtils;
import org.bukkit.Material;
import simple.brainsynder.api.ItemMaker;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.IEntityWizard;
import simplepets.brainsynder.menu.menuItems.base.MenuItemAbstract;
import simplepets.brainsynder.pet.PetDefault;
import simplepets.brainsynder.wrapper.WizardSpell;

public class Spell extends MenuItemAbstract {

    public Spell(PetDefault type, IEntityPet entityPet) {
        super(type, entityPet);
    }
    public Spell(PetDefault type) {
        super(type);
    }

    @Override
    public ItemMaker getItem() {
        ItemMaker item = null;
        if (entityPet instanceof IEntityWizard) {
            IEntityWizard var = (IEntityWizard) entityPet;
            WizardSpell typeID = var.getSpell();
            switch (typeID) {
                case BLINDNESS:
                    item = new ItemMaker(Material.INK_SACK);
                    break;
                case DISAPPEAR:
                    item = new ItemMaker(Material.INK_SACK, (byte) 12);
                    break;
                case FANGS:
                    item = new ItemMaker(Material.INK_SACK, (byte) 3);
                    break;
                case NONE:
                    item = new ItemMaker(Material.BARRIER);
                    break;
                case SUMMON_VEX:
                    item = new ItemMaker(Material.INK_SACK, (byte) 8);
                    break;
                case WOLOLO:
                    item = new ItemMaker(Material.INK_SACK, (byte) 14);
                    break;
            }
            item.setName(WordUtils.capitalize(typeID.name().toLowerCase().replace('_', ' ')));
        }
        return item;
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
