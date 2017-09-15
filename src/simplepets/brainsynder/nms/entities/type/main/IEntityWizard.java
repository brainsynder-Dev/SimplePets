package simplepets.brainsynder.nms.entities.type.main;

import simplepets.brainsynder.wrapper.WizardSpell;

public interface IEntityWizard extends IEntityPet {
    WizardSpell getSpell();

    void setSpell(WizardSpell spell);
}
