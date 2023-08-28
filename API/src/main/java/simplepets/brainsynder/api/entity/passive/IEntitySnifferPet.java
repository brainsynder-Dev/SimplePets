package simplepets.brainsynder.api.entity.passive;

import lib.brainsynder.ServerVersion;
import lib.brainsynder.SupportedVersion;
import simplepets.brainsynder.api.entity.misc.EntityPetType;
import simplepets.brainsynder.api.entity.misc.IAgeablePet;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.wrappers.SnifferState;

@SupportedVersion(version = ServerVersion.v1_20)
@EntityPetType(petType = PetType.SNIFFER)
public interface IEntitySnifferPet extends IAgeablePet {

    SnifferState getSnifferState ();

    void setSnifferState (SnifferState state);

}
