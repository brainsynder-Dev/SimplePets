package simplepets.brainsynder.events;

import simplepets.brainsynder.utils.PetMap;
import simplepets.brainsynder.utils.PetRespawner;

import java.util.UUID;

class EventCore {
    PetMap<UUID, PetRespawner> petTypeMap = new PetMap<>();
}
