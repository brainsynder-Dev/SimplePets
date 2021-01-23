package simplepets.brainsynder.api.entity.misc;

import simplepets.brainsynder.api.entity.IEntityPet;
import simplepets.brainsynder.api.wrappers.WizardSpell;

public interface IEntityWizard extends IEntityPet {
    WizardSpell getSpell();

    void setSpell(WizardSpell spell);
}
