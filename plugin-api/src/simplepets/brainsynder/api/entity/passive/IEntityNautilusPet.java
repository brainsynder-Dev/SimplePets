package simplepets.brainsynder.api.entity.passive;

import lib.brainsynder.ServerVersion;
import lib.brainsynder.SupportedVersion;
import simplepets.brainsynder.api.entity.misc.EntityPetType;
import simplepets.brainsynder.api.entity.misc.ISaddle;
import simplepets.brainsynder.api.entity.misc.ITameable;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.wrappers.NautilusArmorType;

@SupportedVersion(version = ServerVersion.v1_21_11)
@EntityPetType(petType = PetType.NAUTILUS)
public interface IEntityNautilusPet extends ITameable, ISaddle {
    void setArmor (NautilusArmorType armor);

    NautilusArmorType getArmor();
}
