package simplepets.brainsynder.menu.menuItems;

import org.apache.commons.lang.WordUtils;
import org.bukkit.Material;
import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.entity.IEntityWizard;
import simplepets.brainsynder.menu.menuItems.base.MenuItemAbstract;
import simplepets.brainsynder.pet.PetDefault;
import simplepets.brainsynder.utils.ItemBuilder;
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
                    item = new ItemBuilder(Material.INK_SACK);
                    break;
                case DISAPPEAR:
                    item = new ItemBuilder(Material.INK_SACK, (byte) 12);
                    break;
                case FANGS:
                    item = new ItemBuilder(Material.INK_SACK, (byte) 3);
                    break;
                case NONE:
                    item = new ItemBuilder(Material.BARRIER);
                    break;
                case SUMMON_VEX:
                    item = new ItemBuilder(Material.INK_SACK, (byte) 8);
                    break;
                case WOLOLO:
                    item = new ItemBuilder(Material.INK_SACK, (byte) 14);
                    break;
            }
            item.withName(WordUtils.capitalize(typeID.name().toLowerCase().replace('_', ' ')));
        }
        return item;
    }

    @Override
    public List<ItemBuilder> getDefaultItems() {
        ItemBuilder item = null;
        if (entityPet instanceof IEntityWizard) {
            IEntityWizard var = (IEntityWizard) entityPet;
            WizardSpell typeID = var.getSpell();
            switch (typeID) {
                case BLINDNESS:
                    item = new ItemBuilder(Material.INK_SACK);
                    break;
                case DISAPPEAR:
                    item = new ItemBuilder(Material.INK_SACK, (byte) 12);
                    break;
                case FANGS:
                    item = new ItemBuilder(Material.INK_SACK, (byte) 3);
                    break;
                case NONE:
                    item = new ItemBuilder(Material.BARRIER);
                    break;
                case SUMMON_VEX:
                    item = new ItemBuilder(Material.INK_SACK, (byte) 8);
                    break;
                case WOLOLO:
                    item = new ItemBuilder(Material.INK_SACK, (byte) 14);
                    break;
            }
            item.withName(WordUtils.capitalize(typeID.name().toLowerCase().replace('_', ' ')));
        }
        return new ArrayList<>(); // TODO
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
