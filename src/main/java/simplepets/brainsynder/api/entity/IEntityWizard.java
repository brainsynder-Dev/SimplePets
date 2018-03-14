package simplepets.brainsynder.api.entity;

import simplepets.brainsynder.wrapper.WizardSpell;

public interface IEntityWizard extends IEntityPet {
    WizardSpell getSpell();

    void setSpell(WizardSpell spell);
}
