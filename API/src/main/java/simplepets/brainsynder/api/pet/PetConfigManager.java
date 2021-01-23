package simplepets.brainsynder.api.pet;

import java.util.Optional;

public interface PetConfigManager {
    Optional<IPetConfig> getPetConfig (PetType type);
}
