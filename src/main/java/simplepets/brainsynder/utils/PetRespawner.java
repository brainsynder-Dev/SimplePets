package simplepets.brainsynder.utils;

import lombok.Getter;
import simple.brainsynder.nbt.StorageTagCompound;
import simplepets.brainsynder.nms.entities.type.main.IEntityPet;
import simplepets.brainsynder.pet.PetType;

public class PetRespawner {
    @Getter
    private PetType petType;
    @Getter
    private StorageTagCompound entityData;

    public PetRespawner(IEntityPet entityPet) {
        this.petType = entityPet.getPet().getPetType();
        this.entityData = entityPet.asCompound();
    }
}
