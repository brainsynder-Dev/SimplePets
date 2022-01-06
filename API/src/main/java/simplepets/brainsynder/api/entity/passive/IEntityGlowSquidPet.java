package simplepets.brainsynder.api.entity.passive;

import lib.brainsynder.ServerVersion;
import lib.brainsynder.SupportedVersion;
import simplepets.brainsynder.api.entity.misc.EntityPetType;
import simplepets.brainsynder.api.pet.PetType;

@SupportedVersion(version = ServerVersion.v1_17)
@EntityPetType(petType = PetType.GLOW_SQUID)
public interface IEntityGlowSquidPet extends IEntitySquidPet {
    boolean isSquidGlowing ();
    void setSquidGlowing (boolean glowing);
}
