package simplepets.brainsynder.api.pet.data;

import lib.brainsynder.apache.WordUtils;
import simplepets.brainsynder.api.Namespace;
import simplepets.brainsynder.api.entity.misc.IEntityWizard;
import simplepets.brainsynder.api.pet.PetData;
import simplepets.brainsynder.api.wrappers.WizardSpell;

@Namespace(namespace = "spell")
public class SpellData extends PetData<IEntityWizard> {
    public SpellData() {
        for (WizardSpell spell : WizardSpell.values()) {
            addDefaultItem(spell.name(), spell.getIcon().withName("&#c8c8c8{name}: &a"+ WordUtils.capitalize(spell.name().toLowerCase().replace('_', ' '))));
        }
    }

    @Override
    public Object getDefaultValue() {
        return WizardSpell.NONE;
    }

    @Override
    public void onLeftClick(IEntityWizard entity) {
        entity.setSpell(WizardSpell.getNext(entity.getSpell()));
    }

    @Override
    public void onRightClick(IEntityWizard entity) {
        entity.setSpell(WizardSpell.getPrevious(entity.getSpell()));
    }

    @Override
    public Object value(IEntityWizard entity) {
        return entity.getSpell().name();
    }
}
