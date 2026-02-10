package simplepets.brainsynder.api.entity.passive;

import lib.brainsynder.ServerVersion;
import lib.brainsynder.SupportedVersion;
import simplepets.brainsynder.api.entity.misc.EntityPetType;
import simplepets.brainsynder.api.pet.PetType;
import simplepets.brainsynder.api.wrappers.ZombieNautilusPetVariant;

@SupportedVersion(version = ServerVersion.v1_21_11)
@EntityPetType(petType = PetType.ZOMBIE_NAUTILUS)
public interface IEntityZombieNautilusPet extends IEntityNautilusPet {
    ZombieNautilusPetVariant getVariant();
    void setVariant(ZombieNautilusPetVariant variant);
}
