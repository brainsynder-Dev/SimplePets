package simplepets.brainsynder.api.entity.passive;

import lib.brainsynder.ServerVersion;
import lib.brainsynder.SupportedVersion;
import simplepets.brainsynder.api.entity.misc.EntityPetType;
import simplepets.brainsynder.api.entity.misc.IAgeablePet;
import simplepets.brainsynder.api.entity.misc.ISaddle;
import simplepets.brainsynder.api.pet.PetType;


@EntityPetType(petType = PetType.SPIDER)
@SupportedVersion(version = ServerVersion.v1_16_R1)
public interface IEntityStriderPet extends IAgeablePet, ISaddle {
    boolean isCold();
    void setCold(boolean cold);
}
